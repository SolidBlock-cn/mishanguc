package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.EnumMap;
import java.util.List;

public interface RoadWithAutoLine extends Road {
  /**
   * 根据附近的连接状态自动产生一个新的方块状态。
   *
   * @param connectionStateMap 连接状态映射，各个方向的连接状态。
   * @param defaultState       默认方块状态。
   * @return 转换后的方块状态。
   */
  BlockState makeState(EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState);

  default BlockState tryMakeState(EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState, BlockPos pos) {
    try {
      return makeState(connectionStateMap, defaultState);
    } catch (Throwable throwable) {
      Mishanguc.MISHANG_LOGGER.error("An error was found when converting road block at {}:", pos, throwable);
      return defaultState;
    }
  }

  /**
   * 获取附近的连接状态映射。
   *
   * @param world 世界。
   * @param pos0  坐标。
   * @return 连接状态的映射。
   */
  default EnumMap<Direction, RoadConnectionState> getConnectionStateMap(
      LevelAccessor world, BlockPos pos0) {
    EnumMap<Direction, RoadConnectionState> connectionStateMap = new EnumMap<>(Direction.class);
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      RoadConnectionState state = null;
      // 检查毗邻方块及其上下方。
      for (BlockPos pos : new BlockPos[]{pos0, pos0.above(), pos0.below()}) {
        BlockState nextState = world.getBlockState(pos.relative(direction, 1));
        Block nextBlock = nextState.getBlock();
        if (nextBlock instanceof final Road road) {
          state = road.getConnectionStateOf(nextState, direction.getOpposite());
          break;
        }
      }
      connectionStateMap.put(direction, ObjectUtils.getIfNull(state, RoadConnectionState::empty));
    }
    return connectionStateMap;
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(new RoadConnectionState(RoadConnectionState.WhetherConnected.MAY_CONNECT, getLineColor(state, direction), EightHorizontalDirection.of(direction), LineType.NORMAL));
  }

  @Override
  default void appendRoadProperties(StateDefinition.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
  }

  @Override
  default InteractionResult onUseRoad(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
    world.setBlock(pos, tryMakeState(getConnectionStateMap(world, pos), state, pos), 2);
    return InteractionResult.SUCCESS;
  }

  @Override
  default InteractionResult onUseRoadWithItem(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    final Item item = player.getItemInHand(hand).getItem();
    if (item instanceof final BlockItem blockItem
        && blockItem.getBlock() instanceof RoadWithAutoLine
        && !Direction.Plane.VERTICAL.test(hit.getDirection())) {
      return InteractionResult.PASS;
    }
    return InteractionResult.TRY_WITH_EMPTY_HAND;
  }

  @Override
  default void neighborRoadUpdate(
      BlockState state, Level world, BlockPos pos, Block sourceBlock, @Nullable Orientation wireOrientation, boolean notify) {
    // 屏蔽上下方的更新。
    if (!(wireOrientation != null && wireOrientation.getFront().getAxis().isVertical())) {
      // flags设为2从而使得 <code>flags&1 !=0</code> 不成立，从而不递归更新邻居，参考 {@link World#setBlockState}。
      world.setBlock(pos, tryMakeState(getConnectionStateMap(world, pos), state, pos), 2);
    }
    Road.super.neighborRoadUpdate(state, world, pos, sourceBlock, wireOrientation, notify);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    Road.super.appendRoadTooltip(stack, context, tooltip, options);
    tooltip.add(
        Component.translatable("block.mishanguc.tooltip.road_with_auto_line.1")
            .withStyle(ChatFormatting.GRAY));
    tooltip.add(
        Component.translatable("block.mishanguc.tooltip.road_with_auto_line.2")
            .withStyle(ChatFormatting.GRAY));
  }

  /**
   * 道路自动连接的类型，分为直角和斜线。
   */
  enum RoadAutoLineType {
    /**
     * 直角
     */
    RIGHT_ANGLE,
    /**
     * 45°的斜线
     */
    BEVEL;
    public static final Codec<RoadAutoLineType> CODEC = Codec.BOOL.xmap(b -> b ? BEVEL : RIGHT_ANGLE, e -> e == BEVEL);
  }
}
