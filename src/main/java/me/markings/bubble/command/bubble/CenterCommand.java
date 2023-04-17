package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcast;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;

public class CenterCommand extends SimpleSubCommand {

    protected CenterCommand() {
        super("center");

        setMinArguments(1);
        setDescription("Toggle centering for broadcast messages.");
        setUsage("<broadcast_name/all>");
        setPermission(Permissions.Command.CENTER);
    }

    @Override
    protected void onCommand() {
        final String broadcast = args[0];
        final Broadcast broadcastInstance = Broadcast.getBroadcast(broadcast);

        if (args[0].equalsIgnoreCase("all"))
            Broadcast.toggleCenteredAll();
        else
            broadcastInstance.toggleCentered();

        tellSuccess("Centering for broadcast " + broadcast + " has been " + (broadcastInstance.getCentered() ? "&aenabled" : "&cdisabled") + "&7!");
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        return new String[]{
                "/{label} {sublabel} <broadcast_name>&7 - Centers the given broadcast message.",
                "/{label} {sublabel} all&7 - Centers all broadcast messages in the 'broadcasts' folder.",
        };
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(Broadcast.getAllBroadcastNames(), "all");

        return new ArrayList<>();
    }
}
