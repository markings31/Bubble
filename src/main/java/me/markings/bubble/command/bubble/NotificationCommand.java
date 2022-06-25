package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.MessageUtil;
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

	private static final String messageArg = MessageUtil.getMessageArg();
	private static final String titleArg = MessageUtil.getTitleArg();
	private static final String actionbarArg = MessageUtil.getActionbarArg();
	private static final String bossbarArg = MessageUtil.getBossbarArg();
	private static final String toastArg = MessageUtil.getToastArg();

	public NotificationCommand() {
		super("notify");

		setMinArguments(3);
		setDescription("Send notification messages to players across the server.");
		//setUsage("<player_name|all> <type> [<image>] [<height>] [<material>] <input>");
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
		final String allInputs = String.join("", args);

		val primaryPart = Variables.replace(inputs[0], target);
		val secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], target);

		switch (args[1].toLowerCase()) {
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
				checkBoolean(Remain.hasHexColors(), "Toast messages are not supported on this server version!");
				Remain.sendToast(target, primaryPart, args[1].equalsIgnoreCase(toastArg) ?
						findMaterial(args[2], "No such material " + args[2] + " found!") : null);
				break;
		}
	}

	@Override
	protected String[] getMultilineUsageMessage() {
		val commandLabel = "&f/bu " + getSublabel();
		return new String[]{
				commandLabel + " <player_name/all> message [image] [height] <input>&7 - Send/announce a standard chat message. ",
				commandLabel + " <player_name/all> title <input|...>&7 - Send/announce a title message.",
				commandLabel + " <player_name/all> actionbar <input>&7 - Send/announce an action bar message.",
				commandLabel + " <player_name/all> bossbar <input>&7 - Send/announce a bossbar message.",
				commandLabel + " <player_name/all> toast [material] <input>&7 - Send/announce a toast achievement.",
				"&f",
				"&c&lNOTE:&c To include subtitles for title messages, simply separate the input by the '|'",
				"&csymbol. (Ex: This is a title!|This is a subtitle!)"
		};
	}

	@Override
	protected List<String> tabComplete() {
		switch (args.length) {
			case 1:
				return completeLastWord("player_name", "all");
			case 2:
				return completeLastWord(messageArg, titleArg, actionbarArg, bossbarArg, toastArg);
			case 3:
				if (args[1].equalsIgnoreCase(toastArg))
					return completeLastWord(CompMaterial.values());
			default:
				return new ArrayList<>();
		}
	}
}
