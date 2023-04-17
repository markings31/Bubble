package me.markings.bubble.conversation;

import lombok.RequiredArgsConstructor;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Notification;
import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.settings.Broadcast;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrefix;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class CreateConverstation extends SimpleConversation {

    private final String broadcastLabel;

    public CreateConverstation(final String broadcastLabel) {
        this.broadcastLabel = broadcastLabel;
    }

    @Override
    protected Prompt getFirstPrompt() {
        return new MessageContentPrompt(broadcastLabel);
    }

    @Override
    protected ConversationPrefix getPrefix() {
        return new SimplePrefix("&8[&9Bubble Prompt&8] &7");
    }

    public static class MessageContentPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private Broadcast broadcast;

        private MessageContentPrompt(final String broadcastLabel) {
            this.broadcastLabel = broadcastLabel;
        }

        public MessageContentPrompt(final Broadcast broadcast) {
            this(broadcast.getBroadcastName());
            this.broadcast = broadcast;
        }

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bMessage|&7Enter the content of the broadcast message (type 'exit' to cancel)");
            CompSound.ENTITY_EXPERIENCE_ORB_PICKUP.play(getPlayer(context));
            return "&7Enter the content of the broadcast message (type 'exit' to cancel)."
                    + "\n&7Note: You can use the pipe symbol (|) to separate multiple lines of text.";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            final List<String> broadcastContent = new ArrayList<>(Arrays.asList(s.split("\\|")));

            if (broadcast != null) {
                broadcast.setMessage(broadcastContent);
                return END_OF_CONVERSATION;
            } else return new PermissionPrompt(broadcastLabel, broadcastContent);
        }
    }

    public static class PermissionPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private final List<String> broadcastContent;

        private Broadcast broadcast;

        private PermissionPrompt(final String broadcastLabel, final List<String> broadcastContent) {
            this.broadcastLabel = broadcastLabel;
            this.broadcastContent = broadcastContent;
        }

        public PermissionPrompt(final Broadcast broadcast) {
            this(broadcast.getBroadcastName(), broadcast.getMessage());
            this.broadcast = broadcast;
        }

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bPermission|&7Enter the permission to view the broadcast (type 'exit' to cancel)");
            return "&7Enter the permission to view the broadcast (type 'exit' to cancel).";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            if (broadcast != null) {
                broadcast.setPermission(s);
                return END_OF_CONVERSATION;
            } else return new WorldPrompt(broadcastLabel, broadcastContent, s);
        }
    }

    @RequiredArgsConstructor
    public static class WorldPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private final List<String> broadcastContent;

        private final String permission;

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bWorlds|&7Enter the worlds where the broadcast will be displayed (type 'exit' to cancel)");
            return "&7Enter the worlds where the broadcast will be displayed (type 'exit' to cancel)."
                    + "Note: Separate multiple worlds with a comma.";
        }

        @Override
        protected boolean isInputValid(final ConversationContext context, final String input) {
            final List<String> worlds = new ArrayList<>(Arrays.asList(input.split(",")));

            return new HashSet<>(Common.getWorldNames()).containsAll(worlds);
        }

        @Override
        protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
            return Messenger.getErrorPrefix() + "One or more world name is invalid!";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            final List<String> worlds = new ArrayList<>(Arrays.asList(s.split(",")));

            return new HeaderPrompt(broadcastLabel, broadcastContent, permission, worlds);
        }
    }

    // TODO: Add header and footer presets for the player to select in the chat.
    public static class HeaderPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private final List<String> broadcastContent;

        private final String permission;

        private final List<String> worlds;

        private Broadcast broadcast;

        private HeaderPrompt(final String broadcastLabel, final List<String> broadcastContent, final String permission, final List<String> worlds) {
            this.broadcastLabel = broadcastLabel;
            this.broadcastContent = broadcastContent;
            this.permission = permission;
            this.worlds = worlds;
        }

        public HeaderPrompt(final Broadcast broadcast) {
            this(broadcast.getBroadcastName(), broadcast.getMessage(), broadcast.getPermission(), broadcast.getWorlds());
            this.broadcast = broadcast;
        }

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bHeader|&7Enter the header of the broadcast message (type 'exit' to cancel)");
            return "&7Enter the header of the broadcast message (type 'exit' to cancel).";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            if (broadcast != null) {
                broadcast.setHeader(s);
                return END_OF_CONVERSATION;
            } else return new FooterPrompt(broadcastLabel, broadcastContent, permission, worlds, s);
        }
    }

    public static class FooterPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private final List<String> broadcastContent;

        private final String permission;

        private final List<String> worlds;

        private final String header;

        private Broadcast broadcast;

        private FooterPrompt(final String broadcastLabel, final List<String> broadcastContent, final String permission, final List<String> worlds, final String header) {
            this.broadcastLabel = broadcastLabel;
            this.broadcastContent = broadcastContent;
            this.permission = permission;
            this.worlds = worlds;
            this.header = header;
        }

        public FooterPrompt(final Broadcast broadcast) {
            this(broadcast.getBroadcastName(), broadcast.getMessage(), broadcast.getPermission(), broadcast.getWorlds(), broadcast.getHeader());
            this.broadcast = broadcast;
        }

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bFooter|&7Enter the footer of the broadcast message (type 'exit' to cancel)");
            return "&7Enter the footer of the broadcast message (type 'exit' to cancel).";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            if (broadcast != null) {
                broadcast.setFooter(s);
                return END_OF_CONVERSATION;
            } else return new CenteredPrompt(broadcastLabel, broadcastContent, permission, worlds, header, s);
        }
    }

    @RequiredArgsConstructor
    private static class CenteredPrompt extends SimplePrompt {

        private final String broadcastLabel;

        private final List<String> broadcastContent;

        private final String permission;

        private final List<String> worlds;

        private final String header;

        private final String footer;

        @Override
        protected String getPrompt(final ConversationContext context) {
            Notification.send(new Tuple<>(Bubble.getInstance().getServer().getConsoleSender(), getPlayer(context)),
                    NotificationTypes.TITLE.getLabel(),
                    "&bCentered Status|&7Should the broadcast be vertically centered? (Y/N; type 'exit' to cancel)");
            return "&7Should the broadcast be vertically centered (Y/N; type 'exit' to cancel)?";
        }

        @Override
        protected boolean isInputValid(final ConversationContext context, final String input) {
            return input.equalsIgnoreCase("Y")
                    || input.equalsIgnoreCase("N")
                    || input.equalsIgnoreCase("yes")
                    || input.equalsIgnoreCase("no");
        }

        @Override
        protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
            return "Please enter either 'Y' or 'N'.";
        }

        @Nullable
        @Override
        protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
            final boolean centered = s.equalsIgnoreCase("y") || s.equalsIgnoreCase("yes");

            Broadcast.createBroadcast(
                    broadcastLabel,
                    header,
                    footer,
                    new SimpleSound(CompSound.NOTE_PLING.getSound().name()),
                    broadcastContent,
                    permission,
                    centered,
                    worlds);

            Messenger.success(getPlayer(conversationContext), "&aSuccessfully created new broadcast " + broadcastLabel + "!");
            return END_OF_CONVERSATION;
        }
    }

    @Override
    protected void onConversationEnd(final ConversationAbandonedEvent event, final boolean canceledFromInactivity) {
        final Player promptPlayer = (Player) event.getContext().getForWhom();

        if (!event.gracefulExit()) {
            if (!canceledFromInactivity)
                Messenger.error(promptPlayer, "Successfully exited from the broadcast creation prompt.");
            else {
                CompSound.NOTE_BASS.play(promptPlayer);
                Messenger.error(promptPlayer, "Exited from the broadcast creation prompt due to inactivity.");
            }
        }
    }
}
