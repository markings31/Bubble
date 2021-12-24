package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BroadcastTask extends BukkitRunnable {

	private static final String messageType = "message";

	private static final String header = Settings.BroadcastSettings.HEADER;
	private static final String footer = Settings.BroadcastSettings.FOOTER;

	private static final Map<List<String>, String> messageList = Settings.BroadcastSettings.MESSAGE_MAP;
	private static final Map<String, String> broadcastPerm = Settings.BroadcastSettings.PERMISSION;
	private static final List<String> worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;

	private static int index;
	private static String currentPath;

	private static final SimpleSound broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE) &&
				!(Remain.getOnlinePlayers().isEmpty() && Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))) {

			executeTasks();

			updateIndex(new ArrayList<>(messageList.keySet()));
		}
	}

	private static void executeTasks() {
		val messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
				RandomUtil.nextItem(messageList.keySet()) : new ArrayList<>(messageList.keySet()).get(index);

		if (Boolean.TRUE.equals(Settings.BroadcastSettings.BUNGEECORD))
			sendBungeeMessages(messages);
		else worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
			val cache = PlayerCache.getCache(player);
			if (cache.isBroadcastStatus()) {

				playerChecks(player, world);

				sendMessages(messages, player);

				new SimpleSound(BroadcastTask.broadcastSound.getSound(), BroadcastTask.broadcastSound.getVolume(), BroadcastTask.broadcastSound.getPitch()).play(player);
			}
		}));
	}

	private static void playerChecks(final Player player, final String world) {
		if (player.getWorld().getName().equals(world)) {
			val currentMessages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
					RandomUtil.nextItem(messageList.keySet()) : new ArrayList<>(messageList.keySet()).get(index);
			currentPath = broadcastPerm.keySet().stream().filter(path
					-> path.equals(messageList.get(currentMessages))).findFirst().orElse(currentPath);
		}

		if (!player.hasPermission(broadcastPerm.get(currentPath)))
			updateIndex(new ArrayList<>(messageList.keySet()));
	}

	private static void sendMessages(final List<String> messages, final Player player) {
		val file = new File("plugins/Bubble/", "settings.yml");
		val config = YamlConfiguration.loadConfiguration(file);
		Common.tellNoPrefix(player, MessageUtil.format(header), "&f");
		messages.forEach(message -> {
			message = Boolean.TRUE.equals(Settings.BroadcastSettings.CENTER_ALL) ? ChatUtil.center(message) : message;

			if (config.getBoolean("Notifications.Broadcast.Messages." + currentPath + ".Center"))
				message = ChatUtil.center(message);

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

	private static void updateIndex(final List<List<String>> messages) {
		index = ++index == messages.size() ? 0 : index;
	}
}