package pers.solid.mishang.uc.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.item.CarryingToolItem;
import pers.solid.mishang.uc.mixin.BucketItemAccessor;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;

import java.util.Objects;

/**
 * 用于预测方块放置时的位置以及方块状态，同时处理方块实体。主要用于强制放置工具和快速建造工具，既可用于放置和破坏，也可用于相应的轮廓绘制。
 */
public class BlockPlacementContext {
  public final Level world;
  public final BlockPos blockPos;
  public final Player player;
  public final ItemStack stack;
  public final BlockHitResult hit;
  /**
   * {@link #hit} 中的方块状态。<br>
   * The {@link BlockState} in the {@link #hit}.
   */
  public final BlockState hitState;
  /**
   * {@link #hit} 中的方块实体。<br>
   * The {@link BlockEntity} in the {@link #hit}.
   */
  public final @Nullable BlockEntity hitEntity;
  /**
   * 放置之前，{@link #posToPlace} 位置处的方块。该方块将会被 {@link #stateToPlace} 替换掉。<br>
   * The block at {@link #posToPlace} before placing. The block will be replaced with {@link
   * #stateToPlace}.
   */
  public final BlockState stateToReplace;
  /**
   * 是否会连同流体一起放置与破坏。
   */
  public final boolean includesFluid;


  public final BlockPlaceContext placementContext;
  /**
   * 如果需要放置方块，则方块放置在此位置。<br>
   * The {@link BlockPos} to place the block if to place it.
   */
  public final BlockPos posToPlace;
  /**
   * 需要放置的方块状态。<br>
   * The {@link BlockState} to place in the {@link #posToPlace}.
   */
  public final BlockState stateToPlace;
  /**
   * 拿着方块物品的手。<br>
   * The hand that holds the BlockItem.
   */
  public @Nullable InteractionHand hand;
  /**
   * 手中的物品堆。该物品堆的物品必须是方块物品，或者是 {@link CarryingToolItem}。如果手中的物品堆是空的，或者不是方块，则该值为 {@code null}。<br>
   * The {@link ItemStack} in the {@code hand}. The item in the <code>ItemStack</code> must be a
   * {@link BlockItem} or {@link CarryingToolItem}. If the item stack in hand is not block item, or is null, then the value is {@code null}.
   */
  public final @Nullable ItemStack stackInHand;

  /**
   * 请留意这个 {@link #player} 如果是 <code>null</code> 将会抛出异常！因此构造时请一定留意！ Please pay attention when
   * constructing because it throws exceptions when {@link #player} is <code>null</code>!
   */
  public BlockPlacementContext(UseOnContext context, boolean includesFluid) {
    this(
        context.getLevel(),
        context.getClickedPos(),
        Objects.requireNonNull(context.getPlayer()),
        context.getItemInHand(),
        ((ItemUsageContextInvoker) context).invokeGetHitResult(),
        includesFluid);
  }

  /**
   * 根据已有的 {@link BlockPlacementContext}，获得一个偏移到 <code>offsetPos</code> 坐标处的新的 <code>
   * BlockPlacementContext</code>. <br>
   * Get a new {@link BlockPlacementContext} from an old context with an <code>offsetPos</code>.
   */
  public BlockPlacementContext(BlockPlacementContext old, BlockPos offsetPos) {
    this(
        old.world,
        offsetPos,
        old.player,
        old.stack,
        new BlockHitResult(old.hit.getLocation().add(
            offsetPos.getX() - old.hit.getBlockPos().getX(),
            offsetPos.getY() - old.hit.getBlockPos().getY(),
            offsetPos.getZ() - old.hit.getBlockPos().getZ()), old.hit.getDirection(), offsetPos, old.hit.isInside()),
        old.includesFluid);
  }

