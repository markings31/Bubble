package me.markings.bubble.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.markings.bubble.PlayerCache;
import org.bukkit.entity.Player;

/**
 * Main API class for the Bubble plugin.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BubbleAPI {

	/**
	 * Returns the {@link Cache} for the given player.
	 *
	 * @param player
	 * @return the player's cache information
	 */
	public static Cache getCache(final Player player) {
		return PlayerCache.getCache(player);
	}

}
