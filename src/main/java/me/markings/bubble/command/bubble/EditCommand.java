package me.markings.bubble.command.bubble;

import lombok.Getter;
import me.markings.bubble.menus.BroadcastSelectionMenu;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcast;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.List;

public class EditCommand extends SimpleSubCommand {

    @Getter
    public static String input;

    protected EditCommand() {
        super("edit");

        setUsage("[broadcast_name]");
        this.setDescription("Edit the contents of each broadcast message.");
        this.setPermission(Permissions.BroadcastEditing.EDIT);
    }

    @Override
    protected void onCommand() {
        this.checkConsole();

        if (args.length > 0) {
            checkNotNull(Broadcast.getBroadcast(args[0]), "Broadcast " + args[0] + " does not exist! Available: " + Common.join(Broadcast.getAllBroadcastNames()));

            new BroadcastSelectionMenu.EditMenu(getPlayer(), args[0]).displayTo(getPlayer());
        } else new BroadcastSelectionMenu(getPlayer()).displayTo(getPlayer());
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(Broadcast.getAllBroadcastNames());
        else return null;
    }
}
