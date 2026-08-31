package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class IceSnowTool extends Item implements MishangucItem, DispenseItemBehavior, HotbarScrollInteraction, WithMishangTooltip {
  public IceSnowTool(Properties settings) {
    super(settings.component(MishangucComponents.STRENGTH, 4));
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public Component getName(ItemStack stack) {
    return TextBridge.translatable("item.mishanguc.ice_snow_tool.format", getName(), Integer.toString(getStrength(stack)));
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.4").withStyle(ChatFormatting.GRAY));
    if (stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.STRENGTH)) {
      tooltip.add(TextBridge.translatable("item.mishanguc.ice_snow_tool.tooltip.strength", TextBridge.literal(Integer.toString(getStrength(stack))).withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
    }
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    final ItemStack stack = user.getItemInHand(hand);
    if (!(world instanceof ServerLevel serverWorld)) {
      return InteractionResult.SUCCESS;
    }
    final HitResult hitResult = user.pick(64, 0, false);
    if (hitResult.getType() == HitResult.Type.MISS)
      return InteractionResult.FAIL;
    final Vec3 pos = hitResult.getLocation();
    final int strength = getStrength(stack);
    if (user.isShiftKeyDown()) {
      applyHeat(serverWorld, pos, strength);
    } else {
      applyIce(serverWorld, pos, strength);
    }
    stack.hurtAndBreak(strength + 1, user, hand.asEquipmentSlot());
    return InteractionResult.SUCCESS;
  }

  /**
   * @see IceBlock
   * @see ServerLevel#tickChunk
   */
  public void applyIce(ServerLevel world, Vec3 pos, int strength) {
    final float probability = getProbability(strength);
    final int range = getRange(strength);
    final BlockPos centerBlockPos = BlockPos.containing(pos);
    for (final BlockPos blockPos : BlockPos.withinManhattan(centerBlockPos, range, 0, range)) {
      if (world.random.nextFloat() > probability) {
        continue;
      }

      final BlockPos topBlockPos = world.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos);

      // 结冰
      final boolean isInsufficientBlockLight = world.getBrightness(LightLayer.BLOCK, topBlockPos) < 10;
      final BlockPos waterBlockPos = topBlockPos.below();
      final boolean isWaterInRange = centerBlockPos.getY() - range <= waterBlockPos.getY() && blockPos.getY() <= centerBlockPos.getY() + range;
      final boolean isWater = isWaterInRange && isInsufficientBlockLight && world.getBlockState(waterBlockPos).getBlock() instanceof LiquidBlock && world.getFluidState(waterBlockPos).getType() == Fluids.WATER;
      if (isWater) {
        world.setBlockAndUpdate(waterBlockPos, Blocks.ICE.defaultBlockState());
      }

      // 模拟降雪
      final boolean isSnowInRange = centerBlockPos.getY() - range <= topBlockPos.getY() && topBlockPos.getY() <= centerBlockPos.getY() + range;
      final int snowAccumulationHeight = world.getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT);
      if (snowAccumulationHeight > 0 && isInsufficientBlockLight && isSnowInRange && Blocks.SNOW.defaultBlockState().canSurvive(world, topBlockPos)) {
        final BlockState blockState = world.getBlockState(topBlockPos);
        if (blockState.is(Blocks.SNOW)) {
          int layers = blockState.getValue(SnowLayerBlock.LAYERS);
          if (layers < Math.min(snowAccumulationHeight, 8)) {
            BlockState blockState2 = blockState.setValue(SnowLayerBlock.LAYERS, layers + 1);
            Block.pushEntitiesUp(blockState, blockState2, world, topBlockPos);
            world.setBlockAndUpdate(topBlockPos, blockState2);
          }
        } else if (blockState.isAir()) {
          world.setBlockAndUpdate(topBlockPos, Blocks.SNOW.defaultBlockState());
        }
      }
    }
    world.sendParticles(ParticleTypes.SNOWFLAKE, pos.x, pos.y, pos.z, (int) Math.pow((range * 2 + 1), 3) / 16, range, range, range, 0);
  }

  public void applyHeat(ServerLevel world, Vec3 pos, int strength) {
    final float probability = getProbability(strength);
    final int range = getRange(strength);
    for (BlockPos blockPos : BlockPos.withinManhattan(BlockPos.containing(pos), range, range, range)) {
      if (world.random.nextFloat() > probability) {
        continue;
      }

      // 结冰
      final BlockState blockState = world.getBlockState(blockPos);
      if (blockState.getBlock() instanceof IceBlock) {
        if (world.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, pos)) {
          world.removeBlock(blockPos, false);
        } else {
          world.setBlockAndUpdate(blockPos, IceBlock.meltsInto());
          world.neighborChanged(blockPos, IceBlock.meltsInto().getBlock(), null);
        }
      }

      // 模拟降雪
      if (blockState.is(Blocks.SNOW)) {
        SnowLayerBlock.dropResources(blockState, world, blockPos);
        world.removeBlock(blockPos, false);
      }
    }
    world.sendParticles(ParticleTypes.SMOKE, pos.x, pos.y, pos.z, (int) Math.pow((range * 2 + 1), 3) / 16, range, range, range, 0);
  }

  @Override
  public ItemStack dispense(BlockSource pointer, ItemStack stack) {
    final int strength = getStrength(stack);
    applyIce(pointer.level(), pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING), getRange(strength)).getCenter(), strength);
    stack.hurtAndBreak(strength + 1, pointer.level(), null, item -> {});
    return stack;
  }

  public static int getStrength(ItemStack stack) {
    return stack.getOrDefault(MishangucComponents.STRENGTH, 4);
  }

  public static float getProbability(int strength) {
    return Mth.clamp(0.7f + strength * 0.1f, 0.7f, 1f);
  }

  public static int getRange(int strength) {
    return Mth.clamp(4 + strength * strength / 2, 4, 64);
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayer player, ItemStack stack) {
    final int strength = getStrength(stack);
    final int newStrength = Mth.positiveModulo(strength - (int) scrollAmount, 8);
    stack.set(MishangucComponents.STRENGTH, newStrength);
  }
}
