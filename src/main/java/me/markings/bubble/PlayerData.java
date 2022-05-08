package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.api.Cache;
import org.bukkit.entity.Player;
import org.mineacademy.fo.collection.expiringmap.ExpiringMap;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import javax.annotation.Nullable;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class PlayerData extends YamlConfig implements Cache {

	private static final ExpiringMap<UUID, PlayerData> cacheMap = ExpiringMap.builder().expiration(30, TimeUnit.MINUTES).build();

	private final String playerName;
	private final UUID uuid;

	private boolean broadcastStatus;
	private boolean broadcastSoundStatus;

	private boolean motdStatus;

	private boolean mentionsStatus;
	private boolean mentionSoundStatus;
	private boolean mentionToastStatus;

	private PlayerData(final String playerName, final UUID uuid) {
		this.playerName = playerName;
		this.uuid = uuid;

		this.loadConfiguration(NO_DEFAULT, "players/" + uuid + ".yml");
		this.save();
	}

	@Override
	protected void onLoad() {
		broadcastStatus = getBoolean("Receive_Broadcasts", true);
		broadcastSoundStatus = getBoolean("Receive_Broadcast_Sound", true);

		motdStatus = getBoolean("Receive_MOTD", true);

		mentionsStatus = getBoolean("Receive_Mentions", true);
		mentionSoundStatus = getBoolean("Receive_Mention_Sound", true);
		mentionToastStatus = getBoolean("Receive_Mentions_Toast", true);
	}

	@Override
	protected void onSave() {
		set("Receive_Broadcasts", this.broadcastStatus);
		set("Receive_Broadcast_Sound", this.broadcastSoundStatus);
		set("Receive_MOTD", motdStatus);
		set("Receive_Mentions", mentionsStatus);
		set("Receive_Mention_Sound", mentionSoundStatus);
		set("Receive_Mentions_Toast", mentionToastStatus);
	}

	@Override
	public void setBroadcastStatus(final boolean broadcastStatus) {
		this.broadcastStatus = broadcastStatus;

		set("Receive_Broadcasts", broadcastStatus);
	}

	@Override
	public void setBroadcastSoundStatus(final boolean broadcastSoundStatus) {
		this.broadcastSoundStatus = broadcastSoundStatus;

		set("Receive_Broadcast_Sound", broadcastSoundStatus);
	}

	@Override
	public void setMotdStatus(final boolean motdStatus) {
		this.motdStatus = motdStatus;

		set("Receive_MOTD", motdStatus);
	}

	@Override
	public void setMentionsStatus(final boolean mentionsStatus) {
		this.mentionsStatus = mentionsStatus;

		set("Receive_Mentions", mentionsStatus);
	}

	@Override
	public void setMentionSoundStatus(final boolean mentionSoundStatus) {
		this.mentionSoundStatus = mentionSoundStatus;

		set("Receive_Mention_Sound", mentionSoundStatus);
	}

	@Override
	public void setMentionToastStatus(final boolean mentionToastStatus) {
		this.mentionToastStatus = mentionToastStatus;

		set("Receive_Mentions_Toast", mentionToastStatus);
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

	public static PlayerData getCache(final UUID uuid) {
		return getCache(Remain.getPlayerByUUID(uuid));
	}

	public static PlayerData getCache(final Player player) {
		final UUID uuid = player.getUniqueId();
		PlayerData data = cacheMap.get(uuid);

		if (data == null) {
			data = new PlayerData(player.getName(), uuid);
			cacheMap.put(uuid, data);
		}

		return data;
	}

	public void removeFromMemory() {
		synchronized (cacheMap) {
			cacheMap.remove(this.uuid);
		}
	}


	public static void clearAllData() {
		synchronized (cacheMap) {
			cacheMap.clear();
		}
	}
}
