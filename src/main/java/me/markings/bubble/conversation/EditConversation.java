package me.markings.bubble.conversation;

import lombok.SneakyThrows;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.conversation.SimplePrompt;

public class EditConversation extends SimplePrompt {

	@Override
	protected String getPrompt(final ConversationContext context) {
		return "&8&l[&9&li&8&l] &ePlease write the desired message in the chat.\n&7&oNote: Use the '|' delimiter to add multiple messages.";
	}

	@Nullable
	@Override
	@SneakyThrows
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		val config = Bubble.getInstance().getBubbleSettings();
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
				"Failed to edit message! Error: ");
		
		return Prompt.END_OF_CONVERSATION;
	}
}
