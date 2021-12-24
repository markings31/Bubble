package me.markings.bubble.command.bubble;

import lombok.Getter;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.menu.EditMenu;
import me.markings.bubble.model.Permissions;
import org.mineacademy.fo.command.SimpleSubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditCommand extends SimpleSubCommand {

	@Getter
	public static String input;

	protected EditCommand() {
		super("edit");

		setMinArguments(1);
		setUsage("<broadcast_label>");
		setPermission(Permissions.Command.EDIT);
	}

	@Override
	protected void onCommand() {
		val newSection = "Notifications.Broadcast.Messages." + args[0];

		input = args[0];

		checkBoolean(Bubble.config.isSet(newSection), "&cNo such section " + args[0] + " found!");

		new EditMenu().displayTo(getPlayer());
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(Objects.requireNonNull(Bubble.config.getConfigurationSection("Notifications.Broadcast.Messages")).getValues(false).keySet());

		return new ArrayList<>();
	}
}
