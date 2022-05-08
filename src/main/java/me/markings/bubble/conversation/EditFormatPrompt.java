package me.markings.bubble.conversation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class EditFormatPrompt extends SimplePrompt {

	@Getter
	private static final EditFormatPrompt instance = new EditFormatPrompt();

	@Override
	protected String getPrompt(final ConversationContext context) {
		Remain.sendTitle((Player) context.getForWhom(), "&9Edit Format", "Please type your message in the chat.");
		return Localization.PromptMessages.FORMAT_PROMPT_MESSAGE;
	}

	@Nullable
	@Override
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		YamlConfiguration.loadConfiguration(Bubble.settingsFile).set("Discord.Discord_To_Minecraft.Chat_Format", s);
		ConfigUtil.saveConfig((Player) conversationContext.getForWhom(),
				"&aSuccessfully set chat format to '" + s + "'&a!",
				"&cFailed to set chat format! Error: ", YamlConfiguration.loadConfiguration(Bubble.settingsFile));

		return Prompt.END_OF_CONVERSATION;
	}
}
