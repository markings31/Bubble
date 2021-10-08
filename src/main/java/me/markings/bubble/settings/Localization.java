package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.settings.SimpleLocalization;

import java.util.List;

@SuppressWarnings("unused")
public class Localization extends SimpleLocalization {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@Override
	protected List<String> getUncommentedSections() {
		return List.of("Welcome");
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
