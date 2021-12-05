package me.markings.bubble.command.bubble;

import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleSubCommand;

public class GUICommand extends SimpleSubCommand {

	protected GUICommand() {
		super("gui");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		Messenger.info(getPlayer(), "Please purchase Bubble Pro for GUI access!");
	}
}
