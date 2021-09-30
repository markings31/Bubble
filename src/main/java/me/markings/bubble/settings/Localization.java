package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.mineacademy.fo.settings.SimpleLocalization;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
public class Localization extends SimpleLocalization {

	private static final String messagePath = "Broadcast.Messages";

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@Override
	protected List<String> getUncommentedSections() {
		return Arrays.asList("Broadcast.Messages", "Welcome");
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BroadcastMessages {

		public static final Map<List<String>, String> MESSAGE_MAP = new HashMap<>();

		public static final Map<String, String> PERMISSION = new HashMap<>();

		public static String HEADER;
		public static String FOOTER;

		private static void init() {
			pathPrefix(null);
			Objects.requireNonNull(getConfig().getConfigurationSection(messagePath)).getKeys(false).forEach(path -> {
				val permissionPath = messagePath + "." + path + ".Permission";

				if (!isSet(permissionPath))
					getConfig().set(permissionPath, "bubble.vip");

				if (!isSet(messagePath + "." + path + ".Message"))
					getConfig().createSection(messagePath + "." + path + ".Message");

				val stringList = getStringList(messagePath + "." + path + ".Message");

				PERMISSION.put(path, getString(permissionPath));
				MESSAGE_MAP.put(stringList, path);
				try {
					getConfig().save(new File("plugins/Bubble/localization/messages_" + SimpleSettings.LOCALE_PREFIX + ".yml"));
				} catch (final IOException e) {
					e.printStackTrace();
				}
			});

			pathPrefix("Broadcast");
			HEADER = getString("Header");
			FOOTER = getString("Footer");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeMessages {
		public static List<String> JOIN_BROADCAST;

		private static void init() {
			pathPrefix("Welcome");
			JOIN_BROADCAST = getStringList("Join_Broadcasts");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class JoinQuitMessages {

		public static String JOIN_MESSAGE;
		public static String QUIT_MESSAGE;

		private static void init() {
			pathPrefix("Join");
			JOIN_MESSAGE = getString("Join_Message");
			QUIT_MESSAGE = getString("Quit_Message");
		}
	}
}
