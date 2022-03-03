package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import pers.solid.mishang.uc.RoadTexture;
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
  public static final RoadBlock ASPHALT_ROAD_BLOCK = new RoadBlock(ASPHALT_ROAD_SETTINGS);
  /**
   * <h2>单直线道路</h2>
   * <p>
   * 白色直线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL);
  /**
   * 白色直角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, false);
  /**
   * 白色斜角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineType.NORMAL, true);
  /**
   * 黄色直线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL);
  /**
   * 黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_RIGHT_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, false);
  /**
   * 黄色斜角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLine.Impl ASPHALT_ROAD_WITH_YELLOW_BEVEL_ANGLE_LINE =
      new RoadWithAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, LineType.NORMAL, true);
  /**
   * 白色加黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_AND_YELLOW_RIGHT_ANGLE_LINE =
      new RoadWithDiffAngleLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.NORMAL,
          LineType.NORMAL,
          false);
  /**
   * 白色粗线加白色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_THICK_AND_NORMAL_RIGHT_ANGLE_LINE =
      new RoadWithDiffAngleLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.WHITE,
          LineType.THICK,
          LineType.NORMAL,
          false);
  /**
   * 白色粗线加黄色直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_THICK_AND_YELLOW_RIGHT_ANGLE_LINE =
      new RoadWithDiffAngleLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.THICK,
          LineType.NORMAL,
          false);
  /**
   * 白色加黄色双线直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE =
      new RoadWithDiffAngleLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.NORMAL,
          LineType.DOUBLE,
          false);
  /**
   * 白色粗线加黄色双线直角
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithDiffAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_THICK_AND_YELLOW_DOUBLE_RIGHT_ANGLE_LINE =
      new RoadWithDiffAngleLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.THICK,
          LineType.DOUBLE,
          false);
  /**
   * <h2>由两条线组成的道路</h2>
   * <p>
   * 白色直角+斜角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightAndAngleLine.Impl
      ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
      new RoadWithStraightAndAngleLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 白色T字形线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.WHITE,
          LineType.NORMAL,
          LineType.NORMAL);
  /**
   * 白色十字交叉线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithCrossLine.Impl ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =
      new RoadWithCrossLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * <h2>带有特殊线的单线道路</h2>
   * <p>
   * 偏移的直线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_OFFSET_STRAIGHT_LINE =
      new RoadWithOffsetStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineType.DOUBLE);
  /**
   * 粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineType.THICK);
  /**
   * 偏移的黄线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithOffsetStraightLine.Impl
      ASPHALT_ROAD_WITH_YELLOW_OFFSET_STRAIGHT_LINE =
      new RoadWithOffsetStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW);
  /**
   * 双黄线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_DOUBLE_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, LineType.DOUBLE);
  /**
   * 粗黄线
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithStraightLine.Impl ASPHALT_ROAD_WITH_YELLOW_STRAIGHT_THICK_LINE =
      new RoadWithStraightLine.Impl(ASPHALT_ROAD_SETTINGS, LineColor.YELLOW, LineType.THICK);
  /**
   * 一侧偏移的直角。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_OUT =
      new RoadWithAngleLineWithOnePartOffset.Impl(
          ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);

  @Cutout
  @RegisterIdentifier
  public static final RoadWithAngleLineWithOnePartOffset.Impl
      ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE_WITH_ONE_PART_OFFSET_IN =
      new RoadWithAngleLineWithOnePartOffset.Impl(
          ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /**
   * <h2>带有特殊线的双线道路</h2>
   * <p>
   * T字形，其中单侧部分为双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.WHITE,
          LineType.NORMAL,
          LineType.DOUBLE);
  /**
   * T字形，其中单侧部分为粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.NORMAL, LineType.THICK);
  /**
   * T字形，其中单侧部分有偏移。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLineWithOffsetSide.Impl
      ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_OFFSET_SIDE =
      new RoadWithJointLineWithOffsetSide.Impl(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /**
   * 直线部分为双线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_DOUBLE_JOINT_LINE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.WHITE,
          LineType.DOUBLE,
          LineType.NORMAL);
  /**
   * 直线部分为粗线。
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_THICK_JOINT_LINE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS, LineColor.WHITE, LineColor.WHITE, LineType.THICK, LineType.NORMAL);
  /**
   * 黄色T字形
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_YELLOW_JOINT_LINE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.YELLOW,
          LineColor.YELLOW,
          LineType.NORMAL,
          LineType.NORMAL);
  /**
   * 黄色加白色
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_YELLOW_JOINT_LINE_WITH_WHITE_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.YELLOW,
          LineColor.WHITE,
          LineType.NORMAL,
          LineType.NORMAL);
  /**
   * 白色加黄色
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_YELLOW_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.NORMAL,
          LineType.NORMAL);
  /**
   * 白色加黄色双
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl
      ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.NORMAL,
          LineType.DOUBLE);
  /**
   * 白色粗加黄色双
   */
  @Cutout
  @RegisterIdentifier
  public static final RoadWithJointLine.Impl
      ASPHALT_ROAD_WITH_WHITE_THICK_JOINT_LINE_WITH_YELLOW_DOUBLE_SIDE =
      new RoadWithJointLine.Impl(
          ASPHALT_ROAD_SETTINGS,
          LineColor.WHITE,
          LineColor.YELLOW,
          LineType.THICK,
          LineType.DOUBLE);

  /**
   * <h2>自动路块</h2>
   * <p>
   * 斜角自动路块。放置后遇到方块更新会自动确定线路走向。
   */
  @RegisterIdentifier
  @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /**
   * 直角自动路块。
   */
  @RegisterIdentifier
  @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /**
   * <h2>其他</h2>
   * <p>
   * 填满的路块。
   */
  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_WHITE =
      new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).mapColor(MapColor.WHITE));

  @RegisterIdentifier
  public static final RoadBlock ASPHALT_ROAD_FILLED_WITH_YELLOW =
      new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_BLOCK).mapColor(MapColor.YELLOW));
}
