package pers.solid.mishang.uc.blocks;

import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

/**
 * <h1>道路方块部分</h1>
 * <p>
 * 最基本的普通路块。
 */
public final class RoadBlocks extends MishangucBlocks {
  public static final RoadBlock ROAD_BLOCK = register("road_block", settings -> new RoadBlock(settings, Mishanguc.id("block/asphalt"), LineColor.NONE), ROAD_SETTINGS);
  /**
   * <h2>单一的直线道路</h2>
   * <p>
   * 白色直线。
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_LINE = register("road_with_white_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.WHITE, LineType.NORMAL, "white_straight_line"), WHITE_ROAD_SETTINGS);
  /**
   * 白色双线。
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_DOUBLE_LINE = register("road_with_white_double_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.WHITE, LineType.DOUBLE, "white_straight_double_line"), WHITE_ROAD_SETTINGS);
  /**
   * 白色粗线。
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_THICK_LINE = register("road_with_white_thick_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.WHITE, LineType.THICK, "white_straight_thick_line"), WHITE_ROAD_SETTINGS);
  /**
   * 黄色直线
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_LINE = register("road_with_yellow_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL, "yellow_straight_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 双黄线
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_DOUBLE_LINE = register("road_with_yellow_double_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.YELLOW, LineType.DOUBLE, "yellow_straight_double_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 粗黄线
   */
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_THICK_LINE = register("road_with_yellow_thick_line", settings -> new RoadWithStraightLine.Impl(settings, LineColor.YELLOW, LineType.THICK, "yellow_straight_thick_line"), YELLOW_ROAD_SETTINGS);
  /**
   * <h3>混色双线</h3>
   * 白色和黄色混合的双直线道路。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_YELLOW_DOUBLE_LINE = register("road_with_white_yellow_double_line", settings -> new RoadWithOffsetStraightLine.Impl(settings, LineColor.WHITE, LineType.DOUBLE, "white_yellow_double_straight_line", 114514), YELLOW_ROAD_SETTINGS);
  /**
   * <h3>偏移的直线</h3>
   * 白色偏移的直线。
   */
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_OFFSET_LINE = register("road_with_white_offset_line", settings -> new RoadWithOffsetStraightLine.Impl(settings, LineColor.WHITE, LineType.NORMAL, "white_offset_straight_line", 2), WHITE_ROAD_SETTINGS);

