package pers.solid.mishang.uc.blocks;

import net.minecraft.item.Items;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.block.*;

public final class LightBlocks extends MishangucBlocks {

  // 白色灯部分

  public static final FullLightBlock WHITE_LIGHT = register("white_light", settings -> new FullLightBlock(settings, Items.WHITE_DYE, Items.WHITE_CONCRETE), WHITE_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock WHITE_LIGHT_SLAB = register("white_light_slab", settings -> new LightSlabBlock(WHITE_LIGHT, settings), WHITE_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock WHITE_LIGHT_COVER = register("white_light_cover", settings -> new LightCoverBlock("white", settings), WHITE_LIGHT_SETTINGS);

  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE = register("white_small_wall_light_tube", settings -> new WallLightBlock("white", settings, false), WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT_TUBE = register("white_medium_wall_light_tube", settings -> new WallLightBlock("white", settings, false), WHITE_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE = register("white_large_wall_light_tube", settings -> new WallLightBlock("white", settings, true), WHITE_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE = register("white_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE = register("white_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("white_double_strip_wall_light_tube", settings -> new StripWallLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE = register("white_thin_strip_corner_light_tube", settings -> new CornerLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE = register("white_thick_strip_corner_light_tube", settings -> new CornerLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("white_double_strip_corner_light_tube", settings -> new CornerLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THIN_COLUMN_LIGHT_TUBE = register("white_thin_column_light_tube", settings -> new ColumnLightBlock("white", settings, 0), WHITE_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_MEDIUM_COLUMN_LIGHT_TUBE = register("white_medium_column_light_tube", settings -> new ColumnLightBlock("white", settings, 1), WHITE_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THICK_COLUMN_LIGHT_TUBE = register("white_thick_column_light_tube", settings -> new ColumnLightBlock("white", settings, 2), WHITE_LIGHT_SETTINGS);
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT = register("white_small_wall_light", settings -> new WallLightBlock("white", settings, false), WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT = register("white_medium_wall_light", settings -> new WallLightBlock("white", settings, false), WHITE_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT = register("white_large_wall_light", settings -> new WallLightBlock("white", settings, true), WHITE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT = register("white_thin_strip_wall_light", settings -> new StripWallLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT = register("white_thick_strip_wall_light", settings -> new StripWallLightBlock("white", settings), WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_THIN_COLUMN_LIGHT = register("white_thin_column_light", settings -> new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 0));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_MEDIUM_COLUMN_LIGHT = register("white_medium_column_light", settings -> new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 1));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_THICK_COLUMN_LIGHT = register("white_thick_column_light", settings -> new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 2));

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION = register("white_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("white", "simple", settings, false), WHITE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION = register("white_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("white", "point", settings, false), WHITE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION = register("white_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("white", "rhombus", settings, false), WHITE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION = register("white_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("white", "hash", settings, false), WHITE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION = register("white_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("white", "round", settings, true), WHITE_WALL_LIGHT_SETTINGS);

  // 黄色灯部分

  public static final FullLightBlock YELLOW_LIGHT = register("yellow_light", settings -> new FullLightBlock(settings, Items.YELLOW_DYE, Items.YELLOW_CONCRETE), YELLOW_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock YELLOW_LIGHT_SLAB = register("yellow_light_slab", settings -> new LightSlabBlock(YELLOW_LIGHT, settings), YELLOW_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock YELLOW_LIGHT_COVER = register("yellow_light_cover", settings -> new LightCoverBlock("yellow", settings), YELLOW_LIGHT_SETTINGS);

  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT_TUBE = register("yellow_small_wall_light_tube", settings -> new WallLightBlock("yellow", settings, false), YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT_TUBE = register("yellow_medium_wall_light_tube", settings -> new WallLightBlock("yellow", settings, false), YELLOW_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT_TUBE = register("yellow_large_wall_light_tube", settings -> new WallLightBlock("yellow", settings, true), YELLOW_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT_TUBE = register("yellow_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT_TUBE = register("yellow_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("yellow_double_strip_wall_light_tube", settings -> new StripWallLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_THIN_STRIP_CORNER_LIGHT_TUBE = register("yellow_thin_strip_corner_light_tube", settings -> new CornerLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_THICK_STRIP_CORNER_LIGHT_TUBE = register("yellow_thick_strip_corner_light_tube", settings -> new CornerLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("yellow_double_strip_corner_light_tube", settings -> new CornerLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THIN_COLUMN_LIGHT_TUBE = register("yellow_thin_column_light_tube", settings -> new ColumnLightBlock("yellow", settings, 0), YELLOW_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_MEDIUM_COLUMN_LIGHT_TUBE = register("yellow_medium_column_light_tube", settings -> new ColumnLightBlock("yellow", settings, 1), YELLOW_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THICK_COLUMN_LIGHT_TUBE = register("yellow_thick_column_light_tube", settings -> new ColumnLightBlock("yellow", settings, 2), YELLOW_LIGHT_SETTINGS);

  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT = register("yellow_small_wall_light", settings -> new WallLightBlock("yellow", settings, false), YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT = register("yellow_medium_wall_light", settings -> new WallLightBlock("yellow", settings, false), YELLOW_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT = register("yellow_large_wall_light", settings -> new WallLightBlock("yellow", settings, true), YELLOW_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT = register("yellow_thin_strip_wall_light", settings -> new StripWallLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT = register("yellow_thick_strip_wall_light", settings -> new StripWallLightBlock("yellow", settings), YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_THIN_COLUMN_LIGHT = register("yellow_thin_column_light", settings -> new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 0));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_MEDIUM_COLUMN_LIGHT = register("yellow_medium_column_light", settings -> new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 1));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_THICK_COLUMN_LIGHT = register("yellow_thick_column_light", settings -> new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 2));

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_SIMPLE_DECORATION = register("yellow_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("yellow", "simple", settings, false), YELLOW_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_POINT_DECORATION = register("yellow_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("yellow", "point", settings, false), YELLOW_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_RHOMBUS_DECORATION = register("yellow_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("yellow", "rhombus", settings, false), YELLOW_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_HASH_DECORATION = register("yellow_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("yellow", "hash", settings, false), YELLOW_WALL_LIGHT_SETTINGS);
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_ROUND_DECORATION = register("yellow_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("yellow", "round", settings, true), YELLOW_WALL_LIGHT_SETTINGS);

  // 橙色灯部分

  public static final FullLightBlock ORANGE_LIGHT = register("orange_light", settings -> new FullLightBlock(settings, Items.ORANGE_DYE, Items.ORANGE_CONCRETE), ORANGE_LIGHT_SETTINGS);
  public static final LightSlabBlock ORANGE_LIGHT_SLAB = register("orange_light_slab", settings -> new LightSlabBlock(ORANGE_LIGHT, settings), ORANGE_LIGHT_SETTINGS);
  public static final LightCoverBlock ORANGE_LIGHT_COVER = register("orange_light_cover", settings -> new LightCoverBlock("orange", settings), ORANGE_LIGHT_SETTINGS);

  public static final WallLightBlock ORANGE_SMALL_WALL_LIGHT_TUBE = register("orange_small_wall_light_tube", settings -> new WallLightBlock("orange", settings, false), ORANGE_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock ORANGE_MEDIUM_WALL_LIGHT_TUBE = register("orange_medium_wall_light_tube", settings -> new WallLightBlock("orange", settings, false), ORANGE_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock ORANGE_LARGE_WALL_LIGHT_TUBE = register("orange_large_wall_light_tube", settings -> new WallLightBlock("orange", settings, true), ORANGE_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock ORANGE_THIN_STRIP_WALL_LIGHT_TUBE = register("orange_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock ORANGE_THICK_STRIP_WALL_LIGHT_TUBE = register("orange_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock ORANGE_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("orange_double_strip_wall_light_tube", settings -> new StripWallLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_THIN_STRIP_CORNER_LIGHT_TUBE = register("orange_thin_strip_corner_light_tube", settings -> new CornerLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_THICK_STRIP_CORNER_LIGHT_TUBE = register("orange_thick_strip_corner_light_tube", settings -> new CornerLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("orange_double_strip_corner_light_tube", settings -> new CornerLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock ORANGE_THIN_COLUMN_LIGHT_TUBE = register("orange_thin_column_light_tube", settings -> new ColumnLightBlock("orange", settings, 0), ORANGE_LIGHT_SETTINGS);
  public static final ColumnLightBlock ORANGE_MEDIUM_COLUMN_LIGHT_TUBE = register("orange_medium_column_light_tube", settings -> new ColumnLightBlock("orange", settings, 1), ORANGE_LIGHT_SETTINGS);
  public static final ColumnLightBlock ORANGE_THICK_COLUMN_LIGHT_TUBE = register("orange_thick_column_light_tube", settings -> new ColumnLightBlock("orange", settings, 2), ORANGE_LIGHT_SETTINGS);

  public static final WallLightBlock ORANGE_SMALL_WALL_LIGHT = register("orange_small_wall_light", settings -> new WallLightBlock("orange", settings, false), ORANGE_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock ORANGE_MEDIUM_WALL_LIGHT = register("orange_medium_wall_light", settings -> new WallLightBlock("orange", settings, false), ORANGE_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock ORANGE_LARGE_WALL_LIGHT = register("orange_large_wall_light", settings -> new WallLightBlock("orange", settings, true), ORANGE_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock ORANGE_THIN_STRIP_WALL_LIGHT = register("orange_thin_strip_wall_light", settings -> new StripWallLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock ORANGE_THICK_STRIP_WALL_LIGHT = register("orange_thick_strip_wall_light", settings -> new StripWallLightBlock("orange", settings), ORANGE_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock ORANGE_THIN_COLUMN_LIGHT = register("orange_thin_column_light", settings -> new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 0));
  public static final ColumnWallLightBlock ORANGE_MEDIUM_COLUMN_LIGHT = register("orange_medium_column_light", settings -> new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 1));
  public static final ColumnWallLightBlock ORANGE_THICK_COLUMN_LIGHT = register("orange_thick_column_light", settings -> new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 2));

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_SIMPLE_DECORATION = register("orange_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("orange", "simple", settings, false), ORANGE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_POINT_DECORATION = register("orange_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("orange", "point", settings, false), ORANGE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_RHOMBUS_DECORATION = register("orange_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("orange", "rhombus", settings, false), ORANGE_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_HASH_DECORATION = register("orange_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("orange", "hash", settings, false), ORANGE_WALL_LIGHT_SETTINGS);
  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_ROUND_DECORATION = register("orange_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("orange", "round", settings, true), ORANGE_WALL_LIGHT_SETTINGS);

  // 绿色灯部分

  public static final FullLightBlock GREEN_LIGHT = register("green_light", settings -> new FullLightBlock(settings, Items.LIME_DYE, Items.LIME_CONCRETE), GREEN_LIGHT_SETTINGS);
  public static final LightSlabBlock GREEN_LIGHT_SLAB = register("green_light_slab", settings -> new LightSlabBlock(GREEN_LIGHT, settings), GREEN_LIGHT_SETTINGS);
  public static final LightCoverBlock GREEN_LIGHT_COVER = register("green_light_cover", settings -> new LightCoverBlock("green", settings), GREEN_LIGHT_SETTINGS);

  public static final WallLightBlock GREEN_SMALL_WALL_LIGHT_TUBE = register("green_small_wall_light_tube", settings -> new WallLightBlock("green", settings, false), GREEN_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock GREEN_MEDIUM_WALL_LIGHT_TUBE = register("green_medium_wall_light_tube", settings -> new WallLightBlock("green", settings, false), GREEN_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock GREEN_LARGE_WALL_LIGHT_TUBE = register("green_large_wall_light_tube", settings -> new WallLightBlock("green", settings, true), GREEN_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock GREEN_THIN_STRIP_WALL_LIGHT_TUBE = register("green_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock GREEN_THICK_STRIP_WALL_LIGHT_TUBE = register("green_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock GREEN_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("green_double_strip_wall_light_tube", settings -> new StripWallLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_THIN_STRIP_CORNER_LIGHT_TUBE = register("green_thin_strip_corner_light_tube", settings -> new CornerLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_THICK_STRIP_CORNER_LIGHT_TUBE = register("green_thick_strip_corner_light_tube", settings -> new CornerLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("green_double_strip_corner_light_tube", settings -> new CornerLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock GREEN_THIN_COLUMN_LIGHT_TUBE = register("green_thin_column_light_tube", settings -> new ColumnLightBlock("green", settings, 0), GREEN_LIGHT_SETTINGS);
  public static final ColumnLightBlock GREEN_MEDIUM_COLUMN_LIGHT_TUBE = register("green_medium_column_light_tube", settings -> new ColumnLightBlock("green", settings, 1), GREEN_LIGHT_SETTINGS);
  public static final ColumnLightBlock GREEN_THICK_COLUMN_LIGHT_TUBE = register("green_thick_column_light_tube", settings -> new ColumnLightBlock("green", settings, 2), GREEN_LIGHT_SETTINGS);

  public static final WallLightBlock GREEN_SMALL_WALL_LIGHT = register("green_small_wall_light", settings -> new WallLightBlock("green", settings, false), GREEN_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock GREEN_MEDIUM_WALL_LIGHT = register("green_medium_wall_light", settings -> new WallLightBlock("green", settings, false), GREEN_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock GREEN_LARGE_WALL_LIGHT = register("green_large_wall_light", settings -> new WallLightBlock("green", settings, true), GREEN_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock GREEN_THIN_STRIP_WALL_LIGHT = register("green_thin_strip_wall_light", settings -> new StripWallLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock GREEN_THICK_STRIP_WALL_LIGHT = register("green_thick_strip_wall_light", settings -> new StripWallLightBlock("green", settings), GREEN_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock GREEN_THIN_COLUMN_LIGHT = register("green_thin_column_light", settings -> new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 0));
  public static final ColumnWallLightBlock GREEN_MEDIUM_COLUMN_LIGHT = register("green_medium_column_light", settings -> new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 1));
  public static final ColumnWallLightBlock GREEN_THICK_COLUMN_LIGHT = register("green_thick_column_light", settings -> new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 2));

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_SIMPLE_DECORATION = register("green_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("green", "simple", settings, false), GREEN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_POINT_DECORATION = register("green_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("green", "point", settings, false), GREEN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_RHOMBUS_DECORATION = register("green_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("green", "rhombus", settings, false), GREEN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_HASH_DECORATION = register("green_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("green", "hash", settings, false), GREEN_WALL_LIGHT_SETTINGS);
  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_ROUND_DECORATION = register("green_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("green", "round", settings, true), GREEN_WALL_LIGHT_SETTINGS);

  // 青色灯部分

  public static final FullLightBlock CYAN_LIGHT = register("cyan_light", settings -> new FullLightBlock(settings, Items.CYAN_DYE, Items.CYAN_CONCRETE), CYAN_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock CYAN_LIGHT_SLAB = register("cyan_light_slab", settings -> new LightSlabBlock(CYAN_LIGHT, settings), CYAN_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock CYAN_LIGHT_COVER = register("cyan_light_cover", settings -> new LightCoverBlock("cyan", settings), CYAN_LIGHT_SETTINGS);

  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT_TUBE = register("cyan_small_wall_light_tube", settings -> new WallLightBlock("cyan", settings, false), CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT_TUBE = register("cyan_medium_wall_light_tube", settings -> new WallLightBlock("cyan", settings, false), CYAN_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT_TUBE = register("cyan_large_wall_light_tube", settings -> new WallLightBlock("cyan", settings, true), CYAN_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT_TUBE = register("cyan_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT_TUBE = register("cyan_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("cyan_double_strip_wall_light_tube", settings -> new StripWallLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_THIN_STRIP_CORNER_LIGHT_TUBE = register("cyan_thin_strip_corner_light_tube", settings -> new CornerLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_THICK_STRIP_CORNER_LIGHT_TUBE = register("cyan_thick_strip_corner_light_tube", settings -> new CornerLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("cyan_double_strip_corner_light_tube", settings -> new CornerLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THIN_COLUMN_LIGHT_TUBE = register("cyan_thin_column_light_tube", settings -> new ColumnLightBlock("cyan", settings, 0), CYAN_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_MEDIUM_COLUMN_LIGHT_TUBE = register("cyan_medium_column_light_tube", settings -> new ColumnLightBlock("cyan", settings, 1), CYAN_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THICK_COLUMN_LIGHT_TUBE = register("cyan_thick_column_light_tube", settings -> new ColumnLightBlock("cyan", settings, 2), CYAN_LIGHT_SETTINGS);

  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT = register("cyan_small_wall_light", settings -> new WallLightBlock("cyan", settings, false), CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT = register("cyan_medium_wall_light", settings -> new WallLightBlock("cyan", settings, false), CYAN_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT = register("cyan_large_wall_light", settings -> new WallLightBlock("cyan", settings, true), CYAN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT = register("cyan_thin_strip_wall_light", settings -> new StripWallLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT = register("cyan_thick_strip_wall_light", settings -> new StripWallLightBlock("cyan", settings), CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_THIN_COLUMN_LIGHT = register("cyan_thin_column_light", settings -> new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 0));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_MEDIUM_COLUMN_LIGHT = register("cyan_medium_column_light", settings -> new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 1));
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_THICK_COLUMN_LIGHT = register("cyan_thick_column_light", settings -> new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 2));
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_SIMPLE_DECORATION = register("cyan_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("cyan", "simple", settings, false), CYAN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_POINT_DECORATION = register("cyan_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("cyan", "point", settings, false), CYAN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_RHOMBUS_DECORATION = register("cyan_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("cyan", "rhombus", settings, false), CYAN_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_HASH_DECORATION = register("cyan_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("cyan", "hash", settings, false), CYAN_WALL_LIGHT_SETTINGS);
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_ROUND_DECORATION = register("cyan_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("cyan", "round", settings, true), CYAN_WALL_LIGHT_SETTINGS);

  // 粉色灯部分

  public static final FullLightBlock PINK_LIGHT = register("pink_light", settings -> new FullLightBlock(settings, Items.PINK_DYE, Items.PINK_CONCRETE), PINK_LIGHT_SETTINGS);
  public static final LightSlabBlock PINK_LIGHT_SLAB = register("pink_light_slab", settings -> new LightSlabBlock(PINK_LIGHT, settings), PINK_LIGHT_SETTINGS);
  public static final LightCoverBlock PINK_LIGHT_COVER = register("pink_light_cover", settings -> new LightCoverBlock("pink", settings), PINK_LIGHT_SETTINGS);

  public static final WallLightBlock PINK_SMALL_WALL_LIGHT_TUBE = register("pink_small_wall_light_tube", settings -> new WallLightBlock("pink", settings, false), PINK_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock PINK_MEDIUM_WALL_LIGHT_TUBE = register("pink_medium_wall_light_tube", settings -> new WallLightBlock("pink", settings, false), PINK_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock PINK_LARGE_WALL_LIGHT_TUBE = register("pink_large_wall_light_tube", settings -> new WallLightBlock("pink", settings, true), PINK_WALL_LIGHT_SETTINGS);


  public static final StripWallLightBlock PINK_THIN_STRIP_WALL_LIGHT_TUBE = register("pink_thin_strip_wall_light_tube", settings -> new StripWallLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock PINK_THICK_STRIP_WALL_LIGHT_TUBE = register("pink_thick_strip_wall_light_tube", settings -> new StripWallLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock PINK_DOUBLE_STRIP_WALL_LIGHT_TUBE = register("pink_double_strip_wall_light_tube", settings -> new StripWallLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_THIN_STRIP_CORNER_LIGHT_TUBE = register("pink_thin_strip_corner_light_tube", settings -> new CornerLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_THICK_STRIP_CORNER_LIGHT_TUBE = register("pink_thick_strip_corner_light_tube", settings -> new CornerLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_DOUBLE_STRIP_CORNER_LIGHT_TUBE = register("pink_double_strip_corner_light_tube", settings -> new CornerLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock PINK_THIN_COLUMN_LIGHT_TUBE = register("pink_thin_column_light_tube", settings -> new ColumnLightBlock("pink", settings, 0), PINK_LIGHT_SETTINGS);
  public static final ColumnLightBlock PINK_MEDIUM_COLUMN_LIGHT_TUBE = register("pink_medium_column_light_tube", settings -> new ColumnLightBlock("pink", settings, 1), PINK_LIGHT_SETTINGS);
  public static final ColumnLightBlock PINK_THICK_COLUMN_LIGHT_TUBE = register("pink_thick_column_light_tube", settings -> new ColumnLightBlock("pink", settings, 2), PINK_LIGHT_SETTINGS);

  public static final WallLightBlock PINK_SMALL_WALL_LIGHT = register("pink_small_wall_light", settings -> new WallLightBlock("pink", settings, false), PINK_WALL_LIGHT_SETTINGS);
  public static final WallLightBlock PINK_MEDIUM_WALL_LIGHT = register("pink_medium_wall_light", settings -> new WallLightBlock("pink", settings, false), PINK_WALL_LIGHT_SETTINGS);

  public static final WallLightBlock PINK_LARGE_WALL_LIGHT = register("pink_large_wall_light", settings -> new WallLightBlock("pink", settings, true), PINK_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock PINK_THIN_STRIP_WALL_LIGHT = register("pink_thin_strip_wall_light", settings -> new StripWallLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock PINK_THICK_STRIP_WALL_LIGHT = register("pink_thick_strip_wall_light", settings -> new StripWallLightBlock("pink", settings), PINK_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock PINK_THIN_COLUMN_LIGHT = register("pink_thin_column_light", settings -> new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 0));
  public static final ColumnWallLightBlock PINK_MEDIUM_COLUMN_LIGHT = register("pink_medium_column_light", settings -> new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 1));
  public static final ColumnWallLightBlock PINK_THICK_COLUMN_LIGHT = register("pink_thick_column_light", settings -> new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 2));

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_SIMPLE_DECORATION = register("pink_wall_light_simple_decoration", settings -> new AutoConnectWallLightBlock("pink", "simple", settings, false), PINK_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_POINT_DECORATION = register("pink_wall_light_point_decoration", settings -> new AutoConnectWallLightBlock("pink", "point", settings, false), PINK_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_RHOMBUS_DECORATION = register("pink_wall_light_rhombus_decoration", settings -> new AutoConnectWallLightBlock("pink", "rhombus", settings, false), PINK_WALL_LIGHT_SETTINGS);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_HASH_DECORATION = register("pink_wall_light_hash_decoration", settings -> new AutoConnectWallLightBlock("pink", "hash", settings, false), PINK_WALL_LIGHT_SETTINGS);
  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_ROUND_DECORATION = register("pink_wall_light_round_decoration", settings -> new AutoConnectWallLightBlock("pink", "round", settings, true), PINK_WALL_LIGHT_SETTINGS);
}
