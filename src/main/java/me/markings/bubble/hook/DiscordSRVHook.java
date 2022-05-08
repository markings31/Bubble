package me.markings.bubble.hook;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.util.WebhookUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.markings.bubble.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.DiscordListener;
import org.mineacademy.fo.remain.Remain;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DiscordSRVHook extends DiscordListener {

	@Getter
	private static final DiscordSRVHook instance = new DiscordSRVHook();

	@Override
	protected void onMessageReceived(final DiscordGuildMessagePreProcessEvent event) {
		val channel = event.getChannel();
		val member = event.getMember();
		val message = event.getMessage();
		val chatMessage = message.getContentDisplay();

		val displayMessage = ChatUtil.removeEmoji(chatMessage);

		if (!displayMessage.trim().isEmpty() && channel.equals(findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)))
				&& Boolean.TRUE.equals(Settings.DiscordSettings.DISCORDMINECRAFT))
			Remain.getOnlinePlayers().forEach(player -> Common.tellNoPrefix(player, Settings.DiscordSettings.CHATFORMAT
					.replace("%user%", member.getEffectiveName())
					.replace("%message%", displayMessage)
					.replace("%channel%", channel.getName())));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMessageSend(final AsyncPlayerChatEvent event) {
		val player = event.getPlayer();
		val message = event.getMessage();

		if (Boolean.TRUE.equals(Settings.DiscordSettings.USEWEBHOOK) && Boolean.TRUE.equals(Settings.DiscordSettings.MINECRAFTDISCORD))
			WebhookUtil.deliverMessage(findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)), player, message);
		else
			findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)).sendMessage(message).queue();
	}

	public void sendAnnouncement(final Player player, final String title, final String description, final Color color, final String url) {
		val member = DiscordUtil.getMemberById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()));
		val profileImg = member == null ? DiscordSRV.getAvatarUrl(player) : member.getUser().getAvatarUrl();
		findChannel(Long.parseLong(Settings.DiscordSettings.ANNOUNCEMENTSID))
				.sendMessageEmbeds(new EmbedBuilder()
						.setAuthor(Settings.DiscordSettings.AUTHOR, "https://www.namemc.com/profile/" + player.getName(),
								profileImg).setTitle(title)
						.setDescription(description).setColor(color).setThumbnail(url == null
								? Settings.DiscordSettings.THUMBNAIL.replace("%player%", player.getName())
								: url)
						.setImage(Settings.DiscordSettings.DEFAULT_IMAGE).build()).queue();
	}

	public void sendWebhookMessage(final Player player, final String message) {
		WebhookUtil.deliverMessage(findChannel(Long.parseLong(Settings.DiscordSettings.ANNOUNCEMENTSID)), player, message);
	}
}
