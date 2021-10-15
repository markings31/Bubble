package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlSectionConfig;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerCache extends YamlSectionConfig {

	private static final ExpiringMap<UUID, PlayerCache> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private final String playerName;

	private final UUID uuid;

	private boolean broadcastStatus;
	private boolean motdStatus;
	private boolean mentionsStatus;

	private PlayerCache(final String name, final UUID uuid) {
		super("Players." + uuid.toString());

		this.playerName = name;
		this.uuid = uuid;

		loadConfiguration(NO_DEFAULT, FoConstants.File.DATA);
	}

	@Override
	protected void onLoadFinish() {
		broadcastStatus = getBoolean("Receive_Broadcasts", true);
		motdStatus = getBoolean("Receive_MOTD", true);
		mentionsStatus = getBoolean("Receive_Mentions", true);
	}

	public void setBroadcastStatus(final boolean broadcastStatus) {
		this.broadcastStatus = broadcastStatus;

		save("Receive_Broadcasts", broadcastStatus);
	}

	public void setMotdStatus(final boolean motdStatus) {
		this.motdStatus = motdStatus;

		save("Receive_MOTD", motdStatus);
	}

	public void setMentionsStatus(final boolean mentionsStatus) {
		this.mentionsStatus = mentionsStatus;

		save("Receive_Mentions", mentionsStatus);
	}

	/* ------------------------------------------------------------------------------- */
	/* Misc methods */
	/* ------------------------------------------------------------------------------- */

	@Nullable
	public Player toPlayer() {
		val player = Remain.getPlayerByUUID(this.uuid);

		return player != null && player.isOnline() ? player : null;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Static Methods
	// --------------------------------------------------------------------------------------------------------------

	public static PlayerCache getCache(final Player player) {
		synchronized (cacheMap) {
			val uniqueId = player.getUniqueId();
			val playerName = player.getName();

			var cache = cacheMap.get(uniqueId);

			if (cache == null) {
				cache = new PlayerCache(playerName, uniqueId);

				cacheMap.put(uniqueId, cache);
			}

			return cache;
		}
	}

	public static void clearAllData() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}
}
