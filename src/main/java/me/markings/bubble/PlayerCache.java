package me.markings.bubble;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public final class PlayerCache extends YamlSectionConfig {

	private static final ExpiringMap<UUID, PlayerCache> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private boolean broadcastStatus;
	private boolean motdStatus;
	private boolean mentionsStatus;

	private PlayerCache(final UUID uuid) {
		super("Players." + uuid.toString());

		loadConfiguration(NO_DEFAULT, FoConstants.File.DATA);
	}

	@Override
	protected void onLoadFinish() {
		broadcastStatus = getBoolean("Receive_Broadcasts", true);
		motdStatus = getBoolean("Receive_MOTD", true);
		mentionsStatus = getBoolean("Receive_Mentions", true);
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static Methods
	// --------------------------------------------------------------------------------------------------------------

	public static PlayerCache getCache(final Player player) {
		synchronized (cacheMap) {
			return cacheMap.computeIfAbsent(player.getUniqueId(), k
					-> cacheMap.get(player.getUniqueId()));
		}
	}

	public static void clearAllData() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}
}
