package me.markings.bubble.conversation;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcasts;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

public class SetHeaderPrompt extends SimplePrompt {

    private final String broadcastLabel;

    public SetHeaderPrompt(final String broadcastLabel) {
        this.broadcastLabel = broadcastLabel;
    }

    @Override
    protected String getPrompt(final ConversationContext conversationContext) {
        Remain.sendTitle((Player) conversationContext.getForWhom(), "&9Set Header", "Please type your message in the chat.");
        return "What would you like to set the header to?\n&7Note: Type 'exit' to cancel.";
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
        Valid.checkPermission(getPlayer(conversationContext), Permissions.BroadcastEditing.HEADER);
        final Broadcasts broadcast = Broadcasts.getBroadcast(broadcastLabel);
        broadcast.setHeader(s);
        Messenger.success(getPlayer(conversationContext), "Successfully set new broadcast header!");

        return Prompt.END_OF_CONVERSATION;
    }
}