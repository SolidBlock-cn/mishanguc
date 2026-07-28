package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.item.NamedBlockItem;
import pers.solid.mishang.uc.mixin.BlockStateModelGeneratorAccessor;
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
public abstract class HandrailBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, MishangucBlock, Handrails {
  /**
   * 该方块是否含水。
   */
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  public static final Map<Direction, VoxelShape> SHAPES = MishangUtils.createHorizontalDirectionToShape(0, 0, 0.5, 16, 16, 2.5);

  public HandrailBlock(Properties settings) {
    super(settings);
    registerDefaultState(defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState state = super.getStateForPlacement(ctx);
    final Level world = ctx.getLevel();
    final BlockPos blockPos = ctx.getClickedPos();
    final BlockState stateToReplace = world.getBlockState(blockPos);
    final Direction facingToReplace = stateToReplace.getBlock() instanceof HandrailBlock ? stateToReplace.getValue(FACING) : null;
    if (state == null) return null;
    final Direction playerFacing = ctx.getHorizontalDirection();
    final Vec3 hitPos = ctx.getClickLocation();
    final Direction.Axis axis = playerFacing.getAxis();
    assert axis != Direction.Axis.Y;
    final BlockState stateBelow = world.getBlockState(blockPos.below());
    final boolean waterlogged = world.getFluidState(blockPos).getType() == Fluids.WATER;
    // 如果底下是楼梯方块，则放置该楼梯扶手方块。
    if (stateBelow.getBlock() instanceof StairBlock && stateBelow.hasProperty(StairBlock.FACING) && stateBelow.hasProperty(StairBlock.HALF) && stateBelow.getValue(StairBlock.HALF) == Half.BOTTOM) {
      return stair().getStateForPlacement(ctx);
    }

    // facing 的计算方法：和玩家水平视角方向平行，具体取决于玩家放置的位置。若玩家放置于中间的位置，则放置对应的中心版本。
    final Direction facing;
    if (axis == Direction.Axis.Z) {
      final double diff = hitPos.z - blockPos.getZ();
      if (0.3 < diff && diff < 0.7) {
        return central().getStateForPlacement(ctx);
      }
      facing = diff < 0.5 ? Direction.SOUTH : Direction.NORTH;
    } else {
      final double diff = hitPos.x - blockPos.getX();
      if (0.3 < diff && diff < 0.7) {
        return central().getStateForPlacement(ctx);
      }
      facing = diff < 0.5 ? Direction.EAST : Direction.WEST;
    }


    // 检测毗邻位置会不会有楼梯方块。
    final BlockState stateInCW = world.getBlockState(blockPos.relative(facing.getClockWise()));
    final boolean isStairsInCW = stateInCW.getBlock() instanceof StairBlock && stateInCW.hasProperty(StairBlock.FACING) && stateInCW.getValue(StairBlock.FACING) == facing.getClockWise() && stateInCW.hasProperty(StairBlock.HALF) && stateInCW.getValue(StairBlock.HALF) == Half.BOTTOM;
    final BlockState stateInCCW = world.getBlockState(blockPos.relative(facing.getCounterClockWise()));
    final boolean isStairsInCCW = stateInCCW.getBlock() instanceof StairBlock && stateInCCW.hasProperty(StairBlock.FACING) && stateInCCW.getValue(StairBlock.FACING) == facing.getCounterClockWise() && stateInCCW.hasProperty(StairBlock.HALF) && stateInCCW.getValue(StairBlock.HALF) == Half.BOTTOM;

    // 检测放置时是否可以称为外部角落的版本。
    final BlockState stateInOpposite = world.getBlockState(blockPos.relative(facing, -1));
    final boolean isConnectedInCW = stateInCW.getBlock() instanceof final Handrails handrails && handrails.connectsIn(stateInCW, facing.getCounterClockWise(), facing);
    final boolean isConnectedInCCW = stateInCCW.getBlock() instanceof final Handrails handrails && handrails.connectsIn(stateInCCW, facing.getClockWise(), facing);

    // 若该方块贴近的方块可连接，且两侧只有一个可以与之连接，则生成一个外部方块。
    if (stateInOpposite.getBlock() instanceof Handrails handrails) {
      final boolean canConnectOuterInCW = isConnectedInCW && handrails.connectsIn(stateInOpposite, facing, facing.getCounterClockWise());
      final boolean canConnectOuterInCCW = isConnectedInCCW && handrails.connectsIn(stateInOpposite, facing, facing.getClockWise());
      if (canConnectOuterInCW != canConnectOuterInCCW) {
        final BlockState outerState = outer().defaultBlockState();
        return outerState
            .setValue(HandrailOuterBlock.FACING, HorizontalCornerDirection.fromDirections(facing.getOpposite(), canConnectOuterInCW ? facing.getClockWise() : facing.getCounterClockWise()))
            .setValue(WATERLOGGED, waterlogged);
      }
    }

    // 若该方块两侧只有一个连接了楼梯，则生成一个楼梯方块。
    if (isStairsInCW != isStairsInCCW) {
      final BlockState placementState = stair().defaultBlockState();
      if (placementState == null) return null;
      final Direction stairFacing = isStairsInCW ? facing.getClockWise() : facing.getCounterClockWise();
      return placementState
          .setValue(HandrailStairBlock.FACING, stairFacing)
          .setValue(HandrailStairBlock.SHAPE, HandrailStairBlock.Shape.BOTTOM)
          .setValue(HandrailStairBlock.POSITION, Util.make(() -> {
            final double diff = switch (stairFacing) {
              case SOUTH -> hitPos.x - blockPos.getX();
              case NORTH -> blockPos.getX() + 1 - hitPos.x;
              case EAST -> blockPos.getZ() + 1 - hitPos.z;
              case WEST -> hitPos.z - blockPos.getZ();
              default -> 0.5;
            };
            return diff < 0.3 ? HandrailStairBlock.Position.RIGHT : diff < 0.7 ? HandrailStairBlock.Position.CENTER : HandrailStairBlock.Position.LEFT;
          }))
          .setValue(WATERLOGGED, waterlogged);
    }

    final @Nullable HorizontalCornerDirection possibleCornerDirection = facingToReplace == null ? null : HorizontalCornerDirection.fromDirections(facing, facingToReplace, null);
    if (possibleCornerDirection != null) {
      return corner().defaultBlockState()
          .setValue(MishangucProperties.HORIZONTAL_CORNER_FACING, possibleCornerDirection)
          .setValue(WATERLOGGED, stateToReplace.getValue(WATERLOGGED));
    }
    return state.setValue(FACING, facing).setValue(WATERLOGGED, waterlogged);
  }

  @Override
  public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
    final ItemStack stack = context.getItemInHand();
    if (state.getBlock().asItem() != stack.getItem()) return false;
    final BlockPos blockPos = context.getClickedPos();
    if (this instanceof ColoredBlock) {
      // 对于染色方块，如果颜色不一致，不可以替换。
      final Level world = context.getLevel();
      if (world.getBlockEntity(blockPos) instanceof ColoredBlockEntity entity) {
        final int colorToReplace = entity.getColor();
        final Integer colorToSet = stack.get(MishangucComponents.COLOR);
        if (colorToSet != null && colorToSet != colorToReplace) {
          return false;
        } else if (colorToSet == null && NamedBlockItem.getDependentColor(context) != colorToReplace) {
          return false;
        }
      }
    }
    final Direction facing = state.getValue(FACING);
    final Direction playerFacing = context.getHorizontalDirection();
    final Vec3 hitPos = context.getClickLocation();
    final Direction.Axis axis = playerFacing.getAxis();
    assert axis != Direction.Axis.Y;
    final Direction possibleNewFacing;
    if (axis == Direction.Axis.Z) {
      final double diff = hitPos.z - blockPos.getZ();
      possibleNewFacing = diff < 0.3 ? Direction.SOUTH : diff > 0.7 ? Direction.NORTH : null;
    } else {
      final double diff = hitPos.x - blockPos.getX();
      possibleNewFacing = diff < 0.3 ? Direction.EAST : diff > 0.7 ? Direction.WEST : null;
    }
    return possibleNewFacing != null && facing.getAxis() != possibleNewFacing.getAxis();
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState stateFrom, Direction direction) {
    final Block block = stateFrom.getBlock();
    if (direction.getAxis().isHorizontal() && block instanceof final Handrails handrails) {
      return block.asItem() == asItem()
          && handrails.connectsIn(stateFrom, direction.getOpposite(), state.getValue(FACING));
    }
    return super.skipRendering(state, stateFrom, direction);
  }

  @Override
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Environment(EnvType.CLIENT)
  public BlockModelDefinitionGenerator createBlockStates(Identifier modelId) {
    return BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId)).with((BlockStateModelGeneratorAccessor.getROTATION_HORIZONTAL_FACING_ALT())).with(BlockModelGenerators.UV_LOCK);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    return SHAPES.get(state.getValue(FACING));
  }

  /**
   * 该方块的纹理变量，即模型中的 {@code "textures"} 字段。重写此方法时，务必注解为 {@code @Environment(EnvType.CLIENT)}。通常来说，其衍生的几个方块（如楼梯、角落等）均会使用此系列的纹理。
   *
   * @return 该方块的纹理变量组合。
   */
  @Environment(EnvType.CLIENT)
  public abstract TextureMapping getTextures();

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
  public boolean connectsIn(BlockState blockState, Direction direction, @Nullable Direction offsetFacing) {
    return offsetFacing != null && blockState.getValue(FACING) == offsetFacing && direction.getAxis() != offsetFacing.getAxis();
  }

  @Override
  protected abstract MapCodec<? extends HandrailBlock> codec();

  @Override
  protected boolean isPathfindable(BlockState state, PathComputationType type) {
    return false;
  }
}
