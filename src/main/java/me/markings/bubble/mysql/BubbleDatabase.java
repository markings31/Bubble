package me.markings.bubble.mysql;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.PlayerData;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BubbleDatabase extends SimpleFlatDatabase<PlayerData> {

	@Getter
	private static final BubbleDatabase instance = new BubbleDatabase();

	@Override
	protected void onLoad(final SerializedMap serializedMap, final PlayerData playerData) {
		val broadcastStatus = serializedMap.get("Receive_Broadcasts", Boolean.class);
		val broadcastSoundStatus = serializedMap.get("Receive_Broadcast_Sound", Boolean.class);
		val motdStatus = serializedMap.get("Reveive_MOTD", Boolean.class);
		val mentionsStatus = serializedMap.get("Receive_Mentions", Boolean.class);
		val mentionSoundStatus = serializedMap.get("Receive_Mention_Sound", Boolean.class);
		val mentionToastStatus = serializedMap.get("Receive_Mentions_Toast", Boolean.class);

		if (broadcastStatus != null)
			playerData.setBroadcastStatus(broadcastStatus);

		if (broadcastSoundStatus != null)
			playerData.setBroadcastSoundStatus(broadcastSoundStatus);

		if (motdStatus != null)
			playerData.setMotdStatus(motdStatus);

		if (mentionsStatus != null)
			playerData.setMentionsStatus(mentionsStatus);

		if (mentionSoundStatus != null)
			playerData.setMentionSoundStatus(mentionSoundStatus);

		if (mentionToastStatus != null)
			playerData.setMentionToastStatus(mentionToastStatus);
	}

	@Override
	protected SerializedMap onSave(final PlayerData playerData) {
		val map = new SerializedMap();

		map.put("Receive_Broadcasts", playerData.isBroadcastStatus());
		map.put("Receive_Broadcast_Sound", playerData.isBroadcastSoundStatus());
		map.put("Receive_MOTD", playerData.isMotdStatus());
		map.put("Receive_Mentions", playerData.isMentionsStatus());
		map.put("Receive_Mention_Sound", playerData.isMentionSoundStatus());
		map.put("Receive_Mentions_Toast", playerData.isMentionToastStatus());

		return map;
	}
}