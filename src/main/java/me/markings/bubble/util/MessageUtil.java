package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
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

	private static final String titlePlaceholder = "<title>";
	private static final String actionbarPlaceholder = "<actionbar>";
	private static final String bossbarPlaceholder = "<bossbar>";
	private static final String toastPlaceholder = "<toast>";

	/*private static final String delayPlaceholder = "<delay:";
	private static final String delayEndPlaceholder = "</delay>";*/

	public static String format(final String message) {
		val fancyLinePlaceholder = "%fancy_line%";
		val centerPlaceholder = "%center%";
		if (message.contains(fancyLinePlaceholder))
			return message.replace(fancyLinePlaceholder, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

		if (message.contains(centerPlaceholder))
			return ChatUtil.center(message.replace(centerPlaceholder, ""));

		return message;
	}

	public static String translateGradient(final String message) {
		val newMessage = stripPlaceholders(message.replace("§", "&"));
		val firstColor = StringUtils.substringBetween(newMessage, ":", "|");
		val secondColor = StringUtils.substringBetween(newMessage, "|", ">");

		val fullGradientPrefix = gradientPlaceholder + firstColor + "|" + secondColor + ">";

		if (newMessage.contains(gradientPlaceholder) && newMessage.contains(gradientEndPlaceholder))
			return getPlaceholder(message) + ChatUtil.generateGradient(newMessage.replace(fullGradientPrefix, "")
					.replace(gradientEndPlaceholder, ""), CompChatColor.of(firstColor), CompChatColor.of(secondColor));

		return message;
	}

	/*public static void executeWithDelay(final String message) {
		val delayTime = SimpleTime.from(StringUtils.substringBetween(message, ":", "|"));

	}*/

	public static void executePlaceholders(final String message, final Player player) {
		if (message.startsWith(commandPlaceholder))
			Common.dispatchCommand(player, message.replace(commandPlaceholder, ""));

		if (message.startsWith(titlePlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " title " + message.replace(titlePlaceholder, ""));

		if (message.startsWith(actionbarPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " actionbar " + message.replace(actionbarPlaceholder, ""));

		if (message.startsWith(bossbarPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " bossbar " + message.replace(bossbarPlaceholder, ""));

		if (message.startsWith(toastPlaceholder))
			Common.dispatchCommand(player, "bu notify " + player.getName() + " toast " + message.replace(toastPlaceholder, ""));
	}

	public static boolean isExecutable(final String message) {
		return message.contains(titlePlaceholder)
				|| message.contains(actionbarPlaceholder)
				|| message.contains(bossbarPlaceholder)
				|| message.contains(toastPlaceholder)
				|| message.contains(commandPlaceholder);
	}

	public static String getPlaceholder(final String message) {
		if (message.contains(titlePlaceholder))
			return titlePlaceholder;
		if (message.contains(actionbarPlaceholder))
			return actionbarPlaceholder;
		if (message.contains(bossbarPlaceholder))
			return bossbarPlaceholder;
		if (message.contains(toastPlaceholder))
			return toastPlaceholder;

		return "";
	}

	public static String stripPlaceholders(final String message) {
		if (message.contains(titlePlaceholder))
			return message.replace(titlePlaceholder, "");
		if (message.contains(actionbarPlaceholder))
			return message.replace(actionbarPlaceholder, "");
		if (message.contains(bossbarPlaceholder))
			return message.replace(bossbarPlaceholder, "");
		if (message.contains(toastPlaceholder))
			return message.replace(toastPlaceholder, "");

		return message;
	}

	public static boolean containsGradient(final String message) {
		return message.contains(gradientPlaceholder) && message.contains(gradientEndPlaceholder);
	}

	public static String replaceVarsAndGradient(final String message, final Player player) {
		val strippedMessage = containsGradient(message)
				? StringUtils.substringBetween(message, ">", getGradientEndPlaceholder())
				: message;
		val replacedMessage = HookManager.replacePlaceholders(player, Variables.replace(format(strippedMessage), player));
		return translateGradient(message.replace(strippedMessage, replacedMessage));
	}
}
