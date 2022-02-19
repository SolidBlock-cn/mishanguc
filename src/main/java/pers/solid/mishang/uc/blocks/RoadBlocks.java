package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

public final class RoadBlocks extends MishangucBlocks {
  /**
   *
   *
   * <h1>道路方块部分</h1>
   *
   * 最基本的普通路块。
   */
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
      new RoadBlock(FabricBlockSettings.copyOf(ASPHALT_ROAD_SETTINGS).mapColor(MapColor.WHITE));
}
