package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.remain.CompChatColor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

	private static final String commandPlaceholder = "<command>";

	public static String format(final String message) {
		val chatLinePlaceholder = "<chat_line>";
		val smoothLinePlaceholder = "<smooth_line>";
		if (message.contains(chatLinePlaceholder))
			return message.replace(chatLinePlaceholder, Common.chatLine());

		if (message.contains(smoothLinePlaceholder))
			return message.replace(smoothLinePlaceholder, Common.chatLineSmooth());

		return message;
	}

	public static String translateGradient(final String message) {
		val gradientPlaceholder = "<gradient:";

		val firstColor = StringUtils.substringBetween(message, ":", "|");
		val secondColor = StringUtils.substringBetween(message, "|", ">");

		val fullGradientPrefix = gradientPlaceholder + firstColor + "|" + secondColor + ">";

		val gradientEndPlaceholder = "</gradient>";

		if (message.contains(gradientPlaceholder) && message.contains(gradientEndPlaceholder))
			return ChatUtil.generateGradient(message.replace(fullGradientPrefix, "")
					.replace(gradientEndPlaceholder, ""), CompChatColor.of(firstColor), CompChatColor.of(secondColor));

		return message;
	}

	public static void executePlaceholders(final String message, final Player player) {
		if (message.startsWith(commandPlaceholder))
			Common.dispatchCommand(player, message.replace(commandPlaceholder, ""));
	}

	public static boolean isCommand(final String message) {
		return message.startsWith(commandPlaceholder);
	}
}
