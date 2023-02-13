package me.markings.bubble.command.bubble;

import lombok.Getter;
import me.markings.bubble.menus.BroadcastSelectionMenu;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.command.SimpleSubCommand;

public class EditCommand extends SimpleSubCommand {

    @Getter
    public static String input;

    protected EditCommand() {
        super("edit");

        setMinArguments(0);
        setDescription("Edit the contents of each broadcast message.");
        setPermission(Permissions.BroadcastEditing.EDIT);
    }

    @Override
    protected void onCommand() {
        if (args.length != 1)
            new BroadcastSelectionMenu(getPlayer()).displayTo(getPlayer());
        else new BroadcastSelectionMenu.EditMenu(getPlayer(), args[0]).displayTo(getPlayer());
    }
}
