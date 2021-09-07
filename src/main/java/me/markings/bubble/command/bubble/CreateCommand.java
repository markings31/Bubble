package me.markings.bubble.command.bubble;

import org.mineacademy.fo.command.SimpleSubCommand;

public class CreateCommand extends SimpleSubCommand {

	public CreateCommand() {
		super("create");

		setMinArguments(1);
		setUsage("<broadcast_label>");
	}

	@Override
	protected void onCommand() {

	}
}
