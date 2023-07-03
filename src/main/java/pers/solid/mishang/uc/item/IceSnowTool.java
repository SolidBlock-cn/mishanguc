package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.Models;
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
import net.minecraft.world.Heightmap;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class IceSnowTool extends Item implements ItemResourceGenerator, DispenserBehavior, HotbarScrollInteraction {
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
    if (!(world instanceof ServerWorld serverWorld)) return TypedActionResult.success(stack);
    final HitResult hitResult = user.raycast(64, 0, false);
    if (hitResult.getType() == HitResult.Type.MISS) return TypedActionResult.fail(stack);
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
    for (BlockPos blockPos : BlockPos.iterateOutwards(new BlockPos(pos), range, range, range)) {
      if (world.random.nextFloat() > probability) {
        continue;
      }

      // 结冰
      final boolean isTopPosition = world.getTopY(Heightmap.Type.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ()) == blockPos.getY();
      final boolean isInValidPos = isTopPosition && pos.getY() >= world.getBottomY() && pos.getY() < world.getTopY();
      final boolean isInsufficientBlockLight = isInValidPos && world.getLightLevel(LightType.BLOCK, blockPos) < 10;
      final boolean isWater = isInsufficientBlockLight && world.getBlockState(blockPos).getBlock() instanceof FluidBlock && world.getFluidState(blockPos).getFluid() == Fluids.WATER;
      if (isWater) {
        world.setBlockState(blockPos, Blocks.ICE.getDefaultState());
      }

      // 模拟降雪
      final int snowAccumulationHeight = 1;
      if (isInsufficientBlockLight && Blocks.SNOW.getDefaultState().canPlaceAt(world, blockPos)) {
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SNOW)) {
          int layers = blockState.get(SnowBlock.LAYERS);
          if (layers < Math.min(snowAccumulationHeight, 8)) {
            BlockState blockState2 = blockState.with(SnowBlock.LAYERS, layers + 1);
            Block.pushEntitiesUpBeforeBlockChange(blockState, blockState2, world, blockPos);
            world.setBlockState(blockPos, blockState2);
          }
        } else {
          world.setBlockState(blockPos, Blocks.SNOW.getDefaultState());
        }
      }
    }
    world.spawnParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, (int) Math.pow((range * 2 + 1), 3) / 16, range, range, range, 0);
  }

  public void applyHeat(@NotNull ServerWorld world, @NotNull Vec3d pos, int strength) {
    final float probability = getProbability(strength);
    final int range = getRange(strength);
    for (BlockPos blockPos : BlockPos.iterateOutwards(new BlockPos(pos), range, range, range)) {
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

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    return ItemResourceGenerator.super.getItemModel().parent(Models.HANDHELD);
  }

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final int strength = getStrength(stack);
    applyIce(pointer.getWorld(), Vec3d.ofCenter(pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING), getRange(strength))), strength);
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
