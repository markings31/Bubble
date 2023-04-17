package me.markings.bubble.command.bubble;

import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Broadcast;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.ChatColor;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.ChatPaginator;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ShowCommand extends SimpleSubCommand {

    private int messagesLength = 1;

    protected ShowCommand() {
        super("show");

        setDescription("Display a preview of all broadcast messages.");
        setPermission(Permissions.Command.SHOW);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final ChatPaginator page = new ChatPaginator(maximumLength() + 6, ChatColor.BLUE);

        page.setHeader("&9" + Common.chatLineSmooth() + "&r", ChatUtil.center("&3&l&nInformation&r"), "&f");
        page.setPages(list()).send(getPlayer());
    }

    private int maximumLength() {
        final Collection<String> labels = Broadcast.getAllBroadcastNames();

        for (int i = 0; i < labels.size(); i++) {
            final String currentLabel = (String) labels.toArray()[i];
            final Collection<String> messageList = Broadcast.getBroadcast(currentLabel).getMessage();
            if (messageList.size() > messagesLength)
                messagesLength = messageList.size();
        }

        return messagesLength + 3;
    }

    private List<SimpleComponent> list() {
        final List<SimpleComponent> messages = new ArrayList<>();
        final Collection<String> labels = Broadcast.getAllBroadcastNames();

        for (int i = 0; i < labels.size(); i++) {
            final String currentLabel = (String) labels.toArray()[i];
            final Broadcast broadcast = Broadcast.getBroadcast(currentLabel);
            final List<String> messageList = broadcast.getMessage();

            for (final String s : Arrays.asList(
                    "&7- &bLabel: &f" + currentLabel,
                    "&7- &bPermission: &f" + broadcast.getPermission(),
                    "&7- &bCentered: &f" + (Settings.NotificationSettings.CENTER_ALL || broadcast.getCentered()),
                    "&f")) {
                final SimpleComponent of = SimpleComponent.of(s);
                messages.add(of);
            }

            messages.add(SimpleComponent.of(broadcast.getBroadcastHeader()));
            messageList.forEach(message -> {
                if (MessageUtil.isExecutable(message))
                    MessageUtil.executePlaceholders(message, getPlayer());
                else messages.add(SimpleComponent.of(MessageUtil.format(broadcast, message)));
            });
            messages.add(SimpleComponent.of(broadcast.getBroadcastFooter()));

            messages.addAll(Arrays.asList(
                    SimpleComponent.of("&f"),
                    SimpleComponent.of(ChatUtil.center("&7&oClick to Edit Message"))
                            .onClickRunCmd("/bubble edit " + currentLabel)));
        }

        return messages;
    }
}
