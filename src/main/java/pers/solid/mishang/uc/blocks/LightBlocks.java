package pers.solid.mishang.uc.blocks;

import net.devtech.arrp.generator.BRRPCubeBlock;
import net.devtech.arrp.json.models.JTextures;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.*;

public final class LightBlocks extends MishangucBlocks {

  @RegisterIdentifier
  public static final BRRPCubeBlock WHITE_LIGHT = new BRRPCubeBlock(WHITE_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/white_light"));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock WHITE_LIGHT_SLAB = new LightSlabBlock(WHITE_LIGHT);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock WHITE_LIGHT_COVER = new LightCoverBlock("white", WHITE_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT_TUBE = new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);


  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);
  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT = new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock WHITE_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock WHITE_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock WHITE_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("white", "simple", WHITE_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("white", "point", WHITE_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("white", "rhombus", WHITE_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("white", "hash", WHITE_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("white", "round", WHITE_WALL_LIGHT_SETTINGS, true);
  @RegisterIdentifier
  public static final BRRPCubeBlock YELLOW_LIGHT = new BRRPCubeBlock(YELLOW_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/yellow_light"));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock YELLOW_LIGHT_SLAB = new LightSlabBlock(YELLOW_LIGHT);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock YELLOW_LIGHT_COVER = new LightCoverBlock("yellow", YELLOW_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);


  @RegisterIdentifier
  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock YELLOW_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock YELLOW_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock YELLOW_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);
  @RegisterIdentifier
  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock YELLOW_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock YELLOW_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock YELLOW_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("yellow", "simple", YELLOW_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("yellow", "point", YELLOW_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("yellow", "rhombus", YELLOW_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("yellow", "hash", YELLOW_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("yellow", "round", YELLOW_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final BRRPCubeBlock CYAN_LIGHT = new BRRPCubeBlock(CYAN_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/cyan_light"));
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock CYAN_LIGHT_SLAB = new LightSlabBlock(CYAN_LIGHT);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock CYAN_LIGHT_COVER = new LightCoverBlock("cyan", CYAN_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);


  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock CYAN_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock CYAN_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock CYAN_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);

  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock CYAN_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock CYAN_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  @RegisterIdentifier
  public static final ColumnWallLightBlock CYAN_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);
  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("cyan", "simple", CYAN_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("cyan", "point", CYAN_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("cyan", "rhombus", CYAN_WALL_LIGHT_SETTINGS, false);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("cyan", "hash", CYAN_WALL_LIGHT_SETTINGS, false);
  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("cyan", "round", CYAN_WALL_LIGHT_SETTINGS, true);
}
