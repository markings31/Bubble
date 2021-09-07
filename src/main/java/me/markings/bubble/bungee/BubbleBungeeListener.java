package me.markings.bubble.bungee;

import org.bukkit.entity.Player;
import org.mineacademy.fo.bungee.BungeeListener;
import org.mineacademy.fo.bungee.message.IncomingMessage;

public class BubbleBungeeListener extends BungeeListener {

	@Override
	public void onMessageReceived(final Player player, final IncomingMessage message) {
	/*	if (message.getAction() == BubbleAction.NOTIFICATION) {
			final String senderName = message.readString();
			final String chatMessage = message.readString();

			Bukkit.getOnlinePlayers().forEach(online -> Common.tellNoPrefix(online,
					String.format("&8[&5%s&8] &7%s: &f%s", message.getServerName(), senderName, chatMessage)));
		}*/
	}
}
