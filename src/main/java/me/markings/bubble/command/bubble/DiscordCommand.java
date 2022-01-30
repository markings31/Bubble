package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.hook.DiscordSRVHook;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.awt.*;

public class DiscordCommand extends SimpleSubCommand {

	protected DiscordCommand() {
		super("discord");

		setMinArguments(2);
		setUsage("<announcement|message> [<image_url>] <message>");
		setPermission(Permissions.Command.DISCORD);
	}

	@Override
	protected void onCommand() {
		val input = joinArgs(2).split("\\|");
		val discord = DiscordSRVHook.getInstance();

		if (args[0].equalsIgnoreCase("announcement")) {
			checkBoolean(input.length >= 2, "Please specify a description for the message!");

			if (args[1].contains(".png") || args[1].contains(".jpg"))
				discord.sendAnnouncement(input[0], input[1], Color.MAGENTA, args[1]);
			else discord.sendAnnouncement(input[0], input[1], Color.MAGENTA);
		} else if (args[0].equalsIgnoreCase("message")) discord.sendWebhookMessage(getPlayer(), args[1]);

		tellSuccess("&aSuccessfully sent to Discord!");
	}
}
