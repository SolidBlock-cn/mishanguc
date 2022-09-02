package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
public class GrowthToolItem extends Item implements InteractsWithEntity {
  public GrowthToolItem(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
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
    apply(world, user, center, hand, !user.isSneaking());
    return use;
  }

  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    if (super.canMine(state, world, pos, miner) && !world.isClient) {
      apply(world, miner, Vec3d.ofCenter(pos), Hand.MAIN_HAND, !miner.isSneaking());
    }
    return false;
  }

  public static void apply(World world, PlayerEntity player, Vec3d center, Hand hand, boolean isPositive) {
    int damage = 0;
    for (BlockPos pos : BlockPos.iterateOutwards(new BlockPos(center), 4, 4, 4)) {
      final BlockState blockState = world.getBlockState(pos);
      if (blockState.getBlock().getStateManager().getProperty("age") instanceof IntProperty intProperty) {
        final Integer target = isPositive ? Collections.max(intProperty.getValues()) : Collections.min(intProperty.getValues());
        if (!blockState.get(intProperty).equals(target)) {
          world.setBlockState(pos, blockState.with(intProperty, target));
          createParticle(world, Vec3d.ofCenter(pos), isPositive);
          damage += 1;
        }
      } else if (isPositive && blockState.getBlock() instanceof Fertilizable fertilizable && fertilizable.isFertilizable(world, pos, blockState, false)) {
        fertilizable.grow(((ServerWorld) world), world.getRandom(), pos, blockState);
        createParticle(world, Vec3d.ofCenter(pos), isPositive);
        damage += 1;
      }
    }
    for (Entity entity : world.getNonSpectatingEntities(Entity.class, Box.of(center, 9, 9, 9))) {
      if (entity instanceof PassiveEntity passiveEntity && passiveEntity.getBreedingAge() < 0) {
        passiveEntity.setBreedingAge(isPositive ? 0 : PassiveEntity.BABY_AGE);
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
    player.getStackInHand(hand).damage(damage, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
  }

  public static void createParticle(World world, Vec3d pos, boolean isPositive) {
    if (world instanceof ServerWorld serverWorld) {
      serverWorld.spawnParticles(isPositive ? ParticleTypes.HAPPY_VILLAGER : ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 8, 1, 1, 1, 0);
    }
  }


  @Override
  public @NotNull ActionResult useEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    final ActionResult actionResult = InteractsWithEntity.super.useEntityCallback(player, world, hand, entity, hitResult);
    if (actionResult == ActionResult.PASS && !world.isClient) {
      apply(world, player, hitResult == null ? entity.getPos() : hitResult.getPos(), hand, !player.isSneaking());
      return ActionResult.SUCCESS;
    }
    return actionResult;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    final ActionResult actionResult = InteractsWithEntity.super.attackEntityCallback(player, world, hand, entity, hitResult);
    if (actionResult == ActionResult.PASS && !world.isClient) {
      apply(world, player, hitResult == null ? entity.getPos() : hitResult.getPos(), hand, !player.isSneaking());
      return ActionResult.SUCCESS;
    }
    return actionResult;
  }
}