  public BlockPlacementContext(
      Level world,
      BlockPos blockPos,
      Player player,
      ItemStack stack,
      BlockHitResult hit,
      boolean includesFluid) {
    this.world = world;
    this.blockPos = blockPos;
    this.player = player;
    this.stack = stack;
    this.hit = hit;
    this.includesFluid = includesFluid;

    // 需要被替换的方块
    hitState = world.getBlockState(hit.getBlockPos());
    hitEntity = world.getBlockEntity(hit.getBlockPos());

    // 需要放置的方块
    @Nullable BlockState stateToPlace1 = null;
    @Nullable ItemStack stackInHand1 = null;
    BlockPlaceContext placementContext1 = null;

    for (@NotNull InteractionHand hand1 : InteractionHand.values()) {
      ItemStack stackInHand0 = this.player.getItemInHand(hand1);
      if (stackInHand0.getItem() instanceof final BlockItem blockItem) {
        // 若手中持有方块物品，则 stateToPlace 为该物品
        /*
          手中物品堆中的方块物品对应的方块。
         */
        final @Nullable Block handBlock = blockItem.getBlock();
        placementContext1 = new BlockPlaceContext(player, hand1, stackInHand0, hit);
        stateToPlace1 = handBlock == null ? null : handBlock.getStateForPlacement(placementContext1);
        if (stateToPlace1 == null) {
          placementContext1 = null;
          continue;
        }

        // 尝试 placeFromTag
        final BlockItemStateProperties blockStateComponent = stackInHand0.get(DataComponents.BLOCK_STATE);
        if (blockStateComponent != null) {
          stateToPlace1 = blockStateComponent.apply(stateToPlace1);
        }
        stackInHand1 = stackInHand0;
        hand = hand1;
        break;
      } else if (stackInHand0.getItem() instanceof CarryingToolItem) {
        placementContext1 = new BlockPlaceContext(player, hand1, stackInHand0, hit);
        stateToPlace1 = CarryingToolItem.getHoldingBlockState(stackInHand0, world);
        stackInHand1 = stackInHand0;
        hand = hand1;
        break;
      } else if (stackInHand0.getItem() instanceof FlintAndSteelItem) {
        stateToPlace1 = Blocks.FIRE.defaultBlockState();
      } else if (stackInHand0.getItem() instanceof BucketItem bucketItem) {
        stateToPlace1 = ((BucketItemAccessor) bucketItem).getContent().defaultFluidState().createLegacyBlock();
      }
    }

    stackInHand = stackInHand1;
    placementContext = placementContext1 == null ? new BlockPlaceContext(player, hand, hitState.getBlock().asItem().getDefaultInstance(), hit) : placementContext1;
    final boolean tweakSlabPlacement;
    if (placementContext.getItemInHand().getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof SlabBlock) {
      tweakSlabPlacement = !BlockPos.containing(hit.getLocation().relative(hit.getDirection(), 0.25)).equals(hit.getBlockPos());
    } else {
      tweakSlabPlacement = false;
    }
    posToPlace = (includesFluid || tweakSlabPlacement) ? blockPos.relative(hit.getDirection()) : placementContext.getClickedPos();
    stateToReplace = world.getBlockState(posToPlace);
    if (stateToPlace1 == null) {
      // 手中没有有效的方块物品，则使用 hitState。
      boolean canReplaceExisting = placementContext.replacingClickedOnBlock() && !includesFluid;
      stateToPlace1 = canReplaceExisting ? hitState.getBlock().getStateForPlacement(placementContext) : null;
    }
    if (stateToPlace1 == null) {
      stateToPlace1 = hitState;
    }

    // 尝试放置含水
    if (!includesFluid && stateToPlace1.getProperties().contains(BlockStateProperties.WATERLOGGED)) {
      stateToPlace1 = stateToPlace1.setValue(BlockStateProperties.WATERLOGGED, stateToReplace.getFluidState().getType() == Fluids.WATER);
    }

    // 对台阶进行修改
    if (tweakSlabPlacement && stateToPlace1.hasProperty(SlabBlock.TYPE) && stateToPlace1.getValue(SlabBlock.TYPE) == SlabType.DOUBLE) {
      if (hitState.getBlock() instanceof SlabBlock && hitState.hasProperty(SlabBlock.TYPE)) {
        stateToPlace1 = stateToPlace1.setValue(SlabBlock.TYPE, hitState.getValue(SlabBlock.TYPE));
      } else {
        stateToPlace1 = stateToPlace1.setValue(SlabBlock.TYPE, (placementContext.getClickLocation().y - blockPos.getY() > 0.5) ? SlabType.TOP : SlabType.BOTTOM);
      }
    }

    // 此时终于确定好了 stateToPlace
    this.stateToPlace = stateToPlace1;
  }

