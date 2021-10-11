package pers.solid.mishang.uc.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;

import java.util.Objects;

public class BlockPlacementContext {
    public final @NotNull World world;
    public final @NotNull BlockPos blockPos;
    public final @NotNull PlayerEntity player;
    public final @NotNull ItemStack stack;
    public final @NotNull BlockHitResult hit;
    /**
     * {@link #hit} 中的方块状态。<br>
     * The {@link BlockState} in the {@link #hit}.
     */
    public final @NotNull BlockState hitState;
    /**
     * 放置之前，{@link #posToPlace} 位置处的方块。该方块将会被 {@link #stateToPlace} 替换掉。<br>
     * The block at {@link #posToPlace} before placing. The block will be replaced with {@link #stateToPlace}.
     */
    public final @NotNull BlockState stateToReplace;
    public final boolean includesFluid;
    public @NotNull ItemPlacementContext placementContext;
    /**
     * 如果需要放置方块，则方块放置在此位置。<br>
     * The {@link BlockPos} to place the block if to place it.
     */
    public @NotNull BlockPos posToPlace;
    /**
     * 需要放置的方块状态。<br>
     * The {@link BlockState} to place in the {@link #posToPlace}.
     */
    public @NotNull BlockState stateToPlace;
    /**
     * 拿着方块物品的手。<br>
     * The hand that holds the BlockItem.
     */
    @Nullable Hand hand;
    /**
     * 手中的物品堆。该物品堆的物品必须是方块物品。<br>
     * The {@link ItemStack} in the {@code hand}. The item in the <code>ItemStack</code> must be a {@link BlockItem}.
     */
    @Nullable ItemStack stackInHand;
    /**
     * 手中物品堆中的方块物品对应的方块。<br>
     * The block of the blockItem in the {@link #stackInHand}.
     */
    @Nullable Block handBlock;

    /**
     * 请留意这个 {@link #player} 如果是 <code>null</code> 将会抛出异常！因此构造时请一定留意！
     * Please pay attention when constructing because it throws exceptions when {@link #player} is <code>null</code>!
     */
    public BlockPlacementContext(ItemUsageContext context, boolean includesFluid) {
        this(context.getWorld(), context.getBlockPos(), Objects.requireNonNull(context.getPlayer()), context.getStack(), ((ItemUsageContextInvoker) context).invokeGetHitResult(), includesFluid);
    }

    /**
     * 根据已有的 {@link BlockPlacementContext}，获得一个偏移到 <code>offsetPos</code> 坐标处的新的 <code>BlockPlacementContext</code>. <br>
     * Get a new {@link BlockPlacementContext} from an old context with an <code>offsetPos</code>.
     */
    public BlockPlacementContext(@NotNull BlockPlacementContext old, @NotNull BlockPos offsetPos) {
        this(old.world, offsetPos, old.player, old.stack, old.hit.withBlockPos(offsetPos), old.includesFluid);
    }

    public BlockPlacementContext(World world, @NotNull BlockPos blockPos, @NotNull PlayerEntity player, @NotNull ItemStack stack, BlockHitResult hit, boolean includesFluid) {
        this.world = world;
        this.blockPos = blockPos;
        this.player = player;
        this.stack = stack;
        this.hit = hit;
        this.includesFluid = includesFluid;
        completeHandStacks();
        hitState = world.getBlockState(hit.getBlockPos());
        placementContext = new ItemPlacementContext(player, hand, hand == null ? new ItemStack(hitState.getBlock().asItem()) : player.getStackInHand(hand), hit);
        posToPlace = includesFluid ? blockPos.offset(hit.getSide()) : placementContext.getBlockPos();
        stateToReplace = world.getBlockState(posToPlace);
        @Nullable BlockState stateToPlace1;
        stateToPlace1 = handBlock == null ? null : handBlock.getPlacementState(placementContext);
        if (stateToPlace1 == null)
            stateToPlace1 = placementContext.canReplaceExisting() ? hitState.getBlock().getPlacementState(placementContext) : null;
        if (stateToPlace1 == null) stateToPlace1 = hitState;
        if (includesFluid && stateToPlace1.getProperties().contains(Properties.WATERLOGGED)) {
            stateToPlace1 = stateToPlace1.with(Properties.WATERLOGGED, stateToPlace1.getFluidState().getFluid() == Fluids.WATER);
        }
        this.stateToPlace = stateToPlace1;
    }

    public static @Nullable BlockPlacementContext ofContext(ItemUsageContext context, boolean includesFluid) {
        if (context.getPlayer() == null) {
            return null;
        } else {
            return new BlockPlacementContext(context, includesFluid);
        }
    }

    /**
     * 若玩家手中拿着方块物品，则获取此方块物品以及对应的手。
     */
    private void completeHandStacks() {
        for (@NotNull Hand hand1 : Hand.values()) {
            stackInHand = player.getStackInHand(hand1);
            if (stackInHand.getItem() instanceof BlockItem) {
                handBlock = ((BlockItem) stackInHand.getItem()).getBlock();
                hand = hand1;
                break;
            }
        }
    }

    /**
     * 放置方块。<br>
     * Place the block.
     * Calls {@link World#setBlockState}.
     */
    public boolean setBlockState(int flags) {
        return world.setBlockState(posToPlace, stateToPlace, flags);
    }

    /**
     * 检查方块能否放置。例如，如果 {@link #stateToPlace} 是花，且 {@link #posToPlace} 是悬在半空中的，则该方法返回 <code>false</code>。<br>
     * Checks if the {@link #stateToPlace} can be placed at {@link #posToPlace}. For example, if {@link #stateToPlace} is a flower, and {@link #posToPlace} is hung in the mid-air, the methods returns <code>false</code>.<br>
     * Calls {@link BlockState#canPlaceAt}.
     */
    public boolean canPlace() {
        return stateToPlace.canPlaceAt(world, posToPlace);
    }

    /**
     * 检查方块能否被替换。例如，如果 {@link #stateToReplace} 是草或者水，则返回 <code>true</code>。<br>
     * Checks if the {@link #stateToReplace} can be replaced in the placement-context. For example, if the {@link #stateToReplace} is a grass or water, then it returns <code>true</code>.<br>
     * Calls {@link BlockState#canReplace}.
     */
    public boolean canReplace() {
        return stateToReplace.canReplace(placementContext);
    }

    /**
     * 播放声音。该函数不会检查 {@link #world} 是否为客户端世界，需要在调用时手动检查。<br>
     * Play the sound of the block placed. Does not check if {@link #world} is client world. You may manually check it.
     */
    public void playSound() {
        BlockSoundGroup blockSoundGroup = stateToPlace.getSoundGroup();
        world.playSound(player, posToPlace, blockSoundGroup.getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
    }
}
