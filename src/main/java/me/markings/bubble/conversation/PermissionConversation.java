package me.markings.bubble.conversation;

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

public class PermissionConversation extends SimplePrompt {

	@Override
	protected String getPrompt(final ConversationContext context) {
		return "&8&l[&9&li&8&l] &ePlease write the desired permission in the chat.\n&7&oNote: Type 'exit' to cancel.";
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