  /**
   * 放置方块。
   */
  public boolean setBlockState(int flags) {
    return world.setBlock(posToPlace, stateToPlace, flags);
  }

  /**
   * 放置方块实体。
   */
  public void setBlockEntity() {
    BlockEntity entityToPlace = world.getBlockEntity(posToPlace);
    if (stackInHand != null && entityToPlace != null) {
      if (stackInHand.get(MishangucComponents.CARRYING_TOOL_DATA) instanceof CarryingToolData.HoldingBlockState holdingBlockState) {
        if (holdingBlockState.blockEntityTag().isPresent()) {
          // 手持 Carrying Tool 时，可能使用该工作的方块实体数据，这一数据并非存储在 block_entity_data 数组组件中。
          TypedEntityData.of(entityToPlace.getType(), holdingBlockState.blockEntityTag().get()).loadInto(entityToPlace, world.registryAccess());
        }
      }
      // 从指定的物品堆对应的方块中读取组件
      BlockItem.updateCustomBlockEntityTag(world, player, posToPlace, stackInHand);
      entityToPlace.applyComponentsFromItemStack(stackInHand);
    } else if (hitEntity != null && entityToPlace != null) {
      final CompoundTag nbt = hitEntity.saveWithoutMetadata(world.registryAccess());
      TypedEntityData.of(entityToPlace.getType(), nbt).loadInto(entityToPlace, world.registryAccess());
      entityToPlace.setChanged();
      world.sendBlockUpdated(posToPlace, entityToPlace.getBlockState(), entityToPlace.getBlockState(), Block.UPDATE_ALL);
    }
  }

  /**
   * 检查方块能否放置。例如，如果 {@link #stateToPlace} 是花，且 {@link #posToPlace} 是悬在半空中的，则该方法返回 <code>false</code>
   * 。<br>
   * Checks if the {@link #stateToPlace} can be placed at {@link #posToPlace}. For example, if
   * {@link #stateToPlace} is a flower, and {@link #posToPlace} is hung in the midair, the methods
   * returns <code>false</code>.<br>
   * Calls {@link BlockState#canSurvive}.
   */
  public boolean canPlace() {
    if (stateToPlace.getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
      return false;
    }
    return stateToPlace.canSurvive(world, posToPlace);
  }

  /**
   * 检查方块能否被替换。例如，如果 {@link #stateToReplace} 是草或者水，则返回 <code>true</code>。<br>
   * Checks if the {@link #stateToReplace} can be replaced in the placement-context. For example, if
   * the {@link #stateToReplace} is a grass or water, then it returns <code>true</code>.<br>
   * Calls {@link BlockState#canBeReplaced}.
   */
  public boolean canReplace() {
    return stateToReplace.canBeReplaced(placementContext);
  }

  /**
   * 播放声音。该函数不会检查 {@link #world} 是否为客户端世界，需要在调用时手动检查。<br>
   * Play the sound of the block placed. Does not check if {@link #world} is client world. You may
   * manually check it.
   */
  public void playSound() {
    SoundType blockSoundGroup = stateToPlace.getSoundType();
    world.playSound(
        player,
        posToPlace,
        blockSoundGroup.getPlaceSound(),
        SoundSource.BLOCKS,
        (blockSoundGroup.getVolume() + 1.0F) / 2.0F,
        blockSoundGroup.getPitch() * 0.8F);
  }
}
