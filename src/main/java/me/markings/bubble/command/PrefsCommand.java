package me.markings.bubble.command;

import me.markings.bubble.menu.NotificationsMenu;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

@AutoRegister
public final class PrefsCommand extends SimpleCommand {

	public PrefsCommand() {
		super("notifications|notifprefs|alerts");

		setDescription("Customize your notification preferences.");
		setPermission(Permissions.Command.PREFS);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		new NotificationsMenu().displayTo(getPlayer());
	}
}
