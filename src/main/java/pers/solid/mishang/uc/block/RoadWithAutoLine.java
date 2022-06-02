package pers.solid.mishang.uc.block;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
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
  BlockState makeState(
      EnumMap<Direction, RoadConnectionState> connectionStateMap, BlockState defaultState);

  /**
   * 获取附近的连接状态映射。
   *
   * @param world 世界。
   * @param pos0  坐标。
   * @return 连接状态的映射。
   */
  default EnumMap<Direction, RoadConnectionState> getConnectionStateMap(
      WorldAccess world, BlockPos pos0) {
    EnumMap<Direction, RoadConnectionState> connectionStateMap = Maps.newEnumMap(Direction.class);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      RoadConnectionState state = RoadConnectionState.empty();
      // 检查毗邻方块及其上下方。
      for (BlockPos pos : new BlockPos[]{pos0, pos0.up(), pos0.down()}) {
        BlockState nextState = world.getBlockState(pos.offset(direction, 1));
        Block nextBlock = nextState.getBlock();
        if (nextBlock instanceof final Road road) {
          RoadConnectionState connectionState =
              road.getConnectionStateOf(nextState, direction.getOpposite());
          if (connectionState.mayConnect()) {
            state = connectionState;
            break;
          }
        }
      }
      connectionStateMap.put(direction, state);
    }
    return connectionStateMap;
  }

  @Override
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return Road.super
        .getConnectionStateOf(state, direction)
        .or(
            RoadConnectionState.mayConnectTo(
                getLineColor(state, direction), Either.left(direction), LineType.NORMAL));
  }

  @Override
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
    Road.super.appendRoadProperties(builder);
  }

  @Override
  default ActionResult onUseRoad(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    Road.super.onUseRoad(state, world, pos, player, hand, hit);
    final Item item = player.getStackInHand(hand).getItem();
    if (item instanceof final BlockItem blockItem
        && blockItem.getBlock() instanceof RoadWithAutoLine
        && !Direction.Type.VERTICAL.test(hit.getSide())) {
      return ActionResult.PASS;
    }
    world.setBlockState(pos, makeState(getConnectionStateMap(world, pos), state), 2);
    return ActionResult.SUCCESS;
  }

  @Override
  default void neighborRoadUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
    // 屏蔽上下方的更新。
    if (!fromPos.equals(pos.up())
        && !fromPos.equals(pos.down())
        && !(world.getBlockState(fromPos).getBlock() instanceof AirBlock))
    // flags设为2从而使得 <code>flags&1 !=0</code> 不成立，从而不递归更新邻居，参考 {@link World#setBlockState}。
    {
      world.setBlockState(pos, makeState(getConnectionStateMap(world, pos), state), 2);
    }
    Road.super.neighborRoadUpdate(state, world, pos, block, fromPos, notify);
  }

  @Override
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    Road.super.appendRoadTooltip(stack, world, tooltip, options);
    tooltip.add(
        Text.translatable("block.mishanguc.tooltip.road_with_auto_line.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        Text.translatable("block.mishanguc.tooltip.road_with_auto_line.2")
            .formatted(Formatting.GRAY));
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
     * 45°的斜角
     */
    BEVEL
  }
}
