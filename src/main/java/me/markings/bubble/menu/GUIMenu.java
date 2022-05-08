package me.markings.bubble.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.markings.bubble.Bubble;
import me.markings.bubble.conversation.*;
import me.markings.bubble.settings.Settings;
import me.markings.bubble.util.ConfigUtil;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.MenuQuantity;
import org.mineacademy.fo.model.SimpleTime;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;
import org.mineacademy.fo.slider.ColoredTextSlider;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("all")
public class GUIMenu extends Menu {

	@Position(9 * 1 + 1)
	private final Button reloadPluginButton;

	@Position(9 * 1 + 7)
	private final Button editSettingsButton;

	private final Button aboutPluginButton;

	private final YamlConfiguration config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);

	public GUIMenu() {
		setTitle("Bubble GUI");
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
	protected void onDisplay(final InventoryDrawer drawer) {
		val textSlider = ColoredTextSlider
				.from(this.getTitle())
				.width(10)
				.primaryColor("&3&l")
				.secondaryColor("&b&l");

		this.animateAsync(2, () -> setTitle(textSlider.next()));
	}

	@Override
	public ItemStack getItemAt(final int slot) {
		if (slot == 9 * 1 + 4)
			return aboutPluginButton.getItem();

		return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
	}

	public class SettingsMenu extends Menu {

		@Position(9 + 2)
		private final Button broadcastSettingsButton;

		@Position(9 * 2 + 3)
		private final Button welcomeSettingsButton;

		@Position(9 * 2 + 5)
		private final Button joinSettingsButton;

		@Position(9 * 1 + 6)
		private final Button chatSettingsButton;

		@Position(9 * 1 + 4)
		private final Button discordSettingsButton;

		public SettingsMenu() {
			super(GUIMenu.this);
			setTitle("&9&lBubble Settings");
			setSize(9 * 4);

			broadcastSettingsButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu(), CompMaterial.ANVIL, "&6Broadcast Settings");

			welcomeSettingsButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu(), CompMaterial.PAPER, "&2MOTD Settings");

			joinSettingsButton = new ButtonMenu(new SettingsMenu.JoinSettingsMenu(), CompMaterial.EMERALD, "&dJoin Settings");

			chatSettingsButton = new ButtonMenu(new SettingsMenu.ChatSettingsMenu(), CompMaterial.ITEM_FRAME, "&eChat Settings");

			discordSettingsButton = new ButtonMenu(new SettingsMenu.DiscordSettingsMenu(), CompMaterial.MINECART, "&9Discord Settings");
		}

		@Override
		public ItemStack getItemAt(final int slot) {
			return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
		}

		@Override
		protected String[] getInfo() {
			return new String[]{
					"&eModify Bubble's settings and behavior here!"
			};
		}

		private final class ChatSettingsMenu extends Menu {

			@Position(9 * 1 + 2)
			private final Button mentionsButton;

			@Position(9 * 1 + 6)
			private final Button mentionsSoundButton;

			public ChatSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&e&lChat Settings");
				setSize(9 * 3);

				mentionsButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
						val path = "Chat.Mentions.Enable";
						config.set(path, !Settings.ChatSettings.ENABLE_MENTIONS);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled mentions "
										+ (config.getBoolean(path) ? BroadcastSettingsMenu.onText : BroadcastSettingsMenu.offText),
								"&cFailed to toggle mentions! Error: ", config);
						restartMenu(config.getBoolean(path) ? "&aMentions ENABLED!" : "&cMentions DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of((CompMaterial.SUNFLOWER),
								"&eMentions &7(Click to toggle)",
								"Status: " + (Settings.ChatSettings.ENABLE_MENTIONS.equals(Boolean.TRUE)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"Send a notification message and sound",
								"to the player if their name is mentioned",
								"in the chat following the given symbol.").make();
					}
				};

				mentionsSoundButton = new ButtonMenu(new SoundMenu(), ItemCreator
						.of(CompMaterial.MUSIC_DISC_WAIT,
								"&dChange Sound"));
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
			}

			private final class SoundMenu extends MenuPagged<CompSound> {

				private SoundMenu() {
					super(SettingsMenu.ChatSettingsMenu.this, Arrays.asList(CompSound.values()));

					setTitle("&5Select a Sound");
				}

				@Override
				protected ItemStack convertToItemStack(final CompSound compSound) {
					val color = RandomUtil.nextItem(CompColor.values());
					return ItemCreator.of(CompMaterial.MUSIC_DISC_CAT,
									(!color.getName().equals("BLACK") ? color.getChatColor() : "&f")
											+ ItemUtil.bountifyCapitalized(compSound.name()))
							.hideTags(true)

							.make();
				}

				@Override
				protected void onPageClick(final Player player, final CompSound compSound, final ClickType clickType) {
					val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
					compSound.play(player);
					config.set("Chat.Mentions.Sound", compSound.getSound() + ", 1F, 1.5F");
					restartMenu("&6Changed Sound to " + ItemUtil.bountifyCapitalized(compSound.name()));
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed broadcast sound!",
							"&cFailed to change broadcast sound! Error: ", YamlConfiguration.loadConfiguration(Bubble.settingsFile));
				}
			}

		}

		private final class BroadcastSettingsMenu extends Menu {

			@Position(9 * 1 + 1)
			private final Button enableBroadcastsButton;

			@Position(9 * 1 + 3)
			private final Button setDelayButton;

			@Position(9 * 1 + 5)
			private final Button setRandomButton;

			@Position(9 * 1 + 7)
			private final Button centerAllButton;

			@Position(9 * 2 + 2)
			private final Button sendAsyncButton;

			@Position(9 * 2 + 4)
			private final Button setSoundButton;

			@Position(9 * 3 + 3)
			private final Button setHeaderButton;

			@Position(9 * 3 + 5)
			private final Button setFooterButton;

			@Position(9 * 2 + 6)
			private final Button editMessagesButton;

			private static final String enabledText = "&aENABLED";
			private static final String disabledText = "&cDISABLED";

			private static final String onText = "&aON!";
			private static final String offText = "&cOFF&a!";

			private static final String path = "Notifications.Broadcast.Enable";

			public BroadcastSettingsMenu() {
				super(SettingsMenu.this);

				setTitle("&6&lBroadcast Settings");
				setSize(9 * 5);

				enableBroadcastsButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(path, !config.getBoolean(path));
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled broadcasts "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle broadcasts! Error: ", config);
						restartMenu(config.getBoolean(path) ? "&aBroadcasts ENABLED!" : "&cBroadcasts DISABLED!");
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of((MinecraftVersion.atLeast(MinecraftVersion.V.v1_14) ? CompMaterial.BELL : CompMaterial.SUNFLOWER),
								"&eAuto-Broadcasts &7(Click to toggle)",
								"Status: " + (config.getBoolean(path)
										? enabledText : disabledText),
								"",
								"Automatically send a preset list of",
								"messages in the chat after a certain",
								"amount of time.").make();
					}
				};

				setDelayButton = new ButtonMenu(new SettingsMenu.BroadcastSettingsMenu.DelayMenu(), CompMaterial.WATER_BUCKET, "&cSet Message Delay");

				setRandomButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val path = "Notifications.Broadcast.Random_Message";
						config.set(path, !Settings.BroadcastSettings.RANDOM_MESSAGE);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled random messages "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle random messages! Error: ", config);
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
								"instead of going in order.").make();
					}
				};

				centerAllButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val path = "Notifications.Broadcast.Center_All";
						config.set(path, !Settings.BroadcastSettings.CENTER_ALL);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled message centering "
										+ (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle message centering! Error: ", config);
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
								"middle of the chat window.").make();
					}
				};

				sendAsyncButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						val path = "Notifications.Broadcast.Send_Asynchronously";
						config.set(path, !Settings.BroadcastSettings.SEND_ASYNC);
						ConfigUtil.saveConfig(player,
								"&aSuccessfully toggled asynchrounous messaging " + (config.getBoolean(path) ? onText : offText),
								"&cFailed to toggle asynchrounous messaging! Error: ", config);
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
								"save performance.").make();
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
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
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

				@Position(9 * 1 + 4)
				private final Button delayButton;

				@Position(9 * 3 + 4)
				private final Button quantityButton;

				private static final String delayPath = "Notifications.Broadcast.Delay";

				public DelayMenu() {
					super(SettingsMenu.BroadcastSettingsMenu.this);

					setTitle("&2&lBroadcast Delay");
					setSize(9 * 4);

					quantityButton = getQuantityButton(this);

					delayButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
							val startingDelay = SimpleTime.from(Objects.requireNonNull(config.getString(delayPath)));
							val newDelay = MathUtil.atLeast(startingDelay.getTimeSeconds() + getNextQuantityPercent(clickType), 1);

							config.set(delayPath, String.valueOf(SimpleTime.fromSeconds((int) newDelay)));
							restartMenu();
						}

						@Override
						public ItemStack getItem() {
							val currentDelay = TimeUtil.formatTimeShort(
									SimpleTime.from(Objects.requireNonNull(config.getString(delayPath))).getTimeSeconds());
							return ItemCreator.of(CompMaterial.NAME_TAG,
											"&aChange Delay",
											"",
											"Current: " + currentDelay,
											"",
											"&8(&7Mouse Click&8)",
											"< -{q} +{q} >".replace("{q}", Common.plural((long) quantity.getAmountPercent(), "second")))
									.make();
						}
					};
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed message delay!",
							"&cFailed to change message delay! Error: ", config);
				}

				@Override
				public ItemStack getItemAt(final int slot) {
					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
				}
			}

			private final class SoundMenu extends MenuPagged<CompSound> {

				private final YamlConfiguration config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);

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

							.make();
				}

				@Override
				protected void onPageClick(final Player player, final CompSound compSound, final ClickType clickType) {
					compSound.play(player);
					config.set("Notifications.Broadcast.Sound", compSound.getSound() + ", 1F, 1.5F");
					restartMenu("&6Changed Sound to " + ItemUtil.bountifyCapitalized(compSound.name()));
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed broadcast sound!",
							"&cFailed to change broadcast sound! Error: ", config);
				}
			}
		}

		private final class MOTDSettingsMenu extends Menu {

			@Position(9 * 1 + 1)
			private final Button enableMOTDButton;

			@Position(9 * 1 + 4)
			private final Button setMOTDDelayButton;

			@Position(9 * 1 + 7)
			private final Button setMOTDSoundButton;

			private static final String enabledText = "&aENABLED";
			private static final String disabledText = "&cDISABLED";

			private static final String path = "Notificatins.Welcome.Enable_MOTD";

			public MOTDSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&2&lMOTD Settings");
				setSize(9 * 3);

				enableMOTDButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(path, !Settings.WelcomeSettings.ENABLE_JOIN_MOTD);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.EMERALD,
								"&eMessage of the Day",
								"Status: " + (config.getBoolean(path) ? enabledText : disabledText),
								"",
								"Send a welcome message to the player",
								"whenever they join the server.").make();
					}
				};

				setMOTDDelayButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu.MOTDDelayMenu(), CompMaterial.FEATHER, "&3Set MOTD Delay");

				setMOTDSoundButton = new ButtonMenu(new SettingsMenu.MOTDSettingsMenu.MOTDSoundMenu(), CompMaterial.MUSIC_DISC_CHIRP, "&dSet MOTD Sound");
			}

			private final class MOTDDelayMenu extends Menu implements MenuQuantitable {

				@Getter
				@Setter
				private MenuQuantity quantity = MenuQuantity.ONE;

				@Position(9 * 1 + 4)
				private final Button delayButton;

				@Position(9 * 3 + 4)
				private final Button quantityButton;

				private static final String motdDelayPath = "Notifications.Welcome.MOTD_Delay";

				public MOTDDelayMenu() {
					super(SettingsMenu.MOTDSettingsMenu.this);

					setTitle("&3&lMOTD Delay");
					setSize(9 * 4);

					quantityButton = getQuantityButton(this);

					delayButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
							val startingDelay = SimpleTime.from(Objects.requireNonNull(config.getString(motdDelayPath)));
							val newDelay = MathUtil.atLeast(startingDelay.getTimeSeconds() + getNextQuantityDouble(clickType), 1);

							config.set(motdDelayPath, String.valueOf(SimpleTime.fromSeconds((int) newDelay)));
							restartMenu();
						}

						@Override
						public ItemStack getItem() {
							val config = YamlConfiguration.loadConfiguration(Bubble.settingsFile);
							val currentDelay = TimeUtil.formatTimeShort(
									SimpleTime.from(Objects.requireNonNull(config.getString(motdDelayPath))).getTimeSeconds());
							return ItemCreator.of(CompMaterial.CLOCK,
											"&aChange Delay",
											"",
											"Current: " + currentDelay,
											"",
											"&8(&7Mouse Click&8)",
											"< -{q} +{q} >".replace("{q}", Common.plural((long) quantity.getAmountDouble(), "second")))
									.make();
						}
					};
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed MOTD delay!",
							"&cFailed to change MOTD delay! Error: ", config);
				}

				@Override
				public ItemStack getItemAt(final int slot) {
					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
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

							.make();
				}

				@Override
				protected void onPageClick(final Player player, final CompSound compSound, final ClickType clickType) {
					compSound.play(player);
					config.set("Notifications.Welcome.Sound", compSound.getSound() + ", 1F, 1.5F");
					restartMenu("&6Changed Sound to " + ItemUtil.bountifyCapitalized(compSound.name()));
				}

				@Override
				protected void onMenuClose(final Player player, final Inventory inventory) {
					ConfigUtil.saveConfig(player,
							"&aSuccessfully changed MOTD sound!",
							"&cFailed to change MOTD sound! Error: ", config);
				}
			}

			@Override
			protected String[] getInfo() {
				return new String[]{
						"&eAdjust the settings related to the",
						"&emessage of the day here!"
				};
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
			}
		}

		private final class JoinSettingsMenu extends Menu {

			@Position(9 * 1 + 1)
			private final Button enableJoinMessageButton;

			@Position(9 * 1 + 3)
			private final Button enableQuitMessageButton;

			@Position(9 * 1 + 5)
			private final Button enableFireworkJoinButton;

			@Position(9 * 1 + 7)
			private final Button enableMuteVanishedButton;

			@Position(9 * 2 + 2)
			private final Button setJoinWorldsButton;

			@Position(9 * 2 + 4)
			private final Button setJoinMessageButton;

			@Position(9 * 2 + 6)
			private final Button setQuitMessageButton;

			private static final String pathJoin = "Notifications.Join.Enable_Join_Message";
			private static final String pathQuit = "Notifications.Join.Enable_Quit_Message";
			private static final String pathFirework = "Notifications.Join.Firework_On_First_Join";
			private static final String pathVanished = "Notifications.Join.Mute_If_Vanished";

			public JoinSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&a&lJoin Settings");
				setSize(9 * 4);

				enableJoinMessageButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(pathJoin, !Settings.JoinSettings.ENABLE_JOIN_MESSAGE);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.DIAMOND,
								"&eJoin Message &7(Click to toggle)",
								"Status: " + (config.getBoolean(pathJoin)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player joins, broadcast the join",
								"message specified in the settings file to.").make();
					}
				};

				enableQuitMessageButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(pathQuit, !Settings.JoinSettings.ENABLE_QUIT_MESSAGE);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.REDSTONE,
								"&eQuit Message &7(Click to toggle)",
								"Status: " + (config.getBoolean(pathQuit)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player quits, broadcast the quits",
								"message specified in the settings file to.").make();
					}
				};

				enableFireworkJoinButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(pathFirework, !Settings.JoinSettings.FIREWORK_JOIN);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.FIREWORK_ROCKET,
								"&eFirst-Join Firework &7(Click to toggle)",
								"Status: " + (config.getBoolean(pathFirework)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"When a player joins for the first time,",
								"launch a firework into the air.").make();
					}
				};

				enableMuteVanishedButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						config.set(pathVanished, !Settings.JoinSettings.MUTE_IF_VANISHED);
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.ENDER_PEARL,
								"&eHide if Vanished &7(Click to toggle)",
								"Status: " + (config.getBoolean(pathVanished)
										? BroadcastSettingsMenu.enabledText : BroadcastSettingsMenu.disabledText),
								"",
								"If a player joins or quits while vanished,",
								"hide the message.").make();
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
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
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

			@Position(9 * 1 + 2)
			private final Button discordMinecraftButton;

			@Position(9 * 1 + 6)
			private final Button minecraftDiscordButton;

			private static final String pathMD = "Discord.Discord_To_Minecraft.Enable";
			private static final String pathDM = "Discord.Minecraft_To_Discord.Enable";

			public DiscordSettingsMenu() {
				super(SettingsMenu.this);
				setTitle("&9&lDiscord Settings");
				setSize(9 * 3);

				discordMinecraftButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						if (click.isLeftClick()) {
							config.set(pathMD, !Settings.DiscordSettings.DISCORDMINECRAFT);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled Discord to Minecraft functionality "
											+ (config.getBoolean(pathMD) ? "&aON" : "&cOFF"),
									"&cFailed to toggle Discord to Minecraft functionality! Error: ", config);
							restartMenu(config.getBoolean(pathMD) ? "&aDiscord to Minecraft ENABLED!" : "&cDiscord to Minecraft DISABLED!");
						} else {
							player.closeInventory();
							EditFormatPrompt.getInstance().show(player);
						}
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.LAPIS_BLOCK, "&bDiscord to Minecraft",
								"",
								"- Left-Click to " + (config.getBoolean(pathMD) ? "&aENABLE" : "&cDISABLE"),
								"- Right-Click to edit chat formatting.").make();
					}
				};

				minecraftDiscordButton = new Button() {
					@Override
					public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
						if (click.isLeftClick()) {
							config.set(pathDM, !Settings.DiscordSettings.MINECRAFTDISCORD);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled Minecraft to Discord functionality "
											+ (config.getBoolean(pathDM) ? "&aON" : "&cOFF"),
									"&cFailed to Minecraft to Discord functionality! Error: ", config);
							restartMenu(config.getBoolean(pathDM) ? "&aMinecraft to Discord ENABLED!" : "&cMinecraft to Discord DISABLED!");
						} else {
							player.closeInventory();
							new MinecraftDiscordMenu().displayTo(player);
						}
					}

					@Override
					public ItemStack getItem() {
						return ItemCreator.of(CompMaterial.GRASS_BLOCK, "&2Minecraft to Discord",
								"",
								"- Left-Click to " + (config.getBoolean(pathDM) ? "&aENABLE" : "&cDISABLE"),
								"- Right-Click to edit chat formatting.").make();
					}
				};
			}

			@Override
			public ItemStack getItemAt(final int slot) {
				return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
			}

			public final class MinecraftDiscordMenu extends Menu {

				@Position(9 * 1 + 2)
				private final Button useWebhookButton;

				@Position(9 * 1 + 4)
				private final Button syncAnnouncementsButton;

				@Position(9 * 1 + 6)
				private final Button announcementsColorButton;

				private static final String pathMDWebhook = "Discord.Minecraft_To_Discord.Use_Webhook";
				private static final String pathMDSyncAnnounce = "Discord.Minecraft_To_Discord.Use_Webhook";

				public MinecraftDiscordMenu() {
					super(DiscordSettingsMenu.this);

					setTitle("&2&lMinecraft to Discord");
					setSize(9 * 3);


					useWebhookButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
							config.set(pathMDWebhook, !Settings.DiscordSettings.USEWEBHOOK);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled webhook functionality "
											+ (config.getBoolean(pathMDWebhook) ? "&aENABLED" : "&cDISABLED"),
									"&cFailed to toggle webhook functionality! Error: ", config);
						}

						@Override
						public ItemStack getItem() {
							return ItemCreator.of(CompMaterial.TRIPWIRE_HOOK, "&cWebhooks",
									"",
									"Status: " + (config.getBoolean(pathMDWebhook)
											? "&aENABLED" : "&cDISABLED")).make();
						}
					};

					syncAnnouncementsButton = new Button() {
						@Override
						public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
							config.set(pathMDSyncAnnounce, !Settings.DiscordSettings.SYNCANNOUNCEMENTS);
							ConfigUtil.saveConfig(player,
									"&aSuccessfully toggled announcement sync "
											+ (config.getBoolean(pathMDSyncAnnounce) ? "&aENABLED" : "&cDISABLED"),
									"&cFailed to toggle announcement sync! Error: ", config);
						}

						@Override
						public ItemStack getItem() {
							return ItemCreator.of(CompMaterial.FEATHER, "&6Sync Announcements",
									"",
									"Status: " + (config.getBoolean(pathMDSyncAnnounce)
											? "&aENABLED" : "&cDISABLED")).make();
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
									"Current Color: " + (getColor(color) + ItemUtil.bountifyCapitalized(color))).make();
						}
					};
				}

				@Override
				public ItemStack getItemAt(final int slot) {
					return ItemCreator.of(CompMaterial.GRAY_STAINED_GLASS_PANE, "").make();
				}
			}

		}
	}

	private static ChatColor getColor(final String color) {
		switch (color.toUpperCase()) {
			case "BLACK":
				return ChatColor.BLACK;
			case "LIGHT_GRAY":
				return ChatColor.GRAY;
			case "DARK_GRAY":
				return ChatColor.DARK_GRAY;
			case "BLUE":
				return ChatColor.BLUE;
			case "GREEN":
				return ChatColor.GREEN;
			case "CYAN":
				return ChatColor.AQUA;
			case "RED":
				return ChatColor.RED;
			case "ORANGE":
				return ChatColor.GOLD;
			case "YELLOW":
				return ChatColor.YELLOW;
			case "PINK":
				return ChatColor.LIGHT_PURPLE;
			default:
				return null;
		}
	}
}
