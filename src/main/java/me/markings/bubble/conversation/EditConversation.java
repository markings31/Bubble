package me.markings.bubble.conversation;

import lombok.SneakyThrows;
import lombok.val;
import me.markings.bubble.Bubble;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.conversation.SimplePrompt;

import java.io.File;

import static me.markings.bubble.command.bubble.EditCommand.input;

public class EditConversation extends SimplePrompt {

	final File file = new File("plugins/Bubble/", "settings.yml");
	final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	@Override
	protected String getPrompt(final ConversationContext context) {
		return "&8&l[&9&li&8&l] &ePlease write the desired message in the chat.\n&7&oNote: Use the '|' delimiter to add multiple messages.";
	}

	@Nullable
	@Override
	@SneakyThrows
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext conversationContext, @NotNull final String s) {
		val commandArg = input;
		val inputs = s.split("\\|");
		val newSection = "Notifications.Broadcast.Messages." + commandArg;

		val section = config.getStringList(newSection + ".Message");

		section.clear();
		for (final String input : inputs) {
			section.add(input);
			config.set(newSection + ".Message", section);
			config.save(file);
			Bubble.getInstance().reload();
		}

		Messenger.success((CommandSender) conversationContext.getForWhom(),
				"&aSuccessfully replaced message section " + commandArg + " with line(s) '" + s + "&a'!");

		return Prompt.END_OF_CONVERSATION;
	}
}
