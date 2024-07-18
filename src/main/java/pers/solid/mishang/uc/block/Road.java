package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

/**
 * 所有道路方块类型均实现的接口。接口可以多重继承，并直接实现于已有类上，因此使用接口。
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
    return new RoadConnectionState(RoadConnectionState.WhetherConnected.NOT_CONNECTED, getLineColor(state, direction), EightHorizontalDirection.of(direction), LineType.NORMAL);
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
   * 处理方块更新。实现此接口的类，应该覆盖 {@link Block#getStateForNeighborUpdate} 并使用此方法。
   *
   * @since 0.2.4
   */
  @ApiStatus.AvailableSince("0.2.4")
  default BlockState withStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    return state;
  }

  /**
   * @see net.minecraft.block.AbstractBlock#onUse(BlockState, World, BlockPos, PlayerEntity, BlockHitResult)
   */
  default ActionResult onUseRoad(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
    return ActionResult.PASS;
  }

  /**
   * @see AbstractRoadBlock#onUseRoadWithItem(ItemStack, BlockState, World, BlockPos, PlayerEntity, Hand, BlockHitResult)
   */
  default ItemActionResult onUseRoadWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  }

  /**
   * 附近有方块更新时的操作。
   *
   * @param state       方块状态。
   * @param world       世界。
   * @param pos         坐标。
   * @param sourceBlock 方块。
   * @param sourcePos   导致触发方块更新的方块。
   * @param notify      一个布尔值。
   * @see AbstractRoadBlock#neighborUpdate
   * @see AbstractRoadSlabBlock#neighborUpdate
   * @see Block#neighborUpdate
   * @see BlockState#neighborUpdate
   */
  default void neighborRoadUpdate(
      BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
  }

  default void appendRoadTooltip(
      ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
  }

  LineColor getLineColor(BlockState blockState, Direction direction);

  LineType getLineType(BlockState blockState, Direction direction);

  /**
   * 给道路添加描述性内容，这部分文本通常是蓝色的。
   */
  @ApiStatus.AvailableSince("0.2.4")
  void appendDescriptionTooltip(List<Text> tooltip, Item.TooltipContext context);

  @Override
  default RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  default CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
    return null;
  }

  default Identifier getPaintingRecipeId() {
    return getRecipeId().brrp_suffixed("_from_painting");
  }
}
