package me.markings.bubble.commands;

import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

public class NotificationCommand extends SimpleSubCommand {

	public NotificationCommand(SimpleCommandGroup parent) {
		super(parent, "notify");

		setMinArguments(3);

		// TODO: Add argument to specify material for toast messages.
		setUsage("<all|playername> <title|bossbar|actionbar|toast> <input|...>");
		setPermission("bubble.command.notify");
	}

	@Override
	protected void onCommand() {
		String param = args[1].toLowerCase();

		String[] inputs = joinArgs(2).split("\\|");
		String primaryPart = Variables.replace(inputs[0], getPlayer());
		String secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], getPlayer());

		if (args[0].equalsIgnoreCase("all"))
			for (Player onlinePlayer : Remain.getOnlinePlayers())
				sendNotification(param, inputs, primaryPart, secondaryPart, onlinePlayer);
		else {
			Player target = findPlayer(args[0]);
			sendNotification(param, inputs, primaryPart, secondaryPart, target);
		}
	}

	private void sendNotification(String param, String[] inputs, String primaryPart, String secondaryPart, Player target) {
		switch (param) {
			case "title":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".title"),
						"&cYou don't have permission to execute this command!");
				checkBoolean(inputs.length == 2, "Usage: /{label} {sublabel} {0} {1} <primary>|<secondary>");

				Remain.sendTitle(target, primaryPart, secondaryPart);
				break;
			case "actionbar":
			case "action":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".actionbar"),
						"&cYou don't have permission to execute this command!");

				Remain.sendActionBar(target, primaryPart);
				break;
			case "bossbar":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".bossbar"),
						"&cYou don't have permission to execute this command!");

				Remain.sendBossbarPercent(target, primaryPart, 100);
				break;
			case "toast":
				checkBoolean(getPlayer().hasPermission(getPermission() + ".toast"),
						"&cYou don't have permission to execute this command!");

				Remain.sendToast(target, primaryPart, CompMaterial.CAKE);
				break;
		}
	}
}
