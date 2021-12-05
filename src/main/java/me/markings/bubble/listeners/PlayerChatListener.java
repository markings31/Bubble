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
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

public class PlayerChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		val mentionSound = Settings.ChatSettings.MENTION_SOUND;
		val eventMessage = event.getMessage();
		val eventPlayerName = event.getPlayer().getDisplayName();

		Remain.getOnlinePlayers().forEach(loopPlayer -> {

			val cache = PlayerCache.getCache(loopPlayer);
			val playerName = loopPlayer.getName();

			event.setCancelled(true);
			if (eventMessage.toLowerCase().contains("@" + playerName.toLowerCase())
					&& !loopPlayer.hasPermission(Settings.ChatSettings.MENTION_IGNORE_PERMISSION)
					&& cache.isMentionsStatus()) {

				if (cache.isMentionToastStatus())
					Remain.sendToast(loopPlayer, "You've been mentioned in the chat! \n"
							+ event.getPlayer() + ": " + eventMessage, CompMaterial.PAPER);
				
				Common.tell(loopPlayer, String.format(event.getFormat(), eventPlayerName, eventMessage)
						.replace("@" + playerName, Settings.ChatSettings.MENTION_COLOR + playerName + "&r"));

				if (cache.isMentionToastStatus())
					Common.dispatchCommand(loopPlayer, "bu notify {player} toast PAPER &e&oYou were mentioned in the chat!");

				if (cache.isMentionSoundStatus())
					new SimpleSound(mentionSound.getSound(), mentionSound.getVolume(), mentionSound.getPitch()).play(loopPlayer);

				return;
			}

			Common.tell(loopPlayer, String.format(event.getFormat(), eventPlayerName, eventMessage));

		});
	}

}
