package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CenterCommand extends SimpleSubCommand {

	protected CenterCommand() {
		super("center");

		setMinArguments(1);
		setDescription("Toggle automatic centering for broadcast messages.");
		setUsage("<label/all>");
		setPermission(Permissions.Command.CENTER);
	}

	@Override
	protected void onCommand() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val centerPath = "Notifications.Broadcast.Messages." + args[0] + ".Centered";
		val centerAllPath = "Notifications.Broadcast.Center_All";

		if (args[0].equalsIgnoreCase("all")) {
			config.set(centerAllPath, !config.getBoolean(centerAllPath));

			ConfigUtil.saveConfig(getPlayer(),
					"&aSuccessfully toggled centered status of all messages to "
							+ (config.getBoolean(centerAllPath) ? "&aENABLED" : "&cDISABLED"),
					"&cFailed to center message! Error: ", config);
		} else {
			checkBoolean(config.contains(centerPath), "No configuration section " + args[0] + " found!");
			ConfigUtil.toggleCentered(centerPath, getPlayer());
		}
	}

	@Override
	protected String[] getMultilineUsageMessage() {
		val commandLabel = "&f/bu " + getSublabel();
		return new String[]{
				commandLabel + " <label>&7 - Centers the messages contained in the 'label' section.",
				commandLabel + " all&7 - Centers all messages across different sections.",
				"&f",
				"&c&lNOTE:&c 'label' refers to the name of the broadcast section."
		};
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Objects.requireNonNull(
					YamlConfiguration.loadConfiguration(Bubble.settingsFile).
							getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet(), "all");

		return new ArrayList<>();
	}
}
