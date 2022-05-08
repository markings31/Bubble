package me.markings.bubble.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.PlayerData;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerChatListener implements Listener {

	@Getter
	private static final PlayerChatListener instance = new PlayerChatListener();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		val mentionSound = Settings.ChatSettings.MENTION_SOUND;
		val eventMessage = event.getMessage();
		val eventPlayerName = event.getPlayer().getDisplayName();
		val previousColor = !Common.lastColorLetter(eventMessage).equals("") ? Common.lastColorLetter(eventMessage) : "&f";

		Debugger.debug("chat",
				"Player: " + eventPlayerName +
						"Message: " + eventMessage);

		Remain.getOnlinePlayers().forEach(loopPlayer -> {

			val cache = PlayerData.getCache(loopPlayer);
			val playerName = loopPlayer.getName();

			event.setCancelled(true);
			if (eventMessage.toLowerCase().contains("@" + playerName.toLowerCase())
					&& !loopPlayer.hasPermission(Settings.ChatSettings.MENTION_IGNORE_PERMISSION)
					&& cache.isMentionsStatus()) {

				Common.tellNoPrefix(loopPlayer, String.format(event.getFormat(), eventPlayerName, eventMessage)
						.replace("@" + playerName, Settings.ChatSettings.MENTION_COLOR + playerName + previousColor));

				Common.log(previousColor);

				if (cache.isMentionToastStatus())
					Common.dispatchCommand(loopPlayer, "bu notify {player} toast PAPER " + Localization.NotificationMessages.MENTIONED_MESSAGE);

				if (cache.isMentionSoundStatus())
					new SimpleSound(mentionSound.getSound(), mentionSound.getVolume(), mentionSound.getPitch()).play(loopPlayer);

				return;
			}

			Common.tellNoPrefix(loopPlayer, String.format(event.getFormat(), eventPlayerName, eventMessage));

		});
	}

}
