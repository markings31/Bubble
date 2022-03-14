package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.PrefsCommand;
import me.markings.bubble.command.ToggleCommand;
import me.markings.bubble.command.bubble.BubbleGroup;
import me.markings.bubble.hook.DiscordSRVHook;
import me.markings.bubble.listeners.DatabaseListener;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.model.Placeholders;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.DatabaseFile;
import me.markings.bubble.settings.MenuSettings;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.metrics.Metrics;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.io.File;

@Getter
public final class Bubble extends SimplePlugin {

	public static final File settingsFile = new File("plugins/Bubble/", FoConstants.File.SETTINGS);

	@Getter
	public FileConfiguration bubbleSettings;

	private final SimpleCommandGroup mainCommand = BubbleGroup.getInstance();

	private final BubbleDatabase database = BubbleDatabase.getInstance();

	@Override
	protected void onPluginStart() {
		if (Boolean.TRUE.equals(Settings.HookSettings.BSTATS))
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

		Valid.checkBoolean(Settings.HookSettings.VAULT.equals(Boolean.TRUE) && HookManager.isVaultLoaded(),
				"Failed to hook into Vault! Please check if the plugin is installed and restart!");

		Valid.checkBoolean(Settings.HookSettings.PAPI.equals(Boolean.TRUE) && HookManager.isPlaceholderAPILoaded(),
				"Failed to hook into PlaceholderAPI! Please check if the plugin is installed and restart!");

		Variable.loadVariables();

		Variables.addExpansion(Placeholders.getInstance());

		bubbleSettings = YamlConfiguration.loadConfiguration(settingsFile);

		registerEvents(DatabaseListener.getInstance());

		registerEventsIf(DiscordSRVHook.getInstance(), HookManager.isDiscordSRVLoaded() && Settings.HookSettings.DISCORDSRV);

		registerEventsIf(PlayerJoinListener.getInstance(), Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) ||
				Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE));

		registerEventsIf(PlayerChatListener.getInstance(), Settings.ChatSettings.ENABLE_MENTIONS.equals(Boolean.TRUE));

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

		Filter.inject();

		Common.log(Common.getLogPrefix() + " Bubble has been successfully enabled!");
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

}
