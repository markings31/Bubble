package me.markings.bubble.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.conversation.*;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.*;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.MenuQuantitable;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuQuantity;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.Arrays;
import java.util.Objects;

public class GUIMenu extends Menu {

	private final Button reloadPluginButton;
	private final Button aboutPluginButton;
	private final Button editSettingsButton;

	public GUIMenu() {
		setTitle("&3&lBubble GUI");
		setSize(9 * 3);

		reloadPluginButton = Button.makeSimple(ItemCreator.of(CompMaterial.FEATHER, "&eReload Plugin"), player -> {
			player.closeInventory();
			Common.dispatchCommandAsPlayer(player, "bu rl");
		});

		aboutPluginButton = Button.makeDummy(ItemCreator.of(CompMaterial.NETHER_STAR,
				"&9&lPlugin Info",
				"&fAn easy, powerful, and customizable notifications",
				"&fand chat tool for Minecraft servers.",
				"",
				"&7Version: &f" + SimplePlugin.getVersion(),
				"&7Author: &f" + Bubble.getInstance().getDescription().getAuthors().toString().replaceAll("(^\\[|]$)", "")));

		editSettingsButton = new ButtonMenu(new SettingsMenu(), ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&6Settings"));

	}

	@Override
	public ItemStack getItemAt(final int slot) {
		return getItemStack(slot, reloadPluginButton, aboutPluginButton, editSettingsButton);
	}

	private ItemStack getItemStack(final int slot, final Button reloadPluginButton, final Button aboutPluginButton, final Button editSettingsButton) {
		if (slot == 9 * 1 + 1)
			return reloadPluginButton.getItem();

		if (slot == 9 * 1 + 4)
			return aboutPluginButton.getItem();

		if (slot == 9 * 1 + 7)
			return editSettingsButton.getItem();

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
	}

	public class SettingsMenu extends Menu {

		private final Button broadcastSettingsButton;
		private final Button welcomeSettingsButton;
		private final Button joinSettingsButton;
		private final Button chatSettingsButton;
		private final Button discordSettingsButton;
		private final Button miscSettingsButton;

		public SettingsMenu() {
			super(GUIMenu.this);
			setTitle("&9&lBubble Settings");
			setSize(9 * 4);

			broadcastSettingsButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu(), CompMaterial.ANVIL, "&6Broadcast Settings");

			welcomeSettingsButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu(), CompMaterial.PAPER, "&2MOTD Settings");

			joinSettingsButton = new ButtonMenu(new SettingsMenu.JoinSettingsMenu(), CompMaterial.EMERALD, "&dJoin Settings");

			chatSettingsButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu(), CompMaterial.ITEM_FRAME, "&eChat Settings");

			discordSettingsButton = new ButtonMenu(new SettingsMenu.DiscordSettingsMenu(), CompMaterial.MINECART, "&9Discord Settings");

			miscSettingsButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu(), CompMaterial.DIAMOND_SWORD, "&bMisc Settings");
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			if (slot == 9 + 1)
				return broadcastSettingsButton.getItem();

			if (slot == 9 + 3)
				return welcomeSettingsButton.getItem();

			if (slot == 9 + 5)
				return joinSettingsButton.getItem();

			if (slot == 9 + 7)
				return chatSettingsButton.getItem();

			if (slot == 9 * 2 + 2)
				return discordSettingsButton.getItem();

			if (slot == 9 * 2 + 6)
				return miscSettingsButton.getItem();

			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					"&eModify Bubble's settings and behavior here!"
			};
		}

		private final class BroadcastSettingsMenu extends Menu {

			private final Button enableBroadcastsButton;
			private final Button setDelayButton;
			private final Button setRandomButton;
			private final Button centerAllButton;
			private final Button sendAsyncButton;
			private final Button setSoundButton;
			private final Button setHeaderButton;
			private final Button setFooterButton;
			private final Button editMessagesButton;

			private static final String enabledText = "&aENABLED";
			private static final String disabledText = "&cDISABLED";

			private static final String onText = "&aON!";
			private static final String offText = "&cOFF&a!";

			public BroadcastSettingsMenu() {
				super(SettingsMenu.this);

				setTitle("&6&lBroadcast Settings");
				setSize(9 * 5);

				enableBroadcastsButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Broadcast.Enable";
						config.set(path, !Settings.BroadcastSettings.ENABLE_BROADCASTS);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled broadcasts "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle broadcasts! Error: ");
						restartMenu(config.getBoolean(path) ? "&aBroadcasts ENABLED!" : "&cBroadcasts DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of((MinecraftVersion.atLeast(MinecraftVersion.V.v1_14) ? CompMaterial.BELL : CompMaterial.SUNFLOWER),
								"&eAuto-Broadcasts &7(Click to toggle)",
								"Status: " + (Settings.BroadcastSettings.ENABLE_BROADCASTS.equals(Boolean.TRUE)
										? enabledText : disabledText),
								"",
								"Automatically send a preset list of",
								"messages in the chat after a certain",
								"amount of time.").build().make();
					}
				};

				setDelayButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu.DelayMenu(), CompMaterial.WATER_BUCKET, "&cSet Message Delay");

				setRandomButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Broadcast.Random_Message";
						config.set(path, !Settings.BroadcastSettings.RANDOM_MESSAGE);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled random messages "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle random messages! Error: ");
						restartMenu(config.getBoolean(path) ? "&aRandom Messages ENABLED!" : "&cRandom Messages DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.BOW,
								"&eRandom Messages &7(Click to toggle)",
								"Status: " + (Settings.BroadcastSettings.RANDOM_MESSAGE.equals(Boolean.TRUE) ? enabledText : disabledText),
								"",
								"&7If auto-broadcasts are enabled, choose",
								"a random message from the given list",
								"instead of going in order.").build().make();
					}
				};

				centerAllButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Broadcast.Center_All";
						config.set(path, !Settings.BroadcastSettings.CENTER_ALL);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled message centering "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle message centering! Error: ");
						restartMenu(config.getBoolean(path) ? "&aCenter Messages ENABLED!" : "&cCenter Messages DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.BOOKSHELF,
								"&eCenter Messages &7(Click to toggle)",
								"Status: " + (Settings.BroadcastSettings.CENTER_ALL.equals(Boolean.TRUE) ? enabledText : disabledText),
								"",
								"If auto-broadcasts are enabled, auto-",
								"matically center the messages along the",
								"middle of the chat window.").build().make();
					}
				};

				sendAsyncButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Broadcast.Send_Asynchronously";
						config.set(path, !Settings.BroadcastSettings.SEND_ASYNC);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled asynchrounous messaging " + (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle asynchrounous messaging! Error: ");
						restartMenu(config.getBoolean(path) ? "&aAsync Messages ENABLED!" : "&cAsync Messages DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.COMPARATOR,
								"&eAsync Messages &7(Click to toggle)",
								"Status: " + (Settings.BroadcastSettings.SEND_ASYNC.equals(Boolean.TRUE) ? enabledText : disabledText),
								"",
								"If auto-broadcasts are enabled, send",
								"the messages asynchronously to help",
								"save performance.").build().make();
					}
				};

				setSoundButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu.SoundMenu(), CompMaterial.MUSIC_DISC_13, "&3Change Sound");

				setHeaderButton = Button.makeSimple(ItemCreator.of(CompMaterial.DIAMOND_HELMET, "&aSet Header"), player -> {
					player.closeInventory();
					SetHeaderPrompt.getInstance().show(player);
				});

				setFooterButton = Button.makeSimple(ItemCreator.of(CompMaterial.DIAMOND_BOOTS, "&9Set Footer"), player -> {
					player.closeInventory();
					SetFooterPrompt.getInstance().show(player);
				});

				editMessagesButton = Button.makeSimple(ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&5Edit Message"), player -> {
					player.closeInventory();
					SelectLabelPrompt.getInstance().show(player);
				});
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				if (slot == 9 * 1 + 1)
					return enableBroadcastsButton.getItem();

				if (slot == 9 * 1 + 3)
					return sendAsyncButton.getItem();

				if (slot == 9 * 1 + 5)
					return setRandomButton.getItem();

				if (slot == 9 * 1 + 7)
					return centerAllButton.getItem();

				if (slot == 9 * 2 + 2)
					return setDelayButton.getItem();

				if (slot == 9 * 2 + 4)
					return setSoundButton.getItem();

				if (slot == 9 * 3 + 3)
					return setHeaderButton.getItem();

				if (slot == 9 * 3 + 5)
					return setFooterButton.getItem();

				if (slot == 9 * 2 + 6)
					return editMessagesButton.getItem();

				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
			}

			@Override
			protected String[] getInfo() {
				return new String[]{
						"&eAdjust settings related to broadcasts here!"
				};
			}

			private final class DelayMenu extends Menu implements MenuQuantitable {

				@Getter
				@Setter
				private MenuQuantity quantity = MenuQuantity.ONE;

				private final Button delayButton;
				private final Button quantityButton;

				private static final String delayPath = "Notifications.Broadcast.Delay";

				public DelayMenu() {
					super(SettingsMenu.BroadcastSettingsMenu.this);

					setTitle("&2&lBroadcast Delay");
					setSize(9 * 4);

					quantityButton = getEditQuantityButton(this);

					delayButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
							val config = Bubble.getInstance().getBubbleSettings();
							val startingDelay = SimpleTime.from(Objects.requireNonNull(config.getString(delayPath)));
							val newDelay = MathUtil.atLeast(startingDelay.getTimeSeconds() + getNextQuantity(clickType), 1);

							config.set(delayPath, String.valueOf(SimpleTime.fromSeconds((int) newDelay)));
							restartMenu();
						}

						@Override
						public ItemStack getItem() {
							val config = Bubble.getInstance().getBubbleSettings();
							val currentDelay = TimeUtil.formatTimeShort(
									SimpleTime.from(Objects.requireNonNull(config.getString(delayPath))).getTimeSeconds());
							return ItemCreator.of(CompMaterial.NAME_TAG,
											"&aChange Delay",
											"",
											"Current: " + currentDelay,
											"",
											"&8(&7Mouse Click&8)",
											"< -{q} +{q} >".replace("{q}", Common.plural(quantity.getAmount(), "second")))
									.build().make();
						}
					};
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed message delay!",
							"&cFailed to change message delay! Error: ");
				}

				@Override
				public ItemStack getItemAt(final int slot) {

					if (slot == getCenterSlot())
						return delayButton.getItem();

					if (slot == getSize() - 5)
						return quantityButton.getItem();

					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
				}
			}

			private final class SoundMenu extends MenuPagged<CompSound> {

				private SoundMenu() {
					super(SettingsMenu.BroadcastSettingsMenu.this, Arrays.asList(CompSound.values()));

					setTitle("&5Select a Sound");
				}

				@Override
				protected ItemStack convertToItemStack(final CompSound compSound) {
					val color = RandomUtil.nextItem(CompColor.values());
					return ItemCreator.of(CompMaterial.MUSIC_DISC_CAT,
									(!color.getName().equals("BLACK") ? color.getChatColor() : "&f")
											+ ItemUtil.bountifyCapitalized(compSound.name()))
							.hideTags(true)
							.build()
							.make();
				}

				@Override
				protected void onPageClick(final Player player, final CompSound compSound, final ClickType clickType) {
					val config = Bubble.getInstance().getBubbleSettings();
					compSound.play(player);
					config.set("Notifications.Broadcast.Sound", compSound.getSound() + ", 1F, 1.5F");
					restartMenu("&6Changed Sound to " + ItemUtil.bountifyCapitalized(compSound.name()));
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed broadcast sound!",
							"&cFailed to change broadcast sound! Error: ");
				}
			}
		}

		private final class MOTDSettingsMenu extends Menu {

			private final Button enableMOTDButton;
			private final Button setMOTDDelayButton;
			private final Button setMOTDSoundButton;

			private static final String enabledText = "&aENABLED";
			private static final String disabledText = "&cDISABLED";

			public MOTDSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&2&lMOTD Settings");
				setSize(9 * 3);

				enableMOTDButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notificatins.Welcome.Enable_MOTD";

						config.set(path, !Settings.WelcomeSettings.ENABLE_JOIN_MOTD);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.EMERALD,
								"&eMessage of the Day",
								"Status: " + (Settings.WelcomeSettings.ENABLE_JOIN_MOTD.equals(Boolean.TRUE) ? enabledText : disabledText),
								"",
								"Send a welcome message to the player",
								"whenever they join the server.").build().make();
					}
				};

				setMOTDDelayButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu.MOTDDelayMenu(), CompMaterial.FEATHER, "&3Set MOTD Delay");

				setMOTDSoundButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu.MOTDSoundMenu(), CompMaterial.MUSIC_DISC_CHIRP, "&dSet MOTD Sound");
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				return getItemStack(slot, enableMOTDButton, setMOTDDelayButton, setMOTDSoundButton);
			}

			private final class MOTDDelayMenu extends Menu implements MenuQuantitable {

				@Getter
				@Setter
				private MenuQuantity quantity = MenuQuantity.ONE;

				private final Button delayButton;
				private final Button quantityButton;

				private static final String motdDelayPath = "Notifications.Welcome.MOTD_Delay";

				public MOTDDelayMenu() {
					super(SettingsMenu.MOTDSettingsMenu.this);

					setTitle("&3&lMOTD Delay");
					setSize(9 * 4);

					quantityButton = getEditQuantityButton(this);

					delayButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
							val config = Bubble.getInstance().getBubbleSettings();
							val startingDelay = SimpleTime.from(Objects.requireNonNull(config.getString(motdDelayPath)));
							val newDelay = MathUtil.atLeast(startingDelay.getTimeSeconds() + getNextQuantity(clickType), 1);

							config.set(motdDelayPath, String.valueOf(SimpleTime.fromSeconds((int) newDelay)));
							restartMenu();
						}

						@Override
						public ItemStack getItem() {
							val config = Bubble.getInstance().getBubbleSettings();
							val currentDelay = TimeUtil.formatTimeShort(
									SimpleTime.from(Objects.requireNonNull(config.getString(motdDelayPath))).getTimeSeconds());
							return ItemCreator.of(CompMaterial.CLOCK,
											"&aChange Delay",
											"",
											"Current: " + currentDelay,
											"",
											"&8(&7Mouse Click&8)",
											"< -{q} +{q} >".replace("{q}", Common.plural(quantity.getAmount(), "second")))
									.build().make();
						}
					};
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed MOTD delay!",
							"&cFailed to change MOTD delay! Error: ");
				}

				@Override
				public ItemStack getItemAt(final int slot) {

					if (slot == getCenterSlot())
						return delayButton.getItem();

					if (slot == getSize() - 5)
						return quantityButton.getItem();

					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
				}
			}

			private final class MOTDSoundMenu extends MenuPagged<CompSound> {

				private MOTDSoundMenu() {
					super(SettingsMenu.MOTDSettingsMenu.this, Arrays.asList(CompSound.values()));

					setTitle("&5Select a Sound");
				}

				@Override
				protected ItemStack convertToItemStack(final CompSound compSound) {
					val color = RandomUtil.nextItem(CompColor.values());
					return ItemCreator.of(CompMaterial.MUSIC_DISC_CAT,
									(!color.getName().equals("BLACK") ? color.getChatColor() : "&f")
											+ ItemUtil.bountifyCapitalized(compSound.name()))
							.hideTags(true)
							.build()
							.make();
				}

				@Override
				protected void onPageClick(final Player player, final CompSound compSound, final ClickType clickType) {
					val config = Bubble.getInstance().getBubbleSettings();
					compSound.play(player);
					config.set("Notifications.Welcome.Sound", compSound.getSound() + ", 1F, 1.5F");
					restartMenu("&6Changed Sound to " + ItemUtil.bountifyCapitalized(compSound.name()));
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed MOTD sound!",
							"&cFailed to change MOTD sound! Error: ");
				}
			}

			@Override
			protected String[] getInfo() {
				return new String[]{
						"&eAdjust the settings related to the",
						"&emessage of the day here!"
				};
			}
		}

		private final class JoinSettingsMenu extends Menu {

			private final Button enableJoinMessageButton;
			private final Button enableQuitMessageButton;
			private final Button enableFireworkJoinButton;
			private final Button enableMuteVanishedButton;
			private final Button setJoinWorldsButton;
			private final Button setJoinMessageButton;
			private final Button setQuitMessageButton;

			public JoinSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&a&lJoin Settings");
				setSize(9 * 4);

				enableJoinMessageButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Join.Enable_Join_Message";

						config.set(path, !Settings.JoinSettings.ENABLE_JOIN_MESSAGE);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.DIAMOND,
								"&eJoin Message &7(Click to toggle)",
								"Status: " + (Settings.JoinSettings.ENABLE_JOIN_MESSAGE.equals(Boolean.TRUE)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player joins, broadcast the join",
								"message specified in the settings file to.").build().make();
					}
				};

				enableQuitMessageButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Join.Enable_Quit_Message";

						config.set(path, !Settings.JoinSettings.ENABLE_QUIT_MESSAGE);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.REDSTONE,
								"&eQuit Message &7(Click to toggle)",
								"Status: " + (Settings.JoinSettings.ENABLE_QUIT_MESSAGE.equals(Boolean.TRUE)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player quits, broadcast the quits",
								"message specified in the settings file to.").build().make();
					}
				};

				enableFireworkJoinButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Join.Firework_On_First_Join";

						config.set(path, !Settings.JoinSettings.FIREWORK_JOIN);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.FIREWORK_ROCKET,
								"&eFirst-Join Firework &7(Click to toggle)",
								"Status: " + (Settings.JoinSettings.FIREWORK_JOIN.equals(Boolean.TRUE)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player joins for the first time,",
								"launch a firework into the air.").build().make();
					}
				};

				enableMuteVanishedButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = Bubble.getInstance().getBubbleSettings();
						val path = "Notifications.Join.Firework_On_First_Join";

						config.set(path, !Settings.JoinSettings.MUTE_IF_VANISHED);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.ENDER_PEARL,
								"&eHide if Vanished &7(Click to toggle)",
								"Status: " + (Settings.JoinSettings.MUTE_IF_VANISHED.equals(Boolean.TRUE)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"If a player joins or quits while vanished,",
								"hide the message.").build().make();
					}
				};

				setJoinWorldsButton = Button.makeSimple(ItemCreator.of(CompMaterial.GRASS_BLOCK,
								"&bSet Worlds",
								"",
								"Set the worlds that join broadcasts",
								"will be sent to."),
						player -> {
							player.closeInventory();
							SetWorldsPrompt.getInstance().show(player);
						});

				setJoinMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.FEATHER,
								"&2Set Join Message",
								"",
								"Set the join message that is broadcast",
								"when a player joins."),
						player -> {
							player.closeInventory();
							SetJoinMessagePrompt.getInstance().show(player);
						});

				setQuitMessageButton = Button.makeSimple(ItemCreator.of(CompMaterial.MINECART,
								"&cSet Quit Message",
								"",
								"Set the quit message that is broadcast",
								"when a player joins."),
						player -> {
							player.closeInventory();
							SetQuitMessagePrompt.getInstance().show(player);
						});
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				if (slot == 9 * 1 + 1)
					return enableJoinMessageButton.getItem();

				if (slot == 9 * 1 + 3)
					return enableQuitMessageButton.getItem();

				if (slot == 9 * 1 + 5)
					return enableFireworkJoinButton.getItem();

				if (slot == 9 * 1 + 7)
					return enableMuteVanishedButton.getItem();

				if (slot == 9 * 2 + 2)
					return setJoinWorldsButton.getItem();

				if (slot == 9 * 2 + 4)
					return setJoinMessageButton.getItem();

				if (slot == 9 * 2 + 6)
					return setQuitMessageButton.getItem();

				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
			}

			@Override
			protected String[] getInfo() {
				return new String[]{
						"&eAdjust the settings related to join",
						"&eand quit messages here!"
				};
			}
		}

		private final class DiscordSettingsMenu extends Menu {

			private final Button discordMinecraftButton;
			private final Button minecraftDiscordButton;

			public DiscordSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&9&lDiscord Settings");
				setSize(9 * 3);

				discordMinecraftButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						if (click.isLeftClick()) {
							val config = Bubble.getInstance().getBubbleSettings();
							val path = "Discord.Discord_To_Minecraft.Enable";
							config.set(path, !Settings.DiscordSettings.DISCORDMINECRAFT);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled Discord to Minecraft functionality "
											+ (config.getBoolean(path) ? "&aON" : "&cOFF"),
									"&cFailed to toggle Discord to Minecraft functionality! Error: ");
							restartMenu(config.getBoolean(path) ? "&aDiscord to Minecraft ENABLED!" : "&cDiscord to Minecraft DISABLED!");
						} else {
							player.closeInventory();
							EditFormatPrompt.getInstance().show(player);
						}
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.LAPIS_BLOCK, "&bDiscord to Minecraft",
								"",
								"- Left-Click to " + (Settings.DiscordSettings.DISCORDMINECRAFT.equals(Boolean.TRUE) ? "&aENABLE" : "&cDISABLE"),
								"- Right-Click to edit chat formatting.").build().make();
					}
				};

				minecraftDiscordButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						if (click.isLeftClick()) {
							val config = Bubble.getInstance().getBubbleSettings();
							val path = "Discord.Minecraft_To_Discord.Enable";
							config.set(path, !Settings.DiscordSettings.MINECRAFTDISCORD);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled Minecraft to Discord functionality "
											+ (config.getBoolean(path) ? "&aON" : "&cOFF"),
									"&cFailed to Minecraft to Discord functionality! Error: ");
							restartMenu(config.getBoolean(path) ? "&aMinecraft to Discord ENABLED!" : "&cMinecraft to Discord DISABLED!");
						} else {
							player.closeInventory();
							new MinecraftDiscordMenu().displayTo(player);
						}
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.GRASS_BLOCK, "&2Minecraft to Discord",
								"",
								"- Left-Click to " + (Settings.DiscordSettings.MINECRAFTDISCORD.equals(Boolean.TRUE) ? "&aENABLE" : "&cDISABLE"),
								"- Right-Click to edit chat formatting.").build().make();
					}
				};
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				if (slot == 9 * 1 + 2)
					return discordMinecraftButton.getItem();

				if (slot == 9 * 1 + 6)
					return minecraftDiscordButton.getItem();

				return null;
			}

			public final class MinecraftDiscordMenu extends Menu {

				private final Button useWebhookButton;
				private final Button syncAnnouncementsButton;
				private final Button announcementsColorButton;

				public MinecraftDiscordMenu() {
					super(DiscordSettingsMenu.this);

					setTitle("&2&lMinecraft to Discord");
					setSize(9 * 3);


					useWebhookButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
							val config = Bubble.getInstance().getBubbleSettings();
							val path = "Discord.Minecraft_To_Discord.Use_Webhook";
							config.set(path, !Settings.DiscordSettings.USEWEBHOOK);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled webhook functionality "
											+ (config.getBoolean(path) ? "&aENABLED" : "&cDISABLED"),
									"&cFailed to toggle webhook functionality! Error: ");
						}

						@Override
						public ItemStack getItem() {
							return ItemCreator.of(CompMaterial.TRIPWIRE_HOOK, "&cWebhooks",
									"",
									"Status: " + (Settings.DiscordSettings.USEWEBHOOK.equals(Boolean.TRUE)
											? "&aENABLED" : "&cDISABLED")).build().make();
						}
					};

					syncAnnouncementsButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
							val config = Bubble.getInstance().getBubbleSettings();
							val path = "Discord.Minecraft_To_Discord.Sync_Announcements";
							config.set(path, !Settings.DiscordSettings.SYNCANNOUNCEMENTS);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled announcement sync "
											+ (config.getBoolean(path) ? "&aENABLED" : "&cDISABLED"),
									"&cFailed to toggle announcement sync! Error: ");
						}

						@Override
						public ItemStack getItem() {
							return ItemCreator.of(CompMaterial.FEATHER, "&6Sync Announcements",
									"",
									"Status: " + (Settings.DiscordSettings.SYNCANNOUNCEMENTS.equals(Boolean.TRUE)
											? "&aENABLED" : "&cDISABLED")).build().make();
						}
					};

					announcementsColorButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
							player.closeInventory();
							SetColorPrompt.getInstance().show(player);
						}

						@Override
						public ItemStack getItem() {
							val color = Settings.DiscordSettings.ANNOUNCEMENTSCOLOR;
							return ItemCreator.of(CompMaterial.FEATHER, "&dSet Announcements Color",
									"",
									"Current Color: " + (getColor(color) + ItemUtil.bountifyCapitalized(color))).build().make();
						}
					};
				}

				private static ChatColor getColor(final String color) {
					return switch (color.toUpperCase()) {
						case "BLACK" -> ChatColor.BLACK;
						case "LIGHT_GRAY" -> ChatColor.GRAY;
						case "DARK_GRAY" -> ChatColor.DARK_GRAY;
						case "BLUE" -> ChatColor.BLUE;
						case "GREEN" -> ChatColor.GREEN;
						case "CYAN" -> ChatColor.AQUA;
						case "RED" -> ChatColor.RED;
						case "ORANGE" -> ChatColor.GOLD;
						case "YELLOW" -> ChatColor.YELLOW;
						case "PINK" -> ChatColor.LIGHT_PURPLE;
						default -> null;
					};
				}

				@Override
				public ItemStack getItemAt(final int slot) {
					if (slot == 9 * 1 + 2)
						return useWebhookButton.getItem();

					if (slot == 9 * 1 + 4)
						return syncAnnouncementsButton.getItem();

					if (slot == 9 * 1 + 6)
						return announcementsColorButton.getItem();

					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").build().make();
				}
			}

		}
	}
}
