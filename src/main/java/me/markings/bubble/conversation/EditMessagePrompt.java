package me.markings.bubble.conversation;

import lombok.*;
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
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EditMessagePrompt extends SimplePrompt {

	@Getter
	private static final EditMessagePrompt instance = new EditMessagePrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle((Player) context.getForWhom(), "&9Edit Message", "Please type your message in the chat.");
		return Localization.PromptMessages.EDIT_PROMPT_MESSAGE;
	}

	@Nullable
	@Override
	@SneakyThrows
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val commandArg = EditCommand.getInput();
		val inputs = input.split("\\|");
		val newSection = "Notifications.Broadcast.Messages." + commandArg;

		val section = config.getStringList(newSection + ".Message");

		section.clear();
		for (final String message : inputs) {
			section.add(message);
			config.set(newSection + ".Message", section);
		}

		ConfigUtil.saveConfig((Player) context.getForWhom(),
				"&aSuccessfully replaced message section " + commandArg + " with line '" + input + "&a'!",
				"Failed to edit message! Error: ", config);

		return Prompt.END_OF_CONVERSATION;
	}
}
