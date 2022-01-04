package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class SetHeaderCommand extends SimpleSubCommand {

	protected SetHeaderCommand() {
		super("setheader");

		setMinArguments(1);
		setUsage("<header>");
		setPermission(Permissions.Command.HEADER);
	}

	@Override
	protected void onCommand() {
		val config = Bubble.getInstance().getBubbleSettings();

		config.set("Notifications.Broadcast.Header", args[0]);

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully set the broadcast header!",
				"&cFailed to set the broadcast header! Error: ");
	}
}
