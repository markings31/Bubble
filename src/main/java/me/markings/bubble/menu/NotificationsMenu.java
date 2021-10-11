package me.markings.bubble.menu;

import me.markings.bubble.PlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class NotificationsMenu extends Menu {

	private static final String clickToToggleMessage = "&eClick this item to toggle on/off";
	private static final String enabledText = "&aENABLED";
	private static final String disabledText = "&cDISABLED";

	private final Button chatSettingsButton;
	private final Button motdSettingsButton;
	private final Button mentionsSettingsButton;

	public NotificationsMenu() {
		setTitle("&6Notification Preferences");
		setSize(9 * 3);

		chatSettingsButton = new ButtonMenu(new ChatSettingsMenu(), CompMaterial.PAPER,
				"&eChat Settings",
				"",
				"Click here to access the chat",
				"settings menu.");

		motdSettingsButton = new ButtonMenu(new MOTDSettingsMenu(), CompMaterial.DIAMOND,
				"&eMOTD Settings",
				"",
				"Click here to access the MOTD",
				"settings menu.");

		mentionsSettingsButton = new ButtonMenu(new MentionsSettingsMenu(), CompMaterial.SUNFLOWER,
				"&eMentions Settings",
				"",
				"Click here to access the mentions",
				"settings menu.");
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		return switch (slot) {
			case 9 + 2 -> chatSettingsButton.getItem();
			case 9 + 4 -> motdSettingsButton.getItem();
			case 9 + 6 -> mentionsSettingsButton.getItem();
			default -> null;
		};
	}

	private final class ChatSettingsMenu extends Menu {

		private final Button toggleBroadcastsButton;

		private ChatSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&eChat Settings");
			setSize(9 * 3);

			toggleBroadcastsButton = new Button() {
				final PlayerCache cache = PlayerCache.getCache(getViewer());

				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					cache.setBroadcastStatus(!cache.isBroadcastStatus());
					restartMenu(cache.isBroadcastStatus() ? "&aBroadcasts ENABLED!" : "&cBroadcasts DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(cache.isBroadcastStatus() ? CompMaterial.LIME_DYE : CompMaterial.GRAY_DYE,
							"&7Broadcasts: " + (cache.isBroadcastStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ebroadcasts that are displayed to you.").build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return slot == 9 + 4 ? toggleBroadcastsButton.getItem() : null;
		}
	}

	public class MOTDSettingsMenu extends Menu {

		private final Button toggleMOTDButton;

		public MOTDSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&dMOTD Settings");
			setSize(9 * 3);

			toggleMOTDButton = new Button() {
				final PlayerCache cache = PlayerCache.getCache(getViewer());

				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					cache.setMotdStatus(!cache.isMotdStatus());
					restartMenu(cache.isMotdStatus() ? "&aMOTD ENABLED!" : "&cMOTD DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.WRITABLE_BOOK,
							"&7Message of the Day: " + (cache.isMotdStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ethe message sent to you when you join",
							"&ethe server.").build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return slot == 9 + 4 ? toggleMOTDButton.getItem() : null;
		}
	}

	public class MentionsSettingsMenu extends Menu {

		private final Button toggleMentionsButton;

		public MentionsSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&2Mentions Settings");
			setSize(9 * 3);

			toggleMentionsButton = new Button() {
				final PlayerCache cache = PlayerCache.getCache(getViewer());

				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					cache.setMentionsStatus(!cache.isMentionsStatus());
					restartMenu(cache.isMentionsStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(CompMaterial.NAME_TAG,
							"&7Chat Mentions: " + (cache.isMentionsStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&enotifications you receive when your",
							"&ename is mentioned in the chat.").build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return slot == 9 + 4 ? toggleMentionsButton.getItem() : null;
		}
	}
}