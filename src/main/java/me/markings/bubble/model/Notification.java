package me.markings.bubble.model;

import lombok.SneakyThrows;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.*;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.ChatImage;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Arrays;

public class Notification {

    // NOTE: Ensure that the 'message' param is joined by spaces.
    public static void send(@Nullable final Tuple<CommandSender, Player> senderRecipient, final String notificationType, final String message, @Nullable final String... args) {
        final CommandSender sender = senderRecipient != null ? senderRecipient.getKey() : null;
        final Player recipient = senderRecipient != null ? senderRecipient.getValue() : null;

        final String[] messageSplit = message.split("\\|");

        Debugger.debug("notifications",
                "Split Message: " + Arrays.toString(messageSplit),
                "Sender: " + sender,
                "Recipient: " + recipient);

        final String primary = messageSplit[0];
        final String secondary = messageSplit.length > 1 ? messageSplit[1] : "";

        checkIfValid(sender, notificationType);

        if (notificationType.equalsIgnoreCase(NotificationTypes.CHAT.getLabel())) {
            message(recipient, primary);
        } else if (notificationType.equalsIgnoreCase(NotificationTypes.TITLE.getLabel())) {
            title(recipient, primary, secondary);
        } else if (notificationType.equalsIgnoreCase(NotificationTypes.ACTIONBAR.getLabel())) {
            actionbar(recipient, primary);
        } else if (notificationType.equalsIgnoreCase(NotificationTypes.BOSSBAR.getLabel())) {
            bossbar(recipient, primary, args != null && args[0] != null ? (int) TimeUtil.toTicks(args[0]) / 20 : 5);
        } else if (notificationType.equalsIgnoreCase(NotificationTypes.TOAST.getLabel())) {
            Valid.checkBoolean(MinecraftVersion.atLeast(MinecraftVersion.V.v1_12), "Toast messages are not supported on this server version!");
            final CompMaterial found = args != null && args[0] != null ? CompMaterial.fromString(args[0]) : CompMaterial.PAPER;

            Valid.checkNotNull(found, "No such material {item} found!");
            toast(recipient, primary, found);
        } else if (notificationType.equalsIgnoreCase(NotificationTypes.IMAGE.getLabel())) {
            Valid.checkNotNull(args);
            final boolean hasImage = args[0].endsWith(".png") || args[0].endsWith(".jpg") || args[0].endsWith(".jpeg");
            final String imagePath = "images/" + args[0];

            Valid.checkBoolean(hasImage, "Please ensure you include the file extension of the image (ex. 'creeper.png').");
            Valid.checkBoolean(Valid.isNumber(args[1]),
                    "Please provide the height of the image you want to be displayed! (/bu notify <player> image <image>.png <height> [message])");
            Notification.chatImage(recipient, message, imagePath, Integer.parseInt(args[1]));
        }

        if (!(sender instanceof ConsoleCommandSender))
            Messenger.success(sender, "&7Sent &f" + notificationType + " &7notification to &f"
                    + (recipient != null ? recipient.getName() : "everyone") + "&7.");
    }

    private static void checkIfValid(final CommandSender sender, final String notificationType) {
        if (NotificationTypes.fromLabel(notificationType) == null)
            Messenger.error(sender, "Invalid notification type! Please use one of the following: " + NotificationTypes.getLabels());
    }

    private static void message(@Nullable final Player target, final String message) {
        if (target != null)
            Common.tellNoPrefix(target, Variables.replace(message, target));
        else
            Remain.getOnlinePlayers().forEach(player -> Common.tellNoPrefix(player, Variables.replace(message, player)));
    }


    private static void title(@Nullable final Player target, final String title, final String subtitle) {
        if (target != null)
            Remain.sendTitle(target, Variables.replace(title, target), Variables.replace(subtitle, target));
        else
            Remain.getOnlinePlayers().forEach(player -> Remain.sendTitle(player, Variables.replace(title, player), Variables.replace(subtitle, player)));
    }

    private static void actionbar(@Nullable final Player target, final String message) {
        if (target != null)
            Remain.sendActionBar(target, Variables.replace(message, target));
        else
            Remain.getOnlinePlayers().forEach(player -> Remain.sendActionBar(player, Variables.replace(message, player)));
    }

    private static void bossbar(@Nullable final Player target, final String message, final int seconds) {
        if (target != null)
            Remain.sendBossbarTimed(target, Variables.replace(message, target), seconds);
        else
            Remain.getOnlinePlayers().forEach(player -> Remain.sendBossbarTimed(player, Variables.replace(message, player), seconds));
    }

    private static void toast(@Nullable final Player target, final String message, final CompMaterial material) {
        Valid.checkBoolean(MinecraftVersion.atLeast(MinecraftVersion.V.v1_16), "Toast messages are not supported on this server version!");

        if (target != null)
            Remain.sendToast(target, Variables.replace(message, target), material);
        else
            Remain.getOnlinePlayers().forEach(player -> Remain.sendToast(player, Variables.replace(message, player), material));
    }

    @SneakyThrows
    private static void chatImage(@Nullable final Player target, final String message, final String imagePath, final int imageHeight) {
        Valid.checkBoolean(FileUtil.getFile(imagePath).exists(), "No such image found in the 'Bubble/images' folder!");
        Valid.checkBoolean(imageHeight <= 32, "Please provide an image height of 32 or less!");
        final String[] splitMessage = Variables.replace(message, target).split("\\|");

        if (target != null)
            ChatImage.fromFile(FileUtil.getFile(imagePath), imageHeight, ChatImage.Type.BLOCK)
                    .appendText(splitMessage)
                    .send(target);
        else Remain.getOnlinePlayers().forEach(player -> {
            try {
                ChatImage.fromFile(FileUtil.getFile(imagePath), imageHeight, ChatImage.Type.BLOCK)
                        .appendText(splitMessage)
                        .send(player);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
