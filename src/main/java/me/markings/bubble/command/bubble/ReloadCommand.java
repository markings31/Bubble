package me.markings.bubble.command.bubble;

import me.markings.bubble.Bubble;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.SimpleLocalization;

public class ReloadCommand extends SimpleSubCommand {

	protected ReloadCommand() {
		super("reload|rl");
	}

	@Override
	protected void onCommand() {
		try {
			Bubble.getInstance().reload();
			tell(SimpleLocalization.Commands.RELOAD_SUCCESS);
		} catch (final @NotNull Throwable t) {
			t.printStackTrace();
			tell(SimpleLocalization.Commands.RELOAD_FAIL.replace("{error}", t.toString()));
		}
	}
}