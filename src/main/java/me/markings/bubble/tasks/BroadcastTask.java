package me.markings.bubble.tasks;

import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class BroadcastTask extends BukkitRunnable {

	private static int index;

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS && !Remain.getOnlinePlayers().isEmpty()) {
			final List<String> worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;

			final List<List<String>> orderedMsgList = Settings.BroadcastSettings.MESSAGE_LIST;
			final List<String> messages = Settings.BroadcastSettings.RANDOM_MESSAGE ?
					RandomUtil.nextItem(orderedMsgList) : orderedMsgList.get(index);

			messages.forEach(message -> {
				MessageUtils.executePlaceholders(message);
				worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
					if (player.getWorld().getName().equals(world)) {
						if (!ChatUtil.isInteractive(message))
							Common.tellNoPrefix(player,
									MessageUtils.format(Settings.BroadcastSettings.HEADER), "&f",
									message, "&f", MessageUtils.format(Settings.BroadcastSettings.FOOTER));
						else
							Common.tellNoPrefix(player, message);

						CompSound.valueOf(Settings.BroadcastSettings.BROADCAST_SOUND).play(player);
					}
				}));
			});

			updateIndex(orderedMsgList);
		}
	}


	private static void updateIndex(final List<List<String>> messages) {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}