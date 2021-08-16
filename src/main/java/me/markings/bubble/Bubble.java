package me.markings.bubble;

import lombok.Getter;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Collections;
import java.util.List;

public final class Bubble extends SimplePlugin {

	@Getter
	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	@Getter
	private final List<Class<? extends YamlStaticConfig>> settings = Collections.singletonList(Settings.class);

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix(SimpleSettings.PLUGIN_PREFIX);

		Common.log(Common.getTellPrefix() + " Bubble has been successfully enabled.");
	}

	@Override
	protected void onPluginPreReload() {
		Settings.BroadcastSettings.MESSAGE_LIST.clear();
	}

	@Override
	protected void onReloadablesStart() {
		registerEvents(new PlayerJoinListener());

		Messenger.setInfoPrefix(SimpleSettings.PLUGIN_PREFIX);

		new BroadcastTask().runTaskTimerAsynchronously(this, 0,
				Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());
	}

}
