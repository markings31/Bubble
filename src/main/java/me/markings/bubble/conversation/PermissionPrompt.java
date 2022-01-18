package me.markings.bubble.conversation;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.util.ConfigUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;

public class PermissionPrompt extends SimplePrompt {

	@Override
	protected String getPrompt(final ConversationContext context) {
		return Messenger.getInfoPrefix() + "&eWhat would you like to set the permission to? (write in the chat)\n&7&oNote: Type 'exit' to cancel.";
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		return NumberUtils.isNumber(input);
	}

	@Override
	protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
		return "Please write the permission as a String text (Example: bubble.vip).";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		val config = Bubble.getInstance().getBubbleSettings();
		val commandArg = EditCommand.getInput();
		val newSection = "Notifications.Broadcast.Messages." + commandArg;

		config.set(newSection + ".Permission", input);
		ConfigUtil.saveConfig((Player) context.getForWhom(),
				"&aSuccessfully changed permission in section " + commandArg + " to " + input + "!",
				"Failed to change permission! Error: ");

		return Prompt.END_OF_CONVERSATION;
	}
}
