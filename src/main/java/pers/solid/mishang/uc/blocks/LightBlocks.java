package pers.solid.mishang.uc.blocks;

import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.brrp.v1.generator.BRRPCubeBlock;
import pers.solid.mishang.uc.block.*;

public final class LightBlocks extends MishangucBlocks {

  public static final BRRPCubeBlock WHITE_LIGHT = new BRRPCubeBlock(WHITE_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/white_light")));
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock WHITE_LIGHT_SLAB = new LightSlabBlock(WHITE_LIGHT);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock WHITE_LIGHT_COVER = new LightCoverBlock("white", WHITE_LIGHT_SETTINGS);

  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT_TUBE = new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock WHITE_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("white", WHITE_LIGHT_SETTINGS, 2);
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock WHITE_MEDIUM_WALL_LIGHT = new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS, true);

  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock WHITE_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("white", WHITE_LIGHT_SETTINGS, 2);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("white", "simple", WHITE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("white", "point", WHITE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("white", "rhombus", WHITE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("white", "hash", WHITE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("white", "round", WHITE_WALL_LIGHT_SETTINGS, true);
  public static final BRRPCubeBlock YELLOW_LIGHT = new BRRPCubeBlock(YELLOW_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/yellow_light")));
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock YELLOW_LIGHT_SLAB = new LightSlabBlock(YELLOW_LIGHT);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock YELLOW_LIGHT_COVER = new LightCoverBlock("yellow", YELLOW_LIGHT_SETTINGS);

  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock YELLOW_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock YELLOW_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock YELLOW_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 2);

  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock YELLOW_MEDIUM_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS, true);
  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock YELLOW_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock YELLOW_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("yellow", YELLOW_LIGHT_SETTINGS, 2);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("yellow", "simple", YELLOW_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("yellow", "point", YELLOW_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("yellow", "rhombus", YELLOW_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("yellow", "hash", YELLOW_WALL_LIGHT_SETTINGS, false);
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("yellow", "round", YELLOW_WALL_LIGHT_SETTINGS, true);
  public static final BRRPCubeBlock ORANGE_LIGHT = new BRRPCubeBlock(ORANGE_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/orange_light")));
  public static final LightSlabBlock ORANGE_LIGHT_SLAB = new LightSlabBlock(ORANGE_LIGHT);
  public static final LightCoverBlock ORANGE_LIGHT_COVER = new LightCoverBlock("orange", ORANGE_LIGHT_SETTINGS);

  public static final WallLightBlock ORANGE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock ORANGE_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock ORANGE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock ORANGE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock ORANGE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock ORANGE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock ORANGE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock ORANGE_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("orange", ORANGE_LIGHT_SETTINGS, 0);
  public static final ColumnLightBlock ORANGE_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("orange", ORANGE_LIGHT_SETTINGS, 1);
  public static final ColumnLightBlock ORANGE_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("orange", ORANGE_LIGHT_SETTINGS, 2);

  public static final WallLightBlock ORANGE_SMALL_WALL_LIGHT =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock ORANGE_MEDIUM_WALL_LIGHT =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock ORANGE_LARGE_WALL_LIGHT =
      new WallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS, true);
  public static final StripWallLightBlock ORANGE_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock ORANGE_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("orange", ORANGE_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock ORANGE_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 0);
  public static final ColumnWallLightBlock ORANGE_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 1);
  public static final ColumnWallLightBlock ORANGE_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("orange", ORANGE_LIGHT_SETTINGS, 2);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("orange", "simple", ORANGE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("orange", "point", ORANGE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("orange", "rhombus", ORANGE_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("orange", "hash", ORANGE_WALL_LIGHT_SETTINGS, false);
  public static final AutoConnectWallLightBlock ORANGE_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("orange", "round", ORANGE_WALL_LIGHT_SETTINGS, true);
  public static final BRRPCubeBlock GREEN_LIGHT = new BRRPCubeBlock(GREEN_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/green_light")));
  public static final LightSlabBlock GREEN_LIGHT_SLAB = new LightSlabBlock(GREEN_LIGHT);
  public static final LightCoverBlock GREEN_LIGHT_COVER = new LightCoverBlock("green", GREEN_LIGHT_SETTINGS);

  public static final WallLightBlock GREEN_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock GREEN_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock GREEN_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock GREEN_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock GREEN_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock GREEN_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock GREEN_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock GREEN_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("green", GREEN_LIGHT_SETTINGS, 0);
  public static final ColumnLightBlock GREEN_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("green", GREEN_LIGHT_SETTINGS, 1);
  public static final ColumnLightBlock GREEN_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("green", GREEN_LIGHT_SETTINGS, 2);

  public static final WallLightBlock GREEN_SMALL_WALL_LIGHT =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock GREEN_MEDIUM_WALL_LIGHT =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock GREEN_LARGE_WALL_LIGHT =
      new WallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS, true);
  public static final StripWallLightBlock GREEN_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock GREEN_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("green", GREEN_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock GREEN_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 0);
  public static final ColumnWallLightBlock GREEN_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 1);
  public static final ColumnWallLightBlock GREEN_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("green", GREEN_LIGHT_SETTINGS, 2);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("green", "simple", GREEN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("green", "point", GREEN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("green", "rhombus", GREEN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("green", "hash", GREEN_WALL_LIGHT_SETTINGS, false);
  public static final AutoConnectWallLightBlock GREEN_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("green", "round", GREEN_WALL_LIGHT_SETTINGS, true);

  public static final BRRPCubeBlock CYAN_LIGHT = new BRRPCubeBlock(CYAN_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/cyan_light")));
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightSlabBlock CYAN_LIGHT_SLAB = new LightSlabBlock(CYAN_LIGHT);
  @ApiStatus.AvailableSince("1.1.0")
  public static final LightCoverBlock CYAN_LIGHT_COVER = new LightCoverBlock("cyan", CYAN_LIGHT_SETTINGS);

  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock CYAN_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock CYAN_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnLightBlock CYAN_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("cyan", CYAN_LIGHT_SETTINGS, 2);

  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);
  @ApiStatus.AvailableSince("1.1.0")
  public static final WallLightBlock CYAN_MEDIUM_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS, true);

  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final StripWallLightBlock CYAN_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 0);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 1);
  @ApiStatus.AvailableSince("1.1.0")
  public static final ColumnWallLightBlock CYAN_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("cyan", CYAN_LIGHT_SETTINGS, 2);
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("cyan", "simple", CYAN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("cyan", "point", CYAN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("cyan", "rhombus", CYAN_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("cyan", "hash", CYAN_WALL_LIGHT_SETTINGS, false);
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("cyan", "round", CYAN_WALL_LIGHT_SETTINGS, true);
  public static final BRRPCubeBlock PINK_LIGHT = new BRRPCubeBlock(PINK_LIGHT_SETTINGS, Identifier.of("mishanguc:block/light"), TextureMap.all(Identifier.of("mishanguc:block/pink_light")));
  public static final LightSlabBlock PINK_LIGHT_SLAB = new LightSlabBlock(PINK_LIGHT);
  public static final LightCoverBlock PINK_LIGHT_COVER = new LightCoverBlock("pink", PINK_LIGHT_SETTINGS);

  public static final WallLightBlock PINK_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock PINK_MEDIUM_WALL_LIGHT_TUBE =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock PINK_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, true);


  public static final StripWallLightBlock PINK_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock PINK_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);

  public static final StripWallLightBlock PINK_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);

  public static final CornerLightBlock PINK_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);
  public static final ColumnLightBlock PINK_THIN_COLUMN_LIGHT_TUBE = new ColumnLightBlock("pink", PINK_LIGHT_SETTINGS, 0);
  public static final ColumnLightBlock PINK_MEDIUM_COLUMN_LIGHT_TUBE = new ColumnLightBlock("pink", PINK_LIGHT_SETTINGS, 1);
  public static final ColumnLightBlock PINK_THICK_COLUMN_LIGHT_TUBE = new ColumnLightBlock("pink", PINK_LIGHT_SETTINGS, 2);

  public static final WallLightBlock PINK_SMALL_WALL_LIGHT =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, false);
  public static final WallLightBlock PINK_MEDIUM_WALL_LIGHT =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, false);

  public static final WallLightBlock PINK_LARGE_WALL_LIGHT =
      new WallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS, true);
  public static final StripWallLightBlock PINK_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);
  public static final StripWallLightBlock PINK_THICK_STRIP_WALL_LIGHT =
      new StripWallLightBlock("pink", PINK_WALL_LIGHT_SETTINGS);
  public static final ColumnWallLightBlock PINK_THIN_COLUMN_LIGHT = new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 0);
  public static final ColumnWallLightBlock PINK_MEDIUM_COLUMN_LIGHT = new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 1);
  public static final ColumnWallLightBlock PINK_THICK_COLUMN_LIGHT = new ColumnWallLightBlock("pink", PINK_LIGHT_SETTINGS, 2);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("pink", "simple", PINK_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("pink", "point", PINK_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("pink", "rhombus", PINK_WALL_LIGHT_SETTINGS, false);

  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("pink", "hash", PINK_WALL_LIGHT_SETTINGS, false);
  public static final AutoConnectWallLightBlock PINK_WALL_LIGHT_ROUND_DECORATION = new AutoConnectWallLightBlock("pink", "round", PINK_WALL_LIGHT_SETTINGS, true);
}
