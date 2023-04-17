package me.markings.bubble.tasks;

import me.markings.bubble.PlayerData;
import me.markings.bubble.settings.Broadcast;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.Remain;

import java.util.List;
import java.util.Objects;

public class BroadcastTask extends BukkitRunnable {

    private static List<List<String>> messageList;
    private static List<String> worlds;

    private static int index;

    private static Broadcast currentBroadcast;

    @Override
    public void run() {
        nextCycle();
    }

    public static void nextCycle() {
        messageList = Broadcast.getAllMessages();

        final List<String> broadcastPerm = Broadcast.getAllPermissions();

        worlds = Broadcast.getAllWorlds();

        Debugger.debug("broadcasts",
                "Messages: " + messageList +
                        " Worlds: " + worlds +
                        " Permissions: " + broadcastPerm);

        if (Settings.NotificationSettings.ENABLE_BROADCASTS && !Remain.getOnlinePlayers().isEmpty()) {
            executeTasks();
            updateIndex();
        }
    }

    public static void executeTasks() {
        final List<String> messages = Settings.NotificationSettings.RANDOM_MESSAGE ?
                RandomUtil.nextItem(messageList) : messageList.get(index);
        currentBroadcast = Broadcast.getBroadcastFromMessage(messages);
        final SimpleSound broadcastSound = Objects.requireNonNull(currentBroadcast).getSound();

        worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
            final PlayerData cache = PlayerData.from(player);
            if (cache.isBroadcastStatus() && player.getWorld().getName().equals(world)) {

                playerChecks(player);

                sendMessages(messages, player);

                if (cache.isBroadcastSoundStatus())
                    new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
            }
        }));
    }

    private static void playerChecks(final Player player) {
        final List<String> currentMessages = Settings.NotificationSettings.RANDOM_MESSAGE ?
                RandomUtil.nextItem(messageList) : messageList.get(index);
        final String permission = Broadcast.getPermissionFromMessage(currentMessages);

        if (!PlayerUtil.hasPerm(player, permission) || permission.isEmpty())
            updateIndex();
    }

    private static void sendMessages(final List<String> messages, final Player player) {
        final String header = Variables.replace(currentBroadcast.getBroadcastHeader(), null);
        final String footer = Variables.replace(currentBroadcast.getBroadcastFooter(), null);
        Common.tellNoPrefix(player, MessageUtil.translateGradient(header));
        messages.forEach(message -> {
            if (MessageUtil.isExecutable(message)) {
                MessageUtil.executePlaceholders(message, player);
                return;
            }

            if (Boolean.TRUE.equals(Broadcast.getCenteredFromMessage(messages)) || Boolean.TRUE.equals(Settings.NotificationSettings.CENTER_ALL))
                message = ChatUtil.center(MessageUtil.translateGradient(message));

            message = Variables.replace(message, player);

            Common.tellNoPrefix(player, message);
        });

        Common.tellNoPrefix(player, MessageUtil.translateGradient(footer));
    }

    private static void updateIndex() {
        index = ++index == BroadcastTask.messageList.size() ? 0 : index;
    }
}