package me.markings.bubble.command.bubble;

import github.scarsz.discordsrv.DiscordSRV;
import lombok.val;
import me.markings.bubble.hook.DiscordSRVHook;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class DiscordCommand extends SimpleSubCommand {

	protected DiscordCommand() {
		super("discord");

		setMinArguments(2);
		setUsage("<announcement|message> [<image_url>] <color> <message>");
		setPermission(Permissions.Command.DISCORD_COMMAND);
	}

	@Override
	protected void onCommand() {
		val hasImage = args[1].contains(".png") || args[1].contains(".jpg");
		val input = joinArgs(hasImage ? 3 : 2).split("\\|");
		val discord = DiscordSRVHook.getInstance();
		val color = MessageUtil.getColor(hasImage ? args[2] : args[1]);

		// Logo image: https://i.ibb.co/dBDtCvT/M.png
		checkBoolean(color != null, "You must specify a color for the message first!");
		if (args[0].equalsIgnoreCase("announcement") || args[0].equalsIgnoreCase("announce")) {
			checkBoolean(input.length >= 2, "Please specify a description for the message!");
			checkBoolean(!Settings.DiscordSettings.ANNOUNCEMENTSID.equals("000000000000000000"),
					"No announcements channel has been set!");

			if (hasImage)
				discord.sendAnnouncement(getPlayer(), input[0], input[1], color, args[1]);
			else discord.sendAnnouncement(getPlayer(), input[0], input[1], color, DiscordSRV.getAvatarUrl(getPlayer()));
		} else if (args[0].equalsIgnoreCase("message")) {
			checkBoolean(!Settings.DiscordSettings.MINECRAFTID.equals("000000000000000000"),
					"No minecraft message channel has been set!");
			discord.sendWebhookMessage(getPlayer(), args[1]);
		}

		tellSuccess("&aSuccessfully sent to Discord!");
	}
}
