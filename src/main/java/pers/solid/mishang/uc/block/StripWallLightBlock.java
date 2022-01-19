package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.Map;

/** 类似于墙上的灯方块，但是是条状的，因此具有多一个属性。 */
public class StripWallLightBlock extends WallLightBlock implements LightConnectable {
  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    final StripType stripType = blockState.get(STRIP_TYPE);
    if (facing != blockState.get(FACING) || direction.getAxis() == facing.getAxis()) {
      return false;
    }
    switch (stripType) {
      case VERTICAL:
        return facing.getAxis() == Direction.Axis.Y
            ? direction.getAxis() == Direction.Axis.Z
            : direction.getAxis() == Direction.Axis.Y;
      case HORIZONTAL:
        return facing.getAxis() == Direction.Axis.Y
            ? direction.getAxis() == Direction.Axis.X
            : direction.getAxis() != Direction.Axis.Y;
      default:
        throw new IllegalStateException("Unexpected value: " + stripType);
    }
  }

  public enum StripType implements StringIdentifiable {
    /** 水平的，对于天花板上或地上的表示为东西方向。 */
    HORIZONTAL,
    /** 垂直的，对于天花板上或地上的表示为南北方向。 */
    VERTICAL;

    @Override
    public String asString() {
      switch (this) {
        case HORIZONTAL:
          return "horizontal";
        case VERTICAL:
          return "vertical";
        default:
          return null;
      }
    }

    public StripType another() {
      switch (this) {
        case HORIZONTAL:
          return VERTICAL;
        case VERTICAL:
          return HORIZONTAL;
        default:
          return this;
      }
    }
  }

  private final Map<Direction, VoxelShape> shapePerDirectionWhenVertical;

  protected static final EnumProperty<StripType> STRIP_TYPE =
      EnumProperty.of("strip_type", StripType.class);

  public StripWallLightBlock(
      Settings settings,
      Map<Direction, VoxelShape> shapePerDirection,
      Map<Direction, VoxelShape> shapePerDirectionWhenVertical) {
    super(settings, shapePerDirection);
    this.shapePerDirectionWhenVertical = shapePerDirectionWhenVertical;
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final BlockState rotate = super.rotate(state, rotation);
    if (rotation == BlockRotation.CLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90) {
      return rotate.with(STRIP_TYPE, rotate.get(STRIP_TYPE).another());
    }
    return rotate;
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(STRIP_TYPE);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    final PlayerEntity player = ctx.getPlayer();
    return placementState.with(
        STRIP_TYPE,
        ctx.getSide().getAxis() == Direction.Axis.Y
            ? (ctx.getPlayerFacing().getAxis() == Direction.Axis.X
                ? StripType.HORIZONTAL
                : StripType.VERTICAL)
            : (player != null && player.isSneaking() ? StripType.VERTICAL : StripType.HORIZONTAL));
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return state.get(STRIP_TYPE) == StripType.VERTICAL
        ? shapePerDirectionWhenVertical.get(state.get(FACING))
        : super.getOutlineShape(state, world, pos, context);
  }
}
