package pers.solid.mishang.uc.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.BlockParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionImpl;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
import java.util.Optional;

public class ExplosionToolItem extends Item implements HotbarScrollInteraction, DispenserBehavior, WithMishangTooltip {
  public ExplosionToolItem(Settings settings) {
    super(settings.component(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT));
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public ActionResult use(World world, PlayerEntity user, Hand hand) {
    final ItemStack stack = user.getStackInHand(hand);
    final HitResult raycast = user.raycast(128, 0, user.isSneaking());
    if (raycast.getType() == HitResult.Type.MISS) {
      return ActionResult.FAIL;
    }
    if (!(world instanceof ServerWorld serverWorld)) {
      return ActionResult.PASS;
    }
    if (!serverWorld.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).get().hasAccess(user, true)) {
      return ActionResult.PASS;
    }
    final Vec3d pos = raycast.getPos();
    final GameRules.BooleanRule booleanRule = serverWorld.getGameRules().get(GameRules.DO_TILE_DROPS);
    final boolean backup = booleanRule.get();
    if (user.isCreative()) {
      // 创造模式下，将游戏规则临时设为不掉落。
      booleanRule.set(false, null);
    }
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);

    Explosion.DestructionType destructionType = component.destructionType();

    ExplosionImpl explosionImpl = new ExplosionImpl(serverWorld, user, user.isSneaking() ? world.getDamageSources().explosion(null) : null, null, pos, component.power(), component.createFire(), destructionType);
    final int blocksCount = explosionImpl.explode();
    ParticleEffect particleEffect = ParticleTypes.EXPLOSION;

    for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
      if (serverPlayerEntity.squaredDistanceTo(pos) < 4096.0) {
        Optional<Vec3d> optional = Optional.ofNullable(explosionImpl.getKnockbackByPlayer().get(serverPlayerEntity));
        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(pos, explosionImpl.getPower(), blocksCount, optional, particleEffect, SoundEvents.ENTITY_GENERIC_EXPLODE, EXPLOSION_BLOCK_PARTICLES));
      }
    }

    stack.damage((int) component.power(), user, hand.getEquipmentSlot());
    if (user.isCreative()) {
      booleanRule.set(backup, null);
    }
    return ActionResult.SUCCESS_SERVER;
  }

  @Override
  public Text getName(ItemStack stack) {
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);
    return TextBridge.translatable(getTranslationKey() + ".formatted", component.power(), TextBridge.translatable("item.mishanguc.explosion_tool.createFire." + component.createFire()), TextBridge.translatable("item.mishanguc.explosion_tool.destructionType." + component.destructionType().name().toLowerCase()));
  }

  public void appendToEntries(ItemGroup.Entries stacks) {
    stacks.add(new ItemStack(this));
    ItemStack stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, true, Explosion.DestructionType.DESTROY));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.DestructionType.KEEP));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.DestructionType.DESTROY_WITH_DECAY));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL_DATA, new ExplosionToolComponent(4, false, Explosion.DestructionType.TRIGGER_BLOCK));
    stacks.add(stack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.1", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.3").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.4").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.5").formatted(Formatting.GRAY));
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final boolean creative = player.isCreative();
    final float power = MathHelper.clamp(stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT).power() - (float) scrollAmount, creative ? -128 : 0, creative ? 128 : 64);
    stack.apply(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT, c -> c.withPower(power));
  }

  /**
   * @see World#EXPLOSION_BLOCK_PARTICLES
   */
  private static final Pool<BlockParticleEffect> EXPLOSION_BLOCK_PARTICLES = Pool.<BlockParticleEffect>builder()
      .add(new BlockParticleEffect(ParticleTypes.POOF, 0.5F, 1.0F))
      .add(new BlockParticleEffect(ParticleTypes.SMOKE, 1.0F, 1.0F))
      .build();

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final ServerWorld serverWorld = pointer.world();
    if (!serverWorld.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).get().hasAccess(null)) {
      return stack;
    }
    final BlockPos basePos = pointer.pos();
    final Direction direction = pointer.state().get(DispenserBlock.FACING);
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT);
    for (int i = 1; i < 33; i++) {
      final BlockPos pos = basePos.offset(direction, i);
      if (serverWorld.getBlockState(pos).getCollisionShape(serverWorld, pos).isEmpty()
          && serverWorld.getEntitiesByClass(Entity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit).and(EntityFlagsPredicate.Builder.create().sneaking(false).build()::test)).isEmpty()
      ) {
        continue;
      }

      Explosion.DestructionType destructionType = component.destructionType();

      ExplosionImpl explosionImpl = new ExplosionImpl(serverWorld, null, serverWorld.getDamageSources().explosion(null), null, pos.toCenterPos(), component.power(), component.createFire(), destructionType);
      final int blockCount = explosionImpl.explode();
      ParticleEffect particleEffect = ParticleTypes.EXPLOSION;

      for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers()) {
        if (serverPlayerEntity.squaredDistanceTo(pos.toCenterPos()) < 4096.0) {
          Optional<Vec3d> optional = Optional.ofNullable(explosionImpl.getKnockbackByPlayer().get(serverPlayerEntity));
          serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(pos.toCenterPos(), explosionImpl.getPower(), blockCount, optional, particleEffect, SoundEvents.ENTITY_GENERIC_EXPLODE, EXPLOSION_BLOCK_PARTICLES));
        }
      }
      stack.damage((int) component.power(), serverWorld, null, item -> {});
    }
    return stack;
  }
}
