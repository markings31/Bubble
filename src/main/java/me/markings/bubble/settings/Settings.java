package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		public static Boolean BUNGEECORD;
		public static Boolean RANDOM_MESSAGE;
		public static Boolean CENTER_ALL;

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static SimpleTime BROADCAST_DELAY;

		public static SimpleSound BROADCAST_SOUND;

		private static void init() {
			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BUNGEECORD = getBoolean("Bungeecord");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
			CENTER_ALL = getBoolean("Center_All");
			BROADCAST_WORLDS = getStringList("Worlds");
			BROADCAST_SOUND = getSound("Sound");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeSettings {
		public static Boolean ENABLE_JOIN_MOTD;
		public static Boolean BUNGEECORD;

		public static List<List<String>> JOIN_MOTD = new ArrayList<>();

		public static SimpleTime MOTD_DELAY;

		public static SimpleSound MOTD_SOUND;

		private static void init() {
			Objects.requireNonNull(getConfig().getConfigurationSection("Notifications.Welcome.Join_MOTD")).getKeys(false)
					.forEach(path -> JOIN_MOTD.add(getConfig().getStringList("Notifications.Welcome.Join_MOTD" + "." + path)));

			pathPrefix("Notifications.Welcome");
			ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
			BUNGEECORD = getBoolean("Bungeecord");
			MOTD_DELAY = getTime("MOTD_Delay");
			MOTD_SOUND = getSound("Sound");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class JoinSettings {

		public static Boolean ENABLE_JOIN_MESSAGE;
		public static Boolean ENABLE_QUIT_MESSAGE;

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static Boolean ENABLE_JOIN_BROADCASTS;

		private static void init() {
			pathPrefix("Notifications.Join");
			ENABLE_JOIN_MESSAGE = getBoolean("Enable_Join_Message");
			ENABLE_QUIT_MESSAGE = getBoolean("Enable_Quit_Message");

			ENABLE_JOIN_BROADCASTS = getBoolean("Enable_First_Join_Broadcast");
			BROADCAST_WORLDS = getStringList("Worlds");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class ChatSettings {

		public static Boolean ENABLE_MENTIONS;

		public static String MENTION_COLOR;

		public static SimpleSound MENTION_SOUND;

		private static void init() {
			pathPrefix("Chat.Mentions");
			ENABLE_MENTIONS = getBoolean("Enable");
			MENTION_COLOR = getString("Color");
			MENTION_SOUND = getSound("Sound");
		}
	}
}
