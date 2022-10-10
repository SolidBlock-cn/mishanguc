package pers.solid.mishang.uc.blocks;

import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

/**
 * <h1>道路方块部分</h1>
 * <p>
 * 最基本的普通路块。
 */
public final class RoadBlocks extends MishangucBlocks {
  @RegisterIdentifier
  public static final RoadBlock ROAD_BLOCK = new RoadBlock(ROAD_SETTINGS);
  /**
   * <h2>单一的直线道路</h2>
   * <p>
   * 白色直线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * 白色双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_DOUBLE_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE);
  /**
   * 白色粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_WHITE_THICK_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.THICK);
  /**
   * 黄色直线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_LINE = new RoadWithStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);
  /**
   * 双黄线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_DOUBLE_LINE = new RoadWithStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.DOUBLE);
  /**
   * 粗黄线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ROAD_WITH_YELLOW_THICK_LINE = new RoadWithStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.THICK);
  /**
   * <h3>偏移的直线</h3>
   * 白色偏移的直线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_OFFSET_LINE = new RoadWithOffsetStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);

  /**
   * 偏移的黄线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_OFFSET_LINE = new RoadWithOffsetStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);
  /**
   * 白色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_WHITE_HALF_DOUBLE_LINE = new RoadWithOffsetStraightLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * 黄色的半双线。
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ROAD_WITH_YELLOW_HALF_DOUBLE_LINE = new RoadWithOffsetStraightLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);
  /**
   * <h2>角落标线</h2>
   * <h3>直角</h3>
   * 白色直角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_RA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, false);
  /**
   * 黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_RA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, false);
  /**
   * 白色加黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_Y_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL, false);
  /**
   * 白色粗线加白色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_N_RA_LINE = new RoadWithDiffAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL, false);
  /**
   * 白色粗线加黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_WT_Y_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL, false);
  /**
   * 白色加黄色双线直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl ROAD_WITH_W_YD_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE, false);
  /**
   * 白色粗线加黄色双线直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ROAD_WITH_WT_YD_RA_LINE = new RoadWithDiffAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE, false);
  /**
   * <h3>斜线</h3>
   * 白色斜线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, true);
  /**
   * 白色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_DOUBLE_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE, true);
  /**
   * 白色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_WHITE_BA_THICK_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.THICK, true);
  /**
   * 黄色斜线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, true);
  /**
   * 黄色双斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_DOUBLE_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.YELLOW, LineType.DOUBLE, true);
  /**
   * 黄色粗斜线。
   */
  @ApiStatus.AvailableSince("1.0.2")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ROAD_WITH_YELLOW_BA_THICK_LINE = new RoadWithAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.YELLOW, LineType.THICK, true);
  /**
   * <h3>有偏移的直角</h3>
   * 白色一侧向外偏移的直角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_OUT = new RoadWithAngleLineWithOnePartOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   * 白色一侧箱内偏移的直角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl ROAD_WITH_WHITE_RA_LINE_OFFSET_IN = new RoadWithAngleLineWithOnePartOffset.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   * <h2>T字形线路</h2>
   * 白色T字形线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL);
  /**
   * 黄色T字形线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_YELLOW_TS_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL);
  /**
   * <p>
   * T字形，其中单侧部分为双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_DOUBLE_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.DOUBLE);
  /**
   * T字形，其中单侧部分为粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_TS_THICK_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.THICK);
  /**
   * T字形，其中单侧部分有偏移。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.Impl ROAD_WITH_WHITE_TS_OFFSET_LINE = new RoadWithJointLineWithOffsetSide.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * T字形，直线部分为双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_DOUBLE_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.DOUBLE, LineType.NORMAL);
  /**
   * T字形，直线部分为粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WHITE_THICK_TS_LINE = new RoadWithJointLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色加白色。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_Y_TS_W_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL);
  /**
   * 白色加黄色。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_Y_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL);
  /**
   * 白色加黄色双。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_W_TS_YD_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.DOUBLE);
  /**
   * 白色粗加黄色。
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_Y_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL);
  /**
   * 白色粗加黄色双。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ROAD_WITH_WT_TS_YD_LINE = new RoadWithJointLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.DOUBLE);
  /**
   * <h2>直斜混合</h2>
   * 白色直线+斜线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WHITE_S_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * 黄色直线+斜线
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YELLOW_S_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);

  /**
   * 白色直线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_W_S_Y_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.NORMAL, LineType.NORMAL);
  /**
   * 黄色直线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_Y_S_W_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.NORMAL, LineType.NORMAL);
  /**
   * 白色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_N_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色粗线+斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_N_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.YELLOW, LineType.THICK, LineType.NORMAL);
  /**
   * 白色粗线+黄色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_WT_S_YN_BA_LINE = new RoadWithStraightAndAngleLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE, LineColor.YELLOW, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色粗线+白色斜线
   */
  @ApiStatus.AvailableSince("0.2.4")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl ROAD_WITH_YT_S_WN_BA_LINE = new RoadWithStraightAndAngleLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * <h2>十字交叉</h2>
   * 白色十字交叉线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithCrossLine.Impl ROAD_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.Impl(WHITE_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 黄色十字交叉线。
   */
  @ApiStatus.AvailableSince("0.2.0")
  @Cutout
  @RegisterIdentifier
  public static final RoadWithCrossLine.Impl ROAD_WITH_YELLOW_CROSS_LINE = new RoadWithCrossLine.Impl(YELLOW_ROAD_SETTINGS, LineColor.YELLOW);


  /**
   * <h2>自动路块</h2>
   * <p>
   * 斜线自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  @RegisterIdentifier
  @Cutout
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_BA_LINE = new RoadBlockWithAutoLine(WHITE_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.BEVEL);
  /**
   * 直角自动路块。
   */
  @RegisterIdentifier
  @Cutout
  public static final RoadBlockWithAutoLine ROAD_WITH_WHITE_AUTO_RA_LINE = new RoadBlockWithAutoLine(WHITE_ROAD_SETTINGS, RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE);
  /**
   * <h2>其他</h2>
   * <p>
   * 填满的路块。
   */
  @RegisterIdentifier
  public static final RoadBlock ROAD_FILLED_WITH_WHITE = new RoadBlock(WHITE_ROAD_SETTINGS);

  @RegisterIdentifier
  public static final RoadBlock ROAD_FILLED_WITH_YELLOW = new RoadBlock(YELLOW_ROAD_SETTINGS);
}
