package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Notification;
import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Settings;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.Tuple;

import java.util.List;

public class DiscordCommand extends SimpleSubCommand {

    protected DiscordCommand() {
        super("discord");

        setUsage("<title|description> [color/HEX] [thumbnail_URL]");
        setMinArguments(2);
        setPermission(Permissions.Command.DISCORD);
    }

    // TODO: Allow HEX colors.
    @Override
    protected void onCommand() {
        this.checkBoolean(!Settings.DiscordSettings.ANNOUNCEMENTSID.equals("000000000000000000"), "Please set a valid Discord channel ID in the settings.yml file.");

        final boolean hasDomain = ChatUtil.isDomain(args[args.length - 1]);

        final int colorIndex = args.length - (hasDomain ? 2 : 1);

        final String content = joinArgs(0, colorIndex);
        final String color = args.length > 1 ? args[colorIndex] : "green";
        final String thumbnailURL = hasDomain ? args[args.length - 1] : null;

        final List<String> validColors = Notification.getValidColors();

        this.checkBoolean(validColors.contains(color) || color.matches(String.valueOf(Common.HEX_COLOR_REGEX)),
                "Please enter a valid color or HEX code. Valid colors: " + Common.join(validColors));

        Notification.send(new Tuple<>(getPlayer(), null), NotificationTypes.DISCORD.getLabel(), content, color, thumbnailURL);
    }
}
