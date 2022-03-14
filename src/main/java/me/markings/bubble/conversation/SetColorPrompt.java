package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.Bubble;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.util.ConfigUtil;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SetColorPrompt extends SimplePrompt {

	@Getter
	private static final SetColorPrompt instance = new SetColorPrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle((Player) context.getForWhom(), "&9Set Color", "Please type your message in the chat.");
		return Localization.PromptMessages.COLOR_PROMPT_MESSAGE;
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		return MessageUtil.getColor(input) != null;
	}

	@Override
	protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
		return Messenger.getErrorPrefix() + "Invalid color! Please visit the wiki for more info on Discord functionality.";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		Bubble.getInstance().getBubbleSettings().set("Discord.Minecraft_To_Discord.Announcements_Color", s);
		ConfigUtil.saveConfig((Player) conversationContext.getForWhom(),
				"&aSuccessfully set announcement color to '" + s + "'&a!",
				"&cFailed to set announcement color! Error: ");

		return Prompt.END_OF_CONVERSATION;
	}
}
