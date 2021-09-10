package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 迷上城建模组的所有方块。
 */
public final class MUBlocks {
    /**
     * 创建的同时注册一个方块
     * @param block 方块，通常是一个新的方块。
     * @param path 命名空间id的路径，不需要包含命名空间，只允许 mishanguc 命名空间。
     * @param <T> 方块类型。
     * @return 方块自身。
     */
    private static <T extends Block> T register(T block, String path) {
        Registry.register(Registry.BLOCK,new Identifier("mishanguc",path),block);
        return block;
    }

    private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS = FabricBlockSettings.of(Material.STONE).hardness(2);
    public static final RoadBlock ASPHALT_ROAD_BLOCK =
            register(new RoadBlock(ASPHALT_ROAD_SETTINGS),"asphalt_road_block");
    public static final RoadBlockStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
            register(new RoadBlockStraightLine(ASPHALT_ROAD_SETTINGS),"asphalt_road_with_white_straight_line");
    public static final RoadBlockCornerLine ASPHALT_ROAD_WITH_WHITE_CORNER_LINE =
            register(new RoadBlockCornerLine(ASPHALT_ROAD_SETTINGS),"asphalt_road_with_white_corner_line");
}
