package pers.solid.mishang.uc.blocks;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
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
  public static final Block WHITE_LIGHT = new Block(WHITE_LIGHT_SETTINGS);
  /**
   * 墙上的灯等方块等用到的方块设置。与{@link #WHITE_LIGHT_SETTINGS}相比，该方块设置具有{@code noCollision}属性。
   */
  private static final FabricBlockSettings WHITE_WALL_LIGHT_SETTINGS =
      FabricBlockSettings.copyOf(WHITE_LIGHT_SETTINGS).noCollision();

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT_TUBE =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THIN_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_THICK_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final StripWallLightBlock WHITE_DOUBLE_STRIP_WALL_LIGHT_TUBE =
      new StripWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THIN_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_THICK_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final CornerLightBlock WHITE_DOUBLE_STRIP_CORNER_LIGHT_TUBE =
      new CornerLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_SMALL_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final WallLightBlock WHITE_LARGE_WALL_LIGHT =
      new WallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_SIMPLE_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_POINT_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_RHOMBUS_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_HASH_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS);

  @RegisterIdentifier
  public static final AutoConnectWallLightBlock WHITE_WALL_LIGHT_ROUND_DECORATION =
      new AutoConnectWallLightBlock(WHITE_WALL_LIGHT_SETTINGS) {
        final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION =
            MishangUtils.createDirectionToShape(0, 0, 0, 16, 1, 16);

        @Override
        public VoxelShape getOutlineShape(
            BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
          return SHAPE_PER_DIRECTION.get(state.get(FACING));
        }
      };
}
