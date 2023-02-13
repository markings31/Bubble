package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.Localization;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditMessagePrompt extends SimplePrompt {

	@Getter
	private static final EditMessagePrompt instance = new EditMessagePrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle(getPlayer(context), "&9Edit Message", "Please type your message in the chat.");
		return Localization.PromptMessages.EDIT_PROMPT_MESSAGE;
	}

	@Override
	protected String getCustomPrefix() {
		return "";
	}

	@Nullable
	@Override
	@SneakyThrows
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		final List<String> message = new ArrayList<>(Arrays.asList(input.split(",")));
		final String label = EditCommand.getInput();

		Broadcasts.getBroadcast(label).setMessage(message);

		Messenger.success(getPlayer(context), "&aSuccessfully edited contents of '" + label + "' to '" + message + "'!");

		return Prompt.END_OF_CONVERSATION;
	}
}
