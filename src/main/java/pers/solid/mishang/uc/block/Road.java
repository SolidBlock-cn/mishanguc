package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.util.EightHorizontalDirection;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;
import pers.solid.mishang.uc.util.RoadConnectionState;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * 所有道路方块类型均实现的接口。接口可以多重继承，并直接实现于已有类上，因此使用接口。
 */
public interface Road extends BlockResourceGenerator {

  EntityAttributeModifier ROAD_SPEED_BOOST = new EntityAttributeModifier(UUID.fromString("693D7032-4767-5A57-A28F-401F8F485772"/* 根据网上的在线 UUID 生成器生成 */), "road_speed_boost", 1.75, EntityAttributeModifier.Operation.MULTIPLY_TOTAL) {
    @Override
    public double getValue() {
      return MishangucRules.currentRoadBoostSpeed;
    }

    @Override
    public NbtCompound toNbt() {
      final NbtCompound nbt = super.toNbt();
      nbt.putDouble("Amount", getValue());
      return nbt;
    }

    /**
     * 由于超类方法中要求是同一类别才能相等，匿名类的使用影响了相等的判断，故这里临时做出修改。
     */
    @Override
    public boolean equals(Object o) {
      return super.equals(o) || (o instanceof EntityAttributeModifier entityAttributeModifier && Objects.equals(entityAttributeModifier.getId(), this.getId()));
    }
  };
  Tag<Block> ROADS = TagFactory.BLOCK.create(new Identifier("mishanguc", "roads"));

  /**
   * 当玩家踩踏在道路方块上时，给予对应的速度倍率值。踩踏在其他方块上时，该倍率值被移除。特别注意，当玩家在道路方块之间上下楼梯的时候，会存在没有踩踏在道路方块上的短暂期间，这种情况下不应该移除其倍率值。<p>
   * 该方法在客户端和服务器的每一刻都会执行。
   * <p>
   * 该方法的逻辑如下：
   * <ul>
   *   <li>当玩家踩在道路方块上时，一定给予效果。</li>
   *   <li>当玩家下方的方块为无碰撞箱的方块且该方块下方为道路方块时，一定不作处理。</li>
   *   <li>不符合上述两条条件，则立即移除效果。</li>
   *   </ul>
   */
  Consumer<World> CHECK_MULTIPLIER = world -> {
    for (PlayerEntity player : world.getPlayers()) {
      final EntityAttributeInstance attributeInstance = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
      final BlockPos stepPos = new BlockPos(player.getPos().x, player.getPos().y - 0.2, player.getPos().z);
      final BlockPos stepPosDown = stepPos.down();
      if (attributeInstance == null) continue;
      final BlockState stepState = world.getBlockState(stepPos);
      if (player.isOnGround() && stepState.isIn(ROADS)) {
        // 玩家踩在道路方块上。
        if (!attributeInstance.hasModifier(ROAD_SPEED_BOOST)) {
          attributeInstance.addTemporaryModifier(ROAD_SPEED_BOOST);
        }
      } else if ((!stepState.getCollisionShape(world, stepPos).isEmpty() || !world.getBlockState(stepPosDown).isIn(ROADS)) && !stepState.isIn(ROADS)) {
        if (attributeInstance.hasModifier(ROAD_SPEED_BOOST)) {
          attributeInstance.removeModifier(ROAD_SPEED_BOOST);
        }
      }
    }
  };

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
  @SuppressWarnings("deprecation")
  default void neighborRoadUpdate(
      BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
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

  /**
   * 给道路添加描述性内容，这部分文本通常是蓝色的。
   */
  @ApiStatus.AvailableSince("0.2.4")
  void appendDescriptionTooltip(List<Text> tooltip, TooltipContext options);
}
