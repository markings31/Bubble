package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetDelayCommand extends SimpleSubCommand {

	protected SetDelayCommand() {
		super("setdelay");

		setMinArguments(1);
		setUsage("<time>");
		setPermission(Permissions.Command.DELAY);
	}

	@Override
	protected void onCommand() {
		val config = Bubble.getInstance().getBubbleSettings();
		val param = joinArgs(0);


		checkBoolean(param.contains("seconds")
						|| param.contains("minutes")
						|| param.contains("hours")
						|| param.contains("days"),
				"Incorrect time format! (Example: 60 seconds, 1 minute, etc.)");

		config.set("Notifications.Broadcast.Delay", param);

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully set broadcast delay to " + param,
				"&cFailed to set the broadcast delay! Error: ");
	}
}
