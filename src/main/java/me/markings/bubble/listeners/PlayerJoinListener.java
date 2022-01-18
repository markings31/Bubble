package me.markings.bubble.listeners;

import lombok.SneakyThrows;
import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Variables;

public class PlayerJoinListener implements Listener {

	@EventHandler
	@SneakyThrows
	public void onJoin(final @NotNull PlayerJoinEvent event) {
		val player = event.getPlayer();
		event.setJoinMessage(Settings.JoinSettings.ENABLE_JOIN_MESSAGE.equals(Boolean.TRUE) ?
				Common.colorize(Variables.replace(Settings.JoinSettings.JOIN_MESSAGE, player)) : event.getJoinMessage());

		val cache = PlayerCache.getCache(player);

		val messages = Settings.WelcomeSettings.JOIN_MOTD;

		val motdSound = Settings.WelcomeSettings.MOTD_SOUND;
		val motdDelay = Settings.WelcomeSettings.MOTD_DELAY;

		Debugger.debug("join",
				"Player: " + player +
						" Cache: " + cache +
						"Enable Join Message: " + Settings.JoinSettings.ENABLE_JOIN_MESSAGE +
						"Enable Join MOTD: " + Settings.WelcomeSettings.ENABLE_JOIN_MOTD);

		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) && cache.isMotdStatus())
			Common.runLaterAsync(motdDelay.getTimeTicks(), () -> {
				messages.forEach(message -> {
					if (MessageUtil.isExecutable(message)) {
						MessageUtil.executePlaceholders(message, player);
						return;
					}
					Common.tell(player, MessageUtil.replaceVarsAndGradient(message, player));
				});
				new SimpleSound(motdSound.getSound(), motdSound.getVolume(), motdSound.getPitch()).play(player);
			});

		if (Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE) && !player.hasPlayedBefore())
			player.getWorld().spawn(player.getLocation(), Firework.class);

	}

}
