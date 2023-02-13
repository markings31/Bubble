package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class AddCommand extends SimpleSubCommand {

    public AddCommand() {
        super("add");

        setMinArguments(2);
        setDescription("Add a line the given broadcast.");
        setUsage("<broadcast_name> <message>");
        setPermission(Permissions.Command.ADD);
    }

    @Override
    protected void onCommand() {
        final String broadcastName = args[0];
        final String message = !args[1].isEmpty() ? joinArgs(1) : "";

        checkBoolean(Broadcasts.getBroadcast(broadcastName) != null, "&cNo such broadcast " + args[0] + " found!");

        final Broadcasts broadcast = Broadcasts.getBroadcast(broadcastName);
        broadcast.addLineToBroadcast(message);

        tellSuccess("&aSuccessfully added line to broadcast " + broadcastName + "!");
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        return new String[]{"/{label} {sublabel} " + getUsage()};
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(Broadcasts.getAllBroadcastNames());

        return new ArrayList<>();
    }
}
