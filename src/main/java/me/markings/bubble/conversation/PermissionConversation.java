package me.markings.bubble.conversation;

import lombok.SneakyThrows;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
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

public class PermissionConversation extends SimplePrompt {

	final File file = new File("plugins/Bubble/", "settings.yml");
	final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	@Override
	protected String getPrompt(final ConversationContext context) {
		return "&8&l[&9&li&8&l] &ePlease write the desired permission in the chat.";
	}

	@Nullable
	@Override
	@SneakyThrows
	protected Prompt acceptValidatedInput(@NotNull final ConversationContext context, @NotNull final String input) {
		val commandArg = EditCommand.getInput();
		val newSection = "Notifications.Broadcast.Messages." + commandArg;

		config.set(newSection + ".Permission", input);
		config.save(file);
		Bubble.getInstance().reload();

		Messenger.success((CommandSender) context.getForWhom(),
				"&aSuccessfully changed permission in section " + commandArg + " to " + input + "!");

		return Prompt.END_OF_CONVERSATION;
	}
}
