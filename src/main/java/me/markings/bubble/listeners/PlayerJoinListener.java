package me.markings.bubble.listeners;

import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Variables;

import java.util.List;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		final List<String> messages = Settings.WelcomeSettings.JOIN_MOTD;
		final List<String> broadcastMessages = Settings.WelcomeSettings.JOIN_BROADCAST;
		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD)
			for (final String message : messages) {
				MessageUtils.checkForPlaceholders(message);
				Common.tellLater(Settings.WelcomeSettings.MOTD_DELAY.getTimeTicks(), player, Variables.replace(message, player));
			}

		if (Settings.WelcomeSettings.ENABLE_JOIN_BROADCASTS && !player.hasPlayedBefore())
			for (int i = 0; i < broadcastMessages.toArray().length; i++) {
				MessageUtils.checkForPlaceholders(broadcastMessages.get(i));
				Common.broadcast(Variables.replace(broadcastMessages.get(i), player));
			}
	}

}
