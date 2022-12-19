package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.block.RoadMarkBlock;

/**
 * 本模组的所有道路标记方块。
 */
@ApiStatus.AvailableSince("1.1.0")
public class RoadMarkBlocks extends MishangucBlocks {
  private static final FabricBlockSettings ROAD_MARK_SETTINGS = FabricBlockSettings.of(Material.STONE).strength(0.5f).mapColor(MapColor.WHITE).nonOpaque().collidable(false);
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_MARK = directional("arrow_straight");
  @Cutout
  public static final RoadMarkBlock ARROW_LEFT_MARK = directional("arrow_left");
  @Cutout
  public static final RoadMarkBlock ARROW_RIGHT_MARK = directional("arrow_right");
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_LEFT_MARK = directional("arrow_straight_left");
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_RIGHT_MARK = directional("arrow_straight_right");
  @Cutout
  public static final RoadMarkBlock ARROW_UTURN_LEFT_MARK = directional("arrow_uturn_left");
  @Cutout
  public static final RoadMarkBlock ARROW_UTURN_RIGHT_MARK = directional("arrow_uturn_right");
  @Cutout
  public static final RoadMarkBlock ARROW_LEFT_RIGHT_MARK = directional("arrow_left_right");
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_LEFT_RIGHT_MARK = directional("arrow_straight_left_right");
  @Cutout
  public static final RoadMarkBlock ARROW_LEFT_MERGE_MARK = directional("arrow_left_merge");
  @Cutout
  public static final RoadMarkBlock ARROW_RIGHT_MERGE_MARK = directional("arrow_right_merge");
  @Cutout
  public static final RoadMarkBlock ARROW_LEFT_UTURN_MARK = directional("arrow_left_uturn");
  @Cutout
  public static final RoadMarkBlock ARROW_RIGHT_UTURN_MARK = directional("arrow_right_uturn");
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_UTURN_LEFT_MARK = directional("arrow_straight_uturn_left");
  @Cutout
  public static final RoadMarkBlock ARROW_STRAIGHT_UTURN_RIGHT_MARK = directional("arrow_straight_uturn_right");
  @Cutout
  public static final RoadMarkBlock DECELERATION_DOUBLE_LINE_MARK = axis("deceleration_double_line");
  @Cutout
  public static final RoadMarkBlock DECELERATION_TRIPLE_LINE_MARK = axis("deceleration_triple_line");
  @Cutout
  public static final RoadMarkBlock DECELERATION_CROSSROADS_MARK = axis("deceleration_crossroads");
  @Cutout
  public static final RoadMarkBlock LANE_DISABLED_MARK = directional("lane_disabled");
  @Cutout
  public static final RoadMarkBlock LANE_NON_VEHICLE_MARK = directional("lane_non_vehicle");

  private static RoadMarkBlock directional(String name) {
    return RoadMarkBlock.createDirectionalFacing("mishanguc:block/" + name, ROAD_MARK_SETTINGS);
  }

  private static RoadMarkBlock axis(String name) {
    return RoadMarkBlock.createAxisFacing("mishanguc:block/" + name, ROAD_MARK_SETTINGS);
  }

  /**
   * 用于 {@link RoadMarkBlock.AxisFacing#mirror}，对方块进行翻转时，需要考虑。
   */
  @SuppressWarnings("JavadocReference")
  public static final BiMap<RoadMarkBlock, RoadMarkBlock> LEFT_TO_RIGHT = ImmutableBiMap.<RoadMarkBlock, RoadMarkBlock>builder()
      .put(ARROW_LEFT_MARK, ARROW_RIGHT_MARK)
      .put(ARROW_STRAIGHT_LEFT_MARK, ARROW_STRAIGHT_RIGHT_MARK)
      .put(ARROW_UTURN_LEFT_MARK, ARROW_UTURN_RIGHT_MARK)
      .put(ARROW_STRAIGHT_UTURN_LEFT_MARK, ARROW_STRAIGHT_UTURN_RIGHT_MARK)
      .put(ARROW_LEFT_UTURN_MARK, ARROW_RIGHT_UTURN_MARK)
      .put(ARROW_LEFT_MERGE_MARK, ARROW_RIGHT_MERGE_MARK)
      .build();
}
