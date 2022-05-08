package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RemoveCommand extends SimpleSubCommand {

	protected RemoveCommand() {
		super("remove");

		setMinArguments(2);
		setDescription("Remove a broadcast section or message.");
		setUsage("<section/line> <label> [line_number]");
		setPermission(Permissions.Command.REMOVE);
	}

	@Override
	protected void onCommand() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val newSection = "Notifications.Broadcast.Messages." + args[1];

		if (args[0].equalsIgnoreCase("section")) {

			if (!config.isSet(newSection)) {
				Messenger.error(getPlayer(), "&cNo such section " + args[1] + " found!");
				return;
			}

			Common.log(newSection);

			config.set(newSection, null);
		} else if (args[0].equalsIgnoreCase("line")) {
			val messageList = config.getStringList(newSection + ".Message");

			if (!Valid.isInteger(args[2])) {
				Messenger.error(getPlayer(), "&cCould not find message located at index " + args[2] + "!");
				return;
			}

			messageList.remove(Integer.parseInt(args[2]) - 1);
			config.set(newSection + ".Message", messageList);
		}

		ConfigUtil.saveConfig(getPlayer(), "&aSuccessfully removed line/section!", "&cFailed to remove line/section! Error: ", config);
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("section", "line");
		if (args.length == 2)
			return completeLastWord(Objects.requireNonNull(
					YamlConfiguration.loadConfiguration(Bubble.settingsFile).getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
