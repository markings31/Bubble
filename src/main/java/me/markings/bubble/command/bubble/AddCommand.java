package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCommand extends SimpleSubCommand {

	public AddCommand() {
		super("add");

		setMinArguments(2);
		setUsage("<section|line> <broadcast_label> [<message>]");
		setPermission(Permissions.Command.ADD);
	}

	@Override
	protected void onCommand() {
		val config = Bubble.getInstance().getBubbleSettings();
		val newSection = "Notifications.Broadcast.Messages." + args[1];
		val messagePathSuffix = ".Message";
		val message = config.getStringList(newSection + messagePathSuffix);

		if (args[0].equalsIgnoreCase("section")) {
			val worldsPath = newSection + ".Worlds";
			val worldsList = config.getStringList(worldsPath);
			config.createSection(newSection);

			message.add("Test Message");
			worldsList.add("world");
			config.set(newSection + ".Permission", "bubble.vip");
			config.set(newSection + ".Centered", false);
			config.set(worldsPath, worldsList);
			config.set(newSection + messagePathSuffix, message);
		} else if (args[0].equalsIgnoreCase("line")) {
			val messageLine = joinArgs(2);
			val section = config.getStringList(newSection + messagePathSuffix);

			section.add(messageLine);
			config.set(newSection + messagePathSuffix, section);
		}

		ConfigUtil.saveConfig(getPlayer(),
				"&aSuccessfully added line/section!",
				"&cFailed to create line/section! Error: ");
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("section", "line");
		if (args.length == 2 && args[0].equalsIgnoreCase("line"))
			return completeLastWord(Objects.requireNonNull(
					Bubble.getInstance().getBubbleSettings().getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
