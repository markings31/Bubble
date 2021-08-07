package me.markings.bubble.tasks;

import me.markings.bubble.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class BroadcastTask extends BukkitRunnable {

	private static int index;

	@Override
	public void run() {
		final List<String> messages = Settings.BroadcastSettings.BROADCAST_MESSAGES;
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS) {
			final String message = Settings.BroadcastSettings.RANDOM_MESSAGE ? RandomUtil.nextItem(messages) : messages.get(index);

			// TODO: Add support for '<center>' and '<smooth_line>' placeholders.
			for (final Player player : Remain.getOnlinePlayers()) {
				Variables.replace(message, player);

				Common.tellNoPrefix(player, "&f", Variables.replace(message, player), "&f");
				CompSound.NOTE_PLING.play(player);
			}

			updateIndex(messages);
		}
	}

	private static void updateIndex(final List<String> messages) {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}