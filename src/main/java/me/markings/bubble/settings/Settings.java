package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public final class Settings extends SimpleSettings {

	private static final String messagePath = "Notifications.Broadcast.Messages";

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	/*@Override
	protected boolean saveComments() {
		return true;
	}

	@Override
	protected List<String> getUncommentedSections() {
		return Arrays.asList(messagePath + ".*", "Welcome.Join_MOTD", "Welcome.Join_Broadcasts");
	}*/

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BroadcastSettings {

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean RANDOM_MESSAGE;

		public static List<List<String>> MESSAGE_LIST = new ArrayList<>();

		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static SimpleTime BROADCAST_DELAY;

		public static String BROADCAST_SOUND;
		public static String HEADER;
		public static String FOOTER;

		private static void init() {
			for (final String path : Objects.requireNonNull(getConfig().
					getConfigurationSection(messagePath)).getKeys(false))
				MESSAGE_LIST.add(getConfig().getStringList(messagePath + "." + path));

			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
			BROADCAST_WORLDS = getStringList("Worlds");
			BROADCAST_SOUND = getString("Sound");
			HEADER = getString("Header");
			FOOTER = getString("Footer");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeSettings {

		public static List<String> JOIN_MOTD;
		public static List<String> JOIN_BROADCAST;
		public static List<String> BROADCAST_WORLDS = new ArrayList<>();

		public static Boolean ENABLE_JOIN_MOTD;
		public static Boolean ENABLE_JOIN_BROADCASTS;

		public static SimpleTime MOTD_DELAY;

		private static void init() {
			pathPrefix("Notifications.Welcome");
			ENABLE_JOIN_MOTD = getBoolean("Enable_MOTD");
			MOTD_DELAY = getTime("MOTD_Delay");
			JOIN_MOTD = getStringList("Join_MOTD");
			ENABLE_JOIN_BROADCASTS = getBoolean("Enable_First_Join_Broadcast");
			BROADCAST_WORLDS = getStringList("Worlds");
			JOIN_BROADCAST = getStringList("Join_Broadcasts");
		}
	}
}
