package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class Localization extends SimpleLocalization {

	private static final String messagePath = "Broadcast.Messages";
	private static final String motdPath = "Welcome.Join_MOTD";

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

		public static final List<List<String>> MESSAGE_LIST = new ArrayList<>();

		public static String HEADER;
		public static String FOOTER;

		private static void init() {
			Objects.requireNonNull(getConfig().getConfigurationSection(messagePath)).getKeys(false)
					.stream().map(path -> getConfig().getStringList(messagePath + "." + path)).forEach(MESSAGE_LIST::add);

			pathPrefix("Broadcast");
			HEADER = getString("Header");
			FOOTER = getString("Footer");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeMessages {

		public static List<List<String>> JOIN_MOTD = new ArrayList<>();

		public static List<String> JOIN_BROADCAST;

		private static void init() {
			Objects.requireNonNull(getConfig().getConfigurationSection(motdPath)).getKeys(false)
					.forEach(path -> JOIN_MOTD.add(getConfig().getStringList(motdPath + "." + path)));

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
