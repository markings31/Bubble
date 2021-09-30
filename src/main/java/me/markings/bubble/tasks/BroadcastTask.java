package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Localization;
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

			if (Remain.getOnlinePlayers().isEmpty() && Boolean.FALSE.equals(Settings.BroadcastSettings.BUNGEECORD))
				return;

			val worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;
			val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

			val messageList = Localization.BroadcastMessages.MESSAGE_MAP;
			val broadcastPerm = Localization.BroadcastMessages.PERMISSION;

			worlds.forEach(world -> {

				List<String> messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
						RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);

				for (val player : Remain.getOnlinePlayers()) {

					val cache = PlayerCache.getCache(player.getUniqueId());

					if (cache.getBroadcastStatus()) {

						if (player.getWorld().getName().equals(world)) {
							val currentMessages = messages;
							currentPath = broadcastPerm.keySet().stream().filter(path -> path.equals(messageList.get(currentMessages))).findFirst().orElse(currentPath);
						}

						if (!player.hasPermission(broadcastPerm.get(currentPath))) {
							updateIndex(messageList.keySet().stream().toList());
							messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
									RandomUtil.nextItem(messageList.keySet()) : messageList.keySet().stream().toList().get(index);
						}

						if (!Settings.BroadcastSettings.BUNGEECORD)
							Common.tellNoPrefix(player, MessageUtil.format(Localization.BroadcastMessages.HEADER), "&f");
						else
							BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, MessageUtil.format(Localization.BroadcastMessages.HEADER) + "\n");

						messages.forEach(message -> {
							message = Settings.BroadcastSettings.CENTER_ALL ? ChatUtil.center(message) : message;
							MessageUtil.executePlaceholders(message, player);
							if (!Settings.BroadcastSettings.BUNGEECORD)
								Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(message, player));
							else
								BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, MessageUtil.replaceVarsAndGradient(message, player));
						});

						if (!Settings.BroadcastSettings.BUNGEECORD)
							Common.tellNoPrefix(player, "&f", MessageUtil.format(Localization.BroadcastMessages.FOOTER));
						else
							BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, "messageType", "\n" + MessageUtil.format(Localization.BroadcastMessages.FOOTER));

						new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
					}
				}
			});

			updateIndex(messageList.keySet().stream().toList());
		}
	}


	private static void updateIndex(final List<List<String>> messages) {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}