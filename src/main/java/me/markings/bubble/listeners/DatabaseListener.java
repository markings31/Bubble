package me.markings.bubble.listeners;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.Remain;

public class DatabaseListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(final AsyncPlayerPreLoginEvent event) {
		if (Boolean.TRUE.equals(Settings.DatabaseSettings.ENABLE_MYSQL)) {
			val uuid = event.getUniqueId();

			if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
				val cache = PlayerCache.getCache(Remain.getPlayerByUUID(uuid));

				BubbleDatabase.getInstance().load(uuid, cache);
			}
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		if (Boolean.TRUE.equals(Settings.DatabaseSettings.ENABLE_MYSQL)) {
			val player = event.getPlayer();
			val cache = PlayerCache.getCache(player);

			Common.runLaterAsync(() -> BubbleDatabase.getInstance().save(player.getName(), player.getUniqueId(), cache));
		}
	}

}
