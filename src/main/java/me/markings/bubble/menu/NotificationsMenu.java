package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class NotificationsMenu extends Menu {

	private static final String clickToToggleMessage = "&eClick this item to toggle on/off";
	private static final String enabledText = "&aENABLED";
	private static final String disabledText = "&cDISABLED";

	private final Button toggleBroadcastsButton;
	private final Button toggleMOTDButton;
	private final Button toggleMentionsButton;

	public NotificationsMenu() {
		setTitle("&6Notification Preferences");

		setSize(9 * 3);

		toggleBroadcastsButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				cache.setBroadcastStatus(!cache.getBroadcastStatus());
				restartMenu(cache.getBroadcastStatus() ? "&aBroadcasts ENABLED!" : "&cBroadcasts DISABLED!");
			}

			@Override
			public ItemStack getItem() {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				return ItemCreator.of(cache.getBroadcastStatus() ? CompMaterial.LIME_DYE : CompMaterial.GRAY_DYE,
						"&7Broadcasts: " + (cache.getBroadcastStatus() ? enabledText : disabledText),
						"",
						clickToToggleMessage,
						"&ebroadcasts that are displayed to you.").build().make();
			}
		};

		toggleMOTDButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				cache.setMOTDStatus(!cache.getMOTDStatus());
				restartMenu(cache.getMOTDStatus() ? "&aMOTD ENABLED!" : "&cMOTD DISABLED!");
			}

			@Override
			public ItemStack getItem() {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				return ItemCreator.of(CompMaterial.WRITABLE_BOOK,
						"&7Message of the Day: " + (cache.getMOTDStatus() ? enabledText : disabledText),
						"",
						clickToToggleMessage,
						"&ethe message sent to you when you join",
						"&ethe server.").build().make();
			}
		};

		toggleMentionsButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				cache.setMentionsStatus(!cache.getMentionsStatus());
				restartMenu(cache.getMentionsStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
			}

			@Override
			public ItemStack getItem() {
				val cache = PlayerCache.getCache(getViewer().getUniqueId());
				return ItemCreator.of(CompMaterial.NAME_TAG,
						"&7Chat Mentions: " + (cache.getMentionsStatus() ? enabledText : disabledText),
						"",
						clickToToggleMessage,
						"&enotifications you receive when your",
						"&ename is mentioned in the chat.").build().make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		return switch (slot) {
			case 9 * 1 + 4 -> toggleBroadcastsButton.getItem();
			case 9 * 1 + 2 -> toggleMOTDButton.getItem();
			case 9 * 1 + 6 -> toggleMentionsButton.getItem();
			default -> null;
		};
	}
}
