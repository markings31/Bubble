package me.markings.bubble.settings;

import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.List;

public class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static class BroadcastSettings {

		public static List<String> BROADCAST_MESSAGES;

		public static Boolean ENABLE_BROADCASTS, RANDOM_MESSAGE;

		public static SimpleTime BROADCAST_DELAY;

		private static void init() {
			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			BROADCAST_MESSAGES = getStringList("Messages");
			RANDOM_MESSAGE = getBoolean("Random_Message");
		}

	}

}
