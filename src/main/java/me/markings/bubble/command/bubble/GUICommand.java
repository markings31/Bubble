package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleSubCommand;

public class GUICommand extends SimpleSubCommand {

	protected GUICommand() {
		super("gui");

		setPermission(Permissions.Command.GUI);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Messenger.info(getPlayer(), "Please purchase Bubble Pro for GUI access!");
	}
}
