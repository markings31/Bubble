package me.markings.bubble.listeners;

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
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Variables;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(final @NotNull PlayerJoinEvent event) {
		val player = event.getPlayer();
		event.setJoinMessage(Settings.JoinSettings.ENABLE_JOIN_MESSAGE.equals(Boolean.TRUE) ?
				Common.colorize(Variables.replace(Settings.JoinSettings.JOIN_MESSAGE, player)) : null);

		val cache = PlayerCache.getCache(player);

		val messages = Settings.WelcomeSettings.JOIN_MOTD;

		val motdSound = Settings.WelcomeSettings.MOTD_SOUND;
		val motdDelay = Settings.WelcomeSettings.MOTD_DELAY;

		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) && cache.isMotdStatus())
			Common.runLaterAsync(motdDelay.getTimeTicks(), () -> {
				messages.forEach(message -> {
					MessageUtil.executePlaceholders(message, player);
					Common.tell(player, MessageUtil.replaceVarsAndGradient(message, player));
				});
				new SimpleSound(motdSound.getSound(), motdSound.getVolume(), motdSound.getPitch()).play(player);
			});

		if (Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE) && !player.hasPlayedBefore())
			player.getWorld().spawn(player.getLocation(), Firework.class);

	}

}
