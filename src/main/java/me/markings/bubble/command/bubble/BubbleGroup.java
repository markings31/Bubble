package me.markings.bubble.command.bubble;

import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class BubbleGroup extends SimpleCommandGroup {

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new NotificationCommand());
		registerSubcommand(new ReloadCommand());
		registerSubcommand(new AddCommand());
		registerSubcommand(new RemoveCommand());
		registerSubcommand(new GUICommand());
		registerSubcommand(new EditCommand());
	}

	@Override
	protected @NotNull String getCredits() {
		return "&7Visit &f&nhttps://markings.me/&r &7for more information.";
	}

	@Override
	protected @NotNull String getHeaderPrefix() {
		return "&b&l";
	}
}
