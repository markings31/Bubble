package me.markings.bubble;

import me.markings.bubble.commands.bubble.BubbleGroup;
import me.markings.bubble.commands.tasks.BroadcastTask;
import me.markings.bubble.settings.Settings;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.HookManager;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Collections;
import java.util.List;

public final class Bubble extends SimplePlugin {

	private final SimpleCommandGroup commandGroup = new BubbleGroup();

	private BroadcastTask broadcastTask;

	@Override
	protected void onPluginStart() {
		if (HookManager.isPlaceholderAPILoaded())
			Common.log("[" + SimplePlugin.getNamed() + "] Successfully hooked onto PlaceholderAPI.");

		Common.setTellPrefix("&8[&b&lBubble&8]&7 ");

		Common.log("[" + SimplePlugin.getNamed() + "] Bubble has been successfully enabled.");

		startBroadcastTask();
	}

	@Override
	protected void onPluginStop() {
		cleanBeforeReload();
	}

	@Override
	protected void onPluginReload() {
		startBroadcastTask();
	}

	private void cleanBeforeReload() {
		stopTaskIfRunning(broadcastTask);
	}

	private void stopTaskIfRunning(BukkitRunnable task) {
		if (task != null) try {
			task.cancel();
		} catch (IllegalStateException ignored) {
		}
	}

	private void startBroadcastTask() {
		broadcastTask = new BroadcastTask();
		broadcastTask.runTaskTimerAsynchronously(this, 0, Settings.BroadcastSettings.BROADCAST_DELAY.getTimeTicks());
	}

	@Override
	public List<Class<? extends YamlStaticConfig>> getSettings() {
		return Collections.singletonList(Settings.class);
	}

	@Override
	public SimpleCommandGroup getMainCommand() {
		return commandGroup;
	}
}
