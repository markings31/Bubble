package me.markings.bubble.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.PlayerData;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;

@AutoRegister
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DatabaseListener implements Listener {

	@Getter
	private static final DatabaseListener instance = new DatabaseListener();

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLogin(final AsyncPlayerPreLoginEvent event) {
		if (Boolean.TRUE.equals(Settings.DatabaseSettings.ENABLE_MYSQL)) {
			val uuid = event.getUniqueId();

			if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
				val cache = PlayerData.getCache(uuid);

				BubbleDatabase.getInstance().load(uuid, cache);
			}
		}
	}

	@EventHandler
	public void onQuit(final PlayerQuitEvent event) {
		if (Boolean.TRUE.equals(Settings.DatabaseSettings.ENABLE_MYSQL)) {
			val player = event.getPlayer();
			val cache = PlayerData.getCache(player);

			Common.runLaterAsync(() -> BubbleDatabase.getInstance().save(player.getName(), player.getUniqueId(), cache));
		}
	}

}
