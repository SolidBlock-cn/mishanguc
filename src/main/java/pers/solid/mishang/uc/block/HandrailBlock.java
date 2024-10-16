package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.enums.BlockHalf;
import net.minecraft.data.client.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.util.Map;

/**
 * <p>栏杆方块。栏杆方块共有 5 种形态：
 * <ul>
 *   <li>普通的栏杆，即这个类，这类方块放置在方块的接近边缘的位置。</li>
 *   <li>中央的栏杆。这类方块往往途径方块正中央，并根据周围的方块来决定其形状。参见 {@link #central()}。</li>
 *   <li>角落的栏杆。相当于两个普通的栏杆结合起来，形成一个角落的位置。参见 {@link #corner()}。</li>
 *   <li>角落外部的栏杆。同样是角落，但只占了一个角落的位置，用于将两个不同方向的普通栏杆在第三个方块的位置连接起来。参见 {@link #outer()}</li>
 *   <li>楼梯上的栏杆。显然，放在楼梯上，它可以是在楼梯位置的边缘或者中间，同时也有可能是在楼梯开始处、结束处或上升的过程中，参见 {@link #stair()}。</li>
 *   </ul>
 *   <p>五种栏杆方块共用同一个物品，物品放置时根据其位置和情形决定栏杆的形态。
 * <p>关于使用该方块的列表，请参见 {@link pers.solid.mishang.uc.blocks.HandrailBlocks}。
 */
public abstract class HandrailBlock extends HorizontalFacingBlock implements Waterloggable, MishangucBlock, Handrails {
  /**
   * 该方块是否含水。
   */
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

  public static final Map<Direction, VoxelShape> SHAPES = MishangUtils.createHorizontalDirectionToShape(0, 0, 0.5, 16, 16, 2.5);

  public HandrailBlock(Settings settings) {
    super(settings);
    setDefaultState(getDefaultState().with(FACING, Direction.SOUTH).with(WATERLOGGED, false));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState state = super.getPlacementState(ctx);
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final BlockState stateToReplace = world.getBlockState(blockPos);
    final Direction facingToReplace = stateToReplace.getBlock() instanceof HandrailBlock ? stateToReplace.get(FACING) : null;
    if (state == null) return null;
    final Direction playerFacing = ctx.getHorizontalPlayerFacing();
    final Vec3d hitPos = ctx.getHitPos();
    final Direction.Axis axis = playerFacing.getAxis();
    assert axis != Direction.Axis.Y;
    final BlockState stateBelow = world.getBlockState(blockPos.down());
    final boolean waterlogged = world.getFluidState(blockPos).getFluid() == Fluids.WATER;
    // 如果底下是楼梯方块，则放置该楼梯扶手方块。
    if (stateBelow.getBlock() instanceof StairsBlock && stateBelow.contains(StairsBlock.FACING) && stateBelow.contains(StairsBlock.HALF) && stateBelow.get(StairsBlock.HALF) == BlockHalf.BOTTOM) {
      return stair().getPlacementState(ctx);
    }

    // facing 的计算方法：和玩家水平视角方向平行，具体取决于玩家放置的位置。若玩家放置于中间的位置，则放置对应的中心版本。
    final Direction facing;
    if (axis == Direction.Axis.Z) {
      final double diff = hitPos.z - blockPos.getZ();
      if (0.3 < diff && diff < 0.7) {
        return central().getPlacementState(ctx);
      }
      facing = diff < 0.5 ? Direction.SOUTH : Direction.NORTH;
    } else {
      final double diff = hitPos.x - blockPos.getX();
      if (0.3 < diff && diff < 0.7) {
        return central().getPlacementState(ctx);
      }
      facing = diff < 0.5 ? Direction.EAST : Direction.WEST;
    }


    // 检测毗邻位置会不会有楼梯方块。
    final BlockState stateInCW = world.getBlockState(blockPos.offset(facing.rotateYClockwise()));
    final boolean isStairsInCW = stateInCW.getBlock() instanceof StairsBlock && stateInCW.contains(StairsBlock.FACING) && stateInCW.get(StairsBlock.FACING) == facing.rotateYClockwise() && stateInCW.contains(StairsBlock.HALF) && stateInCW.get(StairsBlock.HALF) == BlockHalf.BOTTOM;
    final BlockState stateInCCW = world.getBlockState(blockPos.offset(facing.rotateYCounterclockwise()));
    final boolean isStairsInCCW = stateInCCW.getBlock() instanceof StairsBlock && stateInCCW.contains(StairsBlock.FACING) && stateInCCW.get(StairsBlock.FACING) == facing.rotateYCounterclockwise() && stateInCCW.contains(StairsBlock.HALF) && stateInCCW.get(StairsBlock.HALF) == BlockHalf.BOTTOM;

    // 检测放置时是否可以称为外部角落的版本。
    final BlockState stateInOpposite = world.getBlockState(blockPos.offset(facing, -1));
    final boolean isConnectedInCW = stateInCW.getBlock() instanceof final Handrails handrails && handrails.connectsIn(stateInCW, facing.rotateYCounterclockwise(), facing);
    final boolean isConnectedInCCW = stateInCCW.getBlock() instanceof final Handrails handrails && handrails.connectsIn(stateInCCW, facing.rotateYClockwise(), facing);

    // 若该方块贴近的方块可连接，且两侧只有一个可以与之连接，则生成一个外部方块。
    if (stateInOpposite.getBlock() instanceof Handrails handrails) {
      final boolean canConnectOuterInCW = isConnectedInCW && handrails.connectsIn(stateInOpposite, facing, facing.rotateYCounterclockwise());
      final boolean canConnectOuterInCCW = isConnectedInCCW && handrails.connectsIn(stateInOpposite, facing, facing.rotateYClockwise());
      if (canConnectOuterInCW != canConnectOuterInCCW) {
        final BlockState outerState = outer().getDefaultState();
        return outerState
            .with(HandrailOuterBlock.FACING, HorizontalCornerDirection.fromDirections(facing.getOpposite(), canConnectOuterInCW ? facing.rotateYClockwise() : facing.rotateYCounterclockwise()))
            .with(WATERLOGGED, waterlogged);
      }
    }

    // 若该方块两侧只有一个连接了楼梯，则生成一个楼梯方块。
    if (isStairsInCW != isStairsInCCW) {
      final BlockState placementState = stair().getDefaultState();
      if (placementState == null) return null;
      final Direction stairFacing = isStairsInCW ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
      return placementState
          .with(HandrailStairBlock.FACING, stairFacing)
          .with(HandrailStairBlock.SHAPE, HandrailStairBlock.Shape.BOTTOM)
          .with(HandrailStairBlock.POSITION, Util.make(() -> {
            final double diff = switch (stairFacing) {
              case SOUTH -> hitPos.x - blockPos.getX();
              case NORTH -> blockPos.getX() + 1 - hitPos.x;
              case EAST -> blockPos.getZ() + 1 - hitPos.z;
              case WEST -> hitPos.z - blockPos.getZ();
              default -> 0.5;
            };
            return diff < 0.3 ? HandrailStairBlock.Position.RIGHT : diff < 0.7 ? HandrailStairBlock.Position.CENTER : HandrailStairBlock.Position.LEFT;
          }))
          .with(WATERLOGGED, waterlogged);
    }

    final @Nullable HorizontalCornerDirection possibleCornerDirection = facingToReplace == null ? null : HorizontalCornerDirection.fromDirections(facing, facingToReplace, null);
    if (possibleCornerDirection != null) {
      return corner().getDefaultState()
          .with(MishangucProperties.HORIZONTAL_CORNER_FACING, possibleCornerDirection)
          .with(WATERLOGGED, stateToReplace.get(WATERLOGGED));
    }
    return state.with(FACING, facing).with(WATERLOGGED, waterlogged);
  }

