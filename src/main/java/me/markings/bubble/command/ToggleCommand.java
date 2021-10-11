package me.markings.bubble.command;

import lombok.val;
import me.markings.bubble.PlayerCache;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommand;

public class ToggleCommand extends SimpleCommand {

	public ToggleCommand() {
		super("togglebroadcasts|tb");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		val cache = PlayerCache.getCache(getPlayer());

		cache.setBroadcastStatus(!cache.isBroadcastStatus());
		Messenger.success(getPlayer(), "&7Broadcasts have now been toggled " + (cache.isBroadcastStatus() ? "&aON&7." : "&cOFF&7."));
	}
}
