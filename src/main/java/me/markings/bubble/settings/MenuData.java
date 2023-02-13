package me.markings.bubble.settings;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.markings.bubble.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.collection.SerializedMap;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.model.ConfigSerializable;
import org.mineacademy.fo.model.Replacer;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.ConfigItems;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.*;

@Getter
public class MenuData extends YamlConfig {

	private final static ConfigItems<MenuData> menus = ConfigItems.fromFile("", "menus.yml", MenuData.class);

	private final String name;

	private String title;

	private CompMaterial emptySlotMaterial;

	private List<String> info;
	private int size;
	private List<ButtonData> buttons;

	private MenuData(final String name) {
		this.name = name;

		this.setPathPrefix(name);
		this.loadConfiguration(NO_DEFAULT, "menus.yml");
	}

	@Override
	protected void onLoad() {
		this.title = this.getString("Title");
		this.emptySlotMaterial = this.getMaterial("Empty_Slot_Material", CompMaterial.AIR);
		this.info = this.getStringList("Info");
		this.size = (int) MathUtil.calculate(this.getString("Size"));
		this.buttons = this.loadButtons();
	}

	private List<ButtonData> loadButtons() {
		final List<ButtonData> buttons = new ArrayList<>();

		for (final Map.Entry<String, Object> entry : this.getMap("Buttons", String.class, Object.class).entrySet()) {
			final String buttonName = entry.getKey();
			final SerializedMap buttonSettings = SerializedMap.of(entry.getValue());

			buttons.add(ButtonData.deserialize(buttonSettings, buttonName));
		}

		return buttons;
	}

	public void displayTo(final Player player) {
		this.toMenu().displayTo(player);
	}

	public Menu toMenu() {
		return this.toMenu(null);
	}

	public Menu toMenu(final Menu parent) {
		final Map<Integer, Button> buttons = this.getButtons();

		return new Menu(parent) {
			{

				this.setTitle(title);
				this.setSize(size);
			}

			@Override
			protected List<Button> getButtonsToAutoRegister() {
				return new ArrayList<>(buttons.values());
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				if (buttons.containsKey(slot))
					return buttons.get(slot).getItem();

				return ItemCreator.of(emptySlotMaterial, "").make();
			}

			@Override
			protected String[] getInfo() {
				return Valid.isNullOrEmpty(info) ? null : Common.toArray(info);
			}
		};
	}

	public Map<Integer, Button> getButtons() {
		final Map<Integer, Button> buttons = new HashMap<>();

		for (final ButtonData data : this.buttons) {
			buttons.put(data.getSlot(), new Button() {

				@Override
				public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
					final PlayerData cache = PlayerData.from(player);

					if (data.getPlayerCommand() != null) {
						player.chat(data.getPlayerCommand());
					}

					if (data.getAnimation() != null)
						menu.restartMenu(Replacer.replaceArray(data.getAnimation(),
								"{bc_status}", !cache.isBroadcastStatus() ? "&a&lON" : "&c&lOFF",
								"{bc_sound_status}", !cache.isBroadcastSoundStatus() ? "&a&lON" : "&c&lOFF",
								"{motd_status}", !cache.isMotdStatus() ? "&a&lON" : "&c&lOFF",
								"{mentions_status}", !cache.isMentionsStatus() ? "&a&lON" : "&c&lOFF",
								"{mention_sound_status}", !cache.isMentionSoundStatus() ? "&a&lON" : "&c&lOFF",
								"{mention_toast_status}", !cache.isMentionToastStatus() ? "&a&lON" : "&c&lOFF"));

					if (data.isToggleBroadcasts())
						cache.setBroadcastStatus(!cache.isBroadcastStatus());

					if (data.isToggleBroadcastSound())
						cache.setBroadcastSoundStatus(!cache.isBroadcastSoundStatus());

					if (data.isToggleMOTD())
						cache.setMotdStatus(!cache.isMotdStatus());

					if (data.isToggleMentions())
						cache.setMentionsStatus(!cache.isMentionsStatus());

					if (data.isToggleMentionSound())
						cache.setMentionSoundStatus(!cache.isMentionSoundStatus());

					if (data.isToggleMentionToast())
						cache.setMentionToastStatus(!cache.isMentionToastStatus());

					if (data.getMenuToOpen() != null) {
						final MenuData otherMenu = MenuData.findMenu(data.getMenuToOpen());

						if (otherMenu == null)
							menu.animateTitle("Invalid menu: " + data.getMenuToOpen());
						else
							otherMenu.toMenu(menu).displayTo(player);
					}
				}

				@Override
				public ItemStack getItem() {
					return ItemCreator.of(data.getMaterial(), data.getTitle(), data.getLore()).make();
				}
			});
		}

		return buttons;
	}

	@Getter
	@ToString
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	private static class ButtonData implements ConfigSerializable {

		private final String name;

		private int slot;
		private CompMaterial material;
		private String title;
		private List<String> lore;

		private String playerCommand;
		private String animation;
		private String menuToOpen;
		private boolean toggleBroadcasts;
		private boolean toggleBroadcastSound;
		private boolean toggleMOTD;

		private boolean toggleMentions;
		private boolean toggleMentionSound;
		private boolean toggleMentionToast;

		@Override
		public SerializedMap serialize() {
			return null;
		}

		public static ButtonData deserialize(final SerializedMap map, final String buttonName) {
			final ButtonData button = new ButtonData(buttonName);

			map.setRemoveOnGet(true);

			button.slot = map.containsKey("Slot") ? (int) MathUtil.calculate(map.getString("Slot")) : -1;
			Valid.checkBoolean(button.slot != -1, "Missing 'Slot' key from button: " + map);

			button.material = map.getMaterial("Material");
			Valid.checkNotNull(button.material, "Missing 'Material' key from button: " + map);

			button.title = map.getString("Title");
			Valid.checkNotNull(button.title, "Missing 'Title' key from button: " + map);

			button.lore = map.getStringList("Lore");
			Valid.checkNotNull(button.lore, "Missing 'Lore' key from button: " + map);

			final SerializedMap click = map.getMap("Click");

			button.playerCommand = click.getString("Player_Command");
			button.animation = click.getString("Animate");
			button.menuToOpen = click.getString("Menu");
			button.toggleBroadcasts = click.getBoolean("Toggle_Broadcasts", false);
			button.toggleBroadcastSound = click.getBoolean("Toggle_Broadcast_Sound", false);
			button.toggleMOTD = click.getBoolean("Toggle_MOTD", false);
			button.toggleMentions = click.getBoolean("Toggle_Mentions", false);
			button.toggleMentionSound = click.getBoolean("Toggle_Mention_Sound", false);
			button.toggleMentionToast = click.getBoolean("Toggle_Mention_Toast", false);

			return button;
		}
	}

	public static MenuData findMenu(final String name) {
		return menus.findItem(name);
	}

	public static void loadMenus() {
		menus.loadItems();
	}

	public static Set<String> getMenuNames() {
		return menus.getItemNames();
	}
}

