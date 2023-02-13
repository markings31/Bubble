package me.markings.bubble.command.bubble;

import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.model.Permissions;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MinecraftVersion;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.ChatImage;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.io.IOException;
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

    private void sendNotification(final Player target) {
        final String[] inputs = joinArgs((args[1].equalsIgnoreCase(NotificationTypes.TOAST.getLabel()) ? 3 : 2)).split("\\|");
        final String allInputs = String.join("", args);

        final String primaryPart = Variables.replace(inputs[0], target);
        final String secondaryPart = Variables.replace(inputs.length == 1 ? "" : inputs[1], target);

        final String messageType = args[1].toLowerCase();
        final String permission = getPermission() + "." + messageType;

        checkPerm(getSender(), permission);

        if (messageType.equals(NotificationTypes.CHAT.getLabel())) {
            Common.tellNoPrefix(target, Common.colorize(primaryPart));
        } else if (messageType.equals((NotificationTypes.TITLE.getLabel()))) {
            Remain.sendTitle(target, primaryPart, secondaryPart);
        } else if (messageType.equals(NotificationTypes.ACTIONBAR.getLabel())) {
            Remain.sendActionBar(target, primaryPart);
        } else if (messageType.equals(NotificationTypes.BOSSBAR.getLabel())) {
            Remain.sendBossbarTimed(target, primaryPart, !secondaryPart.isEmpty() ? (int) TimeUtil.toTicks(secondaryPart) / 20 : 5);
        } else if (messageType.equals(NotificationTypes.TOAST.getLabel())) {
            checkBoolean(MinecraftVersion.atLeast(MinecraftVersion.V.v1_16), "Toast messages are not supported on this server version!");
            Remain.sendToast(target, primaryPart, args[1].equalsIgnoreCase(NotificationTypes.TOAST.getLabel()) ?
                    findMaterial(args[2], "No such material " + args[2] + " found!") : null);
        } else if (messageType.equals(NotificationTypes.IMAGE.getLabel())) {
            final boolean hasImage = args[2].contains(".png") || args[2].contains(".jpg");
                /*if (Boolean.TRUE.equals(Settings.DiscordSettings.SYNCANNOUNCEMENTS) && !hasImage)
                    DiscordSRVHook.getInstance().sendAnnouncement(
                            getPlayer(),
                            "Announcement",
                            Common.stripColors(primaryPart),
                            MessageUtil.getColor(Settings.DiscordSettings.ANNOUNCEMENTSCOLOR),
                            DiscordSRV.getAvatarUrl(getPlayer()));*/
            if (hasImage) {
                checkArgs(4, "Invalid arguments: /{label} {sublabel} <player_name/all> image {image}.png <height> [message]");
                checkBoolean(Valid.isInteger(args[3]), "Please provide the height of the image you want to be displayed!");
                try {
                    if (allInputs.toLowerCase().contains("-c"))
                        ChatImage.fromFile(new File("plugins/Bubble/images/", args[2]), Integer.parseInt(args[3]), ChatImage.Type.BLOCK)
                                .appendCenteredText(joinArgs(4).replace("-c", "").split("\\|"))
                                .send(target);
                    else
                        ChatImage.fromFile(new File("plugins/Bubble/images/", args[2]), Integer.parseInt(args[3]), ChatImage.Type.BLOCK)
                                .appendText(joinArgs(4).split("\\|"))
                                .send(target);
                } catch (final IOException e) {
                    throw new RuntimeException(e);
                }
            } else Common.tell(target, Common.colorize(primaryPart));
        }
    }

    @Override
    protected String[] getMultilineUsageMessage() {
        final List<String> usageList = new ArrayList<>();
        usageList.add("/{label} {sublabel} <player_name/all> <type> <message> [material]&7 - Send the given player a message of type <type>&7.");
        usageList.add("&f");
        usageList.add("&c&lNOTE:&r&c Message types include 'chat', 'title', 'actionbar', 'bossbar', 'toast', and 'image'.");


        return Common.toArray(usageList);
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWord("all", Common.getPlayerNames(true));
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
            default:
                return new ArrayList<>();
        }
    }
}
