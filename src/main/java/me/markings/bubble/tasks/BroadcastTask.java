package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.Bubble;
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

	private static List<String> messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
			RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);
	private static final List<String> worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;

	// TODO: Test with extracted functions.
	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE)) {

			if ((Remain.getOnlinePlayers().isEmpty() && Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))
					|| (Boolean.TRUE.equals(Settings.BroadcastSettings.BUNGEECORD) && Bubble.getPlayers().isEmpty()))
				return;

			val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

			loopWorlds(broadcastSound);

			updateIndex(messageList.keySet().stream().toList());
		}
	}

	private static void sendMessage(final List<String> messages, final Player player) {
		messages.forEach(message -> {

			message = Boolean.TRUE.equals(Settings.BroadcastSettings.CENTER_ALL) ? ChatUtil.center(message) : message;

			MessageUtil.executePlaceholders(message, player);

			if (Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))
				Common.tellNoPrefix(player,
						MessageUtil.format(header),
						"&f",
						MessageUtil.replaceVarsAndGradient(message, player),
						"&f",
						MessageUtil.format(footer));
			else
				BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType,
						MessageUtil.format(header)
								+ "\n"
								+ MessageUtil.replaceVarsAndGradient(message, player)
								+ "\n"
								+ MessageUtil.format(footer));
		});
	}

	private static void playerChecks(final Player player, final String world) {
		if (player.getWorld().getName().equals(world)) {
			val currentMessages = messages;
			currentPath = broadcastPerm.keySet().stream().filter(path
					-> path.equals(messageList.get(currentMessages))).findFirst().orElse(currentPath);
		}


		if (!player.hasPermission(broadcastPerm.get(currentPath))) {
			updateIndex(messageList.keySet().stream().toList());

			messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
					RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);
		}
	}

	private static void loopWorlds(final SimpleSound broadcastSound) {
		worlds.forEach(world -> {
			for (val player : Remain.getOnlinePlayers()) {

				val cache = PlayerCache.getCache(player);

				if (cache.isBroadcastStatus()) {

					playerChecks(player, world);

					sendMessage(messages, player);

					new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
				}
			}
		});
	}

	private static void updateIndex(final List<List<String>> messages) {
		index = ++index == messages.size() ? 0 : index;
	}
}