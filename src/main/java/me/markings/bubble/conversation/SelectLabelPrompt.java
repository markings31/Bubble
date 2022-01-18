package me.markings.bubble.conversation;

import lombok.val;
import me.markings.bubble.Bubble;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

public class SelectLabelPrompt extends SimplePrompt {

	private String input;

	@Override
	protected String getPrompt(final ConversationContext conversationContext) {
		Remain.sendTitle((Player) conversationContext.getForWhom(), "&9Edit Message", "Please type your message in the chat.");
		return Messenger.getInfoPrefix() + "&7What message group do you wish to edit?\n&7&oNote: Type 'exit' to cancel.";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		val config = Bubble.getInstance().getBubbleSettings();
		val path = "Notifications.Broadcast.Messages." + s;
		input = s;

		if (!config.isSet(path))
			Messenger.error((CommandSender) conversationContext.getForWhom(), "&cCould not find message group " + s + "!");
		else
			Common.dispatchCommandAsPlayer((Player) conversationContext.getForWhom(), "bu edit " + s);

		return Prompt.END_OF_CONVERSATION;
	}
}
