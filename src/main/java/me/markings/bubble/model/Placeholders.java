package me.markings.bubble.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import me.markings.bubble.PlayerData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.model.SimpleExpansion;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Placeholders extends SimpleExpansion {

	@Getter
	private static final SimpleExpansion instance = new Placeholders();

	@Override
	protected String onReplace(@NonNull final CommandSender sender, final String identifier) {
		final Player player = sender instanceof Player && ((Player) sender).isOnline() ? (Player) sender : null;

		assert player != null;

		switch (identifier) {
			case "broadcasts_enabled":
				return String.valueOf(PlayerData.getCache(player).isBroadcastStatus());
			case "broadcast_sound_enabled":
				return String.valueOf(PlayerData.getCache(player).isBroadcastSoundStatus());
			case "motd_enabled":
				return String.valueOf(PlayerData.getCache(player).isMotdStatus());
			case "mentions_enabled":
				return String.valueOf(PlayerData.getCache(player).isMentionsStatus());
			case "mentions_sound_enabled":
				return String.valueOf(PlayerData.getCache(player).isMentionSoundStatus());
			case "mentions_toast_enabled":
				return String.valueOf(PlayerData.getCache(player).isMentionToastStatus());
			case "ping":
				return player.getPing() + "ms";
			case "tps":
				return String.valueOf(Remain.getTPS());
			default:
				return NO_REPLACE;
		}
	}
}
