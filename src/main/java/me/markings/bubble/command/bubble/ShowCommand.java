package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShowCommand extends SimpleSubCommand {

	private static final FileConfiguration config = Bubble.config;

	protected ShowCommand() {
		super("show");

		setMinArguments(1);

		// Add "all" option?
		setUsage("<broadcast_label>");
		setPermission(Permissions.Command.LIST);
	}

	// TODO: Finish (lol)
	@Override
	protected void onCommand() {
		checkConsole();

		val newSection = "Notifications.Broadcast.Messages." + args[0];

		checkBoolean(config.isSet(newSection), "&cNo such section " + args[0] + " found!");

		val messages = config.getStringList(newSection + ".Message");

		tellNoPrefix("&8" + Common.chatLineSmooth(), "&f");
		messages.forEach(message -> tellNoPrefix(MessageUtil.replaceVarsAndGradient(message, null)));

		SimpleComponent.of("\n" + ChatUtil.center("&7&oClick here to edit!")).onClickRunCmd("/bu edit " + args[0]).send(getPlayer());

		tellNoPrefix("&f", "&8" + Common.chatLineSmooth());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Objects.requireNonNull(config.getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
