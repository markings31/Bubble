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

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class BroadcastSettings {

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean RANDOM_MESSAGE;
		public static Boolean CENTER_ALL;
		public static Boolean SEND_ASYNC;

		public static String HEADER;
		public static String FOOTER;

		public static SimpleTime BROADCAST_DELAY;

		public static SimpleSound BROADCAST_SOUND;

		private static void init() {
			setPathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
			CENTER_ALL = getBoolean("Center_All");
			SEND_ASYNC = getBoolean("Send_Asynchronously");
			BROADCAST_SOUND = getSound("Sound");
			HEADER = getString("Header");
			FOOTER = getString("Footer");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class WelcomeSettings {
		public static Boolean ENABLE_JOIN_MOTD;

		public static List<String> JOIN_MOTD = new ArrayList<>();

		public static SimpleTime MOTD_DELAY;

		public static SimpleSound MOTD_SOUND;

		private static void init() {
			setPathPrefix("Notifications.Welcome");
			ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
			MOTD_DELAY = getTime("MOTD_Delay");
			MOTD_SOUND = getSound("Sound");
			JOIN_MOTD = getStringList("Join_MOTD");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class JoinSettings {

		public static String JOIN_MESSAGE;
		public static String QUIT_MESSAGE;

		public static Boolean ENABLE_JOIN_MESSAGE;
		public static Boolean ENABLE_QUIT_MESSAGE;
		public static Boolean FIREWORK_JOIN;
		public static Boolean MUTE_IF_VANISHED;

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		private static void init() {
			setPathPrefix("Notifications.Join");
			ENABLE_JOIN_MESSAGE = getBoolean("Enable_Join_Message");
			ENABLE_QUIT_MESSAGE = getBoolean("Enable_Quit_Message");

			FIREWORK_JOIN = getBoolean("Firework_On_First_Join");

			MUTE_IF_VANISHED = getBoolean("Mute_If_Vanished");

			BROADCAST_WORLDS = getStringList("Worlds");

			JOIN_MESSAGE = getString("Join_Message");
			QUIT_MESSAGE = getString("Quit_Message");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class ChatSettings {

		public static Boolean ENABLE_MENTIONS;

		public static String MENTION_COLOR;
		public static String MENTION_IGNORE_PERMISSION;

		public static SimpleSound MENTION_SOUND;

		private static void init() {
			setPathPrefix("Chat.Mentions");
			ENABLE_MENTIONS = getBoolean("Enable");
			MENTION_IGNORE_PERMISSION = getString("Ignore_Permission");
			MENTION_COLOR = getString("Color");
			MENTION_SOUND = getSound("Sound");
		}
	}

	public final static class DatabaseSettings {

		public static Boolean ENABLE_MYSQL;

		private static void init() {
			setPathPrefix("Database");
			ENABLE_MYSQL = getBoolean("Enable_MySQL");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class HookSettings {

		public static Boolean VAULT;
		public static Boolean PAPI;
		public static Boolean BSTATS;

		private static void init() {
			setPathPrefix("Hooks");
			VAULT = getBoolean("Vault");
			PAPI = getBoolean("PlaceholderAPI");
			BSTATS = getBoolean("BStats");
		}
	}
}

