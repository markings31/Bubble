package me.markings.bubble.command;

import me.markings.bubble.model.NotificationTypes;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

import static org.mineacademy.fo.Common.joinRange;

@AutoRegister
public final class AnnounceCommand extends SimpleCommand {

    public AnnounceCommand() {
        super("announce|an|a");

        setMinArguments(2);
        setDescription("Announce the given message to the server.");
        setUsage("<message/title/bossbar/actionbar/toast> [material] <input|...>");
        setPermission(Permissions.ANNOUNCE);
    }

    @Override
    protected void onCommand() {
        final String messageType = args[0];
        final String input = joinRange((messageType.equals(NotificationTypes.TOAST.getLabel()) ? 2 : 1), args);
        if (messageType.equals(NotificationTypes.TOAST.getLabel()))
            Common.dispatchCommandAsPlayer(getPlayer(), "bu notify all " + messageType + " " + args[1] + " " + input);
        else
            Common.dispatchCommandAsPlayer(getPlayer(), "bu notify all " + messageType + " " + input);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(NotificationTypes.CHAT.getLabel(), NotificationTypes.TITLE.getLabel(),
                    NotificationTypes.ACTIONBAR.getLabel(), NotificationTypes.BOSSBAR.getLabel(), NotificationTypes.TOAST.getLabel());
        if (args.length == 2 && args[0].equalsIgnoreCase(NotificationTypes.TOAST.getLabel()))
            return completeLastWord(CompMaterial.values());

        return new ArrayList<>();
    }
}
