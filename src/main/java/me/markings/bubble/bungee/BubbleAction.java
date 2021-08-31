package me.markings.bubble.bungee;

import lombok.Getter;
import org.mineacademy.fo.bungee.BungeeAction;

public enum BubbleAction implements BungeeAction {

	CHAT_MESSAGE(String.class, String.class);

	@Getter
	private final Class<?>[] content;

	BubbleAction(final Class<?>... content) {
		this.content = content;
	}
}
