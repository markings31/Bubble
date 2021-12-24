package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	private static final String messagePath = "Notifications.Broadcast.Messages";

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@Override
	protected boolean saveComments() {
		return true;
	}

	@Override
	protected List<String> getUncommentedSections() {
		return Collections.singletonList(messagePath);
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BroadcastSettings {

		public static final Map<List<String>, String> MESSAGE_MAP = new HashMap<>();

		public static final Map<String, String> PERMISSION = new HashMap<>();

		public static final Map<String, Boolean> CENTERED = new HashMap<>();

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean BUNGEECORD;
		public static Boolean RANDOM_MESSAGE;
		public static Boolean CENTER_ALL;
		public static Boolean SEND_ASYNC;

		public static String HEADER;
		public static String FOOTER;

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static SimpleTime BROADCAST_DELAY;

		public static SimpleSound BROADCAST_SOUND;

		private static void init() {
			pathPrefix(null);
			generateBroadcastSections();

			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BUNGEECORD = getBoolean("Bungeecord");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
			CENTER_ALL = getBoolean("Center_All");
			SEND_ASYNC = getBoolean("Send_Asynchronously");
			BROADCAST_WORLDS = getStringList("Worlds");
			BROADCAST_SOUND = getSound("Sound");
			HEADER = getString("Header");
			FOOTER = getString("Footer");
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
		public static String MENTION_IGNORE_PERMISSION;

		public static SimpleSound MENTION_SOUND;

		private static void init() {
			pathPrefix("Chat.Mentions");
			ENABLE_MENTIONS = getBoolean("Enable");
			MENTION_IGNORE_PERMISSION = getString("Ignore_Permission");
			MENTION_COLOR = getString("Color");
			MENTION_SOUND = getSound("Sound");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class DatabaseSettings {

		public static Boolean ENABLE_MYSQL;

		private static void init() {
			pathPrefix("Database");
			ENABLE_MYSQL = getBoolean("Enable_MySQL");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class HookSettings {

		public static Boolean VAULT;
		public static Boolean PAPI;
		public static Boolean MVCORE;

		private static void init() {
			pathPrefix("Hooks");
			VAULT = getBoolean("Vault");
			PAPI = getBoolean("PlaceholderAPI");
			MVCORE = getBoolean("MultiverseCore");
		}
	}

	private static void generateBroadcastSections() {
		Objects.requireNonNull(getConfig().getConfigurationSection(messagePath)).getKeys(false).forEach(path -> {
			val permissionPath = messagePath + "." + path + ".Permission";
			val centerPath = messagePath + "." + path + ".Center";

			if (!isSet(permissionPath))
				getConfig().set(permissionPath, "none");

			if (!isSet(centerPath))
				getConfig().set(centerPath, false);

			if (!isSet(messagePath + "." + path + ".Message"))
				getConfig().createSection(messagePath + "." + path + ".Message");

			val stringList = getStringList(messagePath + "." + path + ".Message");

			BroadcastSettings.PERMISSION.put(path, getString(permissionPath));
			BroadcastSettings.CENTERED.put(centerPath, getBoolean(centerPath));
			BroadcastSettings.MESSAGE_MAP.put(stringList, path);
			try {
				getConfig().save(new File("plugins/Bubble/settings.yml"));
			} catch (final IOException e) {
				e.printStackTrace();
			}
		});
	}
}

