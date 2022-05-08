package me.markings.bubble.command.bubble;

import me.markings.bubble.menu.GUIMenu;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.command.SimpleSubCommand;

public class GUICommand extends SimpleSubCommand {

	protected GUICommand() {
		super("gui");

		setDescription("Open the Bubble GUI.");
		setPermission(Permissions.Command.GUI);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		new GUIMenu().displayTo(getPlayer());
	}
}
