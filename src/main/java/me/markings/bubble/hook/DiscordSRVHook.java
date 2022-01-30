package me.markings.bubble.hook;

import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.util.WebhookUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.DiscordListener;
import org.mineacademy.fo.remain.Remain;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiscordSRVHook extends DiscordListener {

	@Getter
	private static final DiscordSRVHook instance = new DiscordSRVHook();

	private static final String messagePrefix = "[D_Message]";
	private static final String announcePrefix = "[D_Announce]";

	@Override
	protected void onMessageReceived(final DiscordGuildMessagePreProcessEvent event) {
		val channel = event.getChannel();
		val member = event.getMember();
		val message = event.getMessage();
		val chatMessage = message.getContentDisplay();

		if (channel.getName().equals("general"))
			Remain.getOnlinePlayers().forEach(player ->
					Common.tellNoPrefix(player, "&9&lDiscord &8--> &7" + member.getEffectiveName() + ": &f" + chatMessage));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMessageSend(final AsyncPlayerChatEvent event) {
		val player = event.getPlayer();
		val message = event.getMessage();
		val args = message.replace(announcePrefix, "").split("\\|");

		if (message.startsWith(messagePrefix)) {
			event.setCancelled(true);
			sendWebhookMessage(player, "general", Common.stripColors(message.replace(messagePrefix, "")));
		} else if (message.startsWith(announcePrefix)) {
			event.setCancelled(true);
			sendAnnouncement(args[0], args[1], Color.GREEN);
		}
	}

	public void sendAnnouncement(final String title, final String description, final Color color) {
		findChannel(461319299945201669L)
				.sendMessageEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).build()).complete();
	}

	public void sendAnnouncement(final String title, final String description, final Color color, final String url) {
		findChannel(461319299945201669L)
				.sendMessageEmbeds(new EmbedBuilder().setTitle(title).setDescription(description).setColor(color).setThumbnail(url).build()).complete();
	}

	public void sendWebhookMessage(final Player player, final String message) {
		WebhookUtil.deliverMessage(findChannel(461319299945201669L), player, message);
	}

}
