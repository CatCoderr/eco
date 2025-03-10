package com.willfp.eco.util;

import com.willfp.eco.core.Eco;
import com.willfp.eco.core.Prerequisite;
import com.willfp.eco.core.data.PlayerProfile;
import com.willfp.eco.core.data.keys.PersistentDataKey;
import com.willfp.eco.core.data.keys.PersistentDataKeyType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Utilities / API methods for players.
 */
public final class PlayerUtils {
    /**
     * The data key for saved player names.
     */
    private static final PersistentDataKey<String> PLAYER_NAME_KEY = new PersistentDataKey<>(
            NamespacedKeyUtils.createEcoKey("player_name"),
            PersistentDataKeyType.STRING,
            "Unknown Player"
    );

    /**
     * Get the audience from a player.
     *
     * @param player The player.
     * @return The audience.
     */
    @NotNull
    public static Audience getAudience(@NotNull final Player player) {
        BukkitAudiences adventure = Eco.getHandler().getAdventure();

        if (Prerequisite.HAS_PAPER.isMet()) {
            if (player instanceof Audience) {
                return (Audience) player;
            } else {
                return Audience.empty();
            }
        } else {
            if (adventure == null) {
                return Audience.empty();
            } else {
                return adventure.player(player);
            }
        }
    }

    /**
     * Get the audience from a command sender.
     *
     * @param sender The command sender.
     * @return The audience.
     */
    @NotNull
    public static Audience getAudience(@NotNull final CommandSender sender) {
        BukkitAudiences adventure = Eco.getHandler().getAdventure();

        if (Prerequisite.HAS_PAPER.isMet()) {
            if (sender instanceof Audience) {
                return (Audience) sender;
            } else {
                return Audience.empty();
            }
        } else {
            if (adventure == null) {
                return Audience.empty();
            } else {
                return adventure.sender(sender);
            }
        }
    }

    /**
     * Get saved display name for an offline player.
     *
     * @param player The player.
     * @return The player name.
     */
    public static String getSavedDisplayName(@NotNull final OfflinePlayer player) {
        PlayerProfile profile = PlayerProfile.load(player);

        if (player instanceof Player onlinePlayer) {
            profile.write(PLAYER_NAME_KEY, onlinePlayer.getDisplayName());
        }

        String saved = profile.read(PLAYER_NAME_KEY);

        if (saved.equals(PLAYER_NAME_KEY.getDefaultValue())) {
            String name = player.getName();
            if (name != null) {
                profile.write(PLAYER_NAME_KEY, player.getName());
                return player.getName();
            }
        }

        return saved;
    }

    /**
     * Update the saved display name for a player.
     *
     * @param player The player.
     */
    public static void updateSavedDisplayName(@NotNull final Player player) {
        PlayerProfile profile = PlayerProfile.load(player);
        profile.write(PLAYER_NAME_KEY, player.getDisplayName());
    }

    private PlayerUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
