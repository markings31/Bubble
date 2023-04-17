package me.markings.bubble.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.markings.bubble.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.SimpleExpansion;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Placeholders extends SimpleExpansion {

    @Getter
    private static final SimpleExpansion instance = new Placeholders();

    @Override
    protected String onReplace(@NonNull final CommandSender sender, final String identifier) {
        final Player player = sender instanceof Player && ((Player) sender).isOnline() ? (Player) sender : null;

        assert player != null;

        switch (identifier) {
            case "bc_status":
                return String.valueOf(PlayerData.from(player).isBroadcastStatus());
            case "bc_sound_status":
                return String.valueOf(PlayerData.from(player).isBroadcastSoundStatus());
            case "motd_status":
                return String.valueOf(PlayerData.from(player).isMotdStatus());
            default:
                return NO_REPLACE;
        }
    }
}
