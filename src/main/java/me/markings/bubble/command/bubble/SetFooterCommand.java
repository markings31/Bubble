package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetFooterCommand extends SimpleSubCommand {

	protected SetFooterCommand() {
		super("setfooter");

		setMinArguments(1);
		setDescription("Set the footer that will appear below each broadcast message.");
		setUsage("<footer>");
		setPermission(Permissions.Command.FOOTER);
	}

	@Override
	protected void onCommand() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);

		config.set("Notifications.Broadcast.Footer", args[0]);

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully set the broadcast footer!",
				"&cFailed to set the broadcast footer! Error: ", config);
	}
}
