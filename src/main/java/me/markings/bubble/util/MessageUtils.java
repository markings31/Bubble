package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.Common;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

	public static String format(final String message) {
		final String chatLinePlaceholder = "<chat_line>";
		final String smoothLinePlaceholder = "<smooth_line>";
		if (message.contains(chatLinePlaceholder))
			return message.replace(chatLinePlaceholder, Common.chatLine());
		else if (message.contains(smoothLinePlaceholder))
			return message.replace(smoothLinePlaceholder, Common.chatLineSmooth());

		return message;
	}

	public static void executePlaceholders(final String message) {
		final String commandPlaceholder = "<command>";
		if (message.startsWith(commandPlaceholder))
			Common.dispatchCommand(null, message.replace(commandPlaceholder, ""));
	}
}
