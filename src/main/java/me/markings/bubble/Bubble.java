package me.markings.bubble;

import lombok.Getter;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.bungee.BubbleBungeeListener;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.bungee.SimpleBungee;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Arrays;
import java.util.List;

public final class Bubble extends SimplePlugin {

	@Getter
	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	@Getter
	private final List<Class<? extends YamlStaticConfig>> settings = Arrays.asList(Settings.class, Localization.class);

	@Getter
	private final SimpleBungee bungeeCord = new SimpleBungee("plugin:bubble", BubbleBungeeListener.class, BubbleAction.class);

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix(SimpleSettings.PLUGIN_PREFIX);

		registerCommand(new AnnounceCommand());

		Common.log(Common.getTellPrefix() + " Bubble has been successfully enabled.");
	}

	@Override
	protected void onPluginPreReload() {
		Localization.BroadcastMessages.MESSAGE_LIST.clear();
		Localization.WelcomeMessages.JOIN_MOTD.clear();
	}

	@Override
	protected void onReloadablesStart() {
		registerEventsIf(new PlayerJoinListener(), Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE)
				|| Settings.JoinSettings.ENABLE_JOIN_BROADCASTS.equals(Boolean.TRUE));

		registerEventsIf(new PlayerChatListener(), Settings.ChatSettings.ENABLE_MENTIONS);

		Messenger.setInfoPrefix(SimpleSettings.PLUGIN_PREFIX);

		new BroadcastTask().runTaskTimerAsynchronously(this, 0,
				Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());
	}

}
