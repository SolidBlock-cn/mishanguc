package pers.solid.mishang.uc.blocks;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

/**
 * <h1>道路方块部分</h1>
 * <p>
 * 最基本的普通路块。
 */
public final class RoadBlocks extends MishangucBlocks {
  public static final RoadBlock ROAD_BLOCK = new RoadBlock(ROAD_SETTINGS, "mishanguc:block/asphalt", LineColor.NONE);
  /**
   * <h2>单一的直线道路</h2>
   * <p>
   * 白色直线。
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, "white_straight_line");
  /**
   * 白色双线。
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_DOUBLE_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE, "white_straight_double_line");
  /**
   * 白色粗线。
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_THICK_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.THICK, "white_straight_thick_line");
  /**
   * 黄色直线
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_LINE = new RoadWithStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, "yellow_straight_line");
  /**
   * 双黄线
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_DOUBLE_LINE = new RoadWithStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.DOUBLE, "yellow_straight_double_line");
  /**
   * 粗黄线
   */
  @Cutout
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_THICK_LINE = new RoadWithStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.THICK, "yellow_straight_thick_line");
  /**
   * <h3>混色双线</h3>
   * 白色和黄色混合的双直线道路。
   */
  @Cutout
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_YELLOW_DOUBLE_LINE = new RoadWithOffsetStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE, "white_yellow_double_straight_line", 114514);
  /**
   * <h3>偏移的直线</h3>
   * 白色偏移的直线。
   */
  @Cutout
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_OFFSET_LINE = new RoadWithOffsetStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, "white_offset_straight_line", 2);

  /**
   * 偏移的黄线。
   */
  @Cutout
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_OFFSET_LINE = new RoadWithOffsetStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, "yellow_offset_straight_line", 2);
  /**
   * 白色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_HALF_DOUBLE_LINE = new RoadWithOffsetStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, "white_half_double_line", 1);
  /**
   * 黄色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_HALF_DOUBLE_LINE = new RoadWithOffsetStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, "yellow_half_double_line", 1);
  /**
   * <h2>角落标线</h2>
   * <h3>直角</h3>
   * 白色直角。
   */
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_RA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, false, "white_right_angle_line");
  /**
   * 黄色直角
   */
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_RA_LINE = new RoadWithAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, false, "yellow_right_angle_line");
  /**
   * 白色加黄色直角
   */
  @Cutout
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_Y_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, false, "yellow_straight_line", "white_and_yellow_right_angle_line");
  /**
   * 白色粗线加白色直角
   */
  @Cutout
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_N_RA_LINE = new RoadWithDiffAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL, false, "white_straight_line", "white_thick_and_normal_right_angle_line");
  /**
   * 白色粗线加黄色直角
   */
  @Cutout
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_Y_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL, false, "yellow_straight_line", "white_thick_and_yellow_right_angle_line");
  /**
   * 白色加黄色双线直角
   */
  @Cutout
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_YD_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE, false, "yellow_straight_double_line", "white_and_yellow_double_right_angle_line");
  /**
   * 白色粗线加黄色双线直角
   */
  @Cutout
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_YD_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE, false, "yellow_straight_double_line", "white_thick_and_yellow_double_right_angle_line");
  /**
   * <h3>斜线</h3>
   * 白色斜线。
   */
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, true, "white_bevel_angle_line");
  /**
   * 白色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_DOUBLE_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE, true, "white_bevel_angle_double_line");
  /**
   * 白色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_THICK_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.THICK, true, "white_bevel_angle_thick_line");
  /**
   * 黄色斜线。
   */
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_LINE = new RoadWithAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, true, "yellow_bevel_angle_line");
  /**
   * 黄色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_DOUBLE_LINE = new RoadWithAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.DOUBLE, true, "yellow_bevel_angle_double_line");
  /**
   * 黄色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_THICK_LINE = new RoadWithAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.THICK, true, "yellow_bevel_angle_thick_line");
  /**
   * <h3>有偏移的直角</h3>
   * 白色一侧向外偏移的直角。
   */
  @Cutout
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT = new RoadWithAngleLineWithOnePartOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, false, "white_offset_straight_line", "white_right_angle_line_with_one_part_offset_out", 2);
  /**
   * 白色一侧箱内偏移的直角。
   */
  @Cutout
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_IN = new RoadWithAngleLineWithOnePartOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, false, "white_offset_straight_line2", "white_right_angle_line_with_one_part_offset_in", -2);
  /**
   * 两边均向外偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_OUT_RA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, false, "white_offset_out_right_angle_line", "white_offset_straight_line", "white_offset_straight_line2", 2);
  /**
   * 两边均向内偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_IN_RA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, false, "white_offset_in_right_angle_line", "white_offset_straight_line2", "white_offset_straight_line", -2);
  /**
   * 两边均向外偏移的斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_OUT_BA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, true, "white_offset_out_bevel_angle_line", "white_offset_straight_line", "white_offset_straight_line2", 2);
  /**
   * 两边均向内偏移的斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_WHITE_OFFSET_IN_BA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, true, "white_offset_in_bevel_angle_line", "white_offset_straight_line2", "white_offset_straight_line", -2);
  /**
   * 黄色一侧向外偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_YELLOW_RA_LINE_OFFSET_OUT = new RoadWithAngleLineWithOnePartOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, false, "yellow_offset_straight_line", "yellow_right_angle_line_with_one_part_offset_out", 2);
  /**
   * 黄色一侧箱内偏移的直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_YELLOW_RA_LINE_OFFSET_IN = new RoadWithAngleLineWithOnePartOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, false, "yellow_offset_straight_line2", "yellow_right_angle_line_with_one_part_offset_in", -2);
  /**
   * 两边均向外偏移的黄色直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_OUT_RA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, false, "yellow_offset_out_right_angle_line", "yellow_offset_straight_line", "yellow_offset_straight_line2", 2);
  /**
   * 两边均向内偏移的黄色直角。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_IN_RA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, false, "yellow_offset_in_right_angle_line", "yellow_offset_straight_line2", "yellow_offset_straight_line", -2);
  /**
   * 两边均向外偏移的黄色斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_OUT_BA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, true, "yellow_offset_out_bevel_angle_line", "yellow_offset_straight_line", "yellow_offset_straight_line2", 2);
  /**
   * 两边均向内偏移的黄色斜线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithAngleLineWithTwoPartsOffset.Impl ROAD_WITH_YELLOW_OFFSET_IN_BA_LINE = new RoadWithAngleLineWithTwoPartsOffset.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, true, "yellow_offset_in_bevel_angle_line", "yellow_offset_straight_line2", "yellow_offset_straight_line", -2);
  /**
   * <h2>T字形线路</h2>
   * <h3>无偏移同色</h3>
   * 白色T字形线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL, "white_joint_line");
  /**
   * 黄色T字形线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_YELLOW_TS_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, "yellow_joint_line");
  /**
   * <p>
   * T字形，其中单侧部分为双线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_DOUBLE_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.DOUBLE, "white_joint_line_with_double_side");
  /**
   * T字形，其中单侧部分为粗线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_THICK_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.THICK, "white_joint_line_with_thick_side");
  /**
   * T字形，直线部分为双线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_DOUBLE_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.DOUBLE, LineType.NORMAL, "white_double_joint_line");
  /**
   * T字形，直线部分为粗线。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_THICK_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL, "white_thick_joint_line");
  /**
   * <h3>无偏移异色</h3>
   * 黄色加白色。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_Y_TS_W_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL, "yellow_joint_line_with_white_side");
  /**
   * 白色加黄色。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_Y_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, "white_joint_line_with_yellow_side");
  /**
   * 白色加黄色双。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_YD_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE, "white_joint_line_with_yellow_double_side");
  /**
   * 白色粗加黄色。
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_Y_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL, "white_thick_joint_line_with_yellow_side");
  /**
   * 白色粗加黄色双。
   */
  @Cutout
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_YD_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE, "white_thick_joint_line_with_yellow_double_side");

  /**
   * <h3>有偏移同色</h3>
   * T字形，其中单侧部分有偏移。
   */
  @Cutout
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_TS_OFFSET_LINE = new RoadWithJointLineWithOffsetSide.Impl(WHITE_ROAD_SETTINGS, ROAD_WITH_WHITE_TS_LINE, "white_joint_line_with_offset_side", 2);
  /**
   * 黄色有偏移T形。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_YELLOW_TS_OFFSET_LINE = new RoadWithJointLineWithOffsetSide.Impl(YELLOW_ROAD_SETTINGS, ROAD_WITH_YELLOW_TS_LINE, "yellow_joint_line_with_offset_side", 2);
  /**
   * 有偏移的T字形，其中直线为双线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_DOUBLE_TS_OFFSET_LINE = new RoadWithJointLineWithOffsetSide.Impl(WHITE_ROAD_SETTINGS, ROAD_WITH_WHITE_DOUBLE_TS_LINE, "white_double_joint_line_with_offset_side", 2);
  /**
   * 有偏移的T字形，其中直线为粗线。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_THICK_TS_OFFSET_LINE = new RoadWithJointLineWithOffsetSide.Impl(WHITE_ROAD_SETTINGS, ROAD_WITH_WHITE_THICK_TS_LINE, "white_thick_joint_line_with_offset_side", 2);

  /**
   * <h3>有偏移异色</h3>
   * 有偏移的黄色加白色。
   */
  @Cutout
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_Y_TS_OFFSET_W_LINE = new RoadWithJointLineWithOffsetSide.Impl(YELLOW_ROAD_SETTINGS, ROAD_WITH_Y_TS_W_LINE, "yellow_joint_line_with_offset_white_side", 2);
  /**
   * 有偏移的白色加黄色。
   */
  @Cutout
  @ApiStatus.AvailableSince("1.1.0")
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_W_TS_OFFSET_Y_LINE = new RoadWithJointLineWithOffsetSide.Impl(YELLOW_ROAD_SETTINGS, ROAD_WITH_W_TS_Y_LINE, "white_joint_line_with_offset_yellow_side", 2);
  /**
   * 有偏移的白色粗加黄色。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WT_TS_OFFSET_Y_LINE = new RoadWithJointLineWithOffsetSide.Impl(YELLOW_ROAD_SETTINGS, ROAD_WITH_WT_TS_Y_LINE, "white_thick_joint_line_with_offset_yellow_side", 2);
  /**
   * <h2>双角落标线</h2>
   * 有两个斜线的线路。
   */
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture ROAD_WITH_WHITE_BI_BA_LINE = new RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture ROAD_WITH_YELLOW_BI_BA_LINE = new RoadWithTwoBevelAngleLines.ImplWithTwoLayerTexture(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture ROAD_WITH_WS_AND_BI_BA_LINE = new RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  @ApiStatus.AvailableSince("1.1.0")
  @Cutout
  public static final RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture ROAD_WITH_YS_AND_BI_BA_LINE = new RoadWithTwoBevelAngleLines.ImplWithThreeLayerTexture(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);

  /**
   * <h2>直斜混合</h2>
   * 白色直线+斜线。
   */
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WHITE_S_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * 黄色直线+斜线
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YELLOW_S_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);

  /**
   * 白色直线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_W_S_Y_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL);
  /**
   * 黄色直线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_Y_S_W_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL);
  /**
   * 白色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_N_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_N_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.YELLOW, LineType.THICK, LineType.NORMAL);
  /**
   * 白色粗线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_YN_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色粗线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_WN_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * <h2>十字交叉</h2>
   * 白色十字交叉线。
   */
  @Cutout
  public static final RoadWithCrossLine.Impl ROAD_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 黄色十字交叉线。
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  public static final RoadWithCrossLine.Impl ROAD_WITH_YELLOW_CROSS_LINE = new RoadWithCrossLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW);


  /**
   * <h2>自动路块</h2>
   * <p>
   * 斜线自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  @Cutout
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_BA_LINE = new RoadBlockWithAutoLine(WHITE_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL, "white_auto_bevel_angle_line");
  /**
   * 直角自动路块。
   */
  @Cutout
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_RA_LINE = new RoadBlockWithAutoLine(WHITE_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE, "white_auto_right_angle_line");
  /**
   * <h2>其他</h2>
   * <p>
   * 填满的路块。
   */
  public static final RoadBlock ROAD_FILLED_WITH_WHITE = new RoadBlock(WHITE_ROAD_SETTINGS, "mishanguc:block/white_ink", LineColor.WHITE);

  public static final RoadBlock ROAD_FILLED_WITH_YELLOW = new RoadBlock(YELLOW_ROAD_SETTINGS, "mishanguc:block/yellow_ink", LineColor.YELLOW);

  public static @NotNull AbstractRoadBlock getRoadBlockWithLine(LineColor lineColor, LineType lineType) {
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
      default -> throw new UnsupportedOperationException(String.format("Cannot determine base block with [color=%s, type=%s]", lineColor.asString(), lineType.asString()));
    };
  }
}
