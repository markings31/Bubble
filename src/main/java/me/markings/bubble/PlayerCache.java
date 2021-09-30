package me.markings.bubble;

import lombok.Getter;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerCache extends YamlSectionConfig {

	private static final ExpiringMap<UUID, PlayerCache> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private final UUID uuid;
	private boolean broadcastStatus;

	private PlayerCache(final UUID uuid) {
		super("Players." + uuid.toString());

		this.uuid = uuid;

		loadConfiguration(NO_DEFAULT, "data.db");
	}

	@Override
	protected void onLoadFinish() {
		broadcastStatus = getBoolean("Receive_Broadcasts", true);
	}


	public boolean getBroadcastStatus() {
		return broadcastStatus;
	}


	public void setBroadcastStatus(final boolean broadcastStatus) {
		this.broadcastStatus = broadcastStatus;

		save("Receive_Broadcasts", broadcastStatus);
	}


	// --------------------------------------------------------------------------------------------------------------
	// Static Methods
	// --------------------------------------------------------------------------------------------------------------

	public static PlayerCache getCache(final UUID uuid) {
		PlayerCache cache = cacheMap.get(uuid);

		if (cache == null) {
			cache = new PlayerCache(uuid);

			cacheMap.put(uuid, new PlayerCache(uuid));
		}

		return cache;
	}

	public static void clearAllData() {
		cacheMap.clear();
	}
}