package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.PrefsCommand;
import me.markings.bubble.command.ToggleCommand;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.listeners.DatabaseListener;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.DatabaseFile;
import me.markings.bubble.settings.MenuSettings;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.metrics.Metrics;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;

@Getter
public final class Bubble extends SimplePlugin {

	public static final File settingsFile = new File("plugins/Bubble/", FoConstants.File.SETTINGS);

	@Getter
	public FileConfiguration bubbleSettings;

	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	private final BubbleDatabase database = BubbleDatabase.getInstance();

	@Override
	protected void onPluginStart() {
		new Metrics(this, 13904);
	}

	@Override
	protected void onPluginPreReload() {
		Settings.BroadcastSettings.MESSAGE_MAP.clear();
		Settings.WelcomeSettings.JOIN_MOTD.clear();

		DatabaseFile.getInstance().reload();

		MenuSettings.PreferencesMenuSettings.getInstance().reload();
		MenuSettings.ChatMenuSettings.getInstance().reload();
		MenuSettings.MOTDMenuSettings.getInstance().reload();
		MenuSettings.MentionsMenuSettings.getInstance().reload();
		MenuSettings.EditMenuSettings.getInstance().reload();

		SimplePlugin.getInstance().getSettings();
	}

	@Override
	protected void onReloadablesStart() {
		Common.setLogPrefix("[Bubble]");

		val dataFile = DatabaseFile.getInstance();

		if (Settings.DatabaseSettings.ENABLE_MYSQL.equals(Boolean.TRUE))
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

		if (Settings.HookSettings.VAULT.equals(Boolean.TRUE))
			Valid.checkBoolean(Common.doesPluginExist("Vault"),
					"Failed to hook into Vault! Please check if the plugin is installed and restart!");

		if (Settings.HookSettings.PAPI.equals(Boolean.TRUE))
			Valid.checkBoolean(Common.doesPluginExist("PlaceholderAPI"),
					"Failed to hook into PlaceholderAPI! Please check if the plugin is installed and restart!");

		Variable.loadVariables();

		registerLocalVariables();
		registerPAPIVariables();

		bubbleSettings = YamlConfiguration.loadConfiguration(settingsFile);

		registerEventsIf(new PlayerJoinListener(), Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) ||
				Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE));

		registerEventsIf(new PlayerChatListener(), Settings.ChatSettings.ENABLE_MENTIONS.equals(Boolean.TRUE));

		Messenger.setInfoPrefix(SimpleSettings.PLUGIN_PREFIX);

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE))
			new BroadcastTask().runTaskTimerAsynchronously(this, 0,
					Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());

		Remain.getOnlinePlayers().forEach(player ->
				database.save(player.getName(), player.getUniqueId(), PlayerCache.getCache(player)));

		DatabaseFile.getInstance().save();

		MenuSettings.PreferencesMenuSettings.getInstance().save();
		MenuSettings.ChatMenuSettings.getInstance().save();
		MenuSettings.MOTDMenuSettings.getInstance().save();
		MenuSettings.MentionsMenuSettings.getInstance().save();
		MenuSettings.EditMenuSettings.getInstance().save();

		PlayerCache.clearAllData();

		Common.log(Common.getLogPrefix() + " Bubble has been successfully enabled!");
	}

	private static void registerLocalVariables() {
		Variables.addVariable("broadcasts_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isBroadcastStatus()));
		Variables.addVariable("broadcast_sound_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isBroadcastSoundStatus()));
		Variables.addVariable("motd_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isMotdStatus()));
		Variables.addVariable("mentions_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isMentionsStatus()));
		Variables.addVariable("mentions_sound_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isMentionSoundStatus()));
		Variables.addVariable("mentions_toast_enabled", player -> String.valueOf(PlayerCache.getCache((Player) player).isMentionToastStatus()));


		Variables.addVariable("ping", player -> player instanceof Player ? ((Player) player).getPing() + "ms" : "0ms");
		Variables.addVariable("tps", sender -> String.valueOf(Remain.getTPS()));
	}

	private static void registerPAPIVariables() {
		HookManager.addPlaceholder("broadcasts_enabled", player -> String.valueOf(PlayerCache.getCache(player).isBroadcastStatus()));
		HookManager.addPlaceholder("broadcast_sound_enabled", player -> String.valueOf(PlayerCache.getCache(player).isBroadcastSoundStatus()));
		HookManager.addPlaceholder("motd_enabled", player -> String.valueOf(PlayerCache.getCache(player).isMotdStatus()));
		HookManager.addPlaceholder("mentions_enabled", player -> String.valueOf(PlayerCache.getCache(player).isMentionsStatus()));
		HookManager.addPlaceholder("mentions_sound_enabled", player -> String.valueOf(PlayerCache.getCache(player).isMentionSoundStatus()));
		HookManager.addPlaceholder("mentions_toast_enabled", player -> String.valueOf(PlayerCache.getCache(player).isMentionToastStatus()));
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

}
