package me.markings.bubble.command.bubble;

import me.markings.bubble.conversation.CreateConverstation;
import me.markings.bubble.settings.Broadcast;
import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCommand extends SimpleSubCommand {

    protected CreateCommand() {
        super("create");

        this.setMinArguments(1);
        this.setUsage("<name>");

        // TODO: Create in model.
        this.setPermission("temporary.permission");
    }

    @Override
    protected void onCommand() {
        this.checkConsole();
        final String broadcastLabel = joinArgs(0).replace(" ", "_");

        this.checkBoolean(Broadcast.getBroadcast(broadcastLabel) == null, "Broadcast " + broadcastLabel + " already exists!");

        new CreateConverstation(broadcastLabel).start(getPlayer());
    }
}
