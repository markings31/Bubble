package me.markings.bubble;

import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;

public final class Bubble extends SimplePlugin {

	private final SimpleCommandGroup commandGroup = new BubbleGroup();

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix("&8[&b&lBubble&8]&7 ");

		Common.log("[" + SimplePlugin.getNamed() + "] Bubble has been successfully enabled.");
	}

	@Override
	protected void onPluginPreReload() {
		Settings.BroadcastSettings.MESSAGE_LIST.clear();
	}

	@Override
	protected void onReloadablesStart() {
		registerEvents(new PlayerJoinListener());

		Common.setTellPrefix(SimpleSettings.PLUGIN_PREFIX);

		new BroadcastTask().runTaskTimerAsynchronously(this, 0, Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return commandGroup;
	}
}
