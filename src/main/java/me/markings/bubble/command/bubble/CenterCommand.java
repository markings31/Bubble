package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.ConfigUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CenterCommand extends SimpleSubCommand {

	protected CenterCommand() {
		super("center");

		setMinArguments(1);
		setUsage("<broadcast_label|all>");
		setPermission(Permissions.Command.CENTER);
	}

	@Override
	protected void onCommand() {
		val config = Bubble.getInstance().getBubbleSettings();
		val centerPath = "Notifications.Broadcast.Messages." + args[0] + ".Centered";
		val centerAllPath = "Notifications.Broadcast.Center_All";

		if (args[0].equalsIgnoreCase("all")) {
			config.set(centerAllPath, !config.getBoolean(centerAllPath));

			ConfigUtil.saveConfig(getPlayer(),
					"&aSuccessfully toggled centered status of all messages to "
							+ (config.getBoolean(centerAllPath) ? "&aENABLED" : "&cDISABLED"),
					"&cFailed to center message! Error: ");
		} else {
			checkBoolean(config.contains(centerPath), "No configuration section " + args[0] + " found!");
			ConfigUtil.toggleCentered(centerPath, getPlayer());
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Objects.requireNonNull(
					Bubble.getInstance().getBubbleSettings().
							getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet(), "all");

		return new ArrayList<>();
	}
}
