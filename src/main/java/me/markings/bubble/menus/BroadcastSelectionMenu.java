package me.markings.bubble.menus;

import me.markings.bubble.conversation.CreateConverstation;
import me.markings.bubble.settings.Broadcast;
import me.markings.bubble.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ItemUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

public class BroadcastSelectionMenu extends MenuPagged<Broadcast> {

    public BroadcastSelectionMenu(final Player player) {
        super(Broadcast.getAllBroadcasts());
        this.setViewer(player);
        this.setTitle("Select Broadcast");
    }

    @Override
    protected ItemStack convertToItemStack(final Broadcast item) {
        return ItemCreator.of(CompMaterial.PAPER, MessageUtil.getRandomColor() + item.getBroadcastName()).make();
    }

    @Override
    protected void onPageClick(final Player player, final Broadcast item, final ClickType click) {
        new EditMenu(this.getViewer(), item.getBroadcastName()).displayTo(this.getViewer());
    }

    @Override
    protected int getInfoButtonPosition() {
        return 9 * 1 + 8;
    }

    @Override
    protected String[] getInfo() {
        return new String[]{
                "&eSelect the broadcast you want to edit!"
        };
    }

    public static class EditMenu extends Menu {
        @Position(9 * 1 + 3)
        private final Button editHeaderButton;

        @Position(9 * 1 + 5)
        private final Button editFooterButton;

        @Position(9 * 2 + 2)
        private final Button editMessageButton;

        @Position(9 * 2 + 4)
        private final Button setCenteredButton;

        @Position(9 * 2 + 6)
        private final Button changePermissionButton;

        @Position(9 * 3 + 3)
        private final Button editSoundButton;

        @Position(9 * 3 + 5)
        private final Button editWorldsButton;

        public EditMenu(final Player player, final String broadcastLabel) {
            super(new BroadcastSelectionMenu(player));
            this.setTitle(broadcastLabel + " Settings");
            this.setViewer(player);
            this.setSize(9 * 5);

            editMessageButton = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    player.closeInventory();
                    new CreateConverstation.MessageContentPrompt(Broadcast.getBroadcast(broadcastLabel)).show(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.WRITABLE_BOOK, "&bChange Message",
                            "", "Click to change the content of the broadcast message.").make();
                }
            };

            setCenteredButton = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    Broadcast.getBroadcast(broadcastLabel).toggleCentered();
                    animateTitle(((Boolean.TRUE.equals(Broadcast.getBroadcast(broadcastLabel).getCentered()) ? "&aEnabled" : "&cDisabled") + " centering!"));
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.ANVIL, "&eToggle Centering",
                            "", "Click to toggle the centering of this broadcast message.").make();
                }
            };

            changePermissionButton = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    player.closeInventory();
                    new CreateConverstation.PermissionPrompt(Broadcast.getBroadcast(broadcastLabel)).show(player);
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.GOLD_INGOT, "&dChange Permission",
                            "",
                            "Click to change the permission required to",
                            "receive the broadcast message.").make();
                }
            };

            editHeaderButton = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    player.closeInventory();
                    new CreateConverstation.HeaderPrompt(Broadcast.getBroadcast(broadcastLabel)).show(getViewer());
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.DIAMOND_HELMET, "&aChange Header",
                            "Click to change the header of the",
                            "broadcast message.").make();
                }
            };

            editFooterButton = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType click) {
                    player.closeInventory();
                    new CreateConverstation.FooterPrompt(Broadcast.getBroadcast(broadcastLabel)).show(getViewer());
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.DIAMOND_BOOTS, "&cChange Footer",
                            "",
                            "Click to change the footer of the",
                            "broadcast message.").make();
                }
            };

            editSoundButton = new ButtonMenu(new SoundSelectionMenu(this.getViewer(), broadcastLabel),
                    ItemCreator.of(CompMaterial.MUSIC_DISC_CHIRP, "&6Change Sound",
                            "",
                            "Click to change the sound that will play",
                            "when this message is broadcast.").make());

            editWorldsButton = new ButtonMenu(new WorldSelectionMenu(this.getViewer(), broadcastLabel),
                    ItemCreator.of(CompMaterial.GRASS_BLOCK, "&2Change Worlds",
                            "",
                            "Click to change the worlds that the",
                            "message group will broadcast to.").make());
        }

        @Override
        public ItemStack getItemAt(final int slot) {
            return ItemCreator.of(CompMaterial.BLACK_STAINED_GLASS_PANE, "&7").make();
        }

        @Override
        protected int getInfoButtonPosition() {
            return 9 * 4;
        }

        @Override
        protected String[] getInfo() {
            return new String[]{
                    "Click one of the items to change the property",
                    "of the broadcast message."
            };
        }

        public class SoundSelectionMenu extends MenuPagged<CompSound> {

            private final String broadcastLabel;

            public SoundSelectionMenu(final Player player, final String broadcastLabel) {
                super(EditMenu.this, CompSound.values());
                this.setViewer(player);

                this.broadcastLabel = broadcastLabel;
            }

            @Override
            protected ItemStack convertToItemStack(final CompSound item) {
                return ItemCreator.of(CompMaterial.MUSIC_DISC_13,
                                MessageUtil.getRandomColor() + ItemUtil.bountifyCapitalized(item.name()))
                        .glow(Broadcast.getBroadcast(broadcastLabel).getSound().getSound().equals(item.getSound())).make();
            }

            @Override
            protected void onPageClick(final Player player, final CompSound item, final ClickType click) {
                final Broadcast broadcastInstance = Broadcast.getBroadcast(broadcastLabel);
                if (!broadcastInstance.getSound().getSound().equals(item.getSound())) {
                    item.play(this.getViewer());
                    broadcastInstance.setSound(item);
                    this.restartMenu("&aSet new sound!");
                }
            }
        }

        public class WorldSelectionMenu extends MenuPagged<World> {

            private final String broadcastLabel;

            public WorldSelectionMenu(final Player player, final String broadcastLabel) {
                super(EditMenu.this, Bukkit.getWorlds());
                this.setViewer(player);
                this.setTitle("Select Worlds");

                this.broadcastLabel = broadcastLabel;
            }

            @Override
            protected ItemStack convertToItemStack(final World item) {
                return ItemCreator.of(CompMaterial.GRASS_BLOCK, MessageUtil.getRandomColor() + item.getName())
                        .glow(Broadcast.getBroadcast(broadcastLabel).getWorlds().contains(item.getName()))
                        .make();
            }

            @Override
            protected void onPageClick(final Player player, final World item, final ClickType click) {
                final Broadcast broadcastInstance = Broadcast.getBroadcast(broadcastLabel);
                final String worldName = item.getName();
                if (!broadcastInstance.getWorlds().contains(worldName)) {
                    broadcastInstance.addWorld(worldName);
                    this.restartMenu("&aAdded " + worldName + "!");
                } else {
                    broadcastInstance.removeWorld(worldName);
                    this.restartMenu("&cRemoved " + worldName + "!");
                }
            }
        }
    }
}