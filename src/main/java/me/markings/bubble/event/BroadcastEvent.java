package me.markings.bubble.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.mineacademy.fo.model.SimpleSound;

@Getter
@Setter
public final class BroadcastEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private String message;
	private SimpleSound sound;

	public BroadcastEvent(final String message, final SimpleSound sound) {
		this.message = message;
		this.sound = sound;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void setCancelled(final boolean b) {
	}

	@NotNull
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@NotNull
	public static HandlerList getHandlerList() {
		return handlers;
	}
}