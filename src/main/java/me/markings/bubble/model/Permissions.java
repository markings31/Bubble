package me.markings.bubble.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.annotation.Permission;
import org.mineacademy.fo.annotation.PermissionGroup;
import org.mineacademy.fo.constants.FoPermissions;

public class Permissions extends FoPermissions {

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@PermissionGroup("Execute main plugin command for /{label}")
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

		@Permission("Reload the plugin.")
		public static final String RELOAD = "bubble.command.reload";

		@Permission("Remove a broadcast section or specific lines from the main settings file.")
		public static final String REMOVE = "bubble.command.reload";

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
		
	}

}