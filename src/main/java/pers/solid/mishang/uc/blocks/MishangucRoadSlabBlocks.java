package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

public class MishangucRoadSlabBlocks extends MishangucBlocks {
  /**
   *
   *
   * <h1>道路台阶部分</h1>
   *
   * 道路方块对应的台阶。
   *
   * @see MishangucRoadBlocks
   * @see MishangucRoadBlocks#ASPHALT_ROAD_BLOCK
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB =
      new RoadSlabBlock(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.NONE);
  /**
   *
   *
   * <h2>单直线道路台阶</h2>
   *
   * @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE
   */
  @InGroup("roads")
  @Cutout
  @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE =
      new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadSlabBlockWithAngleLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithAngleLine ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadSlabBlockWithAngleLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);

  /**
   *
   *
   * <h2>由两条线组成的道路台阶</h2>
   *
   * @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightAndAngleLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadSlabBlockWithStraightAndAngleLine(
              MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE =
      new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_CROSS_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithCrossLine ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE =
      new RoadSlabBlockWithCrossLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的单线道路台阶。</h2>
   *
   * @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_SIDE_LINE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithOffsetStraightLine ASPHALT_ROAD_SLAB_WITH_WHITE_SIDE_LINE =
      new RoadSlabBlockWithOffsetStraightLine(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
          new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithStraightLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
          new RoadSlabBlockWithStraightLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的双线道路台阶</h2>
   *
   * @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE
   */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
          new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE */
  @Cutout @RegisterIdentifier
  public static final RoadSlabBlockWithJointLine
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
          new RoadSlabBlockWithJointLine(MishangucBlocks.ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>其他</h2>
   *
   * @see MishangucRoadBlocks#ASPHALT_ROAD_FILLED_WITH_WHITE
   */
  @RegisterIdentifier
  public static final RoadSlabBlock ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE =
      new RoadSlabBlock(
          FabricBlockSettings.copyOf(MishangucRoadBlocks.ASPHALT_ROAD_FILLED_WITH_WHITE),
          LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadSlabBlockWithAutoLine(
          MishangucBlocks.ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** @see MishangucRoadBlocks#ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE */
  @RegisterIdentifier @Cutout
  public static final RoadSlabBlockWithAutoLine ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadSlabBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);

  static {
    registerAll(MishangucRoadSlabBlocks.class);
  }
}
