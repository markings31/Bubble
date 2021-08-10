package me.markings.bubble.tasks;

import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class BroadcastTask extends BukkitRunnable {

	private static int index;
	private static final String titlePrefix = "[title]";
	private static final String bossbarPrefix = "[bossbar]";
	private static final String actionbarPrefix = "[actionbar]";
	private static final String toastPrefix = "[toast]";
	private static final String commandPrefix = "[command]";
	private static final String centerPlaceholder = "<center>";

	@Override
	public void run() {
		final List<String> messages;
		final String broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS && !Remain.getOnlinePlayers().isEmpty()) {
			final List<List<String>> orderedMsgList = Settings.BroadcastSettings.MESSAGE_LIST;
			messages = Settings.BroadcastSettings.RANDOM_MESSAGE ? RandomUtil.nextItem(orderedMsgList) : orderedMsgList.get(index);

			// TODO: Add support for '<smooth_line>' placeholder.
			for (String message : messages)
				if (message.contains(titlePrefix) || message.contains(bossbarPrefix) || message.contains(actionbarPrefix)
						|| message.contains(toastPrefix) || message.contains(commandPrefix)) {

					MessageUtils.checkForPlaceholders(message);

					if (messages.indexOf(message) == messages.size())
						break;
					
				} else {
					message = message.startsWith(centerPlaceholder) ? ChatUtil.center(message).replace(centerPlaceholder, "") : message;
					for (final Player player : Remain.getOnlinePlayers()) {
						Common.tellNoPrefix(player, "&f", message, "&f");

						CompSound.valueOf(broadcastSound).play(player);
					}
				}
			updateIndex(orderedMsgList);
		}
	}

	private static void updateIndex(final List<List<String>> messages) {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}