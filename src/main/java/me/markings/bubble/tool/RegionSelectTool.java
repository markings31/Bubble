package me.markings.bubble.tool;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.tool.Tool;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegionSelectTool extends Tool {

	@Getter
	private static final RegionSelectTool instance = new RegionSelectTool();

	private static Location primary = null;

	@Getter
	public Location secondary = null;

	@Getter
	public Location center = null;

	@Override
	public ItemStack getItem() {
		return ItemCreator.of(CompMaterial.IRON_AXE, "&bRegion Select Tool").make();
	}

	@Override
	protected void onBlockClick(final PlayerInteractEvent event) {
		val player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			primary = Objects.requireNonNull(event.getClickedBlock()).getLocation();
			Common.tell(player, "&aPoint 1 selected at [" + Common.shortLocation(primary) + "]");
		}

		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && primary != null) {
			secondary = Objects.requireNonNull(event.getClickedBlock()).getLocation();
			Common.tell(player, "&aPoint 2 selected at [" + Common.shortLocation(secondary) + "]");
		}

		event.setCancelled(true);
	}
}
