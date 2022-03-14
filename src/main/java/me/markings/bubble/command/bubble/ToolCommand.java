package me.markings.bubble.command.bubble;

import me.markings.bubble.tool.RegionSelectTool;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.command.SimpleSubCommand;

public class ToolCommand extends SimpleSubCommand {

	protected ToolCommand() {
		super("tool");
	}

	@Override
	protected void onCommand() {
		checkConsole();
		PlayerUtil.addItems(getPlayer().getInventory(), RegionSelectTool.getInstance().getItem());
	}
}
