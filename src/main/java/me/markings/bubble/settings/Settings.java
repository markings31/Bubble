package me.markings.bubble.settings;

import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.List;

public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	public static class BroadcastSettings {

		public static List<String> BROADCAST_MESSAGES;

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean RANDOM_MESSAGE;

		public static SimpleTime BROADCAST_DELAY;

		private BroadcastSettings() {
		}

		private static void init() {
			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			BROADCAST_MESSAGES = getStringList("Messages");
			RANDOM_MESSAGE = getBoolean("Random_Message");
		}
	}

	public static class WelcomeSettings {

		public static List<String> JOIN_MOTD;
		public static List<String> JOIN_BROADCAST;

		public static Boolean ENABLE_JOIN_MOTD;
		public static Boolean ENABLE_JOIN_BROADCASTS;

		public static SimpleTime MOTD_DELAY;

		private WelcomeSettings() {
		}

		private static void init() {
			pathPrefix("Notifications.Welcome");
			ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
			MOTD_DELAY = getTime("MOTD_Delay");
			JOIN_MOTD = getStringList("Join_MOTD");
			ENABLE_JOIN_BROADCASTS = getBoolean("Enable_First_Join_Broadcast");
			JOIN_BROADCAST = getStringList("Join_Broadcasts");
		}

	}

}
