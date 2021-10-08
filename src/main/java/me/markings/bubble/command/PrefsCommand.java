package me.markings.bubble.command;

import me.markings.bubble.menu.NotificationsMenu;
import org.mineacademy.fo.command.SimpleCommand;

public class PrefsCommand extends SimpleCommand {

	public PrefsCommand() {
		super("preferences|prefs|pref");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		new NotificationsMenu().displayTo(getPlayer());
	}
}
