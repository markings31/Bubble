package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;
import java.util.Map;

public class BroadcastTask extends BukkitRunnable {

	private static int index;
	private static String currentPath;

	private static final String messageType = "message";

	private static final String header = Settings.BroadcastSettings.HEADER;
	private static final String footer = Settings.BroadcastSettings.FOOTER;

	private static final Map<List<String>, String> messageList = Settings.BroadcastSettings.MESSAGE_MAP;
	private static final Map<String, String> broadcastPerm = Settings.BroadcastSettings.PERMISSION;

	private static final List<String> worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE) &&
				!(Remain.getOnlinePlayers().isEmpty() && Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))) {

			val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

			executeTasks(broadcastSound);

			updateIndex(messageList.keySet().stream().toList());
		}
	}

	private static void sendMessages(final List<String> messages, final Player player) {
		Common.tellNoPrefix(player, MessageUtil.format(header), "&f");
		messages.forEach(message -> {
			message = Boolean.TRUE.equals(Settings.BroadcastSettings.CENTER_ALL) ? ChatUtil.center(message) : message;

			MessageUtil.executePlaceholders(message, player);

			Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(message, player));
		});

		Common.tellNoPrefix(player, "&f", MessageUtil.format(footer));
	}

	private static void sendBungeeMessages(final List<String> messages) {
		Common.log("Sent Bungee 1");
		BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, MessageUtil.format(header) + "\n");

		messages.forEach(message -> {
			Common.log("Sent Bungee 2");
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType,
					(Boolean.TRUE.equals(Settings.BroadcastSettings.CENTER_ALL) ? ChatUtil.center(message) : message));
		});

		BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, "\n" + MessageUtil.format(footer));
	}

	private static void playerChecks(final Player player, final String world) {
		if (player.getWorld().getName().equals(world)) {
			val currentMessages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
					RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);
			currentPath = broadcastPerm.keySet().stream().filter(path
					-> path.equals(messageList.get(currentMessages))).findFirst().orElse(currentPath);
		}

		if (!player.hasPermission(broadcastPerm.get(currentPath)))
			updateIndex(messageList.keySet().stream().toList());
	}

	private static void executeTasks(final SimpleSound broadcastSound) {
		val messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
				RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);

		if (Boolean.TRUE.equals(Settings.BroadcastSettings.BUNGEECORD))
			sendBungeeMessages(messages);
		else worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
			val cache = PlayerCache.getCache(player);
			if (cache.isBroadcastStatus()) {

				playerChecks(player, world);

				sendMessages(messages, player);

				new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
			}
		}));
	}

	private static void updateIndex(final List<List<String>> messages) {
		index = ++index == messages.size() ? 0 : index;
	}
}