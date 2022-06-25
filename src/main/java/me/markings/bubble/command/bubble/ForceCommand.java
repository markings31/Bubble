package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.command.SimpleSubCommand;

public class ForceCommand extends SimpleSubCommand {

	protected ForceCommand() {
		super("force");

		setDescription("Force the next broadcast message to be displayed in the sequence.");
		setPermission(Permissions.Command.FORCE_COMMAND);
	}

	@Override
	protected void onCommand() {
		BroadcastTask.nextCycle();
	}
}
