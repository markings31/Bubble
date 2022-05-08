package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionPrompt extends SimplePrompt {

	@Getter
	private static final PermissionPrompt instance = new PermissionPrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle((Player) context.getForWhom(), "&9Set Permission", "Please type your message in the chat.");
		return Localization.PromptMessages.PERMISSION_PROMPT_MESSAGE;
	}

	@Override
	protected boolean isInputValid(final ConversationContext context, final String input) {
		return Valid.isInteger(input);
	}

	@Override
	protected String getFailedValidationText(final ConversationContext context, final String invalidInput) {
		return Messenger.getErrorPrefix() + "Please write the permission as a String text (Example: bubble.vip).";
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val commandArg = EditCommand.getInput();
		val newSection = "Notifications.Broadcast.Messages." + commandArg;

		config.set(newSection + ".Permission", input);
		ConfigUtil.saveConfig((Player) context.getForWhom(),
				"&aSuccessfully changed permission in section " + commandArg + " to " + input + "!",
				"Failed to change permission! Error: ", config);

		return Prompt.END_OF_CONVERSATION;
	}
}
