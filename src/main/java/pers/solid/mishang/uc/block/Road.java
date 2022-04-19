package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Either;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

/**
 * 所有道路方块的接口。接口可以多重继承，并直接实现与已有类上，因此使用接口。
 */
public interface Road extends BlockResourceGenerator {

  /**
   * 获取该方块状态中，某个特定方向上的连接状态。连接状态可用于自动路块。
   *
   * @param state     方块状态。
   * @param direction 水平方向。
   * @return 连接状态。
   */
  default RoadConnectionState getConnectionStateOf(BlockState state, Direction direction) {
    return RoadConnectionState.notConnectedTo(
        getLineColor(state, direction), Either.left(direction), LineType.NORMAL);
  }

  /**
   * 实现此接口的类，应当覆盖 <code>appendProperties</code> 并使用此方法。
   *
   * @param builder <code>appendProperties</code> 方法中的 builder。
   */
  default void appendRoadProperties(StateManager.Builder<Block, BlockState> builder) {
  }

  /**
   * 实现此接口的类，应当覆盖 <code>mirror</code> 并使用此方法。
   *
   * @param state  <code>mirror</code> 中的 state。
   * @param mirror <code>mirror</code> 中的 mirror。
   * @return 镜像后的方块状态。
   */
  default BlockState mirrorRoad(BlockState state, BlockMirror mirror) {
    return state;
  }

  /**
   * 实现此接口的类，应当覆盖 <code>rotate</code> 并使用此方法。
   *
   * @param state    <code>rotate</code> 中的 state。
   * @param rotation <code>rotate</code> 中的 rotation。
   * @return 旋转后的方块状态。
   */
  default BlockState rotateRoad(BlockState state, BlockRotation rotation) {
    return state;
  }

  /**
   * 追加放置状态。 实现此接口的类，应当覆盖 <code>getPlacementState</code> 并使用此方法。
   *
   * @param state 需要被修改的方块状态，一般是 <code>super.getPlacementState(ctx)</code> 或者 <code>
   *              this.getDefaultState</code>（其中 this 是方块）。
   * @param ctx   <code>getPlacementState</code> 中的 ctx。
   * @return 追加后的方块状态。
   */
  default BlockState withPlacementState(BlockState state, ItemPlacementContext ctx) {
    return state;
  }

  /**
   * 对道路进行使用的操作。
   *
   * @param state  该道路方块的方块状态。
   * @param world  所在的世界。
   * @param pos    该道路所在的坐标。
   * @param player 使用的玩家。
   * @param hand   玩家使用道路时使用的手。
   * @param hit    玩家使用道路时的碰撞结果。
   * @return 行为结果。
   */
  default ActionResult onUseRoad(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    return ActionResult.PASS;
  }

  /**
   * 附近有方块更新时的操作。
   *
   * @param state   方块状态。
   * @param world   世界。
   * @param pos     坐标。
   * @param block   方块。
   * @param fromPos 导致触发方块更新的方块。
   * @param notify  一个布尔值。
   * @see AbstractRoadBlock#neighborUpdate
   * @see AbstractRoadSlabBlock#neighborUpdate
   * @see Block#neighborUpdate
   * @see BlockState#neighborUpdate
   */
  @SuppressWarnings("deprecation")
  default void neighborRoadUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
  }

  /**
   * 在物品栏中为该道路添加提示信息。<br>
   * 对于 1.16.5 之前的版本，子类覆盖此方法时，必须注解为 {@code @Environment(EnvType.CLIENT)}。<br>
   * 新版本中，由于 {@link Block#appendTooltip(ItemStack, BlockView, List, TooltipContext)} 没有再被注解，故此方法也无需再被注解。
   *
   * @param stack   物品堆。
   * @param world   世界。
   * @param tooltip 提示文字。
   * @param options 提示选项。
   * @see AbstractRoadBlock#appendRoadTooltip
   * @see AbstractRoadSlabBlock#appendTooltip
   * @see Block#appendTooltip
   */
  default void appendRoadTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
  }

  LineColor getLineColor(BlockState blockState, Direction direction);

  LineType getLineType(BlockState blockState, Direction direction);
}
