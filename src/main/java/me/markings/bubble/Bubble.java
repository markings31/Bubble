package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.bungee.BubbleBungeeListener;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.PrefsCommand;
import me.markings.bubble.command.ToggleCommand;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.event.BroadcastEvent;
import me.markings.bubble.listeners.DatabaseListener;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.DatabaseFile;
import me.markings.bubble.settings.MenuSettings;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.bungee.SimpleBungee;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.SimpleSettings;

public final class Bubble extends SimplePlugin {

	@Getter
	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	@Getter
	private final SimpleBungee bungeeCord = new SimpleBungee("plugin:bubble", BubbleBungeeListener.class, BubbleAction.class);

	private final BubbleDatabase database = BubbleDatabase.getInstance();

	@Override
	protected void onPluginStart() {
		Common.setTellPrefix(SimpleSettings.PLUGIN_PREFIX);

		val dataFile = DatabaseFile.getInstance();

		if (Boolean.TRUE.equals(Settings.DatabaseSettings.ENABLE_MYSQL))
			Common.runLaterAsync(() -> database.connect(
					dataFile.getHost(),
					dataFile.getPort(),
					dataFile.getName(),
					dataFile.getUsername(),
					dataFile.getPassword(),
					dataFile.getTableName()));

		registerCommand(new AnnounceCommand());
		registerCommand(new ToggleCommand());
		registerCommand(new PrefsCommand());

		registerEvents(new DatabaseListener());

		if (Common.doesPluginExist("Vault"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into Vault!");

		if (Common.doesPluginExist("PlaceholderAPI"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into PlaceholderAPI!");

		if (Common.doesPluginExist("MultiverseCore"))
			Common.log(Common.getTellPrefix() + "Successfully hooked into MultiverseCore!");

		// TODO: Decapitalize return strings + add toUppercase() to menu variables.
		// Plugin-Wide Variable
		Variables.addVariable("broadcast_status", player -> PlayerCache.getCache((Player) player).isBroadcastStatus() ? "ENABLED" : "DISABLED");

		// PlaceholderAPI Variable
		HookManager.addPlaceholder("broadcast_status", player -> PlayerCache.getCache(player).isBroadcastStatus() ? "ENABLED" : "DISABLED");

		Variables.addVariable("broadcast_sound_status", player -> PlayerCache.getCache((Player) player).isBroadcastSoundStatus() ? "ENABLED" : "DISABLED");
		HookManager.addPlaceholder("broadcast_sound_status", player -> PlayerCache.getCache(player).isBroadcastSoundStatus() ? "ENABLED" : "DISABLED");

		Variables.addVariable("motd_status", player -> PlayerCache.getCache((Player) player).isMotdStatus() ? "ENABLED" : "DISABLED");
		HookManager.addPlaceholder("motd_status", player -> PlayerCache.getCache(player).isMotdStatus() ? "ENABLED" : "DISABLED");

		Variables.addVariable("mentions_status", player -> PlayerCache.getCache((Player) player).isMentionsStatus() ? "ENABLED" : "DISABLED");
		HookManager.addPlaceholder("mentions_status", player -> PlayerCache.getCache(player).isMentionsStatus() ? "ENABLED" : "DISABLED");

		Variables.addVariable("mentions_sound_status", player -> PlayerCache.getCache((Player) player).isMentionSoundStatus() ? "ENABLED" : "DISABLED");
		HookManager.addPlaceholder("mentions_sound_status", player -> PlayerCache.getCache(player).isMentionSoundStatus() ? "ENABLED" : "DISABLED");

		Variables.addVariable("mentions_toast_status", player -> PlayerCache.getCache((Player) player).isMentionToastStatus() ? "ENABLED" : "DISABLED");
		HookManager.addPlaceholder("mentions_toast_status", player -> PlayerCache.getCache(player).isMentionToastStatus() ? "ENABLED" : "DISABLED");

		Common.log(Common.getTellPrefix() + "Bubble has been successfully enabled!");
	}

	@Override
	protected void onPluginPreReload() {
		Settings.BroadcastSettings.MESSAGE_MAP.clear();
		Settings.WelcomeSettings.JOIN_MOTD.clear();
		DatabaseFile.getInstance().reload();
		MenuSettings.getInstance().reload();
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

		Remain.getOnlinePlayers().forEach(player ->
				database.save(player.getName(), player.getUniqueId(), PlayerCache.getCache(player)));
		
		DatabaseFile.getInstance().save();
		MenuSettings.getInstance().save();

		PlayerCache.clearAllData();
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

	@EventHandler
	public void onBroadcast(final BroadcastEvent event) {
		Common.log("Message: " + event.getMessage(), "Sound: " + event.getSound());
	}
}
