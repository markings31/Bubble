package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.conversation.EditConversation;
import me.markings.bubble.conversation.PermissionConversation;
import me.markings.bubble.settings.MenuSettings;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;

public class EditMenu extends Menu {

	private final Button editMessageButton;
	private final Button centerMessageButton;
	private final Button changePermissionButton;

	private static final MenuSettings.EditMenuSettings editMenuSettings = MenuSettings.EditMenuSettings.getInstance();

	public EditMenu() {
		setTitle(editMenuSettings.getEditMenuTitle());
		setSize(9 * 3);

		editMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				new EditConversation().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getEditMessageButtonMaterial(), editMenuSettings.getEditMessageButtonTitle()).build().make();
			}
		};

		centerMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val path = EditCommand.getInput();
				val centerPath = "Notifications.Broadcast.Messages." + path + ".Centered";
				ConfigUtil.toggleCentered(centerPath, player);
				player.closeInventory();
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getCenterMessageButtonMaterial(), editMenuSettings.getCenterMessageButtonTitle()).build().make();
			}
		};

		changePermissionButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				new PermissionConversation().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getChangePermissionButtonMaterial(), editMenuSettings.getChangePermissionButtonTitle()).build().make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		if (slot == editMenuSettings.getEditMessageButtonSlot())
			return editMessageButton.getItem();
		if (slot == editMenuSettings.getCenterMessageButtonSlot())
			return changePermissionButton.getItem();
		if (slot == editMenuSettings.getChangePermissionButtonSlot())
			return centerMessageButton.getItem();

		return null;
	}
}