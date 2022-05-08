package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.command.AnnounceCommand;
import me.markings.bubble.command.PrefsCommand;
import me.markings.bubble.command.ToggleCommand;
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
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.metrics.Metrics;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.Remain;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class Bubble extends SimplePlugin {

	public static final File settingsFile = new File("plugins/Bubble/", FoConstants.File.SETTINGS);

	private final BubbleDatabase database = BubbleDatabase.getInstance();

	@Override
	protected void onPluginStart() {
		if (Boolean.TRUE.equals(Settings.HookSettings.BSTATS))
			new Metrics(this, 13904);

		Common.log(Common.getLogPrefix() + " Bubble has been successfully enabled!");
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
	}

	@Override
	protected void onReloadablesStart() {
		Common.setLogPrefix("&8[&bBubble&8]&f");

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

		registerEvents(DatabaseListener.getInstance());

		if (HookManager.isDiscordSRVLoaded() && Boolean.TRUE.equals(Settings.HookSettings.DISCORDSRV))
			registerEvents(DiscordSRVHook.getInstance());

		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) ||
				Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE))
			registerEvents(PlayerJoinListener.getInstance());

		if (Settings.ChatSettings.ENABLE_MENTIONS.equals(Boolean.TRUE))
			registerEvents(PlayerChatListener.getInstance());

		if (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE))
			new BroadcastTask().runTaskTimerAsynchronously(this, 0,
					Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());

		Remain.getOnlinePlayers().forEach(player ->
				database.save(player.getName(), player.getUniqueId(), PlayerData.getCache(player)));

		DatabaseFile.getInstance().saveToMap();

		MenuSettings.PreferencesMenuSettings.getInstance().save();
		MenuSettings.ChatMenuSettings.getInstance().save();
		MenuSettings.MOTDMenuSettings.getInstance().save();
		MenuSettings.MentionsMenuSettings.getInstance().save();

		PlayerData.clearAllData();
	}

	@Override
	public Set<String> getConsoleFilter() {
		return new HashSet<>(Arrays.asList(
				"Reloading plugin Bubble",
				"______________________________________________________________",
				"[PlaceholderAPI] Successfully registered expansion: bubble",
				"[Bubble] Bubble has been successfully enabled!",
				"  ",
				"issued server command: /#flp",
				"[DiscordSRV] API listener org.mineacademy.fo.model.DiscordListener$DiscordListenerImpl unsubscribed",
				"[DiscordSRV] API listener org.mineacademy.fo.model.DiscordListener$DiscordListenerImpl subscribed (2 methods)"));
	}

	@Override
	public int getFoundedYear() {
		return 2022;
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

}
