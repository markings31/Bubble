package me.markings.bubble.command.bubble;

import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.PushoverUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleSubCommand;

public class PushoverCommand extends SimpleSubCommand {

    protected PushoverCommand() {
        super("pushover|push|po");

        setMinArguments(1);
        setDescription("Send a PushOver notification to the connected account");
        setUsage("<message>");
    }

    @Override
    protected void onCommand() {
        checkBoolean(!Settings.NotificationSettings.APPLICATION_TOKEN.equals("TOKEN"), "You haven't set your Pushover application token in the config!");
        checkBoolean(!Settings.NotificationSettings.USER_KEY.equals("KEY"), "You haven't set your Pushover account key in the config!");
        final String message = joinArgs(0);

        Common.runLaterAsync(() -> {
            try {
                PushoverUtil.pushoverNotification(message, getPlayer());
            } catch (final Exception e) {
                Messenger.error(getPlayer(), "Message could not be sent! Check the console for details.");
                throw new RuntimeException(e);
            }
        });
    }
}
