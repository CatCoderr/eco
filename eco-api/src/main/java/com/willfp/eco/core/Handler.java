package com.willfp.eco.core;

import com.willfp.eco.core.config.updating.ConfigHandler;
import com.willfp.eco.core.config.wrapper.ConfigFactory;
import com.willfp.eco.core.data.keys.KeyRegistry;
import com.willfp.eco.core.data.PlayerProfileHandler;
import com.willfp.eco.core.drops.DropQueueFactory;
import com.willfp.eco.core.events.EventManager;
import com.willfp.eco.core.extensions.ExtensionLoader;
import com.willfp.eco.core.factory.MetadataValueFactory;
import com.willfp.eco.core.factory.NamespacedKeyFactory;
import com.willfp.eco.core.factory.RunnableFactory;
import com.willfp.eco.core.fast.FastItemStack;
import com.willfp.eco.core.gui.GUIFactory;
import com.willfp.eco.core.integrations.placeholder.PlaceholderIntegration;
import com.willfp.eco.core.proxy.Cleaner;
import com.willfp.eco.core.proxy.ProxyFactory;
import com.willfp.eco.core.requirement.RequirementFactory;
import com.willfp.eco.core.scheduling.Scheduler;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Logger;

/**
 * @see Eco#getHandler()
 */
@ApiStatus.Internal
public interface Handler {
    /**
     * Create a scheduler.
     *
     * @param plugin The plugin.
     * @return The scheduler.
     */
    @NotNull
    Scheduler createScheduler(@NotNull EcoPlugin plugin);

    /**
     * Create an event manager.
     *
     * @param plugin The plugin.
     * @return The event manager.
     */
    @NotNull
    EventManager createEventManager(@NotNull EcoPlugin plugin);

    /**
     * Create a NamespacedKey factory.
     *
     * @param plugin The plugin.
     * @return The factory.
     */
    @NotNull
    NamespacedKeyFactory createNamespacedKeyFactory(@NotNull EcoPlugin plugin);

    /**
     * Create a MetadataValue factory.
     *
     * @param plugin The plugin.
     * @return The factory.
     */
    @NotNull
    MetadataValueFactory createMetadataValueFactory(@NotNull EcoPlugin plugin);

    /**
     * Create a Runnable factory.
     *
     * @param plugin The plugin.
     * @return The factory.
     */
    @NotNull
    RunnableFactory createRunnableFactory(@NotNull EcoPlugin plugin);

    /**
     * Create an ExtensionLoader.
     *
     * @param plugin The plugin.
     * @return The factory.
     */
    @NotNull
    ExtensionLoader createExtensionLoader(@NotNull EcoPlugin plugin);

    /**
     * Create a config handler.
     *
     * @param plugin The plugin.
     * @return The handler.
     */
    @NotNull
    ConfigHandler createConfigHandler(@NotNull EcoPlugin plugin);

    /**
     * Create a logger.
     *
     * @param plugin The plugin.
     * @return The logger.
     */
    @NotNull
    Logger createLogger(@NotNull EcoPlugin plugin);

    /**
     * Create a PAPI integration.
     *
     * @param plugin The plugin.
     * @return The integration.
     */
    @NotNull
    PlaceholderIntegration createPAPIIntegration(@NotNull EcoPlugin plugin);

    /**
     * Create a proxy factory.
     *
     * @param plugin The plugin.
     * @return The factory.
     */
    @NotNull
    ProxyFactory createProxyFactory(@NotNull EcoPlugin plugin);

    /**
     * Get eco Spigot plugin.
     *
     * @return The plugin.
     */
    @NotNull
    EcoPlugin getEcoPlugin();

    /**
     * Get config factory.
     *
     * @return The factory.
     */
    @NotNull
    ConfigFactory getConfigFactory();

    /**
     * Get drop queue factory.
     *
     * @return The factory.
     */
    @NotNull
    DropQueueFactory getDropQueueFactory();

    /**
     * Get GUI factory.
     *
     * @return The factory.
     */
    @NotNull
    GUIFactory getGUIFactory();

    /**
     * Get cleaner.
     *
     * @return The cleaner.
     */
    @NotNull
    Cleaner getCleaner();

    /**
     * Add new plugin.
     *
     * @param plugin The plugin.
     */
    void addNewPlugin(@NotNull EcoPlugin plugin);

    /**
     * Get plugin by name.
     *
     * @param name The name.
     * @return The plugin.
     */
    @Nullable
    EcoPlugin getPluginByName(@NotNull String name);

    /**
     * Get all loaded eco plugins.
     *
     * @return A list of plugin names in lowercase.
     */
    @NotNull
    List<String> getLoadedPlugins();

    /**
     * Create a FastItemStack.
     *
     * @param itemStack The base ItemStack.
     * @return The FastItemStack.
     */
    @NotNull
    FastItemStack createFastItemStack(@NotNull ItemStack itemStack);

    /**
     * Register bStats metrics.
     *
     * @param plugin The plugin.
     */
    void registerBStats(@NotNull EcoPlugin plugin);

    /**
     * Get the requirement factory.
     *
     * @return The factory.
     */
    @NotNull
    RequirementFactory getRequirementFactory();

    /**
     * Get Adventure audiences.
     *
     * @return The audiences.
     */
    @Nullable
    BukkitAudiences getAdventure();

    /**
     * Get the key registry.
     *
     * @return The registry.
     */
    @NotNull
    KeyRegistry getKeyRegistry();

    /**
     * Get the PlayerProfile handler.
     *
     * @return The handler.
     */
    PlayerProfileHandler getPlayerProfileHandler();
}
