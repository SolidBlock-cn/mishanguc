package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.ItemGroups;
import pers.solid.mishang.uc.block.MUBlocks;
import pers.solid.mishang.uc.block.Road;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MUItems {

    static {
        for (Field field : MUBlocks.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isStatic(modifier) && Block.class.isAssignableFrom(field.getType())) {
                try {
                    Block value = (Block) field.get(null);
                    if (value instanceof Road) {
                        BlockItem blockItem = registerBlockItemOfRoad(value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static BlockItem registerBlockItemOf(Block block, FabricItemSettings settings) {
        BlockItem item = new BlockItem(block, settings);
        Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), item);
        return item;
    }

    private static BlockItem registerBlockItemOfRoad(Block block) {
        return registerBlockItemOf(block, new FabricItemSettings().group(ItemGroups.ROADS));
    }
}
