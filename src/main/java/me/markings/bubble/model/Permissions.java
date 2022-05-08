package me.markings.bubble.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.command.annotation.Permission;
import org.mineacademy.fo.command.annotation.PermissionGroup;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Permissions {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@PermissionGroup("Main Command")
	public static final class Command {

		@Permission("Add a line or section for broadcasts within the main settings file.")
		public static final String ADD = "bubble.command.add";

		@Permission("Toggle a broadcast message's alignment status.")
		public static final String CENTER = "bubble.command.center";

		@Permission("Edit broadcasts messages in the configuration.")
		public static final String EDIT = "bubble.command.edit";

		@Permission("Access the GUI menu for Bubble.")
		public static final String GUI = "bubble.command.gui";

		@Permission("View a specific broadcast message and its settings content.")
		public static final String SHOW = "bubble.command.show";

		@Permission("Notify a player/the server via a command.")
		public static final String NOTIFY = "bubble.command.notify";

		@Permission("Remove a broadcast section or specific lines from the main settings file.")
		public static final String REMOVE = "bubble.command.remove";

		@Permission("Set the delay between each broadcast message.")
		public static final String DELAY = "bubble.command.setdelay";

		@Permission("Announce a message to all players on the server.")
		public static final String ANNOUNCE = "bubble.command.announce";

		@Permission("Configure your notification preferences via an inventory menu.")
		public static final String PREFS = "bubble.command.prefs";

		@Permission("Toggle the status of your broadcast messages.")
		public static final String TOGGLE = "bubble.command.togglebroadcasts";

		@Permission("Set the header that will be used in broadcast messages.")
		public static final String HEADER = "bubble.command.setheader";

		@Permission("Set the footer that will be used in broadcast messages.")
		public static final String FOOTER = "bubble.command.setfooter";

		@Permission("Send messages to the conneected Discord server.")
		public static final String DISCORD_COMMAND = "bubble.discord.command";
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@PermissionGroup("Discord API")
	public static final class Discord {
		@Permission("Send a webhook message to the connected Discord server.")
		public static final String DISCORD_MESSAGE = "bubble.discord.message";

		@Permission("Send an announcement message to the connected Discord server.")
		public static final String DISCORD_ANNOUNCEMENT = "bubble.discord.announcement";
	}
}
