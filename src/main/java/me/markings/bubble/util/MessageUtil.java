package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.remain.CompChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private static final String animatePlaceholder = "{animate:";
	private static final String scrollPlaceholder = "{scroll:";
	private static final String flashPlaceholder = "{flash:";
	//private static final String scrollingGradientPlaceholder = "{g:";

	public static String format(final String message) {
		val fancyLinePlaceholder = "%fancy_line%";
		val centerPlaceholder = "%center%";
		if (message.contains(fancyLinePlaceholder))
			return message.replace(fancyLinePlaceholder, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");

		if (message.contains(centerPlaceholder))
			return ChatUtil.center(message.replace(centerPlaceholder, ""));

		return message;
	}

	public static List<String> getTitleFrames(final String message) {
		final char[] msgArray = message.toCharArray();
		final ArrayList<String> frames = new ArrayList<>();
		final ArrayList<Integer> indicies = new ArrayList<>();
		val hasPeriod = getPeriod(message) != -1;
		for (int i = 0; i < msgArray.length; i++)
			if (msgArray[i] == ':' && msgArray[i - 1] != 't')
				indicies.add(i);

		for (int i = 0; i < (hasPeriod ? indicies.size() - 1 : indicies.size()); i++) {
			final String substring = message.substring(indicies.get(i) + 1, i == indicies.size() - 1 ? message.indexOf('}') : indicies.get(i + 1));
			frames.add(substring);
		}

		return frames;
	}

	/*public static List<String> getGradientFrames(final String message, final CompChatColor firstColor, final CompChatColor lastColor) {
		final char[] msgArray = ChatUtil.generateGradient(message, firstColor, lastColor).toCharArray();
		final ArrayList<String> frames = new ArrayList<>();
		for (int i = 0; i < msgArray.length; i++)
			frames.add(String.valueOf(msgArray[i]));

		return frames;
	}*/

	public static int getPeriod(final String message) {
		val msgArray = message.toCharArray();
		for (int i = message.length() - 1; i > 0; i--)
			if (msgArray[i] == ':') {
				val period = message.substring(i + 1, message.indexOf('}'));
				return NumberUtils.isNumber(period) ? Integer.parseInt(period) : -1;
			}

		return 10;
	}

	public static List<CompChatColor> getScrollColors(final String message) {
		val msgArray = message.toCharArray();
		final ArrayList<Integer> indicies = new ArrayList<>();
		final ArrayList<CompChatColor> colors = new ArrayList<>();
		for (int i = 0; i < msgArray.length; i++)
			if (msgArray[i] == ':')
				indicies.add(i);

		colors.add(CompChatColor.of(message.substring(indicies.get(0) + 1, indicies.get(1))));
		colors.add(CompChatColor.of(message.substring(indicies.get(1) + 1, indicies.get(2))));
		colors.add(CompChatColor.of(message.substring(indicies.get(2) + 1, indicies.get(3))));

		return colors;
	}

	public static String getLastMessage(final String message) {
		val msgArray = message.toCharArray();
		final ArrayList<Integer> indicies = new ArrayList<>();
		for (int i = message.length() - 1; i > 0; i--)
			if (msgArray[i] == ':')
				indicies.add(i);

		return message.substring(indicies.get(1) + 1, indicies.get(0));
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

	public static Color getColor(final String color) {
		try {
			val field = Class.forName("java.awt.Color").getField(color);
			return (Color) field.get(null);
		} catch (final Exception e) {
			return null;
		}
	}

	public static void executePlaceholders(final String message, final Player player) {
		val colors = getScrollColors(message);
		val period = getPeriod(message) != -1 ? getPeriod(message) : 10;
		val colorArr = colors.toArray(CompChatColor[]::new);

		Common.log(Arrays.toString(colorArr));

		if (message.startsWith(animatePlaceholder))
			AnimationUtil.animateTitle(player, getTitleFrames(message), null, period);

		if (message.startsWith(scrollPlaceholder))
			AnimationUtil.animateTitle(player, AnimationUtil.leftToRightFull(getLastMessage(message), colors.get(0), colors.get(1), colors.get(2)), null, period);

		if (message.startsWith(flashPlaceholder))
			AnimationUtil.animateTitle(player, AnimationUtil.flicker(getLastMessage(message), period, 2, colorArr), null, period);

		/*if (message.startsWith(scrollingGradientPlaceholder))
			AnimationUtil.animateTitle(player, getGradientFrames(getLastMessage(message),
							CompChatColor.of(String.valueOf(colors.get(0)).replace("§", "&")),
							CompChatColor.of(String.valueOf(colors.get(1)).replace("§", "&"))),
					null, period);*/

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
		return message.startsWith(titlePlaceholder)
				|| message.startsWith(actionbarPlaceholder)
				|| message.startsWith(bossbarPlaceholder)
				|| message.startsWith(toastPlaceholder)
				|| message.startsWith(commandPlaceholder)
				|| message.startsWith(animatePlaceholder)
				|| message.startsWith(scrollPlaceholder)
				|| message.startsWith(flashPlaceholder);
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
