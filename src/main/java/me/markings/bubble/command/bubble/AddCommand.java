package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.*;

public class AddCommand extends SimpleSubCommand {

	public AddCommand() {
		super("add");

		setMinArguments(2);
		setDescription("Add a line or section to the given broadcast label.");
		setUsage("<section/line> <label> [message]");
		setPermission(Permissions.Command.ADD);
	}

	@Override
	protected void onCommand() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val newSection = "Messages." + args[1];
		val messagePathSuffix = ".Message";
		val message = config.getStringList(newSection + messagePathSuffix);
		val permissionPath = newSection + ".Permission";
		val centeredPath = newSection + ".Centered";
		val worldsPath = newSection + ".Worlds";

		final Map<List<String>, String> messages = new HashMap<>();
		final Map<String, String> permissions = new HashMap<>();
		final Map<String, Boolean> centered = new HashMap<>();
		final Map<List<String>, String> worlds = new HashMap<>();
		messages.put(message, newSection);
		permissions.put(permissionPath, config.getString(permissionPath));
		centered.put(centeredPath, config.getBoolean(centeredPath));
		worlds.put(config.getStringList(worldsPath), worldsPath);

		if (args[0].equalsIgnoreCase("section")) {
			val worldsList = config.getStringList(worldsPath);
			config.createSection(newSection);

			message.add("Test Message");
			worldsList.add("world");
			config.set(permissionPath, "bubble.vip");
			config.set(centeredPath, false);
			config.set(worldsPath, worldsList);
			config.set(newSection + messagePathSuffix, message);
		} else if (args[0].equalsIgnoreCase("line")) {
			val messageLine = joinArgs(2);
			val section = config.getStringList(newSection + messagePathSuffix);

			section.add(messageLine);
			config.set(newSection + messagePathSuffix, section);
		} else returnInvalidArgs();

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully added line/section!",
				"&cFailed to create line/section! Error: ", config);
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("section", "line");
		if (args.length == 2 && args[0].equalsIgnoreCase("line"))
			return completeLastWord(Objects.requireNonNull(
					YamlConfiguration.loadConfiguration(Bubble.settingsFile).getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
