package me.markings.bubble;

import lombok.Getter;
import me.markings.bubble.listeners.PlayerChatListener;
import me.markings.bubble.listeners.PlayerJoinListener;
import me.markings.bubble.model.Placeholders;
import me.markings.bubble.mysql.BubbleDatabase;
import me.markings.bubble.settings.Broadcasts;
import me.markings.bubble.settings.DatabaseFile;
import me.markings.bubble.settings.MenuData;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.tasks.BroadcastTask;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.constants.FoConstants;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.model.Variable;
import org.mineacademy.fo.model.Variables;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.File;

@Getter
public final class Bubble extends SimplePlugin {

    public static final File settingsFile = new File("plugins/Bubble", FoConstants.File.SETTINGS);

    private final BubbleDatabase database = BubbleDatabase.getInstance();

    @Override
    protected void onPluginStart() {
        // Nothing to see here.
    }

    @Override
    protected void onPluginPreReload() {
        Settings.WelcomeSettings.JOIN_MOTD.clear();
        //DatabaseFile.getInstance().reload();
    }

    @Override
    protected void onReloadablesStart() {
        Common.setLogPrefix("&8[&9Bubble&8]&r");

        /*final DatabaseFile dataFile = DatabaseFile.getInstance();

        if (Settings.DatabaseSettings.ENABLE_MYSQL.equals(Boolean.TRUE))
            Common.runLaterAsync(() -> database.connect(
                    dataFile.getHost(),
                    dataFile.getPort(),
                    dataFile.getName(),
                    dataFile.getUsername(),
                    dataFile.getPassword(),
                    dataFile.getTableName()));*/

        if (Settings.HookSettings.VAULT.equals(Boolean.TRUE))
            if (HookManager.isVaultLoaded())
                Common.log("Hooked into Vault. Visit the settings.yml file for more options.");
            else
                Common.log("Failed to hook into the Vault plugin! Please check that the plugin is installed properly and restart.");

        if (Settings.HookSettings.PAPI.equals(Boolean.TRUE))
            if (HookManager.isPlaceholderAPILoaded())
                Common.log("Hooked into PlaceholderAPI. Visit the settings.yml file for more options.");
            else Common.log("Failed to hook into PlaceholderAPI! Please check if the plugin is installed and restart!");

        Variable.loadVariables();

        Variables.addExpansion(Placeholders.getInstance());

        if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE))
            registerEvents(PlayerJoinListener.getInstance());

        if (Settings.ChatSettings.ENABLE_MENTIONS.equals(Boolean.TRUE))
            registerEvents(PlayerChatListener.getInstance());

        if (Settings.NotificationSettings.ENABLE_BROADCASTS)
            new BroadcastTask().runTaskTimerAsynchronously(this, 0,
                    Settings.NotificationSettings.BROADCAST_DELAY.getTimeTicks());

        DatabaseFile.getInstance().saveToMap();
        Broadcasts.loadBroadcasts();
        MenuData.loadMenus();
    }

    @Override
    public int getMetricsPluginId() {
        return Boolean.TRUE.equals(Settings.HookSettings.BSTATS) ? 13904 : 0;
    }

    @Override
    public int getFoundedYear() {
        return 2021;
    }

    public static Bubble getInstance() {
        return (Bubble) SimplePlugin.getInstance();
    }
}
