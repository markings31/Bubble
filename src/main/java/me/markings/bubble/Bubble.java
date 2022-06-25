package me.markings.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.model.Placeholders;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.DatabaseFile;
import me.markings.bubble.settings.MenuSettings;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.constants.FoConstants;
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
		// Nothing to see here.
	}

	@Override
	protected void onPluginPreReload() {
		//Broadcasts.setMessages(null);
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

		Valid.checkBoolean(Settings.HookSettings.VAULT.equals(Boolean.TRUE) && HookManager.isVaultLoaded(),
				"Failed to hook into Vault! Please check if the plugin is installed and restart!");

		Valid.checkBoolean(Settings.HookSettings.PAPI.equals(Boolean.TRUE) && HookManager.isPlaceholderAPILoaded(),
				"Failed to hook into PlaceholderAPI! Please check if the plugin is installed and restart!");

		Variable.loadVariables();

		Variables.addExpansion(Placeholders.getInstance());

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
		Broadcasts.loadBroadcasts();

		MenuSettings.PreferencesMenuSettings.getInstance().save();
		MenuSettings.ChatMenuSettings.getInstance().save();
		MenuSettings.MOTDMenuSettings.getInstance().save();
		MenuSettings.MentionsMenuSettings.getInstance().save();
	}

	@Override
	public Set<String> getConsoleFilter() {
		return new HashSet<>(Arrays.asList(
				"Reloading plugin Bubble",
				"______________________________________________________________",
				"[PlaceholderAPI] Successfully registered expansion: bubble",
				"[Bubble] Bubble has been successfully enabled!",
				"  ",
				"issued server command: /#flp"));
	}

	@Override
	public int getMetricsPluginId() {
		if (Boolean.TRUE.equals(Settings.HookSettings.BSTATS))
			return 13904;

		return 0;
	}

	@Override
	public int getFoundedYear() {
		return 2022;
	}

	public static Bubble getInstance() {
		return (Bubble) SimplePlugin.getInstance();
	}

}
