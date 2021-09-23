package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.BlockIdentifier;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 迷上城建模组的所有方块。
 */
public final class MUBlocks {
    /**
     * 绝大多数柏油路方块共用的方块设置。
     */
    private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS = FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F).breakByHand(true);

    // 普通路块部分。
    @InGroup("roads")
    @BlockIdentifier("asphalt_road_block")
    public static final RoadBlock ASPHALT_ROAD_BLOCK = new RoadBlock(ASPHALT_ROAD_SETTINGS);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_straight_line")
    public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE = new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_right_angle_line")
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE = new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE,false);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_bevel_angle_line")
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE,true);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_straight_and_bevel_angle_line")
    public static final RoadBlockWithStraightAndAngleLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE = new RoadBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_joint_line")
    public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE = new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_with_white_cross_line")
    public static final RoadBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =new RoadBlockWithCrossLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier
    public static final RoadBlockWithSideLine ASPHALT_ROAD_WITH_WHITE_SIDE_LINE = new RoadBlockWithSideLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @BlockIdentifier("asphalt_road_filled_with_white")
    public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE =new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE));
    @BlockIdentifier
    @Cutout
    public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE = new RoadBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL, RoadTexture.ASPHALT, LineColor.WHITE);
    @BlockIdentifier
    @Cutout
    public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE = new RoadBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, RoadTexture.ASPHALT, LineColor.WHITE);

    // 台阶部分
    @BlockIdentifier("asphalt_road_slab")
    public static final RoadSlabBlock ASPHALT_ROAD_SLAB =new RoadSlabBlock(ASPHALT_ROAD_SETTINGS,LineColor.NONE);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_straight_line")
    public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =new RoadSlabBlockWithStraightLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_right_angle_line")
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE,false);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_bevel_angle_line")
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE,false);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_straight_and_bevel_angle_line")
    public static final RoadSlabBlockWithStraightAndAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =new RoadSlabBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_joint_line")
    public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =new RoadSlabBlockWithJointLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier("asphalt_road_slab_with_white_cross_line")
    public static final RoadSlabBlockWithCrossLine ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =new RoadSlabBlockWithCrossLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @Cutout
    @BlockIdentifier
    public static final RoadSlabBlockWithSideLine ASPHALT_ROAD_SLAB_WITH_WHITE_SIDE_LINE = new RoadSlabBlockWithSideLine(ASPHALT_ROAD_SETTINGS,LineColor.WHITE);
    @BlockIdentifier("asphalt_road_filled_with_white_slab")
    public static final RoadSlabBlock ASPHALT_ROAD_FILLED_WITH_WHITE_SLAB =new RoadSlabBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_FILLED_WITH_WHITE),LineColor.WHITE);
    @BlockIdentifier
    @Cutout
    public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE = new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL, RoadTexture.ASPHALT, LineColor.WHITE);
    @BlockIdentifier
    @Cutout
    public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE = new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, RoadTexture.ASPHALT, LineColor.WHITE);

//    @Translucent
    @BlockIdentifier
    @InGroup("lights")
    public static final Block WHITE_LAMP = new Block(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15));
//    @BlockIdentifier
//    public static final GlowingLampBlock GLOWING_WHITE_LAMP = new GlowingLampBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15));
//    static {
//        BlockRenderLayerMap.INSTANCE.putBlock(GLOWING_WHITE_LAMP, RenderLayer.getTranslucent());
//    }

    /**
     * 创建的同时注册一个方块
     *
     * @param block 方块，通常是一个新的方块。
     * @param path  命名空间id的路径，不需要包含命名空间，只允许 mishanguc 命名空间。
     * @param <T>   方块类型。
     * @return 方块自身。
     */
    private static <T extends Block> T register(T block, String path) {
        return Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), block);
    }

    static {
        for (Field field : MUBlocks.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isStatic(modifier) && Block.class.isAssignableFrom(field.getType())) {
                try {
                    Block value = (Block) field.get(null);
                    if (field.isAnnotationPresent(BlockIdentifier.class)) {
                        final BlockIdentifier annotation = field.getAnnotation(BlockIdentifier.class);
                        String path = annotation.value();
                        if (path.isEmpty()) {
                            path = field.getName().toLowerCase();
                        }
                        value = register(value, path);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
