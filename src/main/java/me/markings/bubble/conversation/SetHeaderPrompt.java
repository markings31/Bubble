package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.Bubble;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetHeaderPrompt extends SimplePrompt {

	@Getter
	private static final SetHeaderPrompt instance = new SetHeaderPrompt();

	@Override
	protected String getPrompt(final ConversationContext conversationContext) {
		Remain.sendTitle((Player) conversationContext.getForWhom(), "&9Set Header", "Please type your message in the chat.");
		return Messenger.getInfoPrefix() + "What would you like to set the header to? (write in the chat)\n&7&oNote: Type 'exit' to cancel.";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		Bubble.getInstance().getBubbleSettings().set("Notifications.Broadcast.Header", s);
		ConfigUtil.saveConfig((Player) conversationContext.getForWhom(),
				"&aSuccessfully set header to '" + s + "'&a!",
				"&cFailed to set header! Error: ");

		return Prompt.END_OF_CONVERSATION;
	}
}
