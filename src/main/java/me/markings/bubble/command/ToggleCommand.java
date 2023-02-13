package me.markings.bubble.command;

import me.markings.bubble.PlayerData;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;

@AutoRegister
public final class ToggleCommand extends SimpleCommand {

    public ToggleCommand() {
        super("togglebroadcasts|tb");

        setDescription("Toggle your ability to receive broadcast messages.");
        setPermission(Permissions.TOGGLE);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final PlayerData cache = PlayerData.from(getPlayer());

        cache.setBroadcastStatus(!cache.isBroadcastStatus());
        Messenger.success(getPlayer(), "&7Broadcast visibility is now " + (cache.isBroadcastStatus() ? "&aenabled&7." : "&cdisabled&7."));
    }
}
