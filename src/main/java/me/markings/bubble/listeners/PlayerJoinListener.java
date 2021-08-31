package me.markings.bubble.listeners;

import lombok.val;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.model.Variables;

import java.util.stream.IntStream;

public class PlayerJoinListener implements Listener {

	@EventHandler
	public void onJoin(final PlayerJoinEvent event) {
		val player = event.getPlayer();

		val messages = Localization.WelcomeMessages.JOIN_MOTD;
		val broadcastMessages = Localization.WelcomeMessages.JOIN_BROADCAST;

		val motdSound = Settings.WelcomeSettings.MOTD_SOUND;
		val motdDelay = Settings.WelcomeSettings.MOTD_DELAY;

		if (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE)) {
			messages.forEach(messageGroup ->
					IntStream.range(0, messageGroup.toArray().length).forEach(i -> {
						if (MessageUtil.isCommand(messageGroup.get(i)))
							MessageUtil.executePlaceholders(messageGroup.get(i), player);
						else {
							final String message = messageGroup.get(i).contains("gradient") ? Variables
									.replace(StringUtils.substringBetween(messageGroup.get(i), ">", "<"), player)
									: Variables.replace(messageGroup.get(i), player);
							Common.tellLater(Settings.WelcomeSettings
									.MOTD_DELAY.getTimeTicks(), player, MessageUtil.format(MessageUtil.translateGradient(message)));
						}

					}));
			Common.runLaterAsync(motdDelay.getTimeTicks(), () ->
					new SimpleSound(motdSound.getSound(), motdSound.getVolume(), motdSound.getPitch()).play(player));
		}

		if (Settings.WelcomeSettings.ENABLE_JOIN_BROADCASTS.equals(Boolean.TRUE) && !player.hasPlayedBefore())
			IntStream.range(0, broadcastMessages.toArray().length).mapToObj(i ->
					Variables.replace(MessageUtil.translateGradient(broadcastMessages.get(i)), player)).forEach(Common::broadcast);
	}

}
