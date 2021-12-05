package me.markings.bubble.listeners;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.bungee.BubbleAction;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.BungeeUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Variables;

import java.util.stream.IntStream;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(final @NotNull PlayerJoinEvent event) {
		val player = event.getPlayer();
		event.setJoinMessage(Settings.JoinSettings.ENABLE_JOIN_MESSAGE.equals(Boolean.TRUE) ?
				Common.colorize(Variables.replace(Localization.JoinQuitMessages.JOIN_MESSAGE, player)) : null);

		val cache = PlayerCache.getCache(player);

		val messages = Settings.WelcomeSettings.JOIN_MOTD;
		val broadcastMessages = Localization.WelcomeMessages.JOIN_BROADCAST;

		val motdSound = Settings.WelcomeSettings.MOTD_SOUND;
		val motdDelay = Settings.WelcomeSettings.MOTD_DELAY;

		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) && cache.isMotdStatus()) {
			Common.runLaterAsync(motdDelay.getTimeTicks(), () -> {
				messages.forEach(messageGroup -> IntStream.range(0, messageGroup.toArray().length).forEach(i -> {
					MessageUtil.executePlaceholders(messageGroup.get(i), player);
					if (!Settings.WelcomeSettings.BUNGEECORD) {
						Common.tell(player, MessageUtil.replaceVarsAndGradient(messageGroup.get(i), player));
					} else
						BungeeUtil.tellBungee(BubbleAction.NOTIFICATION, "message", MessageUtil.replaceVarsAndGradient(messageGroup.get(i), player));
				}));
				new SimpleSound(motdSound.getSound(), motdSound.getVolume(), motdSound.getPitch()).play(player);
			});
		}

		if (Settings.JoinSettings.ENABLE_JOIN_BROADCASTS.equals(Boolean.TRUE) && !player.hasPlayedBefore() && !PlayerUtil.isVanished(player))
			IntStream.range(0, broadcastMessages.toArray().length).mapToObj(i ->
					MessageUtil.replaceVarsAndGradient(broadcastMessages.get(i), player)).forEach(Common::broadcast);
	}

}
