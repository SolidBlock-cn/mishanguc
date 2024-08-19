package pers.solid.mishang.uc.item;

import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class IceSnowTool extends Item implements MishangucItem, DispenserBehavior, HotbarScrollInteraction {
  public IceSnowTool(Settings settings) {
    super(settings);
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public Text getName(ItemStack stack) {
    return TextBridge.translatable("item.mishanguc.ice_snow_tool.format", getName(), Integer.toString(getStrength(stack)));
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.3").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.4").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.strength", TextBridge.literal(Integer.toString(getStrength(stack))).formatted(Formatting.YELLOW)).formatted(Formatting.GRAY));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final ItemStack stack = user.getStackInHand(hand);
    if (!(world instanceof ServerWorld serverWorld))
      return TypedActionResult.success(stack);
    final HitResult hitResult = user.raycast(64, 0, false);
    if (hitResult.getType() == HitResult.Type.MISS)
      return TypedActionResult.fail(stack);
    final Vec3d pos = hitResult.getPos();
    final int strength = getStrength(stack);
    if (user.isSneaking()) {
      applyHeat(serverWorld, pos, strength);
    } else {
      applyIce(serverWorld, pos, strength);
    }
    stack.damage(strength + 1, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
    return TypedActionResult.success(stack);
  }

  /**
   * @see IceBlock
   * @see ServerWorld#tickChunk
   */
  public void applyIce(@NotNull ServerWorld world, @NotNull Vec3d pos, int strength) {
    final float probability = getProbability(strength);
    final int range = getRange(strength);
    final BlockPos centerBlockPos = BlockPos.ofFloored(pos);
    for (final BlockPos blockPos : BlockPos.iterateOutwards(centerBlockPos, range, 0, range)) {
      if (world.random.nextFloat() > probability) {
        continue;
      }

      final BlockPos topBlockPos = world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos);

      // 结冰
      final boolean isInsufficientBlockLight = world.getLightLevel(LightType.BLOCK, topBlockPos) < 10;
      final BlockPos waterBlockPos = topBlockPos.down();
      final boolean isWaterInRange = centerBlockPos.getY() - range <= waterBlockPos.getY() && blockPos.getY() <= centerBlockPos.getY() + range;
      final boolean isWater = isWaterInRange && isInsufficientBlockLight && world.getBlockState(waterBlockPos).getBlock() instanceof FluidBlock && world.getFluidState(waterBlockPos).getFluid() == Fluids.WATER;
      if (isWater) {
        world.setBlockState(waterBlockPos, Blocks.ICE.getDefaultState());
      }

      // 模拟降雪
      final boolean isSnowInRange = centerBlockPos.getY() - range <= topBlockPos.getY() && topBlockPos.getY() <= centerBlockPos.getY() + range;
      final int snowAccumulationHeight = world.getGameRules().getInt(GameRules.SNOW_ACCUMULATION_HEIGHT);
      if (snowAccumulationHeight > 0 && isInsufficientBlockLight && isSnowInRange && Blocks.SNOW.getDefaultState().canPlaceAt(world, topBlockPos)) {
        final BlockState blockState = world.getBlockState(topBlockPos);
        if (blockState.isOf(Blocks.SNOW)) {
          int layers = blockState.get(SnowBlock.LAYERS);
          if (layers < Math.min(snowAccumulationHeight, 8)) {
            BlockState blockState2 = blockState.with(SnowBlock.LAYERS, layers + 1);
            Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, topBlockPos);
            world.setBlockState(topBlockPos, blockState2);
          }
        } else if (blockState.isAir()) {
          world.setBlockState(topBlockPos, Blocks.SNOW.getDefaultState());
        }
      }
    }
    world.spawnParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, (int) Math.pow((range * 2 + 1), 3) / 16, range, range, range, 0);
  }

  public void applyHeat(@NotNull ServerWorld world, @NotNull Vec3d pos, int strength) {
    final float probability = getProbability(strength);
    final int range = getRange(strength);
    for (BlockPos blockPos : BlockPos.iterateOutwards(BlockPos.ofFloored(pos), range, range, range)) {
      if (world.random.nextFloat() > probability) {
        continue;
      }

      // 结冰
      final BlockState blockState = world.getBlockState(blockPos);
      if (blockState.getBlock() instanceof IceBlock) {
        if (world.getDimension().ultrawarm()) {
          world.removeBlock(blockPos, false);
        } else {
          world.setBlockState(blockPos, Blocks.WATER.getDefaultState());
          world.updateNeighbor(blockPos, Blocks.WATER, blockPos);
        }
      }

      // 模拟降雪
      if (blockState.isOf(Blocks.SNOW)) {
        SnowBlock.dropStacks(blockState, world, blockPos);
        world.removeBlock(blockPos, false);
      }
    }
    world.spawnParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, (int) Math.pow((range * 2 + 1), 3) / 16, range, range, range, 0);
  }

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final int strength = getStrength(stack);
    applyIce(pointer.getWorld(), pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING), getRange(strength)).toCenterPos(), strength);
    if (stack.damage(strength + 1, pointer.getWorld().random, null)) {
      stack.setCount(0);
    }
    return stack;
  }

  public static int getStrength(ItemStack stack) {
    final NbtCompound nbt = stack.getNbt();
    return nbt == null || !nbt.contains("strength", NbtElement.NUMBER_TYPE) ? 4 : MathHelper.clamp(nbt.getInt("strength"), 0, 10);
  }

  public static float getProbability(int strength) {
    return MathHelper.clamp(0.7f + strength * 0.1f, 0.7f, 1f);
  }

  public static int getRange(int strength) {
    return MathHelper.clamp(4 + strength * strength / 2, 4, 64);
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final int strength = getStrength(stack);
    final int newStrength = MathHelper.floorMod(strength - (int) scrollAmount, 8);
    stack.getOrCreateNbt().putInt("strength", newStrength);
  }
}
