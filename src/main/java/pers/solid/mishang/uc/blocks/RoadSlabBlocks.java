package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

public final class RoadSlabBlocks extends MishangucBlocks {

  /**
   *
   *
   * <h1>道路台阶部分</h1>
   *
   * 道路方块对应的台阶。
   *
   * @see RoadBlocks#ASPHALT_ROAD_BLOCK
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB =
      new RoadSlabBlock(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.NONE);
  /**
   *
   *
   * <h2>单直线道路台阶</h2>
   *
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);
  /**
   *
   *
   * <h2>由两条线组成的道路台阶</h2>
   *
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadWithStraightAndAngleLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_CROSS_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithCrossLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>带有特殊线的单线道路台阶。</h2>
   *
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_OFFSET_STRAIGHT_LINE =
          new RoadWithOffsetStraightLine.SlabImpl(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadWithStraightLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
          new RoadWithStraightLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 一侧偏移的直角。
   *
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
          new RoadWithAngleLineWithOnePartOffset.SlabImpl(
              ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN */
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
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE
   */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLine.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
          new RoadWithJointLine.SlabImpl(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.SlabImpl
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
          new RoadWithJointLineWithOffsetSide.SlabImpl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   *
   *
   * <h2>自动路台阶</h2>
   *
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE
   */
  @RegisterIdentifier @Cutout
  public static final RoadWithAutoLine.SlabImpl ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadWithAutoLine.SlabImpl(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE */
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
   * <h2>其他</h2>
   *
   * @see RoadBlocks#ASPHALT_ROAD_FILLED_WITH_WHITE
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE =
      new RoadSlabBlock(
          FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).materialColor(MapColor.WHITE),
          LineColor.WHITE);
}
