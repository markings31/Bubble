package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.settings.Localization;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SelectLabelPrompt extends SimplePrompt {

	@Getter
	private static final SelectLabelPrompt instance = new SelectLabelPrompt();

	@Override
	protected String getPrompt(final ConversationContext conversationContext) {
		Remain.sendTitle((Player) conversationContext.getForWhom(), "&9Edit Message", "Please type your message in the chat.");
		return Localization.PromptMessages.LABEL_PROMPT_MESSAGE;
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val path = "Notifications.Broadcast.Messages." + s;

		if (!config.isSet(path))
			Messenger.error((CommandSender) conversationContext.getForWhom(), "&cCould not find message group " + s + "!");
		else
			Common.dispatchCommandAsPlayer((Player) conversationContext.getForWhom(), "bu edit " + s);

		return Prompt.END_OF_CONVERSATION;
	}
}
