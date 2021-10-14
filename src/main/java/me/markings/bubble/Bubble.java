package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.bungee.BubbleBungeeListener;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.PrefsCommand;
import me.markings.bubble.command.ToggleCommand;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.listeners.DatabaseListener;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.bungee.SimpleBungee;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.SimpleSettings;

import java.util.ArrayList;
import java.util.Collection;

public final class Bubble extends SimplePlugin {

	@Getter
	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	@Getter
	private final SimpleBungee bungeeCord = new SimpleBungee("plugin:bubble", BubbleBungeeListener.class, BubbleAction.class);

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix(SimpleSettings.PLUGIN_PREFIX);

		BubbleDatabase.getInstance().connect(
				"db4free.net",
				3306,
				"markings_db",
				"markings",
				"S@sha123",
				"Bubble");

		registerCommand(new AnnounceCommand());
		registerCommand(new ToggleCommand());
		registerCommand(new PrefsCommand());

		registerEvents(new DatabaseListener());

		if (Common.doesPluginExist("Vault"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into Vault!");

		if (Common.doesPluginExist("PlaceholderAPI"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into PlaceholderAPI!");

		if (Common.doesPluginExist("MultiverseCore"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into MultiverseCore");

		Common.log(Common.getTellPrefix() + "Bubble has been successfully enabled!");
	}

	@Override
	protected void onPluginPreReload() {
		Settings.BroadcastSettings.MESSAGE_MAP.clear();
		Settings.WelcomeSettings.JOIN_MOTD.clear();
	}

	@Override
	protected void onReloadablesStart() {
		registerEventsIf(new PlayerJoinListener(), Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE)
				|| Settings.JoinSettings.ENABLE_JOIN_BROADCASTS.equals(Boolean.TRUE));

		registerEventsIf(new PlayerChatListener(), Settings.ChatSettings.ENABLE_MENTIONS);

		Messenger.setInfoPrefix(SimpleSettings.PLUGIN_PREFIX);

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE))
			new BroadcastTask().runTaskTimerAsynchronously(this, 0,
					Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());

		PlayerCache.clearAllData();
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

	public static Collection<ProxiedPlayer> getPlayers() {
		final Collection<ProxiedPlayer> players = new ArrayList<>();
		for (val serverInfo : ProxyServer.getInstance().getServers().values())
			players.addAll(serverInfo.getPlayers());

		return players;
	}

}
