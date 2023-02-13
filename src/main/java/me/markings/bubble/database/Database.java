package me.markings.bubble.database;

import lombok.Getter;
import me.markings.bubble.PlayerData;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.database.SimpleFlatDatabase;

public final class Database extends SimpleFlatDatabase<PlayerData> {

	@Getter
	private final static Database instance = new Database();

	private Database() {
		this.addVariable("table", "Bubble");
	}

	@Override
	protected void onLoad(final SerializedMap serializedMap, final PlayerData playerData) {
		final Boolean broadcastStatus = serializedMap.get("Receive_Broadcasts", Boolean.class);

		if (broadcastStatus != null)
			playerData.setBroadcastStatus(broadcastStatus);
	}

	@Override
	protected SerializedMap onSave(final PlayerData playerData) {
		return null;
	}
}
