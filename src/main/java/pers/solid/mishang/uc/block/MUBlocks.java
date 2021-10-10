package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * 迷上城建模组的所有方块。
 */
public final class MUBlocks {
    //    @Translucent
    @RegisterIdentifier
    @InGroup("lights")
    public static final Block WHITE_LAMP = new Block(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15));
    /**
     * 绝大多数柏油路方块共用的方块设置。
     */
    private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS = FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F).breakByHand(true);
    //// 普通路块部分。
    @InGroup("roads")
    @RegisterIdentifier
    public static final RoadBlock ASPHALT_ROAD_BLOCK = new RoadBlock(ASPHALT_ROAD_SETTINGS);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE = new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE = new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE = new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithStraightAndAngleLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE = new RoadBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE = new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE = new RoadBlockWithCrossLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithOffsetStraightLine ASPHALT_ROAD_WITH_WHITE_SIDE_LINE = new RoadBlockWithOffsetStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE = new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE = new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE = new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE = new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    // 填满的路块。
    @RegisterIdentifier
    public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE = new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE));
    @RegisterIdentifier
    public static final RoadSlabBlock ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE = new RoadSlabBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_FILLED_WITH_WHITE), LineColor.WHITE);
    // 自动路块。
    @RegisterIdentifier
    @Cutout
    public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE = new RoadBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL, RoadTexture.ASPHALT, LineColor.WHITE);
    @RegisterIdentifier
    @Cutout
    public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE = new RoadBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, RoadTexture.ASPHALT, LineColor.WHITE);
    //// 台阶部分。
    @RegisterIdentifier
    public static final RoadSlabBlock ASPHALT_ROAD_SLAB = new RoadSlabBlock(ASPHALT_ROAD_SETTINGS, LineColor.NONE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE = new RoadSlabBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE = new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE = new RoadSlabBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithStraightAndAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE = new RoadSlabBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE = new RoadSlabBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithCrossLine ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE = new RoadSlabBlockWithCrossLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithOffsetStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_SIDE_LINE = new RoadSlabBlockWithOffsetStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE = new RoadSlabBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE = new RoadSlabBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE = new RoadSlabBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @Cutout
    @RegisterIdentifier
    public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE = new RoadSlabBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
    @RegisterIdentifier
    @Cutout
    public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE = new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL, RoadTexture.ASPHALT, LineColor.WHITE);
    @RegisterIdentifier
    @Cutout
    public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE = new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, RoadTexture.ASPHALT, LineColor.WHITE);

    static {
        for (Field field : MUBlocks.class.getFields()) {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(modifier) && Modifier.isStatic(modifier) && Block.class.isAssignableFrom(field.getType())) {
                try {
                    Block value = (Block) field.get(null);
                    if (field.isAnnotationPresent(RegisterIdentifier.class)) {
                        final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
                        String path = annotation.value();
                        if (path.isEmpty()) {
                            path = field.getName().toLowerCase();
                        }
                        register(value, path);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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
        return Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), block);
    }
}
