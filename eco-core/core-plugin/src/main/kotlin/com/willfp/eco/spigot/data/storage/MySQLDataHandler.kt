package com.willfp.eco.spigot.data.storage

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.willfp.eco.core.Eco
import com.willfp.eco.core.data.PlayerProfile
import com.willfp.eco.core.data.keys.PersistentDataKey
import com.willfp.eco.core.data.keys.PersistentDataKeyType
import com.willfp.eco.spigot.EcoSpigotPlugin
import org.apache.logging.log4j.Level
import org.bukkit.NamespacedKey
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import java.util.concurrent.Executors

@Suppress("UNCHECKED_CAST")
class MySQLDataHandler(
    plugin: EcoSpigotPlugin
) : DataHandler {
    private val columns = mutableMapOf<String, Column<*>>()
    private val threadFactory = ThreadFactoryBuilder().setNameFormat("eco-mysql-thread-%d").build()
    private val executor = Executors.newFixedThreadPool(plugin.configYml.getInt("mysql.threads"), threadFactory)

    init {
        Database.connect(
            "jdbc:mysql://" +
                    "${plugin.configYml.getString("mysql.host")}:" +
                    "${plugin.configYml.getString("mysql.port")}/" +
                    plugin.configYml.getString("mysql.database"),
            driver = "com.mysql.cj.jdbc.Driver",
            user = plugin.configYml.getString("mysql.user"),
            password = plugin.configYml.getString("mysql.password")
        )

        transaction {
            SchemaUtils.create(Players)
        }

//        // Get Exposed to shut the hell up
//        exposedLogger::class.java.getDeclaredField("logger").apply { isAccessible = true }
//            .apply {
//                get(exposedLogger).apply {
//                    this.javaClass.getDeclaredMethod("setLevel", Level::class.java)
//                        .invoke(this, Level.OFF)
//                }
//            }
    }

    override fun updateKeys() {
        transaction {
            for (key in Eco.getHandler().keyRegistry.registeredKeys) {
                registerColumn(key, Players)
            }

            SchemaUtils.createMissingTablesAndColumns(Players)
        }
    }

    override fun <T> write(uuid: UUID, key: NamespacedKey, value: T) {
        getPlayer(uuid)
        writeAsserted(uuid, key, value)
    }

    private fun <T> writeAsserted(uuid: UUID, key: NamespacedKey, value: T) {
        val column: Column<T> = getColumn(key.toString()) as Column<T>

        executor.submit {
            transaction {
                Players.update({ Players.id eq uuid }) {
                    it[column] = value
                }
            }
        }
    }

    override fun saveKeysForPlayer(uuid: UUID, keys: Set<PersistentDataKey<*>>) {
        savePlayer(uuid, keys)
    }

    override fun saveAll(uuids: Iterable<UUID>) {
        for (uuid in uuids) {
            savePlayer(uuid)
        }
    }

    private fun savePlayer(uuid: UUID, keys: Set<PersistentDataKey<*>>) {
        val profile = PlayerProfile.load(uuid)

        executor.submit {
            transaction {
                getPlayer(uuid)

                for (key in keys) {
                    writeAsserted(uuid, key.key, profile.read(key))
                }
            }
        }
    }

    override fun <T> read(uuid: UUID, key: NamespacedKey): T? {
        var value: T? = null
        transaction {
            val player = getPlayer(uuid)
            value = player[getColumn(key.toString())] as T?
        }

        return value
    }

    object Players : UUIDTable("eco_players")

    private fun <T> registerColumn(key: PersistentDataKey<T>, table: UUIDTable) {
        table.apply {
            if (this.columns.stream().anyMatch { it.name == key.key.toString() }) {
                return@apply
            }

            when (key.type) {
                PersistentDataKeyType.INT -> registerColumn<Int>(key.key.toString(), IntegerColumnType())
                    .default(key.defaultValue as Int)
                PersistentDataKeyType.DOUBLE -> registerColumn<Double>(key.key.toString(), DoubleColumnType())
                    .default(key.defaultValue as Double)
                PersistentDataKeyType.BOOLEAN -> registerColumn<Boolean>(key.key.toString(), BooleanColumnType())
                    .default(key.defaultValue as Boolean)
                PersistentDataKeyType.STRING -> registerColumn<String>(key.key.toString(), VarCharColumnType(512))
                    .default(key.defaultValue as String)

                else -> throw NullPointerException("Null value found!")
            }
        }
    }

    private fun getColumn(name: String): Column<*> {
        val cached = columns[name]
        if (cached != null) {
            return cached
        }

        columns[name] = Players.columns.stream().filter { it.name == name }.findFirst().get()
        return getColumn(name)
    }

    private fun getPlayer(uuid: UUID): ResultRow {
        val player = transaction {
            Players.select { Players.id eq uuid }.limit(1).singleOrNull()
        }

        return if (player != null) {
            player
        } else {
            transaction {
                Players.insert { it[id] = uuid }
            }
            getPlayer(uuid)
        }
    }
}