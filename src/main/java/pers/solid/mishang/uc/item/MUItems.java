package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.ItemGroups;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.block.MUBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MUItems {

    static {
        @Nullable ItemGroup group = null;
        for (Field field : MUBlocks.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isStatic(modifier) && Block.class.isAssignableFrom(field.getType())) {
                if (field.isAnnotationPresent(InGroup.class)) {
                    switch (field.getAnnotation(InGroup.class).value()) {
                        case "roads":
                            group = ItemGroups.ROADS;
                            break;
                        case "lights":
                            group = ItemGroups.LIGHTS;
                            break;
                        default:
                            group = null;
                    }
                }
                try {
                    Block value = (Block) field.get(null);
                    BlockItem blockItem = registerBlockItemOf(value, new FabricItemSettings().group(group));
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
}
