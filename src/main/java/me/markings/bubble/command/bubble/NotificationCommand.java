package me.markings.bubble.command.bubble;

import github.scarsz.discordsrv.DiscordSRV;
import lombok.val;
import me.markings.bubble.hook.DiscordSRVHook;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.ChatImageUtil;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.io.IOException;
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
				val hasImage = args[2].contains(".png") || args[2].contains(".jpg");
				if (getPlayer() != null)
					checkBoolean(getPlayer().hasPermission(getPermission() + ".message"), noPermissionMsg);
				if (Boolean.TRUE.equals(Settings.DiscordSettings.SYNCANNOUNCEMENTS) && !hasImage)
					DiscordSRVHook.getInstance().sendAnnouncement(
							getPlayer(),
							"Announcement",
							Common.stripColors(primaryPart),
							MessageUtil.getColor(Settings.DiscordSettings.ANNOUNCEMENTSCOLOR),
							DiscordSRV.getAvatarUrl(getPlayer()));
				if (hasImage) {
					checkBoolean(Valid.isInteger(args[3]), "Please provide the height of the image you want to be displayed!");
					try {
						if (allInputs.toLowerCase().contains("-c"))
							ChatImageUtil.fromFile(new File("plugins/Bubble/images/", args[2]), Integer.parseInt(args[3]), ChatImageUtil.Type.BLOCK)
									.appendCenteredText(joinArgs(4).replace("-c", "").split("\\|"))
									.sendToPlayer(target);
						else
							ChatImageUtil.fromFile(new File("plugins/Bubble/images/", args[2]), Integer.parseInt(args[3]), ChatImageUtil.Type.BLOCK)
									.appendText(joinArgs(4).split("\\|"))
									.sendToPlayer(target);
					} catch (final IOException e) {
						e.printStackTrace();
					}
				} else Common.tell(target, Common.colorize(primaryPart));
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
