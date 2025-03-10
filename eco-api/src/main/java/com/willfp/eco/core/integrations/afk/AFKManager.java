package com.willfp.eco.core.integrations.afk;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to handle afk integrations.
 */
public final class AFKManager {
    /**
     * A set of all registered integrations.
     */
    private static final Set<AFKWrapper> REGISTERED = new HashSet<>();

    /**
     * Register a new integration.
     *
     * @param integration The integration to register.
     */
    public static void register(@NotNull final AFKWrapper integration) {
        REGISTERED.add(integration);
    }

    /**
     * Get if a player is afk.
     *
     * @param player The player.
     * @return If afk.
     */
    public static boolean isAfk(@NotNull final Player player) {
        for (AFKWrapper afkWrapper : REGISTERED) {
            if (afkWrapper.isAfk(player)) {
                return true;
            }
        }

        return false;
    }

    private AFKManager() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
