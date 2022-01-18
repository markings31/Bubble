package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BroadcastTask extends BukkitRunnable {

	private static final Map<List<String>, String> messageList = Settings.BroadcastSettings.MESSAGE_MAP;
	private static final Map<String, String> broadcastPerm = Settings.BroadcastSettings.PERMISSION;
	private static final Map<List<String>, String> worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;

	private static int index;
	private static String currentPath;

	@Override
	public void run() {
		Debugger.debug("broadcasts",
				"Messages: " + messageList.keySet() +
						" Worlds: " + worlds.keySet() +
						" Permissions: " + broadcastPerm.values() +
						" Sound: " + Settings.BroadcastSettings.BROADCAST_SOUND);

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE) && !Remain.getOnlinePlayers().isEmpty()) {

			executeTasks();

			updateIndex(new ArrayList<>(messageList.keySet()));
		}
	}

	private static void executeTasks() {
		val messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
				RandomUtil.nextItem(messageList.keySet()) : new ArrayList<>(messageList.keySet()).get(index);
		val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

		worlds.keySet().forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
			val cache = PlayerCache.getCache(player);
			if (cache.isBroadcastStatus()) {

				playerChecks(player, world);

				sendMessages(messages, player);

				if (cache.isBroadcastSoundStatus())
					new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
			}
		}));
	}

	private static void playerChecks(final Player player, final List<String> worlds) {
		worlds.forEach(world -> {
			if (player.getWorld().getName().equals(world)) {
				val currentMessages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
						RandomUtil.nextItem(messageList.keySet()) : new ArrayList<>(messageList.keySet()).get(index);
				currentPath = broadcastPerm.keySet().stream().filter(path
						-> path.equals(messageList.get(currentMessages))).findFirst().orElse(currentPath);
			}

			if (!player.hasPermission(broadcastPerm.get(currentPath)))
				updateIndex(new ArrayList<>(messageList.keySet()));
		});
	}

	private static void sendMessages(final List<String> messages, final Player player) {
		val header = Settings.BroadcastSettings.HEADER;
		val footer = Settings.BroadcastSettings.FOOTER;
		Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(header, player), "&f");
		messages.forEach(message -> {

			if (MessageUtil.isExecutable(message)) {
				MessageUtil.executePlaceholders(message, player);
				return;
			}

			message = Boolean.TRUE.equals(Settings.BroadcastSettings.CENTER_ALL) ? ChatUtil.center(message) : message;

			if (Bubble.getInstance().getBubbleSettings().getBoolean("Notifications.Broadcast.Messages." + currentPath + ".Center"))
				message = ChatUtil.center(message);

			Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(message, player));
		});
		Common.tellNoPrefix(player, "&f", MessageUtil.replaceVarsAndGradient(footer, player));
	}

	private static void updateIndex(final List<List<String>> messages) {
		index = ++index == messages.size() ? 0 : index;
	}
}