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
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.SimpleSettings;

import java.io.File;

@Getter
public final class Bubble extends SimplePlugin {

	private static final String enabledText = "enabled";
	private static final String disabledText = "disabled";

	public static final File settingsFile = new File("plugins/Bubble/", "settings.yml");

	@Getter
	public FileConfiguration bubbleSettings;

	private final SimpleCommandGroup mainCommand = new BubbleGroup();

	private final BubbleDatabase database = BubbleDatabase.getInstance();

	@Override
	protected void onPluginStart() {
		// All functions can be handled both when the server reloads and when it is first booted (onReloadablesStart())
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

		registerEventsIf(new PlayerChatListener(), Settings.ChatSettings.ENABLE_MENTIONS);

		Messenger.setInfoPrefix(SimpleSettings.PLUGIN_PREFIX);

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE))
			new BroadcastTask().runTaskTimerAsynchronously(this, 0,
					Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());
		else
			new BroadcastTask().runTaskTimer(this, 0,
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

		Common.log(Common.getTellPrefix() + "Bubble has been successfully enabled!");
	}

	private static void registerLocalVariables() {
		Variables.addVariable("broadcast_status", player -> PlayerCache.getCache((Player) player).isBroadcastStatus() ? enabledText : disabledText);
		Variables.addVariable("broadcast_sound_status", player -> PlayerCache.getCache((Player) player).isBroadcastSoundStatus() ? enabledText : disabledText);
		Variables.addVariable("motd_status", player -> PlayerCache.getCache((Player) player).isMotdStatus() ? enabledText : disabledText);
		Variables.addVariable("mentions_status", player -> PlayerCache.getCache((Player) player).isMentionsStatus() ? enabledText : disabledText);
		Variables.addVariable("mentions_sound_status", player -> PlayerCache.getCache((Player) player).isMentionSoundStatus() ? enabledText : disabledText);
		Variables.addVariable("mentions_toast_status", player -> PlayerCache.getCache((Player) player).isMentionToastStatus() ? enabledText : disabledText);


		Variables.addVariable("ping", player -> player instanceof Player ? ((Player) player).getPing() + "ms" : "0ms");
		Variables.addVariable("tps", sender -> String.valueOf(Remain.getTPS()));
	}

	private static void registerPAPIVariables() {
		HookManager.addPlaceholder("broadcast_status", player -> PlayerCache.getCache(player).isBroadcastStatus() ? enabledText : disabledText);
		HookManager.addPlaceholder("broadcast_sound_status", player -> PlayerCache.getCache(player).isBroadcastSoundStatus() ? enabledText : disabledText);
		HookManager.addPlaceholder("motd_status", player -> PlayerCache.getCache(player).isMotdStatus() ? enabledText : disabledText);
		HookManager.addPlaceholder("mentions_status", player -> PlayerCache.getCache(player).isMentionsStatus() ? enabledText : disabledText);
		HookManager.addPlaceholder("mentions_sound_status", player -> PlayerCache.getCache(player).isMentionSoundStatus() ? enabledText : disabledText);
		HookManager.addPlaceholder("mentions_toast_status", player -> PlayerCache.getCache(player).isMentionToastStatus() ? enabledText : disabledText);
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

}
