package me.markings.bubble.command;

import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;

public class NotificationCommand extends SimpleSubCommand {

	private static final String noPermissionMsg = "&cYou don't have permission to execute this command!";

	public NotificationCommand() {
		super("notify");

		setMinArguments(3);

		// TODO: Add argument to specify material for toast messages.
		setUsage("<all|playername> <message|title|bossbar|actionbar|toast> <input|...>");
		setPermission("bubble.command.notify");
	}

	@Override
	protected void onCommand() {
		final String param = args[1].toLowerCase();

		final String[] inputs = joinArgs(2).split("\\|");
		final String primaryPart = Variables.replace(inputs[0], getPlayer());
		final String secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], getPlayer());

		if (args[0].equalsIgnoreCase("all"))
			for (final Player onlinePlayer : Remain.getOnlinePlayers())
				sendNotification(param, primaryPart, secondaryPart, onlinePlayer);
		else {
			final Player target = findPlayer(args[0]);
			sendNotification(param, primaryPart, secondaryPart, target);
		}
	}

	private void sendNotification(final String param, final String primaryPart, final String secondaryPart, final Player target) {
		switch (param) {
			case "message":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".message"), noPermissionMsg);

				Common.tell(target, Common.colorize(primaryPart));
				break;
			case "title":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".title"), noPermissionMsg);

				Remain.sendTitle(target, primaryPart, secondaryPart);
				break;
			case "actionbar":
			case "action":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".actionbar"), noPermissionMsg);

				Remain.sendActionBar(target, primaryPart);
				break;
			case "bossbar":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".bossbar"), noPermissionMsg);

				Remain.sendBossbarPercent(target, primaryPart, 100);
				break;
			case "toast":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".toast"), noPermissionMsg);

				Remain.sendToast(target, primaryPart, CompMaterial.CAKE);
				break;
		}
	}

	@Override
	protected List<String> tabComplete() {
		return args.length == 2 ? completeLastWord("message", "title", "bossbar", "actionbar", "toast") : new ArrayList<>();
	}
}
