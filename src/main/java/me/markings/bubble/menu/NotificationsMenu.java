package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.PlayerData;
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

	private final Button chatSettingsButton;
	private final Button motdSettingsButton;
	private final Button mentionsSettingsButton;

	private static final MenuSettings.PreferencesMenuSettings menuSettings = MenuSettings.PreferencesMenuSettings.getInstance();
	private static final MenuSettings.ChatMenuSettings CHAT_MENU_SETTINGS = MenuSettings.ChatMenuSettings.getInstance();
	private static final MenuSettings.MOTDMenuSettings MOTD_MENU_SETTINGS = MenuSettings.MOTDMenuSettings.getInstance();
	private static final MenuSettings.MentionsMenuSettings MENTIONS_MENU_SETTINGS = MenuSettings.MentionsMenuSettings.getInstance();

	public NotificationsMenu() {
		setTitle(menuSettings.getPrefMenuTitle());
		setSize(menuSettings.getPrefMenuSize());

		chatSettingsButton = new ButtonMenu(new ChatSettingsMenu(), menuSettings.getChatSettingsButtonMaterial(),
				menuSettings.getChatSettingsButtonTitle(), menuSettings.getChatSettingsButtonLore());

		motdSettingsButton = new ButtonMenu(new MOTDSettingsMenu(), menuSettings.getMotdSettingsButtonMaterial(),
				menuSettings.getMotdSettingsButtonTitle(), menuSettings.getMotdSettingsButtonLore());

		mentionsSettingsButton = new ButtonMenu(new MentionsSettingsMenu(), menuSettings.getMentionsSettingsButtonMaterial(),
				menuSettings.getMentionsSettingsButtonTitle(), menuSettings.getMentionsSettingsButtonLore());
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		if (slot == CHAT_MENU_SETTINGS.getBroadcastStatusButtonSlot())
			return chatSettingsButton.getItem();
		else if (slot == menuSettings.getMotdSettingsButtonSlot())
			return motdSettingsButton.getItem();
		else if (slot == menuSettings.getMentionsSettingsButtonSlot())
			return mentionsSettingsButton.getItem();

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
	}

	@Override
	protected String[] getInfo() {
		return new String[]{
				"&6Use this menu to configure your",
				"&6notification preferences!"
		};
	}

	private final class ChatSettingsMenu extends Menu {

		private final Button toggleBroadcastsButton;
		private final Button toggleBroadcastSoundButton;

		private ChatSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle(CHAT_MENU_SETTINGS.getChatMenuTitle());
			setSize(CHAT_MENU_SETTINGS.getChatMenuSize());

			toggleBroadcastsButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setBroadcastStatus(!cache.isBroadcastStatus());
					restartMenu(cache.isBroadcastStatus() ? "&aBroadcasts ENABLED" : "&cBroadcasts DISABLED");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(cache.isBroadcastStatus() ? CHAT_MENU_SETTINGS.getBroadcastsEnabledButtonMaterial()
									: CHAT_MENU_SETTINGS.getBroadcastsDisabledButtonMaterial(),
							(cache.isBroadcastStatus() ? CHAT_MENU_SETTINGS.getBroadcastsEnabledButtonTitle()
									: CHAT_MENU_SETTINGS.getBroadcastsDisabledButtonTitle()),
							CHAT_MENU_SETTINGS.getBroadcastStatusButtonLore()).make();
				}
			};

			toggleBroadcastSoundButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setBroadcastSoundStatus(!cache.isBroadcastSoundStatus());
					restartMenu(cache.isBroadcastSoundStatus() ? "&aBroadcast Sound ENABLED!" : "&cBroadcast Sound DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(CHAT_MENU_SETTINGS.getBroadcastSoundButtonMaterial(),
							(cache.isBroadcastSoundStatus() ? CHAT_MENU_SETTINGS.getBroadcastSoundEnabledButtonTitle()
									: CHAT_MENU_SETTINGS.getBroadcastSoundDisabledButtonTitle()),
							CHAT_MENU_SETTINGS.getBroadcastSoundButtonLore()).make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			if (slot == CHAT_MENU_SETTINGS.getBroadcastStatusButtonSlot())
				return toggleBroadcastsButton.getItem();
			else if (slot == CHAT_MENU_SETTINGS.getBroadcastSoundButtonSlot())
				return toggleBroadcastSoundButton.getItem();

			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
		}
	}

	// TODO: Update MOTD settings options.
	public class MOTDSettingsMenu extends Menu {

		private final Button toggleMOTDButton;

		public MOTDSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle(MOTD_MENU_SETTINGS.getMotdMenuTitle());
			setSize(MOTD_MENU_SETTINGS.getMotdMenuSize());

			toggleMOTDButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setMotdStatus(!cache.isMotdStatus());
					restartMenu(cache.isMotdStatus() ? "&aMOTD ENABLED!" : "&cMOTD DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(MOTD_MENU_SETTINGS.getMotdStatusButtonMaterial(),
							(cache.isMotdStatus() ? MOTD_MENU_SETTINGS.getMotdEnabledButtonTitle()
									: MOTD_MENU_SETTINGS.getMotdDisabledButtonTitle()),
							MOTD_MENU_SETTINGS.getMotdStatusButtonLore()).make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return slot == MOTD_MENU_SETTINGS.getMotdStatusButtonSlot() ? toggleMOTDButton.getItem() : ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();

		}
	}

	public class MentionsSettingsMenu extends Menu {

		private final Button toggleMentionsButton;
		private final Button toggleMentionSoundButton;
		private final Button toggleMentionToastButton;

		public MentionsSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle(MENTIONS_MENU_SETTINGS.getMentionMenuTitle());
			setSize(MENTIONS_MENU_SETTINGS.getMentionMenuSize());

			toggleMentionsButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setMentionsStatus(!cache.isMentionsStatus());
					restartMenu(cache.isMentionsStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(MENTIONS_MENU_SETTINGS.getMentionsStatusButtonMaterial(),
							(cache.isMentionsStatus() ? MENTIONS_MENU_SETTINGS.getMentionsEnabledButtonTitle()
									: MENTIONS_MENU_SETTINGS.getMentionsDisabledButtonTitle()), MENTIONS_MENU_SETTINGS.getMentionsStatusButtonLore()).make();
				}
			};

			toggleMentionSoundButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setMentionSoundStatus(!cache.isMentionSoundStatus());
					restartMenu(cache.isMentionSoundStatus() ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(MENTIONS_MENU_SETTINGS.getMentionSoundStatusButtonMaterial(),
							(cache.isMentionSoundStatus() ? MENTIONS_MENU_SETTINGS.getMentionSoundEnabledButtonTitle()
									: MENTIONS_MENU_SETTINGS.getMentionSoundDisabledButtonTitle()), MENTIONS_MENU_SETTINGS.getMentionSoundStatusButtonLore()).make();
				}
			};

			toggleMentionToastButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerData.getCache(getViewer());
					cache.setMentionToastStatus(!cache.isMentionToastStatus());
					restartMenu(cache.isMentionToastStatus() ? "&aMention Toast ENABLED!" : "&cMention Toast DISABLED!");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerData.getCache(getViewer());
					return ItemCreator.of(MENTIONS_MENU_SETTINGS.getMentionToastStatusButtonMaterial(),
							(cache.isMentionToastStatus() ? MENTIONS_MENU_SETTINGS.getMentionToastEnabledButtonTitle()
									: MENTIONS_MENU_SETTINGS.getMentionToastDisabledButtonTitle()),
							MENTIONS_MENU_SETTINGS.getMentionToastStatusButtonLore()).make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			if (slot == MENTIONS_MENU_SETTINGS.getMentionsStatusButtonSlot())
				return toggleMentionsButton.getItem();
			else if (slot == MENTIONS_MENU_SETTINGS.getMentionSoundStatusButtonSlot())
				return toggleMentionSoundButton.getItem();
			else if (slot == MENTIONS_MENU_SETTINGS.getMentionToastStatusButtonSlot())
				return toggleMentionToastButton.getItem();

			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
		}
	}
}
