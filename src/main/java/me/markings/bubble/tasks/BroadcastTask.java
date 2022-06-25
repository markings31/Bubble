package me.markings.bubble.tasks;

import lombok.val;
import me.markings.bubble.PlayerData;
import me.markings.bubble.settings.Broadcasts;
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

import java.util.List;
import java.util.Objects;

public class BroadcastTask extends BukkitRunnable {

	private static List<List<String>> messageList;
	private static List<String> worlds;

	private static int index;

	@Override
	public void run() {
		nextCycle();
	}

	public static void nextCycle() {
		messageList = Broadcasts.getAllMessages();
		val broadcastPerm = Broadcasts.getAllPermissions();
		worlds = Broadcasts.getAllWorlds();

		Debugger.debug("broadcasts",
				"Messages: " + messageList +
						" Worlds: " + worlds +
						" Permissions: " + broadcastPerm +
						" Sound: " + Settings.BroadcastSettings.BROADCAST_SOUND);

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE) && !Remain.getOnlinePlayers().isEmpty()) {

			executeTasks();

			updateIndex();
		}
	}

	public static void executeTasks() {
		val messages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
				RandomUtil.nextItem(messageList) : messageList.get(index);
		val broadcastSound = Settings.BroadcastSettings.BROADCAST_SOUND;

		worlds.forEach(world -> Remain.getOnlinePlayers().forEach(player -> {
			val cache = PlayerData.getCache(player);
			if (cache.isBroadcastStatus()) {

				playerChecks(player, world);

				sendMessages(messages, player);

				if (cache.isBroadcastSoundStatus())
					new SimpleSound(broadcastSound.getSound(), broadcastSound.getVolume(), broadcastSound.getPitch()).play(player);
			}
		}));
	}

	// path --> broadcastName
	private static void playerChecks(final Player player, final String world) {
		if (player.getWorld().getName().equals(world)) {
			val currentMessages = Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ?
					RandomUtil.nextItem(messageList) : messageList.get(index);

			if (!player.hasPermission(Objects.requireNonNull(Broadcasts.getPermissionFromMessage(currentMessages))))
				updateIndex();
		}
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

			if (Boolean.TRUE.equals(Broadcasts.getCenteredFromMessage(messages)))
				message = ChatUtil.center(message);

			Common.tellNoPrefix(player, MessageUtil.replaceVarsAndGradient(message, player));
		});
		Common.tellNoPrefix(player, "&f", MessageUtil.replaceVarsAndGradient(footer, player));
	}

	private static void updateIndex() {
		index = ++index == BroadcastTask.messageList.size() ? 0 : index;
	}
}