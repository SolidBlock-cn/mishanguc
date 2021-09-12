package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * 迷上城建模组的所有方块。
 */
public final class MUBlocks {
    /**
     * 绝大多数柏油路方块共用的方块设置。
     */
    private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS = FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F).breakByHand(true);

    // 普通路块部分。
    public static final RoadBlock ASPHALT_ROAD_BLOCK = register(new RoadBlock(ASPHALT_ROAD_SETTINGS), "asphalt_road_block");
    public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE = register(new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_straight_line");
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE = register(new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_right_angle_line");
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
            register(new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_bevel_angle_line");
    public static final RoadBlockWithStraightAndAngleLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE = register(new RoadBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_straight_and_bevel_angle_line");
    public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE = register(new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_joint_line");
    public static final RoadBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE = register(new RoadBlockWithCrossLine(ASPHALT_ROAD_SETTINGS),"asphalt_road_with_white_cross_line");
    public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE = register(new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE)), "asphalt_road_filled_with_white");

    // 台阶部分
    public static final RoadSlabBlock ASPHALT_ROAD_SLAB = register(new RoadSlabBlock(ASPHALT_ROAD_SETTINGS), "asphalt_road_slab");
    public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE_SLAB = register(new RoadSlabBlockWithStraightLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_straight_line_slab");
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_SLAB = register(new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_right_angle_line_slab");
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE_SLAB = register(new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_bevel_angle_line_slab");
    public static final RoadSlabBlockWithStraightAndAngleLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE_SLAB = register(new RoadSlabBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_straight_and_bevel_angle_line_slab");
    public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_SLAB = register(new RoadSlabBlockWithJointLine(ASPHALT_ROAD_SETTINGS), "asphalt_road_with_white_joint_line_slab");
    public static final RoadSlabBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE_SLAB = register(new RoadSlabBlockWithCrossLine(ASPHALT_ROAD_SETTINGS),"asphalt_road_with_white_cross_line_slab");
    public static final RoadSlabBlock ASPHALT_ROAD_FILLED_WITH_WHITE_SLAB = register(new RoadSlabBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_FILLED_WITH_WHITE)), "asphalt_road_filled_with_white_slab");

    static {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
                ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE,
                ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE,
                ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE,
                ASPHALT_ROAD_WITH_WHITE_JOINT_LINE,
                ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE,
                ASPHALT_ROAD_WITH_WHITE_CROSS_LINE,
                ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE_SLAB,
                ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_SLAB,
                ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE_SLAB,
                ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE_SLAB,
                ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_SLAB,
                ASPHALT_ROAD_WITH_WHITE_CROSS_LINE_SLAB
        );
    }

    /**
     * 创建的同时注册一个方块
     *
     * @param block 方块，通常是一个新的方块。
     * @param path  命名空间id的路径，不需要包含命名空间，只允许 mishanguc 命名空间。
     * @param <T>   方块类型。
     * @return 方块自身。
     */
    private static <T extends Block> T register(T block, String path) {
        Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), block);
        return block;
    }
}
