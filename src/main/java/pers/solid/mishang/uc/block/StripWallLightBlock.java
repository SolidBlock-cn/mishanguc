package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.arrp.FasterJTextures;

import java.util.Map;

/**
 * 类似于墙上的灯方块，但是是条状的，因此具有多一个属性。
 */
public class StripWallLightBlock extends WallLightBlock implements LightConnectable {
  protected static final EnumProperty<StripType> STRIP_TYPE =
      EnumProperty.of("strip_type", StripType.class);
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_HORIZONTAL =
      MishangUtils.createDirectionToShape(0, 0, 4, 16, 2, 12);
  private static final Map<Direction, VoxelShape> SHAPE_PER_DIRECTION_WHEN_VERTICAL =
      MishangUtils.createDirectionToShape(4, 0, 0, 12, 2, 16);

  public StripWallLightBlock(String lightColor, Settings settings) {
    super(lightColor, settings, false);
  }

  @Override
  public boolean isConnectedIn(BlockState blockState, Direction facing, Direction direction) {
    final StripType stripType = blockState.get(STRIP_TYPE);
    if (facing != blockState.get(FACING) || direction.getAxis() == facing.getAxis()) {
      return false;
    }
    return switch (stripType) {
      case VERTICAL -> facing.getAxis() == Direction.Axis.Y
          ? direction.getAxis() == Direction.Axis.Z
          : direction.getAxis() == Direction.Axis.Y;
      case HORIZONTAL -> facing.getAxis() == Direction.Axis.Y
          ? direction.getAxis() == Direction.Axis.X
          : direction.getAxis() != Direction.Axis.Y;
    };
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final BlockState rotate = super.rotate(state, rotation);
    if (rotate.get(FACING).getAxis().isVertical() && (rotation == BlockRotation.CLOCKWISE_90 || rotation == BlockRotation.COUNTERCLOCKWISE_90)) {
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
            ? (ctx.getHorizontalPlayerFacing().getAxis() == Direction.Axis.X
            ? StripType.HORIZONTAL
            : StripType.VERTICAL)
            : (player != null && player.isSneaking() ? StripType.VERTICAL : StripType.HORIZONTAL));
  }

  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return (state.get(STRIP_TYPE) == StripType.VERTICAL
        ? SHAPE_PER_DIRECTION_WHEN_VERTICAL
        : SHAPE_PER_DIRECTION_WHEN_HORIZONTAL)
        .get(state.get(FACING));
  }

  public enum StripType implements StringIdentifiable {
    /**
     * 水平的，对于天花板上或地上的表示为东西方向。
     */
    HORIZONTAL,
    /**
     * 垂直的，对于天花板上或地上的表示为南北方向。
     */
    VERTICAL;

    @Override
    public String asString() {
      return switch (this) {
        case HORIZONTAL -> "horizontal";
        case VERTICAL -> "vertical";
      };
    }

    public StripType another() {
      return switch (this) {
        case HORIZONTAL -> VERTICAL;
        case VERTICAL -> HORIZONTAL;
      };
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return stateFrom.isOf(this) && ((LightConnectable) stateFrom.getBlock()).isConnectedIn(stateFrom, state.get(FACING), direction.getOpposite()) || super.isSideInvisible(state, stateFrom, direction);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JBlockStates getBlockStates() {
    final JVariants variants = new JVariants();
    final Identifier id = getBlockModelId();
    final Identifier idVertical = id.brrp_append("_vertical");
    variants.addVariant("facing=up,strip_type=horizontal", new JBlockModel(id));
    variants.addVariant("facing=up,strip_type=vertical", new JBlockModel(idVertical));
    variants.addVariant("facing=down,strip_type=horizontal", new JBlockModel(id).x(180));
    variants.addVariant("facing=down,strip_type=vertical", new JBlockModel(idVertical).x(180));
    for (Direction direction : Direction.Type.HORIZONTAL) {
      variants.addVariant("strip_type=horizontal,facing", direction.asString(), new JBlockModel(id).x(-90).y(((int) direction.asRotation())));
      variants.addVariant("strip_type=vertical,facing", direction.asString(), new JBlockModel(idVertical).x(-90).y(((int) direction.asRotation())));
    }
    return JBlockStates.ofVariants(variants);
  }

  @ApiStatus.AvailableSince("0.1.7")
  @Environment(EnvType.CLIENT)
  public @Nullable JModel getBlockModelVertical() {
    return new JModel(getModelParent().brrp_append("_vertical"))
        .textures(new FasterJTextures().varP("light", lightColor + "_light"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    super.writeBlockModel(pack);
    pack.addModel(getBlockModelVertical(), getBlockModelId().brrp_append("_vertical"));
  }
}
