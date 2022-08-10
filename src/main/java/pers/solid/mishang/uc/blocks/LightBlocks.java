package pers.solid.mishang.uc.blocks;

import net.devtech.arrp.generator.BRRPCubeBlock;
import net.devtech.arrp.json.models.JTextures;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.block.AutoConnectWallLightBlock;
import pers.solid.mishang.uc.block.CornerLightBlock;
import pers.solid.mishang.uc.block.StripWallLightBlock;
import pers.solid.mishang.uc.block.WallLightBlock;

import java.util.Map;

public final class LightBlocks extends MishangucBlocks {

  @RegisterIdentifier
  public static final BRRPCubeBlock WHITE_LIGHT = new BRRPCubeBlock(WHITE_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/white_light"));

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

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
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock("white", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("white", "simple", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("white", "point", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("white", "rhombus", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("white", "hash", WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION =
      new AutoConnectWallLightBlock("white", "round", WHITE_WALL_LIGHT_SETTINGS) {
        final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION =
            MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

        @Override
        public VoxelShape getOutlineShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
          return SHAPE_PER_DIRECTION.get(state.get(FACING));
        }
      };
  @RegisterIdentifier
  public static final BRRPCubeBlock YELLOW_LIGHT = new BRRPCubeBlock(YELLOW_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/yellow_light"));

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock YELLOW_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

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
  public static final WallLightBlock YELLOW_SMALL_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock YELLOW_LARGE_WALL_LIGHT =
      new WallLightBlock("yellow", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("yellow", "simple", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("yellow", "point", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("yellow", "rhombus", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock YELLOW_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("yellow", "hash", YELLOW_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final BRRPCubeBlock CYAN_LIGHT = new BRRPCubeBlock(CYAN_LIGHT_SETTINGS, "mishanguc:block/light", JTextures.ofAll("mishanguc:block/cyan_light"));

  @RegisterIdentifier
  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock CYAN_THIN_STRIP_WALL_LIGHT =
      new StripWallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

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
  public static final WallLightBlock CYAN_SMALL_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock CYAN_LARGE_WALL_LIGHT =
      new WallLightBlock("cyan", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock("cyan", "simple", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock("cyan", "point", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock("cyan", "rhombus", CYAN_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock CYAN_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock("cyan", "hash", CYAN_WALL_LIGHT_SETTINGS);
}
