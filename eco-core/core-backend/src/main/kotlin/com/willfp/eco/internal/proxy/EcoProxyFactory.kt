package com.willfp.eco.internal.proxy

import com.willfp.eco.core.EcoPlugin
import com.willfp.eco.core.proxy.AbstractProxy
import com.willfp.eco.core.proxy.ProxyConstants
import com.willfp.eco.core.proxy.ProxyFactory
import com.willfp.eco.core.proxy.exceptions.ProxyError
import com.willfp.eco.core.proxy.exceptions.UnsupportedVersionException
import java.net.URLClassLoader
import java.util.*

class EcoProxyFactory(
    private val plugin: EcoPlugin
) : ProxyFactory {
    private val proxyClassLoader: ClassLoader = plugin::class.java.classLoader
    private val cache: MutableMap<Class<out AbstractProxy>, AbstractProxy> = IdentityHashMap()

    override fun <T : AbstractProxy> getProxy(proxyClass: Class<T>): T {
        try {
            val cachedProxy: T? = attemptCache(proxyClass)
            if (cachedProxy != null) {
                return cachedProxy
            }

            val className =
                this.plugin.proxyPackage + "." + ProxyConstants.NMS_VERSION + "." + proxyClass.simpleName.replace(
                    "Proxy",
                    ""
                )
            val clazz = this.plugin::class.java.classLoader.loadClass(className)
            val instance = clazz.getDeclaredConstructor().newInstance()
            if (proxyClass.isAssignableFrom(clazz) && proxyClass.isInstance(instance)) {
                val proxy = proxyClass.cast(instance)
                cache[proxyClass] = proxy
                return proxy
            }
        } catch (e: Exception) {
            throwError(e)
        }

        throwError(IllegalArgumentException())

        throw RuntimeException("Something went wrong.")
    }

    private fun throwError(e: Exception?) {
        e?.printStackTrace()

        if (!SUPPORTED_VERSIONS.contains(ProxyConstants.NMS_VERSION)) {
            throw UnsupportedVersionException("You're running an unsupported server version: " + ProxyConstants.NMS_VERSION)
        } else {
            throw ProxyError("Error with proxies - here's a stacktrace. Only god can help you now.")
        }
    }

    private fun <T : AbstractProxy> attemptCache(proxyClass: Class<T>): T? {
        val proxy = cache[proxyClass] ?: return null

        if (proxyClass.isInstance(proxy)) {
            return proxyClass.cast(proxy)
        }

        return null
    }

    fun clean() {
        if (proxyClassLoader is URLClassLoader) {
            proxyClassLoader.close()
        }

        cache.clear()
    }

    companion object {
        val SUPPORTED_VERSIONS = listOf(
            "v1_16_R3",
            "v1_17_R1"
        )
    }
}
