package pers.solid.mishang.uc.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.*;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.item.NamedBlockItem;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/** 迷上城建模组的所有方块。 */
public final class MishangucBlocks {
  /**
   *
   *
   * <h1>告示牌类方块</h1>
   *
   * 具有多种不同颜色。
   */
  public static final BiMap<DyeColor, TextPadBlock> TEXT_PAD_BLOCKS =
      Arrays.stream(DyeColor.values())
          .collect(
              ImmutableBiMap.toImmutableBiMap(
                  color -> color,
                  color ->
                      new TextPadBlock(
                          Registry.BLOCK.get(new Identifier("minecraft", color + "_concrete")),
                          color)));

  @InGroup("signs")
  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_WHITE_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.WHITE);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_ORANGE_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.ORANGE);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_MAGENTA_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.MAGENTA);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_LIGHT_BLUE_TEXT_PAD =
      TEXT_PAD_BLOCKS.get(DyeColor.LIGHT_BLUE);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_YELLOW_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.YELLOW);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_LIME_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.LIME);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_PINK_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.PINK);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_GRAY_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.GRAY);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_LIGHT_GRAY_TEXT_PAD =
      TEXT_PAD_BLOCKS.get(DyeColor.LIGHT_GRAY);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_CYAN_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.CYAN);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_PURPLE_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.PURPLE);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_BLUE_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.BLUE);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_BROWN_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.BROWN);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_GREEN_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.GREEN);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_RED_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.RED);

  @RegisterIdentifier
  public static final TextPadBlock SIMPLE_BLACK_TEXT_PAD = TEXT_PAD_BLOCKS.get(DyeColor.BLACK);

  public static final Map<DyeColor, HungSignBlock> HUNG_CONCRETE_GLOWING_SIGNS =
      new EnumMap<DyeColor, HungSignBlock>(DyeColor.class) {
        {
          for (DyeColor color : DyeColor.values()) {
            final Block block =
                Registry.BLOCK.get(new Identifier("minecraft", color.asString() + "_concrete"));
            put(color, new HungSignBlock(block, FabricBlockSettings.copyOf(block).luminance(15)));
          }
        }
      };
  public static final Map<DyeColor, HungSignBlock> HUNG_TERRACOTTA_GLOWING_SIGNS =
      new EnumMap<DyeColor, HungSignBlock>(DyeColor.class) {
        {
          for (DyeColor color : DyeColor.values()) {
            final Block block =
                Registry.BLOCK.get(new Identifier("minecraft", color.asString() + "_terracotta"));
            put(color, new HungSignBlock(block, FabricBlockSettings.copyOf(block).luminance(15)));
          }
        }
      };

  @RegisterIdentifier
  public static final HungSignBlock HUNG_WHITE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.WHITE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_ORANGE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.ORANGE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_MAGENTA_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.MAGENTA);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_BLUE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIGHT_BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_YELLOW_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.YELLOW);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIME_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIME);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PINK_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.PINK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GRAY_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_GRAY_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.LIGHT_GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_CYAN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.CYAN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PURPLE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.PURPLE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLUE_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BROWN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BROWN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GREEN_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.GREEN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_RED_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.RED);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACK_CONCRETE_GLOWING_SIGN =
      HUNG_CONCRETE_GLOWING_SIGNS.get(DyeColor.BLACK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_WHITE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.WHITE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_ORANGE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.ORANGE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_MAGENTA_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.MAGENTA);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_BLUE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIGHT_BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_YELLOW_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.YELLOW);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIME_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIME);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PINK_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.PINK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GRAY_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_LIGHT_GRAY_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.LIGHT_GRAY);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_CYAN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.CYAN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_PURPLE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.PURPLE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLUE_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BLUE);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BROWN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BROWN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_GREEN_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.GREEN);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_RED_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.RED);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACK_TERRACOTTA_GLOWING_SIGN =
      HUNG_TERRACOTTA_GLOWING_SIGNS.get(DyeColor.BLACK);

  @RegisterIdentifier
  public static final HungSignBlock HUNG_NETHERRACK_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.NETHERRACK, FabricBlockSettings.copyOf(Blocks.NETHERRACK).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_NETHER_BRICK_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.NETHER_BRICKS, FabricBlockSettings.copyOf(Blocks.NETHER_BRICKS).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_BLACKSTONE_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.BLACKSTONE, FabricBlockSettings.copyOf(Blocks.BLACKSTONE).luminance(15));

  @RegisterIdentifier
  public static final HungSignBlock HUNG_POLISHED_BLACKSTONE_GLOWING_SIGN =
      new HungSignBlock(
          Blocks.POLISHED_BLACKSTONE,
          FabricBlockSettings.copyOf(Blocks.POLISHED_BLACKSTONE).luminance(15));
  /** 绝大多数柏油路方块共用的方块设置。 */
  private static final FabricBlockSettings ASPHALT_ROAD_SETTINGS =
      FabricBlockSettings.of(Material.STONE, MapColor.GRAY).strength(0.5F);
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
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** 白色斜角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
  /** 上面三种白色对应的黄色 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, false);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, true);
  /**
   *
   *
   * <h2>由两条线组成的道路</h2>
   *
   * 白色直角+斜角。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadWithStraightAndAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色T字形线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色十字交叉线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithCrossLine.Impl ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>带有特殊线的单线道路</h2>
   *
   * 偏移的直线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE =
      new RoadWithOffsetStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 双线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 一侧偏移的直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
          new RoadWithAngleLineWithOnePartOffset.Impl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);

  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
          new RoadWithAngleLineWithOnePartOffset.Impl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   *
   *
   * <h2>带有特殊线的双线道路</h2>
   *
   * T字形，其中单侧部分为双线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分为粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      new RoadWithJointLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分有偏移。 */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.Impl
      ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
          new RoadWithJointLineWithOffsetSide.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>自动路块</h2>
   *
   * 斜角自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.Impl ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadWithAutoLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** 直角自动路块。 */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.Impl ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadWithAutoLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
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
  public static final RoadWithStraightLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see #ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
  /**
   *
   *
   * <h2>由两条线组成的道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadWithStraightAndAngleLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_CROSS_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithCrossLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>带有特殊线的单线道路台阶。</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_OFFSET_STRAIGHT_LINE =
          new RoadWithOffsetStraightLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 一侧偏移的直角。
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
          new RoadWithAngleLineWithOnePartOffset.SlabImpl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see #ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
          new RoadWithAngleLineWithOnePartOffset.SlabImpl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   *
   *
   * <h2>带有特殊线的双线道路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
          new RoadWithJointLineWithOffsetSide.SlabImpl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>自动路台阶</h2>
   *
   * @see #ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE
   */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadWithAutoLine.SlabImpl(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** @see #ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadWithAutoLine.SlabImpl(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /**
   *
   *
   * <h1>光源部分</h1>
   *
   * 所有灯方块都会用到的方块设置。
   */
  private static final FabricBlockSettings WHITE_LIGHT_SETTINGS =
      FabricBlockSettings.of(Material.REDSTONE_LAMP).luminance(15);

  @RegisterIdentifier
  @InGroup("lights")
  public static final Block WHITE_LIGHT = new Block(WHITE_LIGHT_SETTINGS);
  /** 墙上的灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。 */
  private static final FabricBlockSettings WHITE_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(WHITE_LIGHT_SETTINGS).noCollision();

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS) {
        final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION =
            MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

        @Override
        public VoxelShape getOutlineShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
          return SHAPE_PER_DIRECTION.get(state.get(FACING));
        }
      };

  static {
    registerAll();
  }

  private MishangucBlocks() {}

  /**
   * 自动注册一个类中的所有静态常量字段的方块，同时创建并注册对应的物品。
   *
   * @see RegisterIdentifier
   */
  private static void registerAll() {
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
                case "signs":
                  group = ModItemGroups.SIGNS;
                  break;
                default:
                  group = null;
              }
            }
            BlockItem item = new NamedBlockItem(value, new FabricItemSettings().group(group));
            Registry.register(Registry.ITEM, new Identifier("mishanguc", path), item);
          }
        } catch (IllegalAccessException e) {
          MishangUc.MISHANG_LOGGER.error(e);
        }
      }
    }
  }

  public static void init() {}
}
