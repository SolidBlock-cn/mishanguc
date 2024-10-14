package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Collections;
import java.util.List;

@ApiStatus.AvailableSince("0.2.4")
public class GrowthToolItem extends Item implements InteractsWithEntity, MishangucItem, DispenserBehavior {
  public GrowthToolItem(Settings settings) {
    super(settings);
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    tooltip.add(TextBridge.translatable("item.mishanguc.growth_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.growth_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.growth_tool.tooltip.3").formatted(Formatting.GRAY));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final TypedActionResult<ItemStack> use = super.use(world, user, hand);
    if (world.isClient) return use;
    final HitResult raycast = user.raycast(64, 0, true);
    if (raycast.getType() == HitResult.Type.MISS) {
      return TypedActionResult.fail(use.getValue());
    }
    final Vec3d center = raycast.getPos();
    final int damage = apply(world, center, !user.isSneaking());
    user.getStackInHand(hand).damage(damage, user, LivingEntity.getSlotForHand(hand));
    return use;
  }

  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    if (super.canMine(state, world, pos, miner) && !world.isClient) {
      final int damage = apply(world, Vec3d.ofCenter(pos), !miner.isSneaking());
      miner.getStackInHand(Hand.MAIN_HAND).damage(damage, miner, EquipmentSlot.MAINHAND);
    }
    return false;
  }

  public static int apply(World world, Vec3d center, boolean isPositive) {
    int damage = 0;
    for (BlockPos pos : BlockPos.iterateOutwards(BlockPos.ofFloored(center), 4, 4, 4)) {
      final BlockState blockState = world.getBlockState(pos);
      if (blockState.getBlock().getStateManager().getProperty("age") instanceof IntProperty intProperty) {
        final Integer target = isPositive ? Collections.max(intProperty.getValues()) : Collections.min(intProperty.getValues());
        if (!blockState.get(intProperty).equals(target)) {
          world.setBlockState(pos, blockState.with(intProperty, target));
          createParticle(world, Vec3d.ofCenter(pos), isPositive);
          damage += 1;
        }
      }
    }
    for (Entity entity : world.getNonSpectatingEntities(Entity.class, Box.of(center, 9, 9, 9))) {
      if (entity instanceof PassiveEntity passiveEntity && passiveEntity.getBreedingAge() < 0) {
        passiveEntity.setBreedingAge(isPositive ? 0 : PassiveEntity.BABY_AGE);
        createParticle(world, entity.getPos(), isPositive);
        damage += 1;
      } else if (entity instanceof SlimeEntity slimeEntity) {
        final int prevSize = slimeEntity.getSize();
        if (isPositive) {
          slimeEntity.setSize(Math.min(prevSize * 2, Math.max(prevSize, 16)), false);
        } else {
          slimeEntity.setSize(prevSize / 2, false);
        }
        createParticle(world, entity.getPos(), isPositive);
        damage += 1;
      } else if (entity instanceof MobEntity mobEntity) {
        if (mobEntity.isBaby() == isPositive) {
          mobEntity.setBaby(!isPositive);
          createParticle(world, entity.getPos(), isPositive);
          damage += 1;
        }
      }
    }
    return damage;
  }

  public static void createParticle(World world, Vec3d pos, boolean isPositive) {
    if (world instanceof ServerWorld serverWorld) {
      serverWorld.spawnParticles(isPositive ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 16, 1, 1, 1, 0);
    }
  }


  @Override
  public @NotNull ActionResult useEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    final ActionResult actionResult = InteractsWithEntity.super.useEntityCallback(player, world, hand, entity, hitResult);
    if (actionResult == ActionResult.PASS && !world.isClient) {
      final int damage = apply(world, hitResult == null ? entity.getPos() : hitResult.getPos(), !player.isSneaking());
      player.getStackInHand(hand).damage(damage, player, LivingEntity.getSlotForHand(hand));
      return ActionResult.SUCCESS;
    }
    return actionResult;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    final ActionResult actionResult = InteractsWithEntity.super.attackEntityCallback(player, world, hand, entity, hitResult);
    if (actionResult == ActionResult.PASS && !world.isClient) {
      final int damage = apply(world, hitResult == null ? entity.getPos() : hitResult.getPos(), !player.isSneaking());
      player.getStackInHand(hand).damage(damage, player, LivingEntity.getSlotForHand(hand));
      return ActionResult.SUCCESS;
    }
    return actionResult;
  }

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final int damage = apply(pointer.world(), pointer.pos().offset(pointer.state().get(DispenserBlock.FACING), 4).toCenterPos(), true);
    stack.damage(damage, pointer.world(), null, item -> {});
    return stack;
  }
}
