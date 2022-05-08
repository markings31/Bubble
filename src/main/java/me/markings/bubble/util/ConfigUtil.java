package me.markings.bubble.util;

import github.scarsz.discordsrv.dependencies.commons.io.FileUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import static com.massivecraft.massivecore.util.WebUtil.CONNECT_TIMEOUT;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigUtil {

	public static void toggleCentered(final String centerPath, final Player player) {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);

		config.set(centerPath, !config.getBoolean(centerPath));

		saveConfig(player, "&aSuccessfully toggled centered status of message to "
				+ (config.getBoolean(centerPath) ? "&aENABLED" : "&cDISABLED"), "&cFailed to center message! Error: ", config);
	}

	public static void saveConfig(final Player player, final String successMessage, final String errorMessage, final YamlConfiguration config) {
		val loader = CommentLoader.getSettingsInstance();
		val header = new ArrayList<>(Arrays.asList(
				"# !-----------------------------------------------------------------------------------------------!",
				"#                       Welcome to the main configuration of ${project.name}",
				"# !-----------------------------------------------------------------------------------------------!"));

		try {

			loader.load(Bubble.settingsFile);
			config.save(Bubble.settingsFile);
			loader.setHeader(header);
			loader.apply(Bubble.settingsFile);

			if (player != null)
				Messenger.success(player, successMessage + "\n&7&oP.S: If you don't see your changes take place, make sure to run /bu rl :)");
			else
				Common.log(successMessage);
		} catch (final IOException e) {
			e.printStackTrace();

			if (player != null)
				Messenger.error(player, errorMessage);
			else
				Common.log(errorMessage);
		}
	}

	public static void saveFromURL(final Player player) throws IOException {
		FileUtils.copyURLToFile(
				new URL("https://minotar.net/avatar/" + player.getName()),
				new File("plugins/Bubble/images/" + player.getUniqueId() + ".png"),
				CONNECT_TIMEOUT,
				600000);
	}
}
