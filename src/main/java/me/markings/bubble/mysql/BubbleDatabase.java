package me.markings.bubble.mysql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.PlayerCache;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BubbleDatabase extends SimpleFlatDatabase<PlayerCache> {

	@Getter
	private static final BubbleDatabase instance = new BubbleDatabase();

	@Override
	protected void onLoad(final SerializedMap serializedMap, final PlayerCache playerCache) {
		val broadcastStatus = serializedMap.get("Receive_Broadcasts", Boolean.class);
		val motdStatus = serializedMap.get("Reveive_MOTD", Boolean.class);
		val mentionsStatus = serializedMap.get("Receive_Mentions", Boolean.class);

		if (broadcastStatus != null)
			playerCache.setBroadcastStatus(broadcastStatus);

		if (motdStatus != null)
			playerCache.setMotdStatus(motdStatus);

		if (mentionsStatus != null)
			playerCache.setMentionsStatus(mentionsStatus);
	}

	@Override
	protected SerializedMap onSave(final PlayerCache playerCache) {
		val map = new SerializedMap();

		map.put("Receive_Broadcasts", playerCache.isBroadcastStatus());
		map.put("Receive_MOTD", playerCache.isMotdStatus());
		map.put("Receive_Mentions", playerCache.isMentionsStatus());

		return map;
	}
}