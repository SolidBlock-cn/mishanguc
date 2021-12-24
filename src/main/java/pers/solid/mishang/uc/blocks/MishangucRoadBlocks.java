package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import pers.solid.mishang.uc.LineColor;
import pers.solid.mishang.uc.RoadTexture;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.InGroup;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

public class MishangucRoadBlocks extends MishangucBlocks {
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
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色直角。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE =
      new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, false);
  /** 白色斜角。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithAngleLine ASPHALT_ROAD_WITH_WHITE_BEVEL_ANGLE_LINE =
      new RoadBlockWithAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE, true);

  /**
   *
   *
   * <h2>由两条线组成的道路</h2>
   *
   * 白色直角+斜角。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightAndAngleLine
      ASPHALT_ROAD_WITH_WHITE_STRAIGHT_AND_BEVEL_ANGLE_LINE =
          new RoadBlockWithStraightAndAngleLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色T字形线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 白色十字交叉线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithCrossLine ASPHALT_ROAD_WITH_WHITE_CROSS_LINE =
      new RoadBlockWithCrossLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的单线道路</h2>
   *
   * 侧边线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithOffsetStraightLine ASPHALT_ROAD_WITH_WHITE_SIDE_LINE =
      new RoadBlockWithOffsetStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 双线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_DOUBLE_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** 粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithStraightLine ASPHALT_ROAD_WITH_WHITE_STRAIGHT_THICK_LINE =
      new RoadBlockWithStraightLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

  /**
   *
   *
   * <h2>带有特殊线的双线道路</h2>
   *
   * T字形，其中单侧部分为双线。
   */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_DOUBLE_SIDE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);
  /** T字形，其中单侧部分为粗线。 */
  @Cutout @RegisterIdentifier
  public static final RoadBlockWithJointLine ASPHALT_ROAD_WITH_WHITE_JOINT_LINE_WITH_THICK_SIDE =
      new RoadBlockWithJointLine(ASPHALT_ROAD_SETTINGS, LineColor.WHITE);

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

  /** 斜角自动路块。放置后遇到方块更新会自动确定线路走向。 */
  @RegisterIdentifier @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_BEVEL_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.BEVEL,
          RoadTexture.ASPHALT,
          LineColor.WHITE);
  /** 直角自动路块。 */
  @RegisterIdentifier @Cutout
  public static final RoadBlockWithAutoLine ASPHALT_ROAD_WITH_WHITE_AUTO_RIGHT_ANGLE_LINE =
      new RoadBlockWithAutoLine(
          ASPHALT_ROAD_SETTINGS,
          RoadWithAutoLine.RoadAutoLineType.RIGHT_ANGLE,
          RoadTexture.ASPHALT,
          LineColor.WHITE);

  static {
    registerAll(MishangucRoadBlocks.class);
  }
}
