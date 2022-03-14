package me.markings.bubble.command.bubble;

import org.mineacademy.fo.ChatUtil;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.CompChatColor;

public class TestCommand extends SimpleSubCommand {

	protected TestCommand() {
		super("test");
	}

	@Override
	protected void onCommand() {
		tell(ChatUtil.generateGradient("Test Message", CompChatColor.YELLOW, CompChatColor.DARK_RED));
	}
}
