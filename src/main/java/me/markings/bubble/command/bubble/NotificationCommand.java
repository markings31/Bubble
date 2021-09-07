package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;

public class NotificationCommand extends SimpleSubCommand {

	private static final String noPermissionMsg = "&cYou don't have permission to execute this command!";

	private static final String messageArg = MessageUtil.getMessageArg();
	private static final String titleArg = MessageUtil.getTitleArg();
	private static final String actionbarArg = MessageUtil.getActionbarArg();
	private static final String bossbarArg = MessageUtil.getBossbarArg();
	private static final String toastArg = MessageUtil.getToastArg();
	private static final String bungeeArg = "bungee";

	public NotificationCommand() {
		super("notify");

		setMinArguments(3);

		setUsage("<playerName|all|bungee> <message|title|bossbar|actionbar|toast> [<material>] <input|...>");
		setPermission("bubble.command.notify");
	}

	@Override
	protected void onCommand() {
		val param = args[1].toLowerCase();

		val material = args[1].equalsIgnoreCase(toastArg) ?
				findMaterial(args[2], "No such material " + args[2] + " found!") : null;

		val inputs = joinArgs((args[1].equalsIgnoreCase(toastArg) ? 3 : 2)).split("\\|");

		if (args[0].equalsIgnoreCase(bungeeArg))
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, param, joinArgs((args[1].equalsIgnoreCase(toastArg) ? 3 : 2)));

		val primaryPart = Variables.replace(inputs[0], getPlayer());
		val secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], getPlayer());

		if (args[0].equalsIgnoreCase("all"))
			Remain.getOnlinePlayers().forEach(onlinePlayer -> sendNotification(param, primaryPart, secondaryPart, onlinePlayer, material));
		else if (!args[0].equalsIgnoreCase(bungeeArg)) {
			val target = findPlayer(args[0]);
			sendNotification(param, primaryPart, secondaryPart, target, material);
		}
	}

	private void sendNotification(final String param, final String primaryPart, final String secondaryPart, final Player target, final CompMaterial material) {
		switch (param) {
			case "message":
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".message"), noPermissionMsg);

				Common.tell(target, Common.colorize(primaryPart));
				break;
			case "title":
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".title"), noPermissionMsg);

				Remain.sendTitle(target, primaryPart, secondaryPart);
				break;
			case "actionbar":
			case "action":
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".actionbar"), noPermissionMsg);

				Remain.sendActionBar(target, primaryPart);
				break;
			case "bossbar":
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".bossbar"), noPermissionMsg);

				Remain.sendBossbarPercent(target, primaryPart, 100);
				break;
			case "toast":
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".toast"), noPermissionMsg);

				Remain.sendToast(target, primaryPart, material);
				break;
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("player", "all", bungeeArg);
		if (args.length == 2)
			return completeLastWord(messageArg, titleArg,
					actionbarArg, bossbarArg, toastArg);
		if (args.length == 3 && args[1].equalsIgnoreCase(toastArg))
			return completeLastWord(CompMaterial.values());

		return new ArrayList<>();
	}
}
