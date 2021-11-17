package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.api.Cache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlSectionConfig;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerCache extends YamlSectionConfig implements Cache {

	private static final ExpiringMap<UUID, PlayerCache> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private final UUID uuid;

	private boolean broadcastStatus;
	private boolean broadcastSoundStatus;

	private boolean motdStatus;

	private boolean mentionsStatus;
	private boolean mentionSoundStatus;
	private boolean mentionToastStatus;

	private PlayerCache(final UUID uuid) {
		super("Players." + uuid.toString());

		this.uuid = uuid;

		loadConfiguration(NO_DEFAULT, FoConstants.File.DATA);
	}

	@Override
	protected void onLoadFinish() {
		broadcastStatus = getBoolean("Receive_Broadcasts", true);
		broadcastSoundStatus = getBoolean("Receive_Broadcast_Sound", true);

		motdStatus = getBoolean("Receive_MOTD", true);

		mentionsStatus = getBoolean("Receive_Mentions", true);
		mentionSoundStatus = getBoolean("Receive_Mention_Sound", true);
		mentionToastStatus = getBoolean("Receive_Mentions_Toast", true);
	}

	public void setBroadcastStatus(final boolean broadcastStatus) {
		this.broadcastStatus = broadcastStatus;

		save("Receive_Broadcasts", broadcastStatus);
	}

	public void setBroadcastSoundStatus(final boolean broadcastSoundStatus) {
		this.broadcastSoundStatus = broadcastSoundStatus;

		save("Receive_Broadcast_Sound", broadcastSoundStatus);
	}

	public void setMotdStatus(final boolean motdStatus) {
		this.motdStatus = motdStatus;

		save("Receive_MOTD", motdStatus);
	}

	public void setMentionsStatus(final boolean mentionsStatus) {
		this.mentionsStatus = mentionsStatus;

		save("Receive_Mentions", mentionsStatus);
	}

	public void setMentionSoundStatus(final boolean mentionSoundStatus) {
		this.mentionSoundStatus = mentionSoundStatus;

		save("Receive_Mention_Sound", mentionSoundStatus);
	}

	public void setMentionToastStatus(final boolean mentionToastStatus) {
		this.mentionToastStatus = mentionToastStatus;

		save("Receive_Mentions_Toast", mentionToastStatus);
	}

	/* ------------------------------------------------------------------------------- */
	/* Misc methods
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
		return getCache(player.getUniqueId());
	}

	public static PlayerCache getCache(final UUID uuid) {
		synchronized (cacheMap) {
			PlayerCache cache = cacheMap.get(uuid);

			if (cache == null) {
				cache = new PlayerCache(uuid);

				cacheMap.put(uuid, cache);
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
