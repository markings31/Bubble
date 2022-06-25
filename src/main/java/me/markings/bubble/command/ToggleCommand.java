package me.markings.bubble.command;

import lombok.val;
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
		setPermission(Permissions.Command.TOGGLE);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		val cache = PlayerData.getCache(getPlayer());

		cache.setBroadcastStatus(!cache.isBroadcastStatus());
		Messenger.success(getPlayer(), "&7Broadcasts have now been toggled " + (cache.isBroadcastStatus() ? "&aON&7." : "&cOFF&7."));
	}
}
