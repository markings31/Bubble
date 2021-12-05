package me.markings.bubble.command.bubble;

import lombok.val;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCommand extends SimpleSubCommand {

	final File file = new File("plugins/Bubble/localization/", "messages_" + SimpleSettings.LOCALE_PREFIX + ".yml");
	final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	public AddCommand() {
		super("add");

		setMinArguments(2);
		setUsage("<section|line> <broadcast_label> [<message>]");
	}

	@Override
	protected void onCommand() {
		val newSection = "Broadcast.Messages." + args[1];
		val messagePathSuffix = ".Message";

		if (args[0].equalsIgnoreCase("section")) {
			config.createSection(newSection);

			val message = config.getStringList(newSection + messagePathSuffix);
			
			message.add("Test Message");
			config.set(newSection + ".Permission", "bubble.vip");
			config.set(newSection + messagePathSuffix, message);
			Messenger.success(getPlayer(), "&aSuccessfully added section " + args[1] + "!");
		} else if (args[0].equalsIgnoreCase("line")) {
			val message = joinArgs(2);
			val section = config.getStringList(newSection + messagePathSuffix);

			section.add(message);
			config.set(newSection + messagePathSuffix, section);
			Messenger.success(getPlayer(), "&aSuccessfully added line '" + message + "' to section " + args[1] + "!");
		}

		try {
			config.save(file);
		} catch (final IOException e) {
			e.printStackTrace();
			Messenger.error(getPlayer(), "&cFailed to create line/section! Error: " + e);
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("section", "line");
		if (args.length == 2 && args[0].equalsIgnoreCase("line"))
			return completeLastWord(Objects.requireNonNull(config.getConfigurationSection("Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
