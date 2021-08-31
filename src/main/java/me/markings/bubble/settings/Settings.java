package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@Override
	protected boolean saveComments() {
		return true;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BroadcastSettings {

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean RANDOM_MESSAGE;

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static SimpleTime BROADCAST_DELAY;

		public static SimpleSound BROADCAST_SOUND;

		private static void init() {

			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
			BROADCAST_WORLDS = getStringList("Worlds");
			BROADCAST_SOUND = getSound("Sound");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeSettings {

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static Boolean ENABLE_JOIN_MOTD;
		public static Boolean ENABLE_JOIN_BROADCASTS;

		public static SimpleTime MOTD_DELAY;

		public static SimpleSound MOTD_SOUND;

		private static void init() {
			pathPrefix("Notifications.Welcome");
			ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
			MOTD_DELAY = getTime("MOTD_Delay");
			ENABLE_JOIN_BROADCASTS = getBoolean("Enable_First_Join_Broadcast");
			BROADCAST_WORLDS = getStringList("Worlds");
			MOTD_SOUND = getSound("Sound");
		}
	}
}
