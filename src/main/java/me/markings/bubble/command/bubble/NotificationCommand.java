package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion;
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

	public NotificationCommand() {
		super("notify");

		setMinArguments(3);

		setUsage("<player_name|all> <message|title|bossbar|actionbar|toast> [<material>] <input|...>");
		setPermission(Permissions.Command.NOTIFY);
	}

	@Override
	protected void onCommand() {
		if (args[0].equalsIgnoreCase("all"))
			Remain.getOnlinePlayers().forEach(this::sendNotification);
		else
			sendNotification(findPlayer(args[0]));
	}

	private void sendNotification(final Player target) {
		val inputs = joinArgs((args[1].equalsIgnoreCase(toastArg) ? 3 : 2)).split("\\|");

		val primaryPart = Variables.replace(inputs[0], getPlayer());
		val secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], getPlayer());

		switch (args[1].toLowerCase()) {
			case "message": {
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".message"), noPermissionMsg);

				Common.tell(target, Common.colorize(primaryPart));
				break;
			}
			case "title": {
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".title"), noPermissionMsg);

				Remain.sendTitle(target, primaryPart, secondaryPart);
				break;
			}
			case "actionbar":
			case "action": {
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".actionbar"), noPermissionMsg);

				Remain.sendActionBar(target, primaryPart);
				break;
			}
			case "bossbar": {
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".bossbar"), noPermissionMsg);

				Remain.sendBossbarPercent(target, primaryPart, 100);
				break;
			}
			case "toast": {
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".toast"), noPermissionMsg);

				checkBoolean(!MinecraftVersion.olderThan(MinecraftVersion.V.v1_12), "Toast messages are not supported on this server version!");

				Remain.sendToast(target, primaryPart, args[1].equalsIgnoreCase(toastArg) ?
						findMaterial(args[2], "No such material " + args[2] + " found!") : null);
				break;
			}
		}
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord("player_name", "all");
		if (args.length == 2)
			return completeLastWord(messageArg, titleArg, actionbarArg, bossbarArg, toastArg);
		if (args.length == 3 && args[1].equalsIgnoreCase(toastArg))
			return completeLastWord(CompMaterial.values());

		return new ArrayList<>();
	}
}
