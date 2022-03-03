package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

import static pers.solid.mishang.uc.blocks.RoadBlocks.*;

/**
 * <h1>道路台阶部分</h1>
 * <p>
 * 道路方块对应的台阶。
 */
public final class RoadSlabBlocks extends MishangucBlocks {

  /**
   * 方块到台阶方块的双向映射表。
   */
  public static final BiMap<Block, SlabBlock> BLOCK_TO_SLABS = HashBiMap.create();

  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ASPHALT_ROAD_SLAB = of(ASPHALT_ROAD_BLOCK);
  /**
   * <h2>单直线道路台阶</h2>
   * <p>
   * 白色直线。
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_LINE = of(ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE);
  /**
   * 白色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE = of(ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE);
  /**
   * 白色斜角
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_BEVEL_ANGLE_LINE = of(ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_STRAIGHT_LINE = of(ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_YELLOW_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_BEVEL_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_YELLOW_BEVEL_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_AND_YELLOW_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_AND_YELLOW_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_THICK_AND_NORMAL_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_THICK_AND_NORMAL_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_THICK_AND_YELLOW_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_THICK_AND_YELLOW_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_THICK_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_THICK_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE = of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithCrossLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_CROSS_LINE = of(ASPHALT_ROAD_WITH_WHITE_CROSS_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_OFFSET_STRAIGHT_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_STRAIGHT_THICK_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_OFFSET_STRAIGHT_LINE =
      of(ASPHALT_ROAD_WITH_YELLOW_OFFSET_STRAIGHT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_STRAIGHT_DOUBLE_LINE =
      of(ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_DOUBLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_STRAIGHT_THICK_LINE =
      of(ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_THICK_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLineWithOnePartOffset.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
      of(ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLineWithOnePartOffset.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
      of(ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLineWithOffsetSide.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_DOUBLE_JOINT_LINE =
      of(ASPHALT_ROAD_WITH_WHITE_DOUBLE_JOINT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_THICK_JOINT_LINE = of(ASPHALT_ROAD_WITH_WHITE_THICK_JOINT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_JOINT_LINE = of(ASPHALT_ROAD_WITH_YELLOW_JOINT_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_YELLOW_JOINT_LINE_WITH_WHITE_SIDE =
      of(ASPHALT_ROAD_WITH_YELLOW_JOINT_LINE_WITH_WHITE_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_YELLOW_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_YELLOW_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl>
      ASPHALT_ROAD_SLAB_WITH_WHITE_THICK_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE =
      of(ASPHALT_ROAD_WITH_WHITE_THICK_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE);
  /**
   * <h2>自动路台阶</h2>
   */
  @RegisterIdentifier
  @Cutout
  public static final SmartRoadSlabBlock<RoadBlockWithAutoLine>
      ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      of(
          ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE,
          new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE));
  /**
   * @see RoadBlocks#ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE
   */
  @RegisterIdentifier
  @Cutout
  public static final SmartRoadSlabBlock<RoadBlockWithAutoLine>
      ASPHALT_ROAD_SLAB_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      of(
          ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE,
          new RoadSlabBlockWithAutoLine(ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE));
  /**
   * <h2>其他</h2>
   */
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ASPHALT_ROAD_SLAB_FILLED_WITH_WHITE =
      of(ASPHALT_ROAD_FILLED_WITH_WHITE);

  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ASPHALT_ROAD_SLAB_FILLED_WITH_YELLOW =
      of(ASPHALT_ROAD_FILLED_WITH_YELLOW);

  private static <T extends Block & Road> SmartRoadSlabBlock<T> of(T baseBlock) {
    return of(baseBlock, new SmartRoadSlabBlock<>(baseBlock));
  }

  private static <T extends Block & Road> SmartRoadSlabBlock<T> of(
      T baseBlock, SmartRoadSlabBlock<T> slab) {
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }

  private static SlabBlock ofSimple(Block baseBlock) {
    final SlabBlock slab = new SlabBlock(FabricBlockSettings.copyOf(baseBlock));
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }
}
