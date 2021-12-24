package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.ModItemGroups;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/** 迷上城建模组的所有方块。 */
public class MishangucBlocks {
  /** 绝大多数柏油路方块共用的方块设置。 */
  protected static final FabricBlockSettings ASPHALT_ROAD_SETTINGS =
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F).breakByHand(true);

  /**
   *
   *
   * <h1>道路方块部分</h1>
   *
   * 最基本的普通路块。
   */
  @InGroup("roads")
  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_BLOCK = new RoadBlock(ASPHALT_ROAD_SETTINGS);

  /**
   *
   *
   * <h2>单直线道路</h2>
   *
   * 白色直线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** 白色斜角。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);

  /**
   *
   *
   * <h2>由两条线组成的道路</h2>
   *
   * 白色直角+斜角。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightAndAngleLine
      ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色T字形线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色十字交叉线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =
      new RoadBlockWithCrossLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的单线道路</h2>
   *
   * 侧边线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithOffsetStraightLine ASPHALT_ROAD_WITH_WHITE_SIDE_LINE =
      new RoadBlockWithOffsetStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 双线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的双线道路</h2>
   *
   * T字形，其中单侧部分为双线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分为粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>其他</h2>
   *
   * 填满的路块。
   */
  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE =
      new RoadBlock(
          FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE));

  /** 斜角自动路块。放置后遇到方块更新会自动确定线路走向。 */
  @RegisterIdentifier @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** 直角自动路块。 */
  @RegisterIdentifier @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);

  /**
   *
   *
   * <h1>道路台阶部分</h1>
   *
   * 道路方块对应的台阶。
   *
   * @see #ASPHALT_ROAD_BLOCK
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB =
      new RoadSlabBlock(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.NONE);
  /**
   *
   *
   * <h2>单直线道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
   */
  @InGroup("roads")
  @Cutout
  @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =
      new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadSlabBlockWithAngleLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see #ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadSlabBlockWithAngleLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);

  /**
   *
   *
   * <h2>由两条线组成的道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightAndAngleLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadSlabBlockWithStraightAndAngleLine(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =
      new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /** @see #ASPHALT_ROAD_WITH_WHITE_CROSS_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithCrossLine ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =
      new RoadSlabBlockWithCrossLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的单线道路台阶。</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_SIDE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithOffsetStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_SIDE_LINE =
      new RoadSlabBlockWithOffsetStraightLine(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
          new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
          new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的双线道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
          new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
          new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>其他</h2>
   *
   * @see #ASPHALT_ROAD_FILLED_WITH_WHITE
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE =
      new RoadSlabBlock(
          FabricBlockSettings.copyOf(MishangucBlocks.ASPHALT_ROAD_FILLED_WITH_WHITE),
          LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadSlabBlockWithAutoLine(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadSlabBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);

  @RegisterIdentifier
  @InGroup("lights")
  public static final Block WHITE_LAMP =
      new Block(FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15));
  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @see RegisterIdentifier
   */
  protected static void registerAll() {
    // 需要将方块物品放入对应的组。
    @Nullable ItemGroup group = null;

    for (Field field : MishangucBlocks.class.getFields()) {
      int modifier = field.getModifiers();
      if (Modifier.isFinal(modifier)
          && Modifier.isStatic(modifier)
          && Block.class.isAssignableFrom(field.getType())) {
        try {

          // 注册方块。
          Block value = (Block) field.get(null);
          if (field.isAnnotationPresent(RegisterIdentifier.class)) {
            final RegisterIdentifier annotation = field.getAnnotation(RegisterIdentifier.class);
            String path = annotation.value();
            if (path.isEmpty()) {
              path = field.getName().toLowerCase();
            }
            Registry.register(Registry.BLOCK, new Identifier("mishanguc", path), value);
            // 如果找到InGroup注解，则将其放入该组，后面的所有方块也一并放到该组，直到再次发现该注解为止。
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
            BlockItem item = new BlockItem(value, new FabricItemSettings().group(group));
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error(e);
        }
      }
    }
  }

  protected MishangucBlocks() {}

  static {
    registerAll();
  }

  public static void init() {}
}
