package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;

/**
 * 所有道路方块类型均实现的接口。接口可以多重继承，并直接实现于已有类上，因此使用接口。
 */
public interface Road extends MishangucBlock {

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

  default CraftingRecipeJsonBuilder getPaintingRecipe(Block base, Block self) {
    return null;
  }

  default Identifier getPaintingRecipeId() {
    return CraftingRecipeJsonBuilder.getItemId((ItemConvertible) this).withSuffixedPath("_from_painting");
  }

  default @Nullable String getRecipeGroup() {
    final Identifier itemId = Registries.ITEM.getId((Item) this);
    return itemId.getNamespace() + ":" + StringUtils.replaceEach(itemId.getPath(), new String[]{"_white_", "_yellow_", "_w_", "_y_"}, new String[]{"_", "_", "_", "_"});
  }

  CauldronBehavior CLEAN_ROAD_BLOCK = (state, world, pos, player, hand, stack) -> {
    if (stack.getItem() instanceof BlockItem blockItem) {
      final Block block = blockItem.getBlock();
      if ((block instanceof AbstractRoadBlock || block instanceof AbstractRoadSlabBlock) && block != RoadBlocks.ROAD_BLOCK && block != RoadBlocks.ROAD_BLOCK.getRoadSlab()) {
        if (!world.isClient) {
          stack.decrementUnlessCreative(1, player);
          final ItemStack itemStack = block instanceof AbstractRoadSlabBlock ? new ItemStack(RoadBlocks.ROAD_BLOCK.getRoadSlab()) : new ItemStack(RoadBlocks.ROAD_BLOCK);
          if (stack.isEmpty()) {
            player.setStackInHand(hand, itemStack);
          } else if (player.getInventory().insertStack(itemStack)) {
            player.playerScreenHandler.syncState();
          } else {
            player.dropItem(itemStack, false);
          }
          LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
        }
        return ItemActionResult.success(world.isClient);
      }
    }
    return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
  };

  /**
   * 对于道路方块，返回 {@code "road" + suffix}。对于道路台阶方块，返回 {@code "road_slab" + suffix}。
   */
  String getModelName(String suffix);

  /**
   * 生成方块的模型。如果此方块是台阶方块，则生成下半和上半台阶方块的模型，共两个模型，其中返回下半台阶方块的模型的 ID。
   *
   * @return 生成的方块模型的 ID。如果是台阶方块，则是下半台阶方块的 ID。
   */
  Identifier uploadModel(String suffix, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys);

  /**
   * 生成方块的模型。如果此方块是台阶方块，则生成下半和上半台阶方块的模型，共两个模型，其中返回下半台阶方块的模型的 ID。
   *
   * @return 生成的方块模型的 ID。如果是台阶方块，则是下半台阶方块的 ID。
   */
  Identifier uploadModel(String suffix, String variant, TextureMap textureMap, BlockStateModelGenerator blockStateModelGenerator, TextureKey... textureKeys);

  /**
   * 对于道路方块，直接返回 {@code stateForFull}。对于道路台阶方块，会将其转化为台阶的方块状态再返回。
   */
  BlockStateSupplier composeState(@NotNull BlockStateSupplier stateForFull);

  @Override
  default String customRecipeCategory() {
    return "roads";
  }
}
