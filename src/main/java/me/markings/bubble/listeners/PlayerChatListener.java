package me.markings.bubble.listeners;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

public class PlayerChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		val mentionSound = Settings.ChatSettings.MENTION_SOUND;
		val eventMessage = event.getMessage();

		Remain.getOnlinePlayers().forEach(loopPlayer -> {

			val cache = PlayerCache.getCache(loopPlayer.getUniqueId());

			event.setCancelled(true);
			if (eventMessage.toLowerCase().contains("@" + loopPlayer.getName().toLowerCase())
					&& !loopPlayer.hasPermission(Settings.ChatSettings.MENTION_IGNORE_PERMISSION)
					&& cache.getMentionsStatus()) {

				Common.tell(loopPlayer, String.format(event.getFormat(), event.getPlayer().getDisplayName(), eventMessage)
						.replace("@" + loopPlayer.getName(), Settings.ChatSettings.MENTION_COLOR + loopPlayer.getName() + "&r"));
				new SimpleSound(mentionSound.getSound(), mentionSound.getVolume(), mentionSound.getPitch()).play(loopPlayer);

				return;
			}

			Common.tell(loopPlayer, String.format(event.getFormat(), event.getPlayer().getDisplayName(), eventMessage));

		});
	}

}
