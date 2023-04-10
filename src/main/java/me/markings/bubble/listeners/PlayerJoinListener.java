package me.markings.bubble.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.markings.bubble.Bubble;
import me.markings.bubble.PlayerData;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.model.Variables;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerJoinListener implements Listener {

    @Getter
    private static final PlayerJoinListener instance = new PlayerJoinListener();

    @EventHandler
    @SneakyThrows
    public void onJoin(final @NotNull PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PlayerData cache = PlayerData.from(player);

        final List<String> messages = Settings.WelcomeSettings.JOIN_MOTD;

        final SimpleSound motdSound = Settings.WelcomeSettings.MOTD_SOUND;
        final SimpleTime motdDelay = Settings.WelcomeSettings.MOTD_DELAY;

        Debugger.debug("join",
                "Player: " + player +
                        " Cache: " + cache +
                        "Enable Join MOTD: " + Settings.WelcomeSettings.ENABLE_JOIN_MOTD);


        //if (Settings.DatabaseSettings.ENABLE_MYSQL)
        //	BubbleDatabase.getInstance().load(player.getUniqueId(), PlayerData.from(player));

        if (player.getUniqueId().equals(UUID.fromString("a44f7a78-796d-4fc1-ae6b-97cd3d959a5e")))
            Common.runLaterAsync(40, () -> Messenger.info(player, "This server is running &b&lBubble v" + Bubble.getVersion()));

        if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) && cache.isMotdStatus())
            Common.runLaterAsync(motdDelay.getTimeTicks(), () -> {
                messages.forEach(message -> {
                    MessageUtil.executePlaceholders(Variables.replace(message, player), player);
                    message = Variables.replace(message, player);
                    Common.tellNoPrefix(player, MessageUtil.translateGradient(message));
                });
                new SimpleSound(motdSound.getSound(), motdSound.getVolume(), motdSound.getPitch()).play(player);
            });
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        PlayerData.remove(player);
    }
}
