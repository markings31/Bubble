package me.markings.bubble.commands.tasks;

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

	private static final List<String> messages = Settings.BroadcastSettings.BROADCAST_MESSAGES;

	private static int index;

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS) {
			String prefix = Settings.PLUGIN_PREFIX;
			String message = Settings.BroadcastSettings.RANDOM_MESSAGE ? RandomUtil.nextItem(messages) : messages.get(index);

			for (Player player : Remain.getOnlinePlayers()) {
				Variables.replace(message, player);

				Common.tellNoPrefix(player, "&f", message.replace("%prefix%", prefix), "&f");
				CompSound.NOTE_PLING.play(player);
			}

			updateIndex();
		}
	}

	private static void updateIndex() {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}