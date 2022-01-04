package me.markings.bubble.command.bubble;

import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.SimpleLocalization;

public class ReloadCommand extends SimpleSubCommand {

	protected ReloadCommand() {
		super("reload|rl");

		setPermission(Permissions.Command.RELOAD);
	}

	@Override
	protected void onCommand() {
		try {
			Bubble.getInstance().reload();
			tell(SimpleLocalization.Commands.RELOAD_SUCCESS);
		} catch (final @NotNull Exception t) {
			t.printStackTrace();
			tell(SimpleLocalization.Commands.RELOAD_FAIL.replace("{error}", t.toString()));
		}
	}
}