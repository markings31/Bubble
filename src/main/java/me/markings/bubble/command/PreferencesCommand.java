package me.markings.bubble.command;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.MenuData;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

@AutoRegister
public final class PreferencesCommand extends SimpleCommand {

    public PreferencesCommand() {
        super("notifications|notifprefs|alerts");

        setDescription("Customize player notification preferences.");
        setPermission(Permissions.PREFS);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final String menuName = MenuData.getMenuNames().toArray()[0].toString();
        final MenuData menu = MenuData.findMenu(menuName);

        menu.displayTo(getPlayer());
    }
}
