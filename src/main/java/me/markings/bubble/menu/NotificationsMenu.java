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

public class NotificationsMenu extends Menu {

	private final Button chatSettingsButton;
	private final Button motdSettingsButton;
	private final Button mentionsSettingsButton;

	private static final MenuSettings menuSettings = MenuSettings.getInstance();

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
		if (slot == menuSettings.getBroadcastStatusButtonSlot())
			return chatSettingsButton.getItem();
		else if (slot == menuSettings.getMotdSettingsButtonSlot())
			return motdSettingsButton.getItem();
		else if (slot == menuSettings.getMentionsSettingsButtonSlot())
			return mentionsSettingsButton.getItem();

		return null;
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

			setTitle(menuSettings.getChatMenuTitle());
			setSize(menuSettings.getChatMenuSize());

			toggleBroadcastsButton = new Button() {
				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					val cache = PlayerCache.getCache(getViewer());
					cache.setBroadcastStatus(!cache.isBroadcastStatus());
					restartMenu(cache.isBroadcastStatus() ? "&aBroadcasts ENABLED" : "&cBroadcasts DISABLED");
				}

				@Override
				public ItemStack getItem() {
					val cache = PlayerCache.getCache(getViewer());
					return ItemCreator.of(cache.isBroadcastStatus() ? menuSettings.getBroadcastsEnabledButtonMaterial()
									: menuSettings.getBroadcastsDisabledButtonMaterial(),
							(cache.isBroadcastStatus() ? menuSettings.getBroadcastsEnabledButtonTitle()
									: menuSettings.getBroadcastsDisabledButtonTitle()),
							menuSettings.getBroadcastStatusButtonLore()).build().make();
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
					return ItemCreator.of(menuSettings.getBroadcastSoundButtonMaterial(),
							(cache.isBroadcastSoundStatus() ? menuSettings.getBroadcastSoundEnabledButtonTitle()
									: menuSettings.getBroadcastSoundDisabledButtonTitle()),
							menuSettings.getBroadcastSoundButtonLore()).build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			if (slot == menuSettings.getBroadcastStatusButtonSlot())
				return toggleBroadcastsButton.getItem();
			else if (slot == menuSettings.getBroadcastSoundButtonSlot())
				return toggleBroadcastSoundButton.getItem();

			return null;
		}
	}

	public class MOTDSettingsMenu extends Menu {

		private final Button toggleMOTDButton;

		public MOTDSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle(menuSettings.getMotdMenuTitle());
			setSize(menuSettings.getMotdMenuSize());

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
					return ItemCreator.of(menuSettings.getMotdStatusButtonMaterial(),
							(cache.isMotdStatus() ? menuSettings.getMotdEnabledButtonTitle()
									: menuSettings.getMotdDisabledButtonTitle()),
							menuSettings.getMotdStatusButtonLore()).build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return slot == menuSettings.getMotdStatusButtonSlot() ? toggleMOTDButton.getItem() : null;
		}
	}

	public class MentionsSettingsMenu extends Menu {

		private final Button toggleMentionsButton;
		private final Button toggleMentionSoundButton;
		private final Button toggleMentionToastButton;

		public MentionsSettingsMenu() {
			super(NotificationsMenu.this);

			setTitle(menuSettings.getMentionMenuTitle());
			setSize(menuSettings.getMentionMenuSize());

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
					return ItemCreator.of(menuSettings.getMentionsStatusButtonMaterial(),
							(cache.isMentionsStatus() ? menuSettings.getMentionsEnabledButtonTitle()
									: menuSettings.getMentionsDisabledButtonTitle()), menuSettings.getMentionsStatusButtonLore()).build().make();
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
					return ItemCreator.of(menuSettings.getMentionSoundStatusButtonMaterial(),
							(cache.isMentionSoundStatus() ? menuSettings.getMentionSoundEnabledButtonTitle()
									: menuSettings.getMentionSoundDisabledButtonTitle()), menuSettings.getBroadcastSoundButtonLore()).build().make();
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
					return ItemCreator.of(menuSettings.getMentionToastStatusButtonMaterial(),
							(cache.isMentionToastStatus() ? menuSettings.getMentionToastEnabledButtonTitle()
									: menuSettings.getMentionToastDisabledButtonTitle()),
							menuSettings.getMentionToastStatusButtonLore()).build().make();
				}
			};
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			if (slot == menuSettings.getMentionsStatusButtonSlot())
				return toggleMentionsButton.getItem();
			else if (slot == menuSettings.getMentionSoundStatusButtonSlot())
				return toggleMentionSoundButton.getItem();
			else if (slot == menuSettings.getMentionToastStatusButtonSlot())
				return toggleMentionToastButton.getItem();

			return null;
		}
	}
}
