package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.PlayerCache;
import me.markings.bubble.settings.MenuSettings;
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
		setTitle(MenuSettings.getInstance().getMenuTitle());
		setSize(MenuSettings.getInstance().getMenuSize());

		chatSettingsButton = new ButtonMenu(new ChatSettingsMenu(), MenuSettings.getInstance().getChatSettingsButtonMaterial(),
				MenuSettings.getInstance().getChatSettingsButtonTitle(), MenuSettings.getInstance().getChatSettingsButtonLore());

		motdSettingsButton = new ButtonMenu(new MOTDSettingsMenu(), MenuSettings.getInstance().getMotdSettingsButtonMaterial(),
				MenuSettings.getInstance().getMotdSettingsButtonTitle(), MenuSettings.getInstance().getMotdSettingsButtonLore());

		mentionsSettingsButton = new ButtonMenu(new MentionsSettingsMenu(), MenuSettings.getInstance().getMentionsSettingsButtonMaterial(),
				MenuSettings.getInstance().getMentionsSettingsButtonTitle(), MenuSettings.getInstance().getMentionsSettingsButtonLore());
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
		private final Button toggleBroadcastSoundButton;

		private ChatSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&eChat Settings");
			setSize(9 * 3);

			toggleBroadcastsButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setBroadcastStatus(!cache.isBroadcastStatus());
					restartMenu(cache.isBroadcastStatus() ? "&aBroadcasts ENABLED!" : "&cBroadcasts DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(cache.isBroadcastStatus() ? CompMaterial.LIME_DYE : CompMaterial.GRAY_DYE,
							"&7Broadcasts: " + (cache.isBroadcastStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ebroadcasts that are displayed to you.").build().make();
				}
			};

			toggleBroadcastSoundButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setBroadcastSoundStatus(!cache.isBroadcastSoundStatus());
					restartMenu(cache.isBroadcastSoundStatus() ? "&aBroadcast Sound ENABLED!" : "&cBroadcast Sound DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(CompMaterial.MUSIC_DISC_13,
							"&7Broadcast Sound: " + (cache.isBroadcastSoundStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ethe sound played to you when you",
							"&ereceive a broadcast message.").build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return switch (slot) {
				case 9 + 2 -> toggleBroadcastsButton.getItem();
				case 9 + 6 -> toggleBroadcastSoundButton.getItem();
				default -> null;
			};
		}
	}

	public class MOTDSettingsMenu extends Menu {

		private final Button toggleMOTDButton;

		public MOTDSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&dMOTD Settings");
			setSize(9 * 3);

			toggleMOTDButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setMotdStatus(!cache.isMotdStatus());
					restartMenu(cache.isMotdStatus() ? "&aMOTD ENABLED!" : "&cMOTD DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
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
		private final Button toggleMentionSoundButton;
		private final Button toggleMentionToastButton;

		public MentionsSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle("&2Mentions Settings");
			setSize(9 * 3);

			toggleMentionsButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setMentionsStatus(!cache.isMentionsStatus());
					restartMenu(cache.isMentionsStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(CompMaterial.NAME_TAG,
							"&7Chat Mentions: " + (cache.isMentionsStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&enotifications you receive when your",
							"&ename is mentioned in the chat.").build().make();
				}
			};

			toggleMentionSoundButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setMentionSoundStatus(!cache.isMentionSoundStatus());
					restartMenu(cache.isMentionSoundStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(CompMaterial.NOTE_BLOCK,
							"&7Mention Sound: " + (cache.isMentionSoundStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ethe sound played to you when your",
							"&ename is mentioned in the chat.").build().make();
				}
			};

			toggleMentionToastButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setMentionToastStatus(!cache.isMentionToastStatus());
					restartMenu(cache.isMentionToastStatus() ? "&aMention Toast ENABLED!" : "&cMention Toast DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(CompMaterial.BREAD,
							"&7Mention Toast: " + (cache.isMentionToastStatus() ? enabledText : disabledText),
							"",
							clickToToggleMessage,
							"&ethe achievement popup received when",
							"&eyour name is mentioned in the chat.").build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return switch (slot) {
				case 9 + 2 -> toggleMentionsButton.getItem();
				case 9 + 4 -> toggleMentionSoundButton.getItem();
				case 9 + 6 -> toggleMentionToastButton.getItem();
				default -> null;
			};
		}
	}
}
