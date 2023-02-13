package me.markings.bubble.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.command.annotation.Permission;
import org.mineacademy.fo.command.annotation.PermissionGroup;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Permissions {

    @Permission("Announce a message to all players on the server.")
    public static final String ANNOUNCE = "bubble.announce";

    @Permission("Configure your notification preferences via an inventory menu.")
    public static final String PREFS = "bubble.preferences";

    @Permission("Toggle the status of your broadcast messages.")
    public static final String TOGGLE = "bubble.togglebroadcasts";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @PermissionGroup("Main Command")
    public static final class Command {

        @Permission("Add a line or section for broadcasts within the main settings file.")
        public static final String ADD = "bubble.command.add";

        @Permission("Toggle a broadcast message's alignment status.")
        public static final String CENTER = "bubble.command.center";

        @Permission("View a specific broadcast message and its settings content.")
        public static final String SHOW = "bubble.command.show";

        @Permission("Notify a player/the server via a command.")
        public static final String NOTIFY = "bubble.command.notify";

        @Permission("Force the next broadcast message to be displayed in the sequence.")
        public static final String FORCE_COMMAND = "bubble.command.force";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @PermissionGroup("Broadcast Editor")
    public static final class BroadcastEditing {
        @Permission("Edit broadcasts with /bu edit. This overrides any subpermissions.")
        public static final String EDIT = "bubble.edit";

        @Permission("Set the permission required to view the broadcast message.")
        public static final String PERMISSION = "bubble.edit.permission";

        @Permission("Set the header that will be used in broadcast messages.")
        public static final String HEADER = "bubble.edit.header";

        @Permission("Set the footer that will be used in broadcast messages.")
        public static final String FOOTER = "bubble.edit.footer";

        @Permission("Set the worlds that the broadcast will be sent to.")
        public static final String WORLDS = "bubble.edit.worlds";
    }
}
