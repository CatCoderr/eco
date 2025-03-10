package com.willfp.eco.spigot

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.Handler
import com.willfp.eco.core.config.updating.ConfigHandler
import com.willfp.eco.core.config.wrapper.ConfigFactory
import com.willfp.eco.core.data.PlayerProfileHandler
import com.willfp.eco.core.data.keys.KeyRegistry
import com.willfp.eco.core.drops.DropQueueFactory
import com.willfp.eco.core.events.EventManager
import com.willfp.eco.core.extensions.ExtensionLoader
import com.willfp.eco.core.factory.MetadataValueFactory
import com.willfp.eco.core.factory.NamespacedKeyFactory
import com.willfp.eco.core.factory.RunnableFactory
import com.willfp.eco.core.fast.FastItemStack
import com.willfp.eco.core.gui.GUIFactory
import com.willfp.eco.core.integrations.placeholder.PlaceholderIntegration
import com.willfp.eco.core.proxy.Cleaner
import com.willfp.eco.core.proxy.ProxyFactory
import com.willfp.eco.core.requirement.RequirementFactory
import com.willfp.eco.core.scheduling.Scheduler
import com.willfp.eco.internal.EcoCleaner
import com.willfp.eco.internal.Plugins
import com.willfp.eco.internal.config.EcoConfigFactory
import com.willfp.eco.internal.config.updating.EcoConfigHandler
import com.willfp.eco.internal.drops.EcoDropQueueFactory
import com.willfp.eco.internal.events.EcoEventManager
import com.willfp.eco.internal.extensions.EcoExtensionLoader
import com.willfp.eco.internal.factory.EcoMetadataValueFactory
import com.willfp.eco.internal.factory.EcoNamespacedKeyFactory
import com.willfp.eco.internal.factory.EcoRunnableFactory
import com.willfp.eco.internal.gui.EcoGUIFactory
import com.willfp.eco.internal.integrations.PlaceholderIntegrationPAPI
import com.willfp.eco.internal.logging.EcoLogger
import com.willfp.eco.internal.proxy.EcoProxyFactory
import com.willfp.eco.internal.requirement.EcoRequirementFactory
import com.willfp.eco.internal.scheduling.EcoScheduler
import com.willfp.eco.proxy.FastItemStackFactoryProxy
import com.willfp.eco.spigot.data.EcoKeyRegistry
import com.willfp.eco.spigot.data.EcoPlayerProfileHandler
import com.willfp.eco.spigot.data.storage.MySQLDataHandler
import com.willfp.eco.spigot.data.storage.YamlDataHandler
import com.willfp.eco.spigot.integrations.bstats.MetricHandler
import net.kyori.adventure.platform.bukkit.BukkitAudiences
import org.bukkit.inventory.ItemStack
import java.util.logging.Logger

@Suppress("UNUSED")
class EcoHandler : EcoSpigotPlugin(), Handler {
    private val cleaner = EcoCleaner()
    private val requirementFactory = EcoRequirementFactory()
    private var adventure: BukkitAudiences? = null
    private val keyRegistry = EcoKeyRegistry(this)
    private val playerProfileHandler = EcoPlayerProfileHandler(
        if (this.configYml.getBool("mysql.enabled"))
            MySQLDataHandler(this) else YamlDataHandler(this)
    )

    override fun createScheduler(plugin: EcoPlugin): Scheduler {
        return EcoScheduler(plugin)
    }

    override fun createEventManager(plugin: EcoPlugin): EventManager {
        return EcoEventManager(plugin)
    }

    override fun createNamespacedKeyFactory(plugin: EcoPlugin): NamespacedKeyFactory {
        return EcoNamespacedKeyFactory(plugin)
    }

    override fun createMetadataValueFactory(plugin: EcoPlugin): MetadataValueFactory {
        return EcoMetadataValueFactory(plugin)
    }

    override fun createRunnableFactory(plugin: EcoPlugin): RunnableFactory {
        return EcoRunnableFactory(plugin)
    }

    override fun createExtensionLoader(plugin: EcoPlugin): ExtensionLoader {
        return EcoExtensionLoader(plugin)
    }

    override fun createConfigHandler(plugin: EcoPlugin): ConfigHandler {
        return EcoConfigHandler(plugin)
    }

    override fun createLogger(plugin: EcoPlugin): Logger {
        return EcoLogger(plugin)
    }

    override fun createPAPIIntegration(plugin: EcoPlugin): PlaceholderIntegration {
        return PlaceholderIntegrationPAPI(plugin)
    }

    override fun getEcoPlugin(): EcoPlugin {
        return this
    }

    override fun getConfigFactory(): ConfigFactory {
        return EcoConfigFactory()
    }

    override fun getDropQueueFactory(): DropQueueFactory {
        return EcoDropQueueFactory()
    }

    override fun getGUIFactory(): GUIFactory {
        return EcoGUIFactory()
    }

    override fun getCleaner(): Cleaner {
        return cleaner
    }

    override fun createProxyFactory(plugin: EcoPlugin): ProxyFactory {
        return EcoProxyFactory(plugin)
    }

    override fun addNewPlugin(plugin: EcoPlugin) {
        Plugins.LOADED_ECO_PLUGINS[plugin.name.lowercase()] = plugin
    }

    override fun getLoadedPlugins(): List<String> {
        return Plugins.LOADED_ECO_PLUGINS.keys.toMutableList()
    }

    override fun getPluginByName(name: String): EcoPlugin? {
        return Plugins.LOADED_ECO_PLUGINS[name.lowercase()]
    }

    override fun createFastItemStack(itemStack: ItemStack): FastItemStack {
        return getProxy(FastItemStackFactoryProxy::class.java).create(itemStack)
    }

    override fun registerBStats(plugin: EcoPlugin) {
        MetricHandler.createMetrics(plugin, this.ecoPlugin)
    }

    override fun getRequirementFactory(): RequirementFactory {
        return requirementFactory
    }

    override fun getAdventure(): BukkitAudiences? {
        return adventure
    }

    override fun getKeyRegistry(): KeyRegistry {
        return keyRegistry
    }

    override fun getPlayerProfileHandler(): PlayerProfileHandler {
        return playerProfileHandler
    }

    fun setAdventure(adventure: BukkitAudiences) {
        this.adventure = adventure
    }
}