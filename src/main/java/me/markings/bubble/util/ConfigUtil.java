package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Messenger;

import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtil {

	public static void toggleCentered(final String centerPath, final Player player) {
		val config = Bubble.getInstance().getBubbleSettings();

		config.set(centerPath, !config.getBoolean(centerPath));

		saveConfig(player, "&aSuccessfully toggled centered status of message to "
				+ (config.getBoolean(centerPath) ? "&aENABLED" : "&cDISABLED"), "&cFailed to center message! Error: ");
	}

	public static void saveConfig(final Player player, final String successMessage, final String errorMessage) {
		val config = Bubble.getInstance().getBubbleSettings();

		try {
			config.save(Bubble.settingsFile);
			Bubble.getInstance().reload();
			Messenger.success(player, successMessage);
		} catch (final IOException e) {
			e.printStackTrace();
			Messenger.error(player, errorMessage + e);
		}
	}

}
