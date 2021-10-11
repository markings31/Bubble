package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class BroadcastTask extends BukkitRunnable {

	private static int index;
	private String currentPath;

	private static final String messageType = "message";

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE)) {

			if ((Remain.getOnlinePlayers().isEmpty() && Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))
					|| (Boolean.TRUE.equals(Settings.BroadcastSettings.BUNGEECORD) && Bubble.getPlayers().isEmpty()))
				return;

			val worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;
			val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

			val header = Settings.BroadcastSettings.HEADER;
			val footer = Settings.BroadcastSettings.FOOTER;

			val messageList = Settings.BroadcastSettings.MESSAGE_MAP;
			val broadcastPerm = Settings.BroadcastSettings.PERMISSION;

			worlds.forEach(world -> {

				var messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
						RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);

				for (val player : Remain.getOnlinePlayers()) {

					val cache = PlayerCache.getCache(player);

					if (cache.isBroadcastStatus()) {

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

						messages.forEach(message -> {
							message = Settings.BroadcastSettings.CENTER_ALL ? ChatUtil.center(message) : message;
							MessageUtil.executePlaceholders(message, player);
							if (!Settings.BroadcastSettings.BUNGEECORD)
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

						new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
					}
				}
			});

			updateIndex(messageList.keySet().stream().toList());
		}
	}


	private static void updateIndex(final List<List<String>> messages) {
		index = ++index == messages.size() ? 0 : index;
	}
}