package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RemoveCommand extends SimpleSubCommand {

	final File file = new File("plugins/Bubble/", "settings.yml");
	final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	protected RemoveCommand() {
		super("remove");

		setMinArguments(2);
		setUsage("<section|line> <broadcast_label> [<line_number>]");
		setPermission(Permissions.Command.REMOVE);
	}

	// TODO: Finish line removal support.
	@Override
	protected void onCommand() {
		val newSection = "Notifications.Broadcast.Messages." + args[1];

		if (args[0].equalsIgnoreCase("section")) {

			if (!config.isSet(newSection)) {
				Messenger.error(getPlayer(), "&cNo such section " + args[1] + " found!");
				return;
			}

			config.set(newSection, null);
		} else if (args[0].equalsIgnoreCase("line")) {
			val messageList = config.getStringList(newSection + ".Message");

			if (!NumberUtils.isNumber(args[2])) {
				Messenger.error(getPlayer(), "&cCould not find message located at index " + args[2] + "!");
				return;
			}

			Common.log(config.getConfigurationSection(newSection + ".Message") + "");

			messageList.remove(Integer.parseInt(args[2]) - 1);
			config.set(newSection + ".Message", messageList);
		}

		try {
			config.save(file);
			Bubble.getInstance().reload();
			Messenger.success(getPlayer(), "&aSuccessfully removed line/section!");
		} catch (final IOException e) {
			e.printStackTrace();
			Messenger.error(getPlayer(), "&cFailed to remove line/section! Error: " + e);
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("section", "line");
		if (args.length == 2)
			return completeLastWord(Objects.requireNonNull(config.getConfigurationSection("Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
