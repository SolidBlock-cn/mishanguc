package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.jetbrains.annotations.ApiStatus;
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
  public static final BiMap<AbstractRoadBlock, AbstractRoadSlabBlock> BLOCK_TO_SLABS = HashBiMap.create();

  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ROAD_SLAB = of(ROAD_BLOCK);
  /**
   * <h2>单一的直线道路台阶</h2>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_WHITE_LINE = of(ROAD_WITH_WHITE_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_WHITE_DOUBLE_LINE = of(ROAD_WITH_WHITE_DOUBLE_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_WHITE_THICK_LINE = of(ROAD_WITH_WHITE_THICK_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_YELLOW_DOUBLE_LINE = of(ROAD_WITH_YELLOW_DOUBLE_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_YELLOW_LINE = of(ROAD_WITH_YELLOW_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightLine.Impl> ROAD_SLAB_WITH_YELLOW_THICK_LINE = of(ROAD_WITH_YELLOW_THICK_LINE);
  /**
   * <h3>偏移的直线台阶</h3>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl> ROAD_SLAB_WITH_WHITE_OFFSET_LINE = of(ROAD_WITH_WHITE_OFFSET_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl> ROAD_SLAB_WITH_YELLOW_OFFSET_LINE = of(ROAD_WITH_YELLOW_OFFSET_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl> ROAD_SLAB_WITH_WHITE_HALF_DOUBLE_LINE = of(ROAD_WITH_WHITE_HALF_DOUBLE_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithOffsetStraightLine.Impl> ROAD_SLAB_WITH_YELLOW_HALF_DOUBLE_LINE = of(ROAD_WITH_YELLOW_HALF_DOUBLE_LINE);
  /**
   * <h2>角落标线</h2>
   * <h3>直角</h3>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl> ROAD_SLAB_WITH_WHITE_RA_LINE = of(ROAD_WITH_WHITE_RA_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl> ROAD_SLAB_WITH_YELLOW_RA_LINE = of(ROAD_WITH_YELLOW_RA_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl> ROAD_SLAB_WITH_W_Y_RA_LINE = of(ROAD_WITH_W_Y_RA_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl> ROAD_SLAB_WITH_WT_N_RA_LINE = of(ROAD_WITH_WT_N_RA_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl> ROAD_SLAB_WITH_WT_Y_RA_LINE = of(ROAD_WITH_WT_Y_RA_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl> ROAD_SLAB_WITH_W_YD_RA_LINE = of(RoadBlocks.ROAD_WITH_W_YD_RA_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithDiffAngleLine.Impl> ROAD_SLAB_WITH_WT_YD_RA_LINE = of(ROAD_WITH_WT_YD_RA_LINE);
  /**
   * <h3>斜线</h3>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl> ROAD_SLAB_WITH_WHITE_BA_LINE = of(ROAD_WITH_WHITE_BA_LINE);
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLine.Impl> ROAD_SLAB_WITH_YELLOW_BA_LINE = of(ROAD_WITH_YELLOW_BA_LINE);
  /**
   * <h3>有偏移的直角</h3>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLineWithOnePartOffset.Impl> ROAD_SLAB_WITH_WHITE_RA_LINE_OFFSET_OUT = of(ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithAngleLineWithOnePartOffset.Impl> ROAD_SLAB_WITH_WHITE_RA_LINE_OFFSET_IN = of(ROAD_WITH_WHITE_RA_LINE_OFFSET_IN);
  /**
   * <h2>T字形线路</h2>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WHITE_TS_LINE = of(ROAD_WITH_WHITE_TS_LINE);

  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_YELLOW_TS_LINE = of(ROAD_WITH_YELLOW_TS_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WHITE_TS_DOUBLE_LINE = of(ROAD_WITH_WHITE_TS_DOUBLE_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WHITE_TS_THICK_LINE = of(ROAD_WITH_WHITE_TS_THICK_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLineWithOffsetSide.Impl> ROAD_SLAB_WITH_WHITE_TS_OFFSET_LINE = of(ROAD_WITH_WHITE_TS_OFFSET_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WHITE_DOUBLE_TS_LINE = of(ROAD_WITH_WHITE_DOUBLE_TS_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WHITE_THICK_TS_LINE = of(ROAD_WITH_WHITE_THICK_TS_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_Y_TS_W_LINE = of(ROAD_WITH_Y_TS_W_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_W_TS_Y_LINE = of(ROAD_WITH_W_TS_Y_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_W_TS_YD_LINE = of(ROAD_WITH_W_TS_YD_LINE);

  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WT_TS_Y_LINE = of(ROAD_WITH_WT_TS_Y_LINE);

  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithJointLine.Impl> ROAD_SLAB_WITH_WT_TS_YD_LINE = of(ROAD_WITH_WT_TS_YD_LINE);
  /**
   * <h2>直斜混合</h2>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_WHITE_S_BA_LINE = of(ROAD_WITH_WHITE_S_BA_LINE);

  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_YELLOW_S_BA_LINE = of(ROAD_WITH_YELLOW_S_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_W_S_Y_BA_LINE = of(ROAD_WITH_W_S_Y_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_Y_S_W_BA_LINE = of(ROAD_WITH_Y_S_W_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_WT_S_N_BA_LINE = of(ROAD_WITH_WT_S_N_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_YT_S_N_BA_LINE = of(ROAD_WITH_YT_S_N_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_WT_S_YN_BA_LINE = of(ROAD_WITH_WT_S_YN_BA_LINE);
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithStraightAndAngleLine.Impl> ROAD_SLAB_WITH_YT_S_WN_BA_LINE = of(ROAD_WITH_YT_S_WN_BA_LINE);
  /**
   * <h2>十字交叉</h2>
   */
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithCrossLine.Impl> ROAD_SLAB_WITH_WHITE_CROSS_LINE = of(ROAD_WITH_WHITE_CROSS_LINE);
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadWithCrossLine.Impl> ROAD_SLAB_WITH_YELLOW_CROSS_LINE = of(ROAD_WITH_YELLOW_CROSS_LINE);


  /**
   * <h2>自动路台阶</h2>
   */
  @RegisterIdentifier
  @Cutout
  public static final SmartRoadSlabBlock<RoadBlockWithAutoLine>
      ROAD_SLAB_WITH_WHITE_AUTO_BA_LINE = of(ROAD_WITH_WHITE_AUTO_BA_LINE, new RoadSlabBlockWithAutoLine(ROAD_WITH_WHITE_AUTO_BA_LINE));
  /**
   * @see RoadBlocks#ROAD_WITH_WHITE_AUTO_RA_LINE
   */
  @RegisterIdentifier
  @Cutout
  public static final SmartRoadSlabBlock<RoadBlockWithAutoLine>
      ROAD_SLAB_WITH_WHITE_AUTO_RA_LINE = of(ROAD_WITH_WHITE_AUTO_RA_LINE, new RoadSlabBlockWithAutoLine(ROAD_WITH_WHITE_AUTO_RA_LINE));
  /**
   * <h2>其他</h2>
   */
  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ROAD_SLAB_FILLED_WITH_WHITE = of(ROAD_FILLED_WITH_WHITE);

  @RegisterIdentifier
  public static final SmartRoadSlabBlock<RoadBlock> ROAD_SLAB_FILLED_WITH_YELLOW = of(ROAD_FILLED_WITH_YELLOW);

  private static <T extends AbstractRoadBlock> SmartRoadSlabBlock<T> of(T baseBlock) {
    return of(baseBlock, new SmartRoadSlabBlock<>(baseBlock));
  }

  private static <T extends AbstractRoadBlock & Road> SmartRoadSlabBlock<T> of(
      T baseBlock, SmartRoadSlabBlock<T> slab) {
    if (BLOCK_TO_SLABS.containsKey(baseBlock)) {
      throw new IllegalArgumentException(String.format("The slab for this road (%s) already exists!", baseBlock));
    }
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }

}
