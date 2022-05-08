package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.model.ChatPaginator;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShowCommand extends SimpleSubCommand {

	private int messagesLength = 1;
	private static final String path = "Notifications.Broadcast.Messages";

	protected ShowCommand() {
		super("show");

		setDescription("Display a preview of the given broadcast message.");
		setPermission(Permissions.Command.SHOW);
	}

	@Override
	protected void onCommand() {
		checkConsole();
		val page = new ChatPaginator(maximumLength() + 6, ChatColor.BLUE);

		page.setHeader("&9" + Common.chatLineSmooth() + "&r", ChatUtil.center("&3&l&nInformation&r"), "&f");
		page.setPages(list()).send(getPlayer());
	}

	private int maximumLength() {
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val labels = Objects.requireNonNull(config.getConfigurationSection(path)).getValues(false).keySet();

		IntStream.range(0, labels.size()).mapToObj(i -> (String) labels.toArray()[i]).forEachOrdered(currentLabel -> {
			val pathLabel = path + "." + currentLabel;
			val messageList = config.getStringList(pathLabel + ".Message");
			messageList.stream().filter(message -> messageList.size() > messagesLength).forEachOrdered(message -> messagesLength = messageList.size());
		});

		return messagesLength;
	}

	private List<SimpleComponent> list() {
		final List<SimpleComponent> messages = new ArrayList<>();
		val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
		val labels = Objects.requireNonNull(config.getConfigurationSection(path)).getValues(false).keySet();

		IntStream.range(0, labels.size()).mapToObj(i -> (String) labels.toArray()[i]).forEachOrdered(currentLabel -> {
			val pathLabel = path + "." + currentLabel;
			val messageList = config.getStringList(pathLabel + ".Message");

			if (messageList.size() > messagesLength)
				messagesLength = messageList.size();

			Stream.of(
					"&7- &b&lLabel: &f" + currentLabel,
					"&7- &b&lPermission: &f" + config.getString(pathLabel + ".Permission"),
					"&7- &b&lCentered: &f" + (Settings.BroadcastSettings.CENTER_ALL || config.getBoolean(pathLabel + ".Centered")),
					"&f",
					"&b&lMessage:",
					"&f").map(SimpleComponent::of).forEach(messages::add);

			messageList.forEach(message -> messages.add(SimpleComponent.of(MessageUtil.format(message))));
			messages.add(SimpleComponent.of("&f"));
		});

		return messages;
	}
}
