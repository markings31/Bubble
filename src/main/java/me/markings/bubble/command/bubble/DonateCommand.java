package me.markings.bubble.command.bubble;

import lombok.val;
import me.markings.bubble.util.ConfigUtil;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleSubCommand;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.remain.Remain;

import java.io.IOException;

public class DonateCommand extends SimpleSubCommand {

	protected DonateCommand() {
		super("donate");

		setMinArguments(1);
		setUsage("<player> [package_name]");
	}

	@Override
	protected void onCommand() {
		val target = findPlayer(args[0]);

		try {
			ConfigUtil.saveFromURL(target);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		val degreeStep = 2;
		val circleRadius = 3;

		val location = target.getLocation();

		for (int degree = 0; degree < 360; degree += degreeStep) {
			val radians = Math.toRadians(degree);

			val x = Math.cos(radians) * circleRadius;
			val z = Math.sin(radians) * circleRadius;

			if (location.getY() + z < location.getY())
				continue;

			CompParticle.VILLAGER_HAPPY.spawn(location.clone().add(x, z, 0));
		}

		CompSound.LEVEL_UP.play(target);
		Remain.sendTitle(target, "&9&lThank you for donating!", "&bEnjoy your new perks! :)");
		Common.dispatchCommandAsPlayer(
				getPlayer(), "bu notify " + target.getName() + " message " + target.getUniqueId() +
						".png 8 |&9&lThank you for donating!||&bYour donation allows us to improve the|" +
						"&bserver and add new and exciting features.||&bWe hope you enjoy your new perks! -c");
	}
}
