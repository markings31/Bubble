package me.markings.bubble.menu;

import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.command.bubble.EditCommand;
import me.markings.bubble.conversation.EditConversation;
import me.markings.bubble.conversation.PermissionConversation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Messenger;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import java.io.File;
import java.io.IOException;

public class EditMenu extends Menu {

	final File file = new File("plugins/Bubble/", "settings.yml");
	final FileConfiguration config = YamlConfiguration.loadConfiguration(file);

	private final Button editMessageButton;
	private final Button centerMessageButton;
	private final Button changePermissionButton;

	public EditMenu() {
		setTitle("&9&lEditing Message");
		setSize(9 * 3);

		editMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				new EditConversation().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&bEdit Message").build().make();
			}
		};

		centerMessageButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				val path = EditCommand.getInput();
				val centerPath = "Notifications.Broadcast.Messages." + path + ".Center";
				config.set(centerPath, !config.getBoolean(centerPath));
				
				player.closeInventory();

				try {
					config.save(file);
					Bubble.getInstance().reload();
					Messenger.success(player, "&aSuccessfully toggled centered status of message to "
							+ (config.getBoolean(centerPath) ? "&aENABLED" : "&cDISABLED"));
				} catch (IOException e) {
					e.printStackTrace();
					Messenger.error(player, "&cFailed to center message! Error: " + e);
				}
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.ANVIL, "&eCenter Message").build().make();
			}
		};

		changePermissionButton = new Button() {
			@Override
			public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
				new PermissionConversation().show(player);
			}

			@Override
			public ItemStack getItem() {
				return ItemCreator.of(CompMaterial.GOLD_INGOT, "&dChange Permission").build().make();
			}
		};
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		switch (slot) {
			case 9 + 1:
				return editMessageButton.getItem();
			case 9 + 4:
				return changePermissionButton.getItem();
			case 9 + 7:
				return centerMessageButton.getItem();
			default:
				return null;
		}
	}
}
