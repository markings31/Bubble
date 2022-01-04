package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowCommand extends SimpleSubCommand {

	protected ShowCommand() {
		super("show");

		setMinArguments(1);

		// Add "all" option?
		setUsage("<broadcast_label>");
		setPermission(Permissions.Command.SHOW);
	}

	@Override
	protected void onCommand() {
		checkConsole();

		val config = Bubble.getInstance().getBubbleSettings();

		val newSection = "Notifications.Broadcast.Messages." + args[0];

		checkBoolean(config.isSet(newSection), "&cNo such section " + args[0] + " found!");

		val messages = config.getStringList(newSection + ".Message");

		tell(
				"&8" + Common.chatLineSmooth(),
				ChatUtil.center("&3&l&nInformation:&r"),
				"&f",
				"&7- &b&lLabel: &f" + args[0],
				"&7- &b&lPermission: &f" + config.getString(newSection + ".Permission"),
				"&7- &b&lCentered: &f" + (Settings.BroadcastSettings.CENTER_ALL || config.getBoolean(newSection + ".Centered")),
				"&f",
				"&b&lMessage:",
				"&f");

		messages.forEach(message -> tellNoPrefix(MessageUtil.format(message)));

		SimpleComponent.of("\n" + ChatUtil.center("&f&nClick here to edit!&r")).onClickRunCmd("/bu edit " + args[0]).send(getPlayer());

		tellNoPrefix("&f", "&8" + Common.chatLineSmooth());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Objects.requireNonNull(Bubble.getInstance().getBubbleSettings().getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
