package me.markings.bubble.api;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.markings.bubble.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.Remain;

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
		return PlayerData.getCache(player);
	}

	/**
	 * Sends a title message to the given player.
	 *
	 * @param player
	 * @param title
	 * @param subtitle
	 */
	public static void sendTitle(final Player player, final String title, final String subtitle) {
		Remain.sendTitle(player, title, subtitle);
	}

	/**
	 * Sends an actionbar message to the given player.
	 *
	 * @param player
	 * @param text
	 */
	public static void sendActionBar(final Player player, final String text) {
		Remain.sendActionBar(player, text);
	}

	/**
	 * Sends a bossbar message to the given player.
	 *
	 * @param player
	 * @param text
	 * @param percentFilled
	 */
	public static void sendBossbar(final Player player, final String text, final int percentFilled) {
		Remain.sendBossbarPercent(player, text, percentFilled);
	}

	/**
	 * Sends a toast message to the given player.
	 *
	 * @param player
	 * @param text
	 * @param material
	 */
	public static void sendToast(final Player player, final String text, final Material material) {
		Remain.sendToast(player, text, CompMaterial.fromMaterial(material));
	}

}
