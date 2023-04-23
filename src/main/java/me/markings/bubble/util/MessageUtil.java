package me.markings.bubble.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.EffectPlaceholders;
import me.markings.bubble.model.Notification;
import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.settings.Broadcast;
import me.markings.bubble.settings.Settings;
import org.bukkit.entity.Player;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.RandomUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.model.Tuple;
import org.mineacademy.fo.remain.CompChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

    //private static final String scrollingGradientPlaceholder = "{g:";

    private static final String gradientEndPlaceholder = "</>";

    private static final String gradientPattern = "^([A-Fa-f\\d]{6}|[A-Fa-f\\d]{3}):([A-Fa-f\\d]{6}|[A-Fa-f\\d]{3})$";

    public static String format(final Broadcast broadcast, String message) {
        message = format(message);
        if (broadcast.getCentered() || Settings.NotificationSettings.CENTER_ALL)
            return ChatUtil.center(message);

        return message;
    }

    public static String format(final String message) {
        final String fancyLinePlaceholder = "{fancy_line}";
        if (message.contains(fancyLinePlaceholder))
            return message.replace(fancyLinePlaceholder, "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");


        return message;
    }

    public static List<String> getTitleFrames(final String message) {
        final char[] msgArray = message.toCharArray();
        final ArrayList<String> frames;
        final ArrayList<Integer> indices;
        final boolean hasPeriod = getPeriod(message) != -1;
        indices = IntStream
                .range(0, msgArray.length).filter(i -> msgArray[i] == ':' && msgArray[i - 1] != 't')
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));

        frames = IntStream
                .range(0, (hasPeriod ? indices.size() - 1 : indices.size())).
                mapToObj(i -> message.substring(indices.get(i) + 1, i == indices.size() - 1 ? message.indexOf('}') : indices.get(i + 1)))
                .collect(Collectors.toCollection(ArrayList::new));

        return frames;
    }

    /*public static List<String> getGradientFrames(final String message, final CompChatColor firstColor, final CompChatColor lastColor) {
        final char[] msgArray = ChatUtil.generateGradient(message, firstColor, lastColor).toCharArray();
        final ArrayList<String> frames = new ArrayList<>();
        for (int i = 0; i < msgArray.length; i++)
            frames.add(String.valueOf(msgArray[i]));

        return frames;
    }*/

    /*// TODO: Move string permission to 'Permissions' class.
    public static void blockAndNotify(final Player player, final String warningMessage, final String blockedMessage, final boolean notifyStaff) {
        Common.tellNoPrefix(player, warningMessage);
        if (notifyStaff) {
            Remain.getOnlinePlayers().forEach(onlinePlayer -> {
                if (onlinePlayer.hasPermission(Permissions.Chat.NOTIFY)) {
                    Common.tell(onlinePlayer, "&7Player &d" + player.getName() + " &7attempted to bypass the chat filter. &8[&c'" + blockedMessage + "'&8]");
                }
            });
        }
    }*/

    public static int getPeriod(final String message) {
        final char[] msgArray = message.toCharArray();
        for (int i = message.length() - 1; i > 0; i--)
            if (msgArray[i] == ':') {
                final String period = message.substring(i + 1, message.indexOf('}'));
                return Valid.isInteger(period) ? Integer.parseInt(period) : -1;
            }

        return 10;
    }

    public static List<CompChatColor> getColors(final String message) {
        final ArrayList<CompChatColor> colors = new ArrayList<>();
        final int colonIndex = message.indexOf(':');

        colors.add(CompChatColor.of(message.substring(colonIndex - 7, colonIndex - 1)));
        colors.add(CompChatColor.of(message.substring(colonIndex + 1, colonIndex + 7)));

        return colors;
    }

    public static String getLastMessage(final String message) {
        final char[] msgArray = message.toCharArray();
        final List<Integer> indices = new ArrayList<>();
        for (int i = message.length() - 1; i > 0; i--)
            if (msgArray[i] == ':')
                indices.add(i);

        return message.substring(indices.get(1) + 1, indices.get(0));
    }

    public static String translateGradient(final String message) {
        final String newMessage = stripPlaceholders(Common.revertColorizing(message));

        if (containsGradient(newMessage)) {
            final String firstColor = "#" + newMessage.substring(newMessage.indexOf(":") - 6, newMessage.indexOf(":"));
            final String secondColor = "#" + newMessage.substring(newMessage.indexOf(":") + 1, newMessage.indexOf(":") + 7);
            final String previousMessage = newMessage.substring(0, newMessage.indexOf(":") - 7);
            final String translatedMessage = newMessage.substring(newMessage.indexOf(":") + 7,
                    newMessage.contains(gradientEndPlaceholder) ? newMessage.indexOf(gradientEndPlaceholder) : newMessage.length()).replace(">", "");
            final String followingMessage = Common.lastColor(message)
                    + newMessage.substring(newMessage.contains(gradientEndPlaceholder)
                    ? newMessage.indexOf(gradientEndPlaceholder) + 3
                    : newMessage.length());

            return getPlaceholder(message) + previousMessage + ChatUtil.generateGradient(translatedMessage,
                    CompChatColor.of(firstColor), CompChatColor.of(secondColor)) + "&r" + followingMessage;
        }

        return message;
    }

    public static void executePlaceholders(final String message, final Player player) {
        if (!isExecutable(message))
            return;

        List<CompChatColor> colors = new ArrayList<>();
        int period = 0;
        CompChatColor[] colorArr = new CompChatColor[3];

        if (message.startsWith(EffectPlaceholders.ANIMATE.getPrefix()) || message.startsWith(EffectPlaceholders.SCROLL.getPrefix()) || message.startsWith(EffectPlaceholders.FLASH.getPrefix())) {
            colors = getColors(message);
            period = getPeriod(message) != -1 ? getPeriod(message) : 10;
            colorArr = colors.toArray(colorArr);
        }

        if (message.startsWith(EffectPlaceholders.ANIMATE.getPrefix()))
            AnimationUtil.animateTitle(player, getTitleFrames(message), null, period);

        if (message.startsWith(EffectPlaceholders.SCROLL.getPrefix()))
            AnimationUtil.animateTitle(player, AnimationUtil.leftToRightFull(getLastMessage(message), colors.get(0), colors.get(1), colors.get(2)), null, period);

        if (message.startsWith(EffectPlaceholders.FLASH.getPrefix()))
            AnimationUtil.animateTitle(player, AnimationUtil.flicker(getLastMessage(message), period, 2, colorArr), null, period);

        /*if (message.startsWith(scrollingGradientPlaceholder))
            AnimationUtil.animateTitle(player, getGradientFrames(getLastMessage(message),
                            CompChatColor.of(String.valueOf(colors.get(0)).replace("§", "&")),
                            CompChatColor.of(String.valueOf(colors.get(1)).replace("§", "&"))),
                    null, period);*/

        if (message.startsWith(EffectPlaceholders.COMMAND.getPrefix()))
            Common.dispatchCommand(player, message.replace(EffectPlaceholders.COMMAND.getPrefix(), ""));

        if (message.startsWith(EffectPlaceholders.TITLE.getPrefix()))
            Notification.send(new Tuple<>(Bubble.getInstance().getConsole(), player), NotificationTypes.TITLE.getLabel(), message.replace(EffectPlaceholders.TITLE.getPrefix(), ""));

        if (message.startsWith(EffectPlaceholders.ACTIONBAR.getPrefix()))
            Notification.send(new Tuple<>(Bubble.getInstance().getConsole(), player), NotificationTypes.ACTIONBAR.getLabel(), message.replace(EffectPlaceholders.ACTIONBAR.getPrefix(), ""));

        if (message.startsWith(EffectPlaceholders.BOSSBAR.getPrefix()))
            Notification.send(new Tuple<>(Bubble.getInstance().getConsole(), player), NotificationTypes.BOSSBAR.getLabel(), message.replace(EffectPlaceholders.BOSSBAR.getPrefix(), ""));

        if (message.startsWith(EffectPlaceholders.TOAST.getPrefix()))
            Notification.send(new Tuple<>(Bubble.getInstance().getConsole(), player), NotificationTypes.TOAST.getLabel(), message.replace(EffectPlaceholders.TOAST.getPrefix(), ""));
    }

    public static boolean isExecutable(final String message) {
        return message.startsWith(EffectPlaceholders.TITLE.getPrefix())
                || message.startsWith(EffectPlaceholders.ACTIONBAR.getPrefix())
                || message.startsWith(EffectPlaceholders.BOSSBAR.getPrefix())
                || message.startsWith(EffectPlaceholders.TOAST.getPrefix())
                || message.startsWith(EffectPlaceholders.COMMAND.getPrefix())
                || message.startsWith(EffectPlaceholders.ANIMATE.getPrefix())
                || message.startsWith(EffectPlaceholders.SCROLL.getPrefix())
                || message.startsWith(EffectPlaceholders.FLASH.getPrefix());
    }

    public static String getPlaceholder(final String message) {
        if (message.contains(EffectPlaceholders.TITLE.getPrefix()))
            return EffectPlaceholders.TITLE.getPrefix();
        if (message.contains(EffectPlaceholders.ACTIONBAR.getPrefix()))
            return EffectPlaceholders.ACTIONBAR.getPrefix();
        if (message.contains(EffectPlaceholders.BOSSBAR.getPrefix()))
            return EffectPlaceholders.BOSSBAR.getPrefix();
        if (message.contains(EffectPlaceholders.TOAST.getPrefix()))
            return EffectPlaceholders.TOAST.getPrefix();

        return "";
    }

    public static String stripPlaceholders(final String message) {
        if (message.contains(EffectPlaceholders.TITLE.getPrefix()))
            return message.replace(EffectPlaceholders.TITLE.getPrefix(), "");
        if (message.contains(EffectPlaceholders.ACTIONBAR.getPrefix()))
            return message.replace(EffectPlaceholders.ACTIONBAR.getPrefix(), "");
        if (message.contains(EffectPlaceholders.BOSSBAR.getPrefix()))
            return message.replace(EffectPlaceholders.BOSSBAR.getPrefix(), "");
        if (message.contains(EffectPlaceholders.TOAST.getPrefix()))
            return message.replace(EffectPlaceholders.TOAST.getPrefix(), "");

        return message;
    }

    public static boolean containsGradient(final String message) {
        final String gradientPrefix;
        if (message.contains(":") && message.contains(gradientEndPlaceholder)) {
            gradientPrefix = message.substring(message.indexOf(":") - 6, message.indexOf(":") + 7);

            return gradientPrefix.matches(gradientPattern);
        }

        return false;
    }

    public static CompChatColor getRandomColor() {
        return RandomUtil.nextItem(Arrays.stream(CompChatColor.values()).filter((color) ->
                !color.equals(CompChatColor.UNDERLINE)
                        && !color.equals(CompChatColor.BOLD)
                        && !color.equals(CompChatColor.ITALIC)
                        && !color.equals(CompChatColor.MAGIC)
                        && !color.equals(CompChatColor.STRIKETHROUGH)
                        && !color.equals(CompChatColor.BLACK)).collect(Collectors.toList()));
    }
}
