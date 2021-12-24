package me.markings.bubble.command;

import me.markings.bubble.menu.NotificationsMenu;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.command.SimpleCommand;

public class PrefsCommand extends SimpleCommand {

	public PrefsCommand() {
		super("preferences|prefs|pref");
		
		setPermission(Permissions.Command.PREFS);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		new NotificationsMenu().displayTo(getPlayer());
	}
}