  @Override
  public boolean canReplace(BlockState state, ItemPlacementContext context) {
    if (state.getBlock().asItem() != context.getStack().getItem()) return false;
    final Direction facing = state.get(FACING);
    final Direction playerFacing = context.getHorizontalPlayerFacing();
    final Vec3d hitPos = context.getHitPos();
    final BlockPos blockPos = context.getBlockPos();
    final Direction.Axis axis = playerFacing.getAxis();
    assert axis != Direction.Axis.Y;
    final Direction possibleNewFacing;
    if (axis == Direction.Axis.Z) {
      final double diff = hitPos.z - blockPos.getZ();
      possibleNewFacing = diff < 0.3 ? Direction.SOUTH :
          diff > 0.7 ? Direction.NORTH : null;
    } else {
      final double diff = hitPos.x - blockPos.getX();
      possibleNewFacing = diff < 0.3 ? Direction.EAST :
          diff > 0.7 ? Direction.WEST : null;
    }
    return possibleNewFacing != null && facing.getAxis() != possibleNewFacing.getAxis();
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    final Block block = stateFrom.getBlock();
    if (direction.getAxis().isHorizontal() && block instanceof final Handrails handrails) {
      return block.asItem() == asItem()
          && handrails.connectsIn(stateFrom, direction.getOpposite(), state.get(FACING));
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  public @NotNull BlockStateSupplier createBlockStates(Identifier modelId) {
    return BlockStateModelGenerator.createSingletonBlockState(this, modelId).coordinate(BlockStateVariantMap.create(Properties.HORIZONTAL_FACING).register(direction -> BlockStateVariant.create().put(MishangUtils.DIRECTION_Y_VARIANT, direction).put(VariantSettings.UVLOCK, true)));
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPES.get(state.get(FACING));
  }

  /**
   * 该方块的纹理变量，即模型中的 {@code "textures"} 字段。重写此方法时，务必注解为 {@code @Environment(EnvType.CLIENT)}。通常来说，其衍生的几个方块（如楼梯、角落等）均会使用此系列的纹理。
   *
   * @return 该方块的纹理变量组合。
   */
  @Environment(EnvType.CLIENT)
  public abstract @NotNull TextureMap getTextures();

  /**
   * 该方块对应的中心版本。
   *
   * @return 该方块对应的位于方块中央位置的方块。应该是直接返回一个常量实例字段。
   */
  public abstract HandrailCentralBlock<? extends HandrailBlock> central();

  /**
   * 该方块对应的角落版本。
   *
   * @return 该方块对应的位于角落位置的方块。应该是直接返回一个常量实例字段。
   */
  public abstract HandrailCornerBlock<? extends HandrailBlock> corner();

  /**
   * 该方块对应的楼梯版本。
   *
   * @return 该方块对应的位于楼梯扶手位置的方块。应该是直接返回一个常量实例的字段。
   */
  public abstract HandrailStairBlock<? extends HandrailBlock> stair();

  /**
   * 该方块对应的外角落版本。
   *
   * @return 该方块对应的位于角落外部位置的方块。应该是直接返回一个常量实例的字段。
   */
  public abstract HandrailOuterBlock<? extends HandrailBlock> outer();

  public final Block[] selfAndVariants() {
    return new Block[]{this, central(), corner(), stair(), outer()};
  }

  @Override
  public abstract @Nullable Block baseBlock();

  @Override
  public boolean connectsIn(@NotNull BlockState blockState, @NotNull Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing != null && blockState.get(FACING) == offsetFacing && direction.getAxis() != offsetFacing.getAxis();
  }

  @Override
  protected abstract MapCodec<? extends HandrailBlock> getCodec();
}
