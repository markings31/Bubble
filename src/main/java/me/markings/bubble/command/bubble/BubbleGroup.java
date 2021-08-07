package me.markings.bubble.command.bubble;

import me.markings.bubble.command.NotificationCommand;
import org.mineacademy.fo.command.SimpleCommandGroup;

public class BubbleGroup extends SimpleCommandGroup {

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new NotificationCommand());
		registerSubcommand(new ReloadCommand());
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
