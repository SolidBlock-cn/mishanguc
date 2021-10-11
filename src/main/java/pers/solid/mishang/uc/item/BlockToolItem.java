package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;

public abstract class BlockToolItem extends Item {
    /**
     * 该物品是否包括流体。<br>
     * 如果该值为 <code>null</code>，则一般表示“视情况”，通常情况下是仅潜行时包括流体。
     * Whether fluids are included.<br>
     * If the value of it is <code>null</code>, it usually means "it depends", typically "does while sneaking".
     */
    final @Nullable Boolean includesFluid;

    public BlockToolItem(Settings settings, @Nullable Boolean includesFluid) {
        super(settings);
        this.includesFluid = includesFluid;
    }

    /**
     * Extends the accessibility of {@link Item#raycast} without mixin.
     *
     * @see Item#raycast
     */
    public static BlockHitResult raycast(World world, PlayerEntity player, RaycastContext.FluidHandling fluidHandling) {
        return Item.raycast(world, player, fluidHandling);
    }

    /**
     * 玩家手持物品点击方块的行为。通常此时准星已经指向一个非流体方块。<br>
     * Behaviour when a player click a block holding the item. Usually the crossbar has already focused on a non-fluid block.<br>
     * 如果 {@link #includesFluid} 为 <code>true</code>，则该方法不会执行，因为 {@link #use} 会执行，并执行可以包含流体的视线追踪。<br>
     * If {@link #includesFluid} is <code>false</code>, it does not execute, because {@link #use} executes, and performs raycast that may include fluids.<br>
     * 会在服务端和客户端同时执行。Executes both on client and server side.
     *
     * @see Item#useOnBlock(ItemUsageContext)
     */
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getPlayer() == null || includesFluid(context.getStack(), context.getPlayer().isSneaking()))
            return ActionResult.PASS;
        final ActionResult actionResult = useOnBlock(context, false);
        if (actionResult == ActionResult.PASS) {
            return super.useOnBlock(context);
        } else {
            return actionResult;
        }
    }

    public final ActionResult useOnBlock(ItemUsageContext context, boolean fluidIncluded) {
        return useOnBlock(context.getPlayer(), context.getWorld(), ((ItemUsageContextInvoker) context).invokeGetHitResult(), context, fluidIncluded);
    }

    /**
     * 默认情况下，该方法仅在 {@link #includesFluid} 为 <code>false</code> 的情况下执行，此时会进行视线追踪并获取可能为流体的方块触及结果。<br>
     * By default this methods only performs when {@link #includesFluid} returns <code>false</code>, when it performs raycast and get the {@link BlockHitResult} that may be of a fluid.
     * 会在服务端和客户端同时执行。Executes both on client and server side.
     *
     * @see #raycast
     * @see Item#use(World, PlayerEntity, Hand)
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final ItemStack stackInHand = user.getStackInHand(hand);
        final boolean includesFluid = includesFluid(stackInHand, user.isSneaking());
        if (!includesFluid) return TypedActionResult.pass(stackInHand);
        BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.ANY);
        if (hitResult.getType() == HitResult.Type.MISS) return TypedActionResult.fail(stackInHand);
        final ActionResult actionResult = this.useOnBlock(user, world, hitResult, new ItemUsageContext(user, hand, hitResult), includesFluid);
        if (actionResult == ActionResult.PASS) {
            return super.use(world, user, hand);
        } else {
            return new TypedActionResult<>(actionResult, stackInHand);
        }
    }

    public final ActionResult mineBlock(PlayerEntity player, World world, BlockPos blockPos, boolean fluidIncluded) {
        return mineBlock(player, world, blockPos, world.getBlockState(blockPos), fluidIncluded);
    }

    /**
     * 使用此物品右键单击物品时的反应。
     * The reaction when right-clicking the block with the item.
     */
    public abstract ActionResult useOnBlock(PlayerEntity player, World world, BlockHitResult blockHitResult, @Nullable ItemUsageContext itemUsageContext, boolean fluidIncluded);

    public abstract ActionResult mineBlock(PlayerEntity player, World world, BlockPos blockPos, BlockState blockState, boolean fluidIncluded);

    public boolean includesFluid(ItemStack stack, boolean def) {
        final @Nullable Boolean includesFluid = this.includesFluid(stack);
        if (includesFluid == null) return def;
        else return includesFluid;
    }

    /**
     * 如果物品堆的物品标签包含 IncludesFluid 标签，则返回其值，否则返回物品对象中的 {@link #includesFluid}。
     * Returns the value of tag IncludesFluid if it exists, otherwise returns {@link #includesFluid}.
     *
     * @param stack The item stack.
     * @return Whether it can detect fluid. May be {@code null}able, which means it depends.
     */
    public @Nullable Boolean includesFluid(ItemStack stack) {
        final NbtCompound tag = stack.getTag();
        if (tag == null || !tag.contains("IncludesFluid")) return this.includesFluid;
        else return tag.getBoolean("IncludesFluid");
    }

    @Override
    public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        final ActionResult actionResult = mineBlock(miner, world, pos, state, includesFluid(miner.getMainHandStack().isEmpty() ? miner.getOffHandStack() : miner.getMainHandStack(), miner.isSneaking()));
        if (actionResult == ActionResult.PASS) {
            return super.canMine(state, world, pos, miner);
        } else {
            return !resistsMining(state, world, pos, miner);
        }
    }

    /**
     * @return Whether it makes {@link #canMine} returns <code>false</code>.
     */
    public boolean resistsMining(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
        return true;
    }
}
