package com.willfp.eco.core.config.json;

import com.willfp.eco.core.Eco;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.PluginLike;
import com.willfp.eco.core.config.json.wrapper.LoadableJSONConfigWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Config implementation for configs present in the plugin's base directory (eg config.json).
 * <p>
 * Automatically updates.
 */
public abstract class JSONBaseConfig extends LoadableJSONConfigWrapper {
    /**
     * @param configName      The name of the config
     * @param removeUnused    Whether keys not present in the default config should be removed on update.
     * @param plugin          The plugin.
     * @param updateBlacklist Substring of keys to not add/remove keys for.
     */
    protected JSONBaseConfig(@NotNull final String configName,
                             final boolean removeUnused,
                             @NotNull final PluginLike plugin,
                             @NotNull final String... updateBlacklist) {
        super(
                Eco.getHandler().getConfigFactory().createUpdatableJSONConfig(
                        configName,
                        plugin,
                        "",
                        plugin.getClass(),
                        removeUnused, updateBlacklist
                )
        );
    }

    /**
     * @param configName   The name of the config
     * @param removeUnused Whether keys not present in the default config should be removed on update.
     * @param plugin       The plugin.
     */
    protected JSONBaseConfig(@NotNull final String configName,
                             final boolean removeUnused,
                             @NotNull final PluginLike plugin) {
        super(
                Eco.getHandler().getConfigFactory().createUpdatableJSONConfig(
                        configName,
                        plugin,
                        "",
                        plugin.getClass(),
                        removeUnused
                )
        );
    }

    /**
     * @param configName      The name of the config
     * @param removeUnused    Whether keys not present in the default config should be removed on update.
     * @param plugin          The plugin.
     * @param updateBlacklist Substring of keys to not add/remove keys for.
     */
    protected JSONBaseConfig(@NotNull final String configName,
                             final boolean removeUnused,
                             @NotNull final EcoPlugin plugin,
                             @NotNull final String... updateBlacklist) {
        this(configName, removeUnused, (PluginLike) plugin, updateBlacklist);
    }

    /**
     * @param configName   The name of the config
     * @param removeUnused Whether keys not present in the default config should be removed on update.
     * @param plugin       The plugin.
     */
    protected JSONBaseConfig(@NotNull final String configName,
                             final boolean removeUnused,
                             @NotNull final EcoPlugin plugin) {
        this(configName, removeUnused, (PluginLike) plugin);
    }
}
