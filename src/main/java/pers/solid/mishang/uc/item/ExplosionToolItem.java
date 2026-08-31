package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.particles.ExplosionParticleInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.Optional;

public class ExplosionToolItem extends Item implements HotbarScrollInteraction, DispenseItemBehavior, WithMishangTooltip {
  public ExplosionToolItem(Properties settings) {
    super(settings.component(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT));
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    final ItemStack stack = user.getItemInHand(hand);
    final HitResult raycast = user.pick(128, 0, user.isShiftKeyDown());
    if (raycast.getType() == HitResult.Type.MISS) {
      return InteractionResult.FAIL;
    }
    if (!(world instanceof ServerLevel serverWorld)) {
      return InteractionResult.SUCCESS;
    }
    if (!serverWorld.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).hasAccess(user, true)) {
      return InteractionResult.PASS;
    }
    final Vec3 pos = raycast.getLocation();
    final boolean backup = serverWorld.getGameRules().get(GameRules.BLOCK_DROPS);
    if (user.isCreative()) {
      // 创造模式下，将游戏规则临时设为不掉落。
      serverWorld.getGameRules().set(GameRules.BLOCK_DROPS, false, null);
    }
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);

    Explosion.BlockInteraction destructionType = component.destructionType();

    ServerExplosion explosionImpl = new ServerExplosion(serverWorld, user, user.isShiftKeyDown() ? world.damageSources().explosion(null) : null, null, pos, component.power(), component.createFire(), destructionType);
    final int blocksCount = explosionImpl.explode();
    ParticleOptions particleEffect = ParticleTypes.EXPLOSION;

    for (ServerPlayer serverPlayerEntity : serverWorld.players()) {
      if (serverPlayerEntity.distanceToSqr(pos) < 4096.0) {
        Optional<Vec3> optional = Optional.ofNullable(explosionImpl.getHitPlayers().get(serverPlayerEntity));
        serverPlayerEntity.connection.send(new ClientboundExplodePacket(pos, explosionImpl.radius(), blocksCount, optional, particleEffect, SoundEvents.GENERIC_EXPLODE, EXPLOSION_BLOCK_PARTICLES));
      }
    }

    stack.hurtAndBreak((int) component.power(), user, hand.asEquipmentSlot());
    if (user.isCreative()) {
      serverWorld.getGameRules().set(GameRules.BLOCK_DROPS, backup, null);
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public Component getName(ItemStack stack) {
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);
    return Component.translatable(getDescriptionId() + ".formatted", component.power(), Component.translatable("item.mishanguc.explosion_tool.createFire." + component.createFire()), Component.translatable("item.mishanguc.explosion_tool.destructionType." + component.destructionType().name().toLowerCase()));
  }

  public void appendToEntries(CreativeModeTab.Output stacks) {
    stacks.accept(new ItemStack(this));
    ItemStack stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, true, Explosion.BlockInteraction.DESTROY));
    stacks.accept(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.BlockInteraction.KEEP));
    stacks.accept(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.BlockInteraction.DESTROY_WITH_DECAY));
    stacks.accept(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.BlockInteraction.TRIGGER_BLOCK));
    stacks.accept(stack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.explosion_tool.tooltip.1", Component.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.explosion_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.explosion_tool.tooltip.3").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.explosion_tool.tooltip.4").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.explosion_tool.tooltip.5").withStyle(ChatFormatting.GRAY));
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayer player, ItemStack stack) {
    final boolean creative = player.isCreative();
    final float power = Mth.clamp(stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT).power() - (float) scrollAmount, creative ? -128 : 0, creative ? 128 : 64);
    stack.update(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT, c -> c.withPower(power));
  }

  /**
   * @see Level#DEFAULT_EXPLOSION_BLOCK_PARTICLES
   */
  private static final WeightedList<ExplosionParticleInfo> EXPLOSION_BLOCK_PARTICLES = WeightedList.<ExplosionParticleInfo>builder()
      .add(new ExplosionParticleInfo(ParticleTypes.POOF, 0.5F, 1.0F))
      .add(new ExplosionParticleInfo(ParticleTypes.SMOKE, 1.0F, 1.0F))
      .build();

  @Override
  public ItemStack dispense(BlockSource pointer, ItemStack stack) {
    final ServerLevel serverWorld = pointer.level();
    if (!serverWorld.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).hasAccess(null)) {
      return stack;
    }
    final BlockPos basePos = pointer.pos();
    final Direction direction = pointer.state().getValue(DispenserBlock.FACING);
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);
    for (int i = 1; i < 33; i++) {
      final BlockPos pos = basePos.relative(direction, i);
      if (serverWorld.getBlockState(pos).getCollisionShape(serverWorld, pos).isEmpty()
          && serverWorld.getEntitiesOfClass(Entity.class, new AABB(pos), EntitySelector.NO_SPECTATORS.and(Entity::isPickable).and(EntityFlagsPredicate.Builder.flags().setCrouching(false).build()::matches)).isEmpty()
      ) {
        continue;
      }

      Explosion.BlockInteraction destructionType = component.destructionType();

      ServerExplosion explosionImpl = new ServerExplosion(serverWorld, null, serverWorld.damageSources().explosion(null), null, pos.getCenter(), component.power(), component.createFire(), destructionType);
      final int blockCount = explosionImpl.explode();
      ParticleOptions particleEffect = ParticleTypes.EXPLOSION;

      for (ServerPlayer serverPlayerEntity : serverWorld.players()) {
        if (serverPlayerEntity.distanceToSqr(pos.getCenter()) < 4096.0) {
          Optional<Vec3> optional = Optional.ofNullable(explosionImpl.getHitPlayers().get(serverPlayerEntity));
          serverPlayerEntity.connection.send(new ClientboundExplodePacket(pos.getCenter(), explosionImpl.radius(), blockCount, optional, particleEffect, SoundEvents.GENERIC_EXPLODE, EXPLOSION_BLOCK_PARTICLES));
        }
      }
      stack.hurtAndBreak((int) component.power(), serverWorld, null, item -> {});
    }
    return stack;
  }
}
