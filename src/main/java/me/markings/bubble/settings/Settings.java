package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.Bubble;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

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
	public final static class BroadcastSettings {

		public static final Map<List<String>, String> MESSAGE_MAP = new HashMap<>();

		public static final Map<String, String> PERMISSION = new HashMap<>();

		public static final Map<String, Boolean> CENTERED = new HashMap<>();

		public static Map<List<String>, String> BROADCAST_WORLDS = new HashMap<>();

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
		public static Boolean DISCORDSRV;
		public static Boolean BSTATS;

		private static void init() {
			setPathPrefix("Hooks");
			VAULT = getBoolean("Vault");
			PAPI = getBoolean("PlaceholderAPI");
			DISCORDSRV = getBoolean("DiscordSRV");
			BSTATS = getBoolean("BStats");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public final static class DiscordSettings {

		public static Boolean DISCORDMINECRAFT;
		public static String CHATFORMAT;

		public static Boolean MINECRAFTDISCORD;
		public static Boolean USEWEBHOOK;
		public static Boolean SYNCANNOUNCEMENTS;

		public static String ANNOUNCEMENTSID;
		public static String MINECRAFTID;
		public static String ANNOUNCEMENTSCOLOR;

		public static String AUTHOR;
		public static String THUMBNAIL;
		public static String DEFAULT_IMAGE;

		private static void init() {
			setPathPrefix("Discord.Discord_To_Minecraft");
			DISCORDMINECRAFT = getBoolean("Enable");
			CHATFORMAT = getString("Chat_Format");

			setPathPrefix("Discord.Minecraft_To_Discord");
			MINECRAFTDISCORD = getBoolean("Enable");
			USEWEBHOOK = getBoolean("Use_Webhook");
			SYNCANNOUNCEMENTS = getBoolean("Sync_Announcements");
			ANNOUNCEMENTSCOLOR = getString("Announcements_Color");

			setPathPrefix("Discord");
			ANNOUNCEMENTSID = getString("Announcements_Channel_ID");
			MINECRAFTID = getString("Minecraft_Channel_ID");
			AUTHOR = getString("Author");
			THUMBNAIL = getString("Thumbnail");
			DEFAULT_IMAGE = getString("Default_Image");
		}

	}

	public static void generateBroadcastSections() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		Objects.requireNonNull(config.getConfigurationSection(messagePath)).getKeys(false).forEach(path -> {
			val permissionPath = messagePath + "." + path + ".Permission";
			val centerPath = messagePath + "." + path + ".Centered";
			val worldsPath = messagePath + "." + path + ".Worlds";

			if (!config.contains(permissionPath))
				config.set(permissionPath, "none");

			if (!config.contains(centerPath))
				config.set(centerPath, false);

			if (!config.contains(messagePath + "." + path + ".Message"))
				config.createSection(messagePath + "." + path + ".Message");

			if (!config.contains(worldsPath))
				config.createSection(worldsPath);

			val stringList = config.getStringList(messagePath + "." + path + ".Message");
			val worldList = config.getStringList(worldsPath);

			BroadcastSettings.PERMISSION.put(path, config.getString(permissionPath));
			BroadcastSettings.CENTERED.put(centerPath, config.getBoolean(centerPath));
			BroadcastSettings.MESSAGE_MAP.put(stringList, path);
			BroadcastSettings.BROADCAST_WORLDS.put(worldList, worldsPath);
		});
	}

	@Override
	protected void beforeLoad() {
		generateBroadcastSections();
		//CommentLoader.getSettingsInstance().apply(Bubble.settingsFile);
	}
}

