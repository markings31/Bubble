package me.markings.bubble.listeners;

import me.markings.bubble.settings.Settings;
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
			for (int i = 0; i < messages.toArray().length; i++)
				Common.tellLater(Settings.WelcomeSettings.MOTD_DELAY.getTimeTicks(), player, Variables.replace(messages.get(i)
						.replace("%player%", player.getName()), player));

		if (Settings.WelcomeSettings.ENABLE_JOIN_BROADCASTS && !player.hasPlayedBefore())
			for (int i = 0; i < broadcastMessages.toArray().length; i++)
				Common.broadcast(Variables.replace(broadcastMessages.get(i)
						.replace("%player%", player.getName()), player));
	}

}
