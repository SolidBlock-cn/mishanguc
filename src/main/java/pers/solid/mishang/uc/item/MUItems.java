package pers.solid.mishang.uc.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.ModItemGroups;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.SimpleModel;
import pers.solid.mishang.uc.block.MUBlocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MUItems {

    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final RoadConnectionStateDebuggingToolItem ROAD_CONNECTION_STATE_DEBUGGING_TOOL = new RoadConnectionStateDebuggingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), false);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final IdCheckerToolItem ID_CHECKER_TOOL = new IdCheckerToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final IdCheckerToolItem FLUID_ID_CHECKER_TOOL = new IdCheckerToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final FastBuildingToolItem FAST_BUILDING_TOOL = new FastBuildingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final RotatingToolItem ROTATING_TOOL = new RotatingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final MirroringToolItem MIRRORING_TOOL = new MirroringToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final SlabToolItem SLAB_TOOL = new SlabToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS));
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final ForcePlacingToolItem FORCE_PLACING_TOOL = new ForcePlacingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final ForcePlacingToolItem FLUID_FORCE_PLACING_TOOL = new ForcePlacingToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final BlockStateToolItem BLOCK_STATE_TOOL = new BlockStateToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final BlockStateToolItem FLUID_STATE_TOOL = new BlockStateToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), true);
    @RegisterIdentifier
    @SimpleModel(parent = "item/handheld")
    public static final DataTagToolItem DATA_TAG_TOOL = new DataTagToolItem(new FabricItemSettings().group(ModItemGroups.TOOLS), null);

    static {
        // 注册方块物品。
        @Nullable ItemGroup group = null;
        for (Field field : MUBlocks.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isStatic(modifier) && Block.class.isAssignableFrom(field.getType())) {
                if (field.isAnnotationPresent(InGroup.class)) {
                    switch (field.getAnnotation(InGroup.class).value()) {
                        case "roads":
                            group = ModItemGroups.ROADS;
                            break;
                        case "lights":
                            group = ModItemGroups.LIGHTS;
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
