package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.bungee.BubbleAction;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompChatColor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

	@Getter
	static final String gradientPlaceholder = "<gradient:";

	@Getter
	static final String gradientEndPlaceholder = "</gradient>";

	@Getter
	private static final String commandPlaceholder = "<command>";

	@Getter
	private static final String messageArg = "message";

	@Getter
	private static final String titleArg = "title";

	@Getter
	private static final String bossbarArg = "bossbar";

	@Getter
	private static final String actionbarArg = "actionbar";

	@Getter
	private static final String toastArg = "toast";

	private static final String bungeeMessagePlaceholder = "<bungee_message>";
	private static final String bungeeTitlePlaceholder = "<bungee_title>";
	private static final String bungeeActionBarPlaceholder = "<bungee_actionbar>";
	private static final String bungeeBossBarPlaceholder = "<bungee_bossbar>";

	public static String format(final String message) {
		val chatLinePlaceholder = "<chat_line>";
		val smoothLinePlaceholder = "<smooth_line>";
		val fancyLinePlaceholder = "<fancy_line>";
		val centerPlaceholder = "<center>";
		if (message.contains(chatLinePlaceholder))
			return message.replace(chatLinePlaceholder, Common.chatLine());

		if (message.contains(smoothLinePlaceholder))
			return message.replace(smoothLinePlaceholder, Common.chatLineSmooth());

		if (message.contains(fancyLinePlaceholder))
			return message.replace(fancyLinePlaceholder, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

		if (message.contains(centerPlaceholder))
			return ChatUtil.center(message.replace(centerPlaceholder, ""));

		return message;
	}

	public static String translateGradient(final String message) {
		val firstColor = StringUtils.substringBetween(message, ":", "|");
		val secondColor = StringUtils.substringBetween(message, "|", ">");

		val fullGradientPrefix = gradientPlaceholder + firstColor + "|" + secondColor + ">";

		if (message.contains(gradientPlaceholder) && message.contains(gradientEndPlaceholder))
			return ChatUtil.generateGradient(message.replace(fullGradientPrefix, "")
					.replace(gradientEndPlaceholder, ""), CompChatColor.of(firstColor), CompChatColor.of(secondColor));

		return message;
	}

	public static void executePlaceholders(final String message, final Player player) {
		if (message.startsWith(commandPlaceholder))
			Common.dispatchCommand(player, message.replace(commandPlaceholder, ""));

		if (message.startsWith(bungeeMessagePlaceholder))
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, messageArg, replaceVarsAndGradient(message.replace(bungeeMessagePlaceholder, ""), player));

		if (message.startsWith(bungeeTitlePlaceholder))
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, titleArg, Variables.replace(message.replace(bungeeTitlePlaceholder, ""), player));

		if (message.startsWith(bungeeActionBarPlaceholder))
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, actionbarArg, Variables.replace(message.replace(bungeeActionBarPlaceholder, ""), player));

		if (message.startsWith(bungeeBossBarPlaceholder))
			BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, bossbarArg, Variables.replace(message.replace(bungeeBossBarPlaceholder, ""), player));
	}

	public static boolean isCommand(final String message) {
		return message.startsWith(commandPlaceholder);
	}

	public static boolean isBungeeTitle(final String message) {
		return message.startsWith(bungeeTitlePlaceholder);
	}

	public static boolean containsGradient(final String message) {
		return message.contains(gradientPlaceholder) && message.contains(gradientEndPlaceholder);
	}

	public static String replaceVarsAndGradient(final String message, final Player player) {
		val strippedMessage = containsGradient(message)
				? StringUtils.substringBetween(message, ">", getGradientEndPlaceholder())
				: message;
		val replacedMessage = Variables.replace(format(strippedMessage), player);
		return translateGradient(message.replace(strippedMessage, replacedMessage));
	}
}
