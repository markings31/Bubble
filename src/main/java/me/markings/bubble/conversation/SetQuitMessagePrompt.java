package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
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
public class SetQuitMessagePrompt extends SimplePrompt {

	@Getter
	private static final SetQuitMessagePrompt instance = new SetQuitMessagePrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle((Player) context.getForWhom(), "&9Set Quit Message", "Please type your message in the chat.");
		return Localization.PromptMessages.QUIT_PROMPT_MESSAGE;
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		config.set("Notifications.Join.Quit_Message", s);
		ConfigUtil.saveConfig((Player) conversationContext.getForWhom(),
				"&aSuccessfully set quit message!",
				"&cFailed to set quit message. Error: ", config);

		return Prompt.END_OF_CONVERSATION;
	}
}
