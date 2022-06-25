package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.conversation.EditMessagePrompt;
import me.markings.bubble.conversation.PermissionPrompt;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

public class EditMenu extends Menu {

	private final Button editMessageButton;
	private final Button centerMessageButton;
	private final Button changePermissionButton;

	public EditMenu() {
		setTitle("&9&lEditing Message");
		setSize(9 * 3);

		editMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				EditMessagePrompt.getInstance().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&bEdit Message",
						"", "Click here to edit this group's message content!").make();
			}
		};

		centerMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val path = EditCommand.getInput();
				val centerPath = "Notifications.Broadcast.Messages." + path + ".Centered";
				ConfigUtil.toggleCentered(centerPath, player);
				animateTitle(((YamlConfiguration.loadConfiguration(Bubble.settingsFile).getBoolean(centerPath) ? "&a&lENABLED" : "&cDISABLED") + " &7&lcentering!"));
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.ANVIL, "&eCenter Message",
						"", "Click here to center the messages in this group!").make();
			}
		};

		changePermissionButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				PermissionPrompt.getInstance().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.GOLD_INGOT, "&dChange Permission",
						"",
						"Click here to change the permission required to",
						"view the messages in this group!").make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		switch (slot) {
			case 9 * 1 + 1:
				return editMessageButton.getItem();
			case 9 * 1 + 4:
				return centerMessageButton.getItem();
			case 9 * 1 + 7:
				return changePermissionButton.getItem();
			default:
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
		}
	}
}