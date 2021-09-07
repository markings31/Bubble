package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.util.List;

public class BroadcastTask extends BukkitRunnable {

	private static int index;

	private static final String messageType = "message";

	@Override
	public void run() {
		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE) && !Remain.getOnlinePlayers().isEmpty()) {
			val worlds = Settings.BroadcastSettings.BROADCAST_WORLDS;
			val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

			val enablePermission = Settings.BroadcastSettings.ENABLE_PERMISSION;

			val orderedMsgList = Localization.BroadcastMessages.MESSAGE_LIST;
			val messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
					RandomUtil.nextItem(orderedMsgList) : orderedMsgList.get(index);

			worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
				if (player.getWorld().getName().equals(world)) {

					if (enablePermission && !player.hasPermission(Settings.BroadcastSettings.BROADCAST_PERMISSION))
						return;

					if (!Settings.BroadcastSettings.BUNGEECORD)
						Common.tellNoPrefix(player, MessageUtil.format(Localization.BroadcastMessages.HEADER), "&f");
					else
						BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, MessageUtil.format(Localization.BroadcastMessages.HEADER) + "\n");
					
					messages.forEach(message -> {
						if (MessageUtil.isCommand(message) || MessageUtil.isBungeeTitle(message))
							MessageUtil.executePlaceholders(message, player);
						else if (!Settings.BroadcastSettings.BUNGEECORD)
							Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(message, player));
						else
							BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, MessageUtil.replaceVarsAndGradient(message, player));
					});

					if (!Settings.BroadcastSettings.BUNGEECORD)
						Common.tellNoPrefix(player, "&f", MessageUtil.format(Localization.BroadcastMessages.FOOTER));
					else
						BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageType, "\n" + MessageUtil.format(Localization.BroadcastMessages.FOOTER));

					new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
				}
			}));

			updateIndex(orderedMsgList);
		}
	}


	private static void updateIndex(final List<List<String>> messages) {
		index++;
		index = index == messages.size() ? 0 : index;
	}
}