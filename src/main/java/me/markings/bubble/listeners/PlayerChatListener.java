package me.markings.bubble.listeners;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerChatListener implements Listener {

	private String mention = "";

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		val mentionSound = Settings.ChatSettings.MENTION_SOUND;
		val eventMessage = event.getMessage();
		val mentionPlayer = new AtomicReference<>();
		Remain.getOnlinePlayers().forEach(loopPlayer -> {
			val groupedMessage = eventMessage.split(" ");
			Arrays.asList(groupedMessage).forEach(messageArg -> {
				if (messageArg.toLowerCase().contains("@" + loopPlayer.getName().toLowerCase())) {
					mentionPlayer.set(loopPlayer.getName());
					mention = "@" + mentionPlayer;
				}
			});
		});
		val player = Bukkit.getPlayer(mentionPlayer.toString());
		final PlayerCache cache;

		if (player != null && player.isOnline()) {
			cache = PlayerCache.getCache(player.getUniqueId());
			if (!cache.getMentionsStatus())
				return;
		} else
			return;


		val i = event.getRecipients().iterator();
		while (i.hasNext()) {
			val recipient = i.next();
			if (recipient != player) {
				Common.tell(recipient, String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage()).replace("@", ""));
				i.remove();
			}
		}

		event.setMessage(eventMessage.replace(mention, Common.colorize(Settings.ChatSettings.MENTION_COLOR
				+ mention.replace("@", "") + "&r")));

		new SimpleSound(mentionSound.getSound(), mentionSound.getVolume(), mentionSound.getPitch()).play(player);
	}

}
