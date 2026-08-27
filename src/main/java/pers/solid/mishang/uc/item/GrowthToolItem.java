package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.AgeableMobAccessor;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.Collections;
import java.util.List;

@ApiStatus.AvailableSince("0.2.4")
public class GrowthToolItem extends Item implements InteractsWithEntity, MishangucItem, DispenseItemBehavior, WithMishangTooltip {
  public GrowthToolItem(Properties settings) {
    super(settings);
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.growth_tool.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.growth_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.growth_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    if (world.isClientSide()) return InteractionResult.PASS;
    final HitResult raycast = user.pick(64, 0, true);
    if (raycast.getType() == HitResult.Type.MISS) {
      return InteractionResult.FAIL;
    }
    final Vec3 center = raycast.getLocation();
    final int damage = apply(user, world, center, !user.isShiftKeyDown());
    user.getItemInHand(hand).hurtAndBreak(damage, user, hand.asEquipmentSlot());
    return damage > 0 ? InteractionResult.SUCCESS_SERVER : InteractionResult.FAIL;
  }

  @Override
  public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
    if (super.canDestroyBlock(stack, state, world, pos, user) && !world.isClientSide()) {
      final int damage = apply(user instanceof Player player ? player : null, world, Vec3.atCenterOf(pos), !user.isShiftKeyDown());
      user.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(damage, user, EquipmentSlot.MAINHAND);
    }
    return false;
  }

  public static int apply(@Nullable Player player, Level world, Vec3 center, boolean isPositive) {
    int damage = 0;
    for (BlockPos pos : BlockPos.withinManhattan(BlockPos.containing(center), 4, 4, 4)) {
      final BlockState blockState = world.getBlockState(pos);
      if (blockState.getBlock().getStateDefinition().getProperty("age") instanceof IntegerProperty intProperty) {
        final Integer target = isPositive ? Collections.max(intProperty.getPossibleValues()) : Collections.min(intProperty.getPossibleValues());
        if (!blockState.getValue(intProperty).equals(target)) {
          world.setBlockAndUpdate(pos, blockState.setValue(intProperty, target));
          createParticle(world, Vec3.atCenterOf(pos), isPositive);
          damage += 1;
        }
      }
    }
    final ItemStack offhandStack = player == null ? ItemStack.EMPTY : player.getOffhandItem();
    final List<Entity> entities = world.getEntitiesOfClass(Entity.class, AABB.ofSize(center, 9, 9, 9));
    for (Entity entity : entities) {
      switch (entity) {
        case AgeableMob ageableMob -> {
          boolean shouldDamage = false;
          if (ageableMob instanceof AbstractCubeMob abstractCubeMob) {
            final int prevSize = abstractCubeMob.getSize();
            final int newSize;
            if (isPositive) {
              newSize = Math.min(prevSize + 1, 8);
            } else {
              newSize = Math.max(1, prevSize - 1);
            }
            if (prevSize != newSize) {
              abstractCubeMob.setSize(newSize, true);
              createParticle(world, entity.position(), isPositive);
              shouldDamage = true;
            }
          }
          final boolean isUsingGoldenDandelion = !(ageableMob instanceof AbstractCubeMob cube && cube.getSize() > 1 != isPositive) && AgeableMob.canUseGoldenDandelion(offhandStack, true, 0, ageableMob) && ageableMob.isAgeLocked() == isPositive;
          if (isUsingGoldenDandelion) {
            AgeableMob.setAgeLocked(ageableMob, ageableMob::isAgeLocked, player, offhandStack, mob -> {
              if (!(mob instanceof AgeableMob ageableMob0)) return;
              ((AgeableMobAccessor) ageableMob0).callSetAgeLockedData();
            });

            final boolean isAgeLocked = ageableMob.isAgeLocked();
            float yParticleOffset = isAgeLocked ? 0.2F : 0.0F;
            Vec3 spawnPosition = new Vec3(ageableMob.getRandomX(1.0F), ageableMob.getRandomY(0.2) + (double) ageableMob.getBbHeight() + (double) yParticleOffset, ageableMob.getRandomZ(1.0F));
            if (world instanceof ServerLevel serverLevel) {
              serverLevel.sendParticles(isAgeLocked ? ParticleTypes.PAUSE_MOB_GROWTH : ParticleTypes.RESET_MOB_GROWTH, spawnPosition.x, spawnPosition.y, spawnPosition.z, 16, 1, 1, 1, 0);
            }
            shouldDamage = true;
          }
          if (ageableMob.isBaby() == isPositive) {
            ageableMob.setAge((ageableMob instanceof AbstractCubeMob cube ? cube.getSize() > 1 : isPositive) ? 0 : AgeableMob.BABY_START_AGE);
            if (!isUsingGoldenDandelion) {
              createParticle(world, entity.position(), isPositive);
            }
            shouldDamage = true;
          }

          if (shouldDamage) {
            damage += 1;
          }
        }
        case Mob mobEntity -> {
          if (mobEntity.isBaby() == isPositive) {
            mobEntity.setBaby(!isPositive);
            createParticle(world, entity.position(), isPositive);
            damage += 1;
          }
        }
        default -> {
        }
      }
    }
    return damage;
  }

  public static void createParticle(Level world, Vec3 pos, boolean isPositive) {
    if (world instanceof ServerLevel serverWorld) {
      serverWorld.sendParticles(isPositive ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 16, 1, 1, 1, 0);
    }
  }

  @Override
  public InteractionResult useEntityCallback(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (player instanceof LocalPlayer) return InteractionResult.CONSUME;
    final int damage = apply(player, player.level(), entity.position(), !player.isShiftKeyDown());
    player.getItemInHand(hand).hurtAndBreak(damage, player, hand.asEquipmentSlot());
    if (damage > 0) {
      // 由于在 Fabric API 中，返回 SUCCESS_SERVER 并不会使玩家真正挥手，故这里手动调用以特殊处理。
      player.swing(hand, true);
    }
    return damage > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
  }

  @Override
  public InteractionResult attackEntityCallback(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    final InteractionResult actionResult = InteractsWithEntity.super.attackEntityCallback(player, world, hand, entity, hitResult);
    if (actionResult == InteractionResult.PASS && !world.isClientSide()) {
      final int damage = apply(player, world, hitResult == null ? entity.position() : hitResult.getLocation(), !player.isShiftKeyDown());
      player.getItemInHand(hand).hurtAndBreak(damage, player, hand.asEquipmentSlot());
      return InteractionResult.SUCCESS;
    }
    return actionResult;
  }

  @Override
  public ItemStack dispense(BlockSource pointer, ItemStack stack) {
    final int damage = apply(null, pointer.level(), Vec3.atCenterOf(pointer.pos().relative(pointer.state().getValue(DispenserBlock.FACING), 4)), true);
    stack.hurtAndBreak(damage, pointer.level(), null, item -> {});
    return stack;
  }
}
