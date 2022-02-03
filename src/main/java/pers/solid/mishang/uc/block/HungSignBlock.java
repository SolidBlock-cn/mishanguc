package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUc;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;

public class HungSignBlock extends Block implements Waterloggable, BlockEntityProvider {

  public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  /** 告示牌是否对左侧连接。若 AXIS=X，则 left 表示南方；若 AXIS=Z，则 left 表示西方。 */
  public static final BooleanProperty LEFT = BooleanProperty.of("left");
  /** 告示牌是否对左侧连接。若 AXIS=X，则 left 表示北方；若 AXIS=Z，则 left 表示东方。 */
  public static final BooleanProperty RIGHT = BooleanProperty.of("right");

  private static final VoxelShape SHAPE_X = createCuboidShape(7.5, 6, 0, 8.5, 12, 16);
  private static final VoxelShape SHAPE_Z = createCuboidShape(0, 6, 7.5, 16, 12, 8.5);
  public final @Nullable Block baseBlock;

  public HungSignBlock(Settings settings) {
    this(null, settings);
  }

  public HungSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    this.setDefaultState(
        this.stateManager
            .getDefaultState()
            .with(WATERLOGGED, false)
            .with(AXIS, Direction.Axis.X)
            .with(LEFT, false)
            .with(RIGHT, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(AXIS, WATERLOGGED, LEFT, RIGHT);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    if (placementState == null) {
      return null;
    }
    return placementState
        .with(AXIS, ctx.getPlayerFacing().getAxis())
        .with(
            WATERLOGGED,
            ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new HungSignBlockEntity(pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final Direction.Axis axis = state.get(AXIS);
    switch (axis) {
      case X:
        return SHAPE_X;
      case Z:
        return SHAPE_Z;
      default:
        return VoxelShapes.empty();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    state =
        super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    final @Nullable BooleanProperty property;
    final Direction.Axis axis = state.get(AXIS);
    switch (axis) {
      case X:
        property =
            direction == Direction.SOUTH ? LEFT : direction == Direction.NORTH ? RIGHT : null;
        break;
      case Z:
        property = direction == Direction.WEST ? LEFT : direction == Direction.EAST ? RIGHT : null;
        break;
      default:
        property = null;
    }
    if (property != null) {
      state =
          state.with(
              property,
              neighborState.getBlock() instanceof HungSignBlock && neighborState.get(AXIS) == axis);
    }

    return state;
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    final Direction.Axis oldAxis = state.get(AXIS);
    state =
        super.rotate(state, rotation)
            .with(
                AXIS,
                rotation == BlockRotation.CLOCKWISE_90
                        || rotation == BlockRotation.COUNTERCLOCKWISE_90
                    ? (oldAxis == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X)
                    : oldAxis);
    //noinspection AlibabaAvoidComplexCondition
    if (rotation == BlockRotation.CLOCKWISE_180
        || (oldAxis == Direction.Axis.X && rotation == BlockRotation.COUNTERCLOCKWISE_90)
        || (oldAxis == Direction.Axis.Z && rotation == BlockRotation.CLOCKWISE_90)) {
      state = state.with(LEFT, state.get(RIGHT)).with(RIGHT, state.get(LEFT));
    }
    return state;
  }

  /**
   * 点击告示牌方块时，允许玩家对告示牌进行编辑。冒险模式的玩家无权进行编辑。 <br>
   * 本方法在编写时，适当参照了 {@link net.minecraft.item.SignItem#postPlacement(BlockPos, World, PlayerEntity,
   * ItemStack, BlockState)}。<br>
   * 告示牌界面的打开逻辑又可以参考 {@link
   * net.minecraft.client.network.ClientPlayerEntity#openEditSignScreen(SignBlockEntity)} 和 {@link
   * net.minecraft.server.network.ServerPlayerEntity#openEditSignScreen(SignBlockEntity)}。
   */
  @SuppressWarnings({"deprecation", "JavadocReference"})
  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hand, hit);
    if (actionResult == ActionResult.PASS && !world.isClient) {
      // 在服务端触发打开告示牌编辑界面
      final BlockEntity blockEntity = world.getBlockEntity(pos);
      // 若方块实体不对应，或者编辑的这一侧不可编辑，则略过。
      if (!(blockEntity instanceof HungSignBlockEntity) || !state.get(AXIS).test(hit.getSide())) {
        return ActionResult.PASS;
      }
      final HungSignBlockEntity hungSignBlockEntity = (HungSignBlockEntity) blockEntity;
      if (hungSignBlockEntity.editor != null
          && hungSignBlockEntity.editor.isSpectator()
          && !hungSignBlockEntity.editor.isLiving()
          && hungSignBlockEntity.editor.world != world) {
        // 这种情况下，占用该告示牌的玩家为无效玩家，取消该无效玩家的编辑权限。
        hungSignBlockEntity.editedSide = null;
        hungSignBlockEntity.editor = null;
      }
      if (hungSignBlockEntity.editedSide != null || hungSignBlockEntity.editor != null) {
        // 这种情况下，告示牌被占用，玩家无权编辑。
        MishangUc.MISHANG_LOGGER.warn(
            "Refused to edit because the edited side is {} and the editor is {}.",
            hungSignBlockEntity.editedSide,
            hungSignBlockEntity.editor);
        return ActionResult.SUCCESS;
      }
      hungSignBlockEntity.editedSide = hit.getSide();
      hungSignBlockEntity.editor = player;
      ServerPlayNetworking.send(
          ((ServerPlayerEntity) player),
          new Identifier("mishanguc", "edit_sign"),
          PacketByteBufs.create().writeBlockPos(pos).writeEnumConstant(hit.getSide()));
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return new TranslatableText("block.mishanguc.hung_glowing_sign", baseBlock.getName());
    }
    return super.getName();
  }
}
