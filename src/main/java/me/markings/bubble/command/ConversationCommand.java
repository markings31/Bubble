package me.markings.bubble.command;

import me.markings.bubble.conversation.EditConversation;
import org.mineacademy.fo.command.SimpleCommand;

public class ConversationCommand extends SimpleCommand {

	public ConversationCommand() {
		super("editconvo");
	}

	@Override
	protected void onCommand() {
		new EditConversation().show(getPlayer());
	}
}
