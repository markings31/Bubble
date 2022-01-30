package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.conversation.EditPrompt;
import me.markings.bubble.conversation.PermissionPrompt;
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
		setSize(editMenuSettings.getEditMenuSize());


		editMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				EditPrompt.getInstance().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getEditMessageButtonMaterial(), editMenuSettings.getEditMessageButtonTitle(),
						editMenuSettings.getEditMessageButtonLore()).build().make();
			}
		};

		centerMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val path = EditCommand.getInput();
				val centerPath = "Notifications.Broadcast.Messages." + path + ".Centered";
				ConfigUtil.toggleCentered(centerPath, player);
				animateTitle(((Bubble.getInstance().getBubbleSettings().getBoolean(centerPath) ? "&a&lENABLED" : "&cDISABLED") + " &7&lcentering!"));
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getCenterMessageButtonMaterial(), editMenuSettings.getCenterMessageButtonTitle(),
						editMenuSettings.getCenterMessageButtonLore()).build().make();
			}
		};

		changePermissionButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				PermissionPrompt.getInstance().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(editMenuSettings.getChangePermissionButtonMaterial(), editMenuSettings.getChangePermissionButtonTitle(),
						editMenuSettings.getChangePermissionButtonLore()).build().make();
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
