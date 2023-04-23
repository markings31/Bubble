package me.markings.bubble.hook;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.dependencies.jda.api.EmbedBuilder;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Member;
import github.scarsz.discordsrv.dependencies.jda.api.entities.Message;
import github.scarsz.discordsrv.dependencies.jda.api.entities.TextChannel;
import github.scarsz.discordsrv.util.DiscordUtil;
import github.scarsz.discordsrv.util.WebhookUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.settings.Settings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.model.DiscordListener;
import org.mineacademy.fo.model.Replacer;

import java.awt.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DiscordSRVHook extends DiscordListener {

    @Getter
    private static final DiscordSRVHook instance = new DiscordSRVHook();

    // Discord --> Minecraft
    @Override
    protected void onMessageReceived(final DiscordGuildMessagePreProcessEvent event) {
        final TextChannel channel = event.getChannel();
        final Member member = event.getMember();
        final Message message = event.getMessage();
        final String chatMessage = message.getContentDisplay();

        final String displayMessage = ChatUtil.removeEmoji(chatMessage);

        if (!displayMessage.trim().isEmpty() && findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)) != null
                && Boolean.TRUE.equals(Settings.DiscordSettings.DISCORDMINECRAFT))
            Common.broadcast(Replacer.replaceVariables(Settings.DiscordSettings.CHATFORMAT,
                    SerializedMap.ofArray("user", member.getEffectiveName(), "message", displayMessage, "channel", channel.getName())));
    }


    // Minecraft --> Discord
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessageSend(final AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final String message = event.getMessage();

        if (Boolean.TRUE.equals(Settings.DiscordSettings.MINECRAFTDISCORD)) {
            if (Boolean.TRUE.equals(Settings.DiscordSettings.USEWEBHOOK))
                sendWebhookMessage(player, findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)).getName(), message);
            else
                sendMessage(findChannel(Long.parseLong(Settings.DiscordSettings.MINECRAFTID)).getName(), message);
        }
    }

    // TODO: Move to Notification.java class.
    public void sendAnnouncement(final Player player, final String title, final String description, final Color color, final String imageUrl) {
        final Member member = DiscordUtil.getMemberById(DiscordSRV.getPlugin().getAccountLinkManager().getDiscordId(player.getUniqueId()));
        final String profileImg = member == null ? DiscordSRV.getAvatarUrl(player) : member.getUser().getAvatarUrl();
        findChannel(Long.parseLong(Settings.DiscordSettings.ANNOUNCEMENTSID))
                .sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor(Settings.DiscordSettings.AUTHOR, "https://www.namemc.com/profile/" + player.getName(),
                                profileImg).setTitle(title)
                        .setDescription(description).setColor(color).setThumbnail(imageUrl == null
                                ? Settings.DiscordSettings.THUMBNAIL.replace("%player%", player.getName())
                                : imageUrl)
                        .setImage(Settings.DiscordSettings.DEFAULT_IMAGE).build()).queue();
    }

    public void sendWebhookMessage(final Player player, final String message) {
        WebhookUtil.deliverMessage(findChannel(Long.parseLong(Settings.DiscordSettings.ANNOUNCEMENTSID)), player, message);
    }
}