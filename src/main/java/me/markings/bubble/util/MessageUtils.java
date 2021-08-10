package me.markings.bubble.util;

import org.mineacademy.fo.Common;

public class MessageUtils {

	private static final String titlePrefix = "[title]";
	private static final String bossbarPrefix = "[bossbar]";
	private static final String actionbarPrefix = "[actionbar]";
	private static final String toastPrefix = "[toast]";
	private static final String commandPrefix = "[command]";

	public static void checkForPlaceholders(final String message) {
		if (message.startsWith(titlePrefix))
			Common.dispatchCommand(null, "bu notify all title "
					+ message.replace(titlePrefix, ""));

		else if (message.startsWith(bossbarPrefix))
			Common.dispatchCommand(null, "bu notify all bossbar "
					+ message.replace(bossbarPrefix, ""));

		else if (message.startsWith(actionbarPrefix))
			Common.dispatchCommand(null, "bu notify all actionbar "
					+ message.replace(actionbarPrefix, ""));

		else if (message.startsWith(toastPrefix))
			Common.dispatchCommand(null, "bu notify all toast "
					+ message.replace(toastPrefix, ""));

		else if (message.startsWith(commandPrefix))
			Common.dispatchCommand(null, message.replace(commandPrefix, ""));
	}
}
