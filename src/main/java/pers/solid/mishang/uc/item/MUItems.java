package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.ItemGroups;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.MUBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MUItems {

    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL = new RoadConnectionStateDebuggingToolItem(new FabricItemSettings().group(ItemGroups.TOOLS));
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final IdCheckerToolItem ID_CHECKER_TOOL = new IdCheckerToolItem(new FabricItemSettings().group(ItemGroups.TOOLS));
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final FastBuildingToolItem FAST_BUILDING_TOOL = new FastBuildingToolItem(new FabricItemSettings().group(ItemGroups.TOOLS));

    static {
        // 注册方块物品。
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
        // 注册物品。
        for (Field field : MUItems.class.getFields()) {
            final int modifiers = field.getModifiers();
            if (Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers) && Item.class.isAssignableFrom(field.getType())) {
                try {
                    Item value = (Item) field.get(null);
                    Registry.register(Registry.ITEM, new Identifier("mishanguc", field.getName().toLowerCase()), value);
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
