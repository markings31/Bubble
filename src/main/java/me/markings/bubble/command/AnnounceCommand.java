package me.markings.bubble.command;

import lombok.val;
import me.markings.bubble.model.Permissions;
import me.markings.bubble.util.MessageUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.List;

import static org.mineacademy.fo.Common.joinRange;

@AutoRegister
public final class AnnounceCommand extends SimpleCommand {

	public AnnounceCommand() {
		super("announce|an|a");

		setMinArguments(2);
		setDescription("Announce the given message to the server.");
		setUsage("<message/title/bossbar/actionbar/toast> [material] <input|...>");
		setPermission(Permissions.Command.ANNOUNCE);
	}

	@Override
	protected void onCommand() {
		val input = joinRange((args[0].equals(MessageUtil.getToastArg()) ? 2 : 1), args);
		if (args[0].equals(MessageUtil.getToastArg()))
			Common.dispatchCommandAsPlayer(getPlayer(), "bu notify all " + args[0] + " " + args[1] + " " + input);
		else
			Common.dispatchCommandAsPlayer(getPlayer(), "bu notify all " + args[0] + " " + input);
	}

	@Override
	protected List<String> tabComplete() {
		if (args.length == 1)
			return completeLastWord(MessageUtil.getMessageArg(), MessageUtil.getTitleArg(),
					MessageUtil.getActionbarArg(), MessageUtil.getBossbarArg(), MessageUtil.getToastArg());
		if (args.length == 2 && args[0].equalsIgnoreCase(MessageUtil.getToastArg()))
			return completeLastWord(CompMaterial.values());

		return new ArrayList<>();
	}
}