  /**
   * 偏移的黄线。
   */
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_OFFSET_LINE = register("road_with_yellow_offset_line", settings -> new RoadWithOffsetStraightLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL, "yellow_offset_straight_line", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 白色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_HALF_DOUBLE_LINE = register("road_with_white_half_double_line", settings -> new RoadWithOffsetStraightLine.Impl(settings, LineColor.WHITE, LineType.NORMAL, "white_half_double_line", 1), WHITE_ROAD_SETTINGS);
  /**
   * 黄色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_HALF_DOUBLE_LINE = register("road_with_yellow_half_double_line", settings -> new RoadWithOffsetStraightLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL, "yellow_half_double_line", 1), YELLOW_ROAD_SETTINGS);
  /**
   * <h2>角落标线</h2>
   * <h3>直角</h3>
   * 白色直角。
   */
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_RA_LINE = register("road_with_white_ra_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.WHITE, LineType.NORMAL, false, "white_right_angle_line"), WHITE_ROAD_SETTINGS);
  /**
   * 黄色直角
   */
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_RA_LINE = register("road_with_yellow_ra_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL, false, "yellow_right_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色加黄色直角
   */
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_Y_RA_LINE = register("road_with_w_y_ra_line", settings -> new RoadWithDiffAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, false, "yellow_straight_line", "white_and_yellow_right_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗线加白色直角
   */
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_N_RA_LINE = register("road_with_wt_n_ra_line", settings -> new RoadWithDiffAngleLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL, false, "white_straight_line", "white_thick_and_normal_right_angle_line"), WHITE_ROAD_SETTINGS);
  /**
   * 白色粗线加黄色直角
   */
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_Y_RA_LINE = register("road_with_wt_y_ra_line", settings -> new RoadWithDiffAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL, false, "yellow_straight_line", "white_thick_and_yellow_right_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色加黄色双线直角
   */
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_YD_RA_LINE = register("road_with_w_yd_ra_line", settings -> new RoadWithDiffAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE, false, "yellow_straight_double_line", "white_and_yellow_double_right_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗线加黄色双线直角
   */
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_YD_RA_LINE = register("road_with_wt_yd_ra_line", settings -> new RoadWithDiffAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE, false, "yellow_straight_double_line", "white_thick_and_yellow_double_right_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * <h3>斜线</h3>
   * 白色斜线。
   */
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_LINE = register("road_with_white_ba_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.WHITE, LineType.NORMAL, true, "white_bevel_angle_line"), WHITE_ROAD_SETTINGS);
  /**
   * 白色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_DOUBLE_LINE = register("road_with_white_ba_double_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.WHITE, LineType.DOUBLE, true, "white_bevel_angle_double_line"), WHITE_ROAD_SETTINGS);
  /**
   * 白色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_THICK_LINE = register("road_with_white_ba_thick_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.WHITE, LineType.THICK, true, "white_bevel_angle_thick_line"), WHITE_ROAD_SETTINGS);
  /**
   * 黄色斜线。
   */
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_LINE = register("road_with_yellow_ba_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL, true, "yellow_bevel_angle_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 黄色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_DOUBLE_LINE = register("road_with_yellow_ba_double_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.YELLOW, LineType.DOUBLE, true, "yellow_bevel_angle_double_line"), YELLOW_ROAD_SETTINGS);
  /**
   * 黄色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_THICK_LINE = register("road_with_yellow_ba_thick_line", settings -> new RoadWithAngleLine.Impl(settings, LineColor.YELLOW, LineType.THICK, true, "yellow_bevel_angle_thick_line"), YELLOW_ROAD_SETTINGS);
  /**
   * <h3>有偏移的直角</h3>
   * 白色一侧向外偏移的直角。
   */
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT = register("road_with_white_ra_line_offset_out", settings -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, LineColor.WHITE, false, "white_offset_straight_line", "white_right_angle_line_with_one_part_offset_out", 2), WHITE_ROAD_SETTINGS);
  /**
   * 白色一侧箱内偏移的直角。
   */
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_IN = register("road_with_white_ra_line_offset_in", settings -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, LineColor.WHITE, false, "white_offset_straight_line2", "white_right_angle_line_with_one_part_offset_in", -2), WHITE_ROAD_SETTINGS);
  /**
   * 两边均向外偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_OUT_RA_LINE = register("road_with_white_offset_out_ra_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.WHITE, LineType.NORMAL, false, "white_offset_out_right_angle_line", "white_offset_straight_line", "white_offset_straight_line2", 2), WHITE_ROAD_SETTINGS);
  /**
   * 两边均向内偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_IN_RA_LINE = register("road_with_white_offset_in_ra_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.WHITE, LineType.NORMAL, false, "white_offset_in_right_angle_line", "white_offset_straight_line2", "white_offset_straight_line", -2), WHITE_ROAD_SETTINGS);
  /**
   * 两边均向外偏移的斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_OUT_BA_LINE = register("road_with_white_offset_out_ba_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.WHITE, LineType.NORMAL, true, "white_offset_out_bevel_angle_line", "white_offset_straight_line", "white_offset_straight_line2", 2), WHITE_ROAD_SETTINGS);
  /**
   * 两边均向内偏移的斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_IN_BA_LINE = register("road_with_white_offset_in_ba_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.WHITE, LineType.NORMAL, true, "white_offset_in_bevel_angle_line", "white_offset_straight_line2", "white_offset_straight_line", -2), WHITE_ROAD_SETTINGS);
  /**
   * 黄色一侧向外偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_YELLOW_RA_LINE_OFFSET_OUT = register("road_with_yellow_ra_line_offset_out", settings -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, LineColor.YELLOW, false, "yellow_offset_straight_line", "yellow_right_angle_line_with_one_part_offset_out", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 黄色一侧箱内偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_YELLOW_RA_LINE_OFFSET_IN = register("road_with_yellow_ra_line_offset_in", settings -> new RoadWithAngleLineWithOnePartOffset.Impl(settings, LineColor.YELLOW, false, "yellow_offset_straight_line2", "yellow_right_angle_line_with_one_part_offset_in", -2), YELLOW_ROAD_SETTINGS);
  /**
   * 两边均向外偏移的黄色直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_OUT_RA_LINE = register("road_with_yellow_offset_out_ra_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.YELLOW, LineType.NORMAL, false, "yellow_offset_out_right_angle_line", "yellow_offset_straight_line", "yellow_offset_straight_line2", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 两边均向内偏移的黄色直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_IN_RA_LINE = register("road_with_yellow_offset_in_ra_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.YELLOW, LineType.NORMAL, false, "yellow_offset_in_right_angle_line", "yellow_offset_straight_line2", "yellow_offset_straight_line", -2), YELLOW_ROAD_SETTINGS);
  /**
   * 两边均向外偏移的黄色斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_OUT_BA_LINE = register("road_with_yellow_offset_out_ba_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.YELLOW, LineType.NORMAL, true, "yellow_offset_out_bevel_angle_line", "yellow_offset_straight_line", "yellow_offset_straight_line2", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 两边均向内偏移的黄色斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_IN_BA_LINE = register("road_with_yellow_offset_in_ba_line", settings -> new RoadWithAngleLineWithTwoPartsOffset.Impl(settings, LineColor.YELLOW, LineType.NORMAL, true, "yellow_offset_in_bevel_angle_line", "yellow_offset_straight_line2", "yellow_offset_straight_line", -2), YELLOW_ROAD_SETTINGS);
  /**
   * <h2>T字形线路</h2>
   * <h3>无偏移同色</h3>
   * 白色T字形线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_LINE = register("road_with_white_ts_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL, "white_joint_line"), WHITE_ROAD_SETTINGS);
  /**
   * 黄色T字形线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_YELLOW_TS_LINE = register("road_with_yellow_ts_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.YELLOW, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, "yellow_joint_line"), YELLOW_ROAD_SETTINGS);
  /**
   * <p>
   * T字形，其中单侧部分为双线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_DOUBLE_LINE = register("road_with_white_ts_double_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.DOUBLE, "white_joint_line_with_double_side"), WHITE_ROAD_SETTINGS);
  /**
   * T字形，其中单侧部分为粗线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_THICK_LINE = register("road_with_white_ts_thick_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.THICK, "white_joint_line_with_thick_side"), WHITE_ROAD_SETTINGS);
  /**
   * T字形，直线部分为双线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_DOUBLE_TS_LINE = register("road_with_white_double_ts_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.DOUBLE, LineType.NORMAL, "white_double_joint_line"), WHITE_ROAD_SETTINGS);
  /**
   * T字形，直线部分为粗线。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_THICK_TS_LINE = register("road_with_white_thick_ts_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL, "white_thick_joint_line"), WHITE_ROAD_SETTINGS);
  /**
   * <h3>无偏移异色</h3>
   * 黄色加白色。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_Y_TS_W_LINE = register("road_with_y_ts_w_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL, "yellow_joint_line_with_white_side"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色加黄色。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_Y_LINE = register("road_with_w_ts_y_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, "white_joint_line_with_yellow_side"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色加黄色双。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_YD_LINE = register("road_with_w_ts_yd_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE, "white_joint_line_with_yellow_double_side"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗加黄色。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_Y_LINE = register("road_with_wt_ts_y_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL, "white_thick_joint_line_with_yellow_side"), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗加黄色双。
   */
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_YD_LINE = register("road_with_wt_ts_yd_line", settings -> new RoadWithJointLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE, "white_thick_joint_line_with_yellow_double_side"), YELLOW_ROAD_SETTINGS);

  /**
   * <h3>有偏移同色</h3>
   * T字形，其中单侧部分有偏移。
   */
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_TS_OFFSET_LINE = register("road_with_white_ts_offset_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_WHITE_TS_LINE, "white_joint_line_with_offset_side", 2), WHITE_ROAD_SETTINGS);
  /**
   * 黄色有偏移T形。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_YELLOW_TS_OFFSET_LINE = register("road_with_yellow_ts_offset_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_YELLOW_TS_LINE, "yellow_joint_line_with_offset_side", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 有偏移的T字形，其中直线为双线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_DOUBLE_TS_OFFSET_LINE = register("road_with_white_double_ts_offset_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_WHITE_DOUBLE_TS_LINE, "white_double_joint_line_with_offset_side", 2), WHITE_ROAD_SETTINGS);
  /**
   * 有偏移的T字形，其中直线为粗线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_THICK_TS_OFFSET_LINE = register("road_with_white_thick_ts_offset_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_WHITE_THICK_TS_LINE, "white_thick_joint_line_with_offset_side", 2), WHITE_ROAD_SETTINGS);

  /**
   * <h3>有偏移异色</h3>
   * 有偏移的黄色加白色。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_Y_TS_OFFSET_W_LINE = register("road_with_y_ts_offset_w_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_Y_TS_W_LINE, "yellow_joint_line_with_offset_white_side", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 有偏移的白色加黄色。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_W_TS_OFFSET_Y_LINE = register("road_with_w_ts_offset_y_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_W_TS_Y_LINE, "white_joint_line_with_offset_yellow_side", 2), YELLOW_ROAD_SETTINGS);
  /**
   * 有偏移的白色粗加黄色。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WT_TS_OFFSET_Y_LINE = register("road_with_wt_ts_offset_y_line", settings -> new RoadWithJointLineWithOffsetSide.Impl(settings, ROAD_WITH_WT_TS_Y_LINE, "white_thick_joint_line_with_offset_yellow_side", 2), YELLOW_ROAD_SETTINGS);
  /**
   * <h2>双角落标线</h2>
   * 有两个斜线的线路。
   */
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture ROAD_WITH_WHITE_BI_BA_LINE = register("road_with_white_bi_ba_line", settings -> new RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture(settings, LineColor.WHITE, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture ROAD_WITH_YELLOW_BI_BA_LINE = register("road_with_yellow_bi_ba_line", settings -> new RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture(settings, LineColor.YELLOW, LineType.NORMAL), YELLOW_ROAD_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture ROAD_WITH_WS_AND_BI_BA_LINE = register("road_with_ws_and_bi_ba_line", settings -> new RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture(settings, LineColor.WHITE, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture ROAD_WITH_YS_AND_BI_BA_LINE = register("road_with_ys_and_bi_ba_line", settings -> new RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture(settings, LineColor.YELLOW, LineType.NORMAL), YELLOW_ROAD_SETTINGS);

  /**
   * <h2>直斜混合</h2>
   * 白色直线+斜线。
   */
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WHITE_S_BA_LINE = register("road_with_white_s_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.WHITE, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  /**
   * 黄色直线+斜线
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YELLOW_S_BA_LINE = register("road_with_yellow_s_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.YELLOW, LineType.NORMAL), YELLOW_ROAD_SETTINGS);

  /**
   * 白色直线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_W_S_Y_BA_LINE = register("road_with_w_s_y_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  /**
   * 黄色直线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_Y_S_W_BA_LINE = register("road_with_y_s_w_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_N_BA_LINE = register("road_with_wt_s_n_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  /**
   * 黄色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_N_BA_LINE = register("road_with_yt_s_n_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.YELLOW, LineColor.YELLOW, LineType.THICK, LineType.NORMAL), YELLOW_ROAD_SETTINGS);
  /**
   * 白色粗线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_YN_BA_LINE = register("road_with_wt_s_yn_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL), WHITE_ROAD_SETTINGS);
  /**
   * 黄色粗线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_WN_BA_LINE = register("road_with_yt_s_wn_ba_line", settings -> new RoadWithStraightAndAngleLine.Impl(settings, LineColor.YELLOW, LineColor.WHITE, LineType.THICK, LineType.NORMAL), YELLOW_ROAD_SETTINGS);
  /**
   * <h2>十字交叉</h2>
   * 白色十字交叉线。
   */
  public static final RoadWithCrossLine.Impl ROAD_WITH_WHITE_CROSS_LINE = register("road_with_white_cross_line", settings ->
      new RoadWithCrossLine.Impl(settings, LineColor.WHITE), WHITE_ROAD_SETTINGS);
  /**
   * 黄色十字交叉线。
   */
  @ApiStatus.AvailableSince("0.2.0")
  public static final RoadWithCrossLine.Impl ROAD_WITH_YELLOW_CROSS_LINE = register("road_with_yellow_cross_line", settings -> new RoadWithCrossLine.Impl(settings, LineColor.YELLOW), YELLOW_ROAD_SETTINGS);


  /**
   * <h2>自动路块</h2>
   * <p>
   * 斜线自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_BA_LINE = register("road_with_white_auto_ba_line", settings -> new RoadBlockWithAutoLine(settings, RoadWithAutoLine.RoadAutoLineType.BEVEL, "white_auto_bevel_angle_line"), WHITE_ROAD_SETTINGS);
  /**
   * 直角自动路块。
   */
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_RA_LINE = register("road_with_white_auto_ra_line", settings -> new RoadBlockWithAutoLine(settings, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, "white_auto_right_angle_line"), WHITE_ROAD_SETTINGS);
  /**
   * <h2>其他</h2>
   * <p>
   * 填满的路块。
   */
  public static final RoadBlock ROAD_FILLED_WITH_WHITE = register("road_filled_with_white", settings -> new RoadBlock(settings, Mishanguc.id("block/white_ink"), LineColor.WHITE), WHITE_ROAD_SETTINGS);

  public static final RoadBlock ROAD_FILLED_WITH_YELLOW = register("road_filled_with_yellow", settings -> new RoadBlock(settings, Mishanguc.id("block/yellow_ink"), LineColor.YELLOW), YELLOW_ROAD_SETTINGS);

  public static AbstractRoadBlock getRoadBlockWithLine(LineColor lineColor, LineType lineType) {
    return switch (lineColor) {
      case WHITE -> switch (lineType) {
        case NORMAL -> ROAD_WITH_WHITE_LINE;
        case DOUBLE -> ROAD_WITH_WHITE_DOUBLE_LINE;
        case THICK -> ROAD_WITH_WHITE_THICK_LINE;
      };
      case YELLOW -> switch (lineType) {
        case NORMAL -> ROAD_WITH_YELLOW_LINE;
        case DOUBLE -> ROAD_WITH_YELLOW_DOUBLE_LINE;
        case THICK -> ROAD_WITH_YELLOW_THICK_LINE;
      };
      default -> throw new UnsupportedOperationException(String.format("Cannot determine base block with [color=%s, type=%s]", lineColor.getSerializedName(), lineType.getSerializedName()));
    };
  }
}
