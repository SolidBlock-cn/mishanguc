package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.ItemGroups;
import pers.solid.mishang.uc.block.MUBlocks;

public final class MUItems {

    private static BlockItem registerBlockItemOf(Block block, FabricItemSettings settings) {
        BlockItem item = new BlockItem(block,settings);
        Registry.register(Registry.ITEM,Registry.BLOCK.getId(block),item);
        return item;
    }

    private static BlockItem registerBlockItemOfRoad(Block block) {
        return registerBlockItemOf(block,new FabricItemSettings().group(ItemGroups.ROADS));
    }

    static {
        registerBlockItemOfRoad(MUBlocks.ASPHALT_ROAD_BLOCK);
        registerBlockItemOfRoad(MUBlocks.ASPHALT_ROAD_WITH_WHITE_CORNER_LINE);
        registerBlockItemOfRoad(MUBlocks.ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE);
    }
}
