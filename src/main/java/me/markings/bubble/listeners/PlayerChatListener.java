package me.markings.bubble.listeners;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.markings.bubble.PlayerData;
import me.markings.bubble.settings.Localization;
import me.markings.bubble.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.debug.Debugger;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.Remain;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerChatListener implements Listener {

	@Getter
	private static final PlayerChatListener instance = new PlayerChatListener();

	// Handles mentioning system.
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerChat(final AsyncPlayerChatEvent event) {
		final SimpleSound mentionSound = Settings.ChatSettings.MENTION_SOUND;
		final String message = event.getMessage();
		final String eventPlayerName = event.getPlayer().getDisplayName();
		final String previousColor = !Common.lastColorLetter(message).equals("") ? Common.lastColorLetter(message) : "&f";

		Debugger.debug("chat",
				"Player: " + eventPlayerName +
						"Message: " + message);

		/*
		// -----------------------------------------------------------------------------
		// Chat Filtering
		// -----------------------------------------------------------------------------

		boolean blocked = false;

		if (Settings.ChatSettings.ENABLE_CHAT_FILTER && !player.hasPermission(Permissions.Chat.BYPASS)) {

			val addressRegex = "^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

			// TODO: Merge stripped & substituted checks.
			val strippedMessage = message.replaceAll("[^a-zA-Z ]", "");
			String substitutedMessage = message
					.replace("0", "O")
					.replace("1", "I")
					.replace("3", "E")
					.replace("4", "A")
					.replace("5", "S")
					.replace("6", "G")
					.replace("7", "T")
					.replace("8", "B")
					.replace("@", "A")
					.replace("<", "C")
					.replace("#", "H")
					.replace("$", "S")
					.replace("+", "T")
					.replace("(", "C")
					.replace("{", "C")
					.replace("|", "L")
					.replace("!", "I")
					.replace("]", "I")
					.replace(">", "");
			String cleanMessage = message.toLowerCase();
			String addressContainter;
			final String[] words = substitutedMessage.split(" ");
			String[] alphabeticalSet = strippedMessage.split(" ");


			// Attempts to find "dot" phrase in message to split.
			if (
					strippedMessage.contains("d")
							&& strippedMessage.contains("t")
							&& strippedMessage.indexOf("d") < strippedMessage.indexOf("t")
							&& !strippedMessage.contains(" ")
			) {
				final String firstDotDelimiter = strippedMessage.substring(strippedMessage.indexOf("d"), strippedMessage.indexOf("t") + 1);
				final String secondDotDelimiter = strippedMessage.substring(strippedMessage.lastIndexOf("d"), strippedMessage.lastIndexOf("d") + 3);
				final String dotSplit = strippedMessage
						.replace(firstDotDelimiter, " " + firstDotDelimiter + " ")
						.replace(secondDotDelimiter, " " + secondDotDelimiter + " ");

				alphabeticalSet = dotSplit.split(" ");
			}

			// Removes extra letters in a word/phrase.
			for (int i = 0; i < words.length; i++) {
				for (int j = 0; j < words[i].length(); j++) {
					final int lastCharIndex = words[i].lastIndexOf(words[i].charAt(j));
					if (j != lastCharIndex) {
						words[i] = words[i].substring(0, j + 1)
								+ words[i].substring(lastCharIndex + 1);
						substitutedMessage = Common.join(words).replace(",", "");
					}
				}
			}

			// Blocks various slurs and inappropriate words.
			for (final String blockedWord : Settings.ChatSettings.BLOCKED_WORDS) {
				final String subNoSpaces = substitutedMessage.replace(" ", "");
				if (!Settings.ChatSettings.EXEMPT_WORDS.contains(subNoSpaces)) {
					if (getSimilarityPercentage(subNoSpaces, blockedWord) >= SIMILARITY_THRESHOLD_LANGUAGE) {
						blockAndNotify(player, "&cPlease refrain from using inappropriate language in chat. &4(\"" + message + "\")", message, true);
						blocked = true;
						break;
					}

					// TODO: Fix substituted advertising bypass.
					for (final String word : words) {
						Common.log(getSimilarityPercentage(word, "dot") + "");
						if (getSimilarityPercentage(word, "dot") >= Settings.ChatSettings.SIMILARITY_THRESHOLD_ADVERTISING) {
							addressContainter = substitutedMessage.replace(word, ".").replace(" ", "");
							cleanMessage = addressContainter;
						}

						if (getSimilarityPercentage(word, blockedWord) >= SIMILARITY_THRESHOLD_LANGUAGE || word.contains(blockedWord)) {
							blockAndNotify(player, "&cPlease refrain from using inappropriate language in chat. &4(\"" + message + "\")", message, true);
							blocked = true;
							break;
						}
					}
				}

				for (final String phrase : alphabeticalSet) {
					if (getSimilarityPercentage(phrase, "dot") >= Settings.ChatSettings.SIMILARITY_THRESHOLD_ADVERTISING) {
						addressContainter = substitutedMessage.replace(phrase, ".").replace(" ", "");
						cleanMessage = addressContainter;
					}

					if (getSimilarityPercentage(phrase, blockedWord) >= SIMILARITY_THRESHOLD_LANGUAGE && !Settings.ChatSettings.EXEMPT_WORDS.contains(phrase)) {
						blockAndNotify(player, "&cPlease refrain from using inappropriate language in chat. &4(\"" + message + "\")", message, true);
						blocked = true;
						break;
					}
				}
			}

			// TODO: Add comparison system for domain names.
			if (ChatUtil.isDomain(message)
					|| cleanMessage.contains(".net")
					|| cleanMessage.contains(".com")
					|| cleanMessage.contains(".org")
					|| cleanMessage.contains(".io")
					|| cleanMessage.contains(".gg")
					|| cleanMessage.contains(".co")
					|| cleanMessage.contains(".xyz")
					|| cleanMessage.matches(addressRegex)) {
				blocked = true;
				blockAndNotify(player, "&cPlease refrain from posting IPs or links in chat. &4(\"" + message + "\")", message, true);
			}

			// count the number of capital letters in the message
			int capitalCount = 0;
			for (int i = 0; i < message.length(); i++) {
				if (Character.isUpperCase(message.charAt(i))) {
					capitalCount++;
				}
			}

			// Blocks excessive capitalization.
			if (ChatUtil.getCapsPercentage(message) > Settings.ChatSettings.CAPS_THRESHOLD && capitalCount > 6) {
				blockAndNotify(player, "&cPlease limit the number of capital letters in your message. &4(\"" + message + "\")", message, true);
				event.setMessage(message.toLowerCase());
			}

			if (blocked)
				event.setCancelled(true);
		}*/

		// -----------------------------------------------------------------------------
		// Mention System
		// -----------------------------------------------------------------------------

		Remain.getOnlinePlayers().forEach(loopPlayer -> {

			final PlayerData cache = PlayerData.from(loopPlayer);
			final String playerName = loopPlayer.getName();

			event.setCancelled(true);
			if (message.toLowerCase().contains("@" + playerName.toLowerCase())
					&& !loopPlayer.hasPermission(Settings.ChatSettings.MENTION_IGNORE_PERMISSION)
					&& cache.isMentionsStatus()) {

				Common.tellNoPrefix(loopPlayer, String.format(event.getFormat(), eventPlayerName, message)
						.replace("@" + playerName, Settings.ChatSettings.MENTION_COLOR) + playerName + previousColor);

				Common.log(previousColor);

				if (cache.isMentionToastStatus())
					Common.dispatchCommand(loopPlayer, "bu notify {player} toast PAPER " + Localization.NotificationMessages.MENTIONED_MESSAGE);

				if (cache.isMentionSoundStatus())
					new SimpleSound(mentionSound.getSound(), mentionSound.getVolume(), mentionSound.getPitch()).play(loopPlayer);

				return;
			}

			Common.tellNoPrefix(loopPlayer, String.format(event.getFormat(), eventPlayerName, message));

		});
	}

}
