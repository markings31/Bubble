package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Notification;
import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.model.Permissions;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.FileUtil;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;

public class NotifyCommand extends SimpleSubCommand {

    public NotifyCommand() {
        super("notify");

        setMinArguments(3);
        setDescription("Send notification messages to players.");
        setPermission(Permissions.Command.NOTIFY);
    }

    @Override
    protected void onCommand() {
        if (args[0].equalsIgnoreCase("all"))
            Remain.getOnlinePlayers().forEach(this::sendNotification);
        else
            sendNotification(findPlayer(args[0]));
    }

    // TODO: Implement integrity checks (refer to ChatControl Red).
    private void sendNotification(final Player target) {
        final String type = args[1];
        final String message = joinArgs(type.equalsIgnoreCase(NotificationTypes.TOAST.getLabel()) ? 3 : 2);
        final String permission = getPermission() + "." + type;

        checkPerm(getSender(), permission);

        Notification.send(new Tuple<>(getPlayer(), target), type, message, args.length > 3 ? args[2] : null);
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        final List<String> usageList = new ArrayList<>();
        usageList.add("/{label} {sublabel} <player/all> <type> [material] <message>&7 - Send the given player a message of type <type>&7.");
        usageList.add("&f");
        usageList.add("&c&lNOTE:&r&c Message types include 'chat', 'title', 'actionbar', 'bossbar', 'toast', and 'image'.");


        return Common.toArray(usageList);
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWordPlayerNames();
            case 2:
                return completeLastWord(
                        NotificationTypes.CHAT.getLabel(),
                        NotificationTypes.TITLE.getLabel(),
                        NotificationTypes.ACTIONBAR.getLabel(),
                        NotificationTypes.BOSSBAR.getLabel(),
                        NotificationTypes.TOAST.getLabel(),
                        NotificationTypes.IMAGE.getLabel());
            case 3:
                if (args[1].equalsIgnoreCase(NotificationTypes.TOAST.getLabel()))
                    return completeLastWord(CompMaterial.values());
                else if (args[1].equals(NotificationTypes.IMAGE.getLabel()))
                    return completeLastWord(FileUtil.getFile("images/").list());
            default:
                return new ArrayList<>();
        }
    }
}
