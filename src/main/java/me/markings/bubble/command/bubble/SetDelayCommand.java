package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetDelayCommand extends SimpleSubCommand {

	protected SetDelayCommand() {
		super("setdelay");

		setMinArguments(1);
		setDescription("Set the delay between each broadcast message.");
		setUsage("<time>");
		setPermission(Permissions.Command.DELAY);
	}

	@Override
	protected void onCommand() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val param = joinArgs(0);


		checkBoolean(param.contains("second")
						|| param.contains("minute")
						|| param.contains("hour")
						|| param.contains("day"),
				"Incorrect time format! (Example: 60 seconds, 1 minute, etc.)");

		config.set("Notifications.Broadcast.Delay", param);

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully set broadcast delay to " + param,
				"&cFailed to set the broadcast delay! Error: ", config);
	}

	@Override
	protected String[] getMultilineUsageMessage() {
		val commandLabel = "&f/bu " + getSublabel();
		return new String[]{
				commandLabel + " 1 minute &7 - Sets the broadcast delay to 1 minute.",
				commandLabel + " 30 seconds &7 - Sets the broadcast delay to 30 seconds."
		};
	}
}
