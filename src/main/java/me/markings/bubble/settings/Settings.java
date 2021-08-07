package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Settings extends SimpleSettings {

	@Override
	protected int getConfigVersion() {
		return 1;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class BroadcastSettings {

		public static List<String> BROADCAST_MESSAGES = new ArrayList<>();

		public static Boolean ENABLE_BROADCASTS;
		public static Boolean RANDOM_MESSAGE;

		public static SimpleTime BROADCAST_DELAY;

		private static void init() {
			for (final String path : Objects.requireNonNull(getConfig().getConfigurationSection("Notifications.Broadcast.Messages")).getKeys(false))
				BROADCAST_MESSAGES.addAll(Objects.requireNonNull(getConfig().getStringList("Notifications.Broadcast.Messages." + path)));

			pathPrefix("Notifications.Broadcast");
			ENABLE_BROADCASTS = getBoolean("Enable");
			BROADCAST_DELAY = getTime("Delay");
			RANDOM_MESSAGE = getBoolean("Random_Message");
		}
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class WelcomeSettings {

		public static List<String> JOIN_MOTD;
		public static List<String> JOIN_BROADCAST;

		public static Boolean ENABLE_JOIN_MOTD;
		public static Boolean ENABLE_JOIN_BROADCASTS;

		public static SimpleTime MOTD_DELAY;

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
