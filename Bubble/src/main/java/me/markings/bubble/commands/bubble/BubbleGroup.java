package me.markings.bubble.commands.bubble;

import me.markings.bubble.commands.NotificationCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class BubbleGroup extends SimpleCommandGroup {

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new NotificationCommand(this));
		registerSubcommand(new ReloadCommand(this));
	}

	@Override
	protected String getCredits() {
		return "&7Visit &f&nhttps://markings.me/ &r&7 for more information.";
	}

	@Override
	protected String getHeaderPrefix() {
		return "&b&l";
	}
}
