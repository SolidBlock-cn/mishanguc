package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityFlagsPredicate;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class ExplosionToolItem extends Item implements HotbarScrollInteraction, DispenserBehavior, ItemResourceGenerator {
  public ExplosionToolItem(Settings settings) {
    super(settings.component(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT));
    DispenserBlock.registerBehavior(this, this);
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final ItemStack stack = user.getStackInHand(hand);
    final HitResult raycast = user.raycast(128, 0, user.isSneaking());
    if (raycast.getType() == HitResult.Type.MISS) {
      return TypedActionResult.fail(stack);
    }
    if (world.isClient) {
      return TypedActionResult.pass(stack);
    }
    if (!world.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).get().hasAccess(user, true)) {
      return TypedActionResult.pass(super.use(world, user, hand).getValue());
    }
    final Vec3d pos = raycast.getPos();
    final GameRules.BooleanRule booleanRule = world.getGameRules().get(GameRules.DO_TILE_DROPS);
    final boolean backup = booleanRule.get();
    if (user.isCreative()) {
      // 创造模式下，将游戏规则临时设为不掉落。
      booleanRule.set(false, null);
    }
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT);
    Explosion explosion = new Explosion(world, user, user.isSneaking() ? world.getDamageSources().explosion(null) : null, null, pos.x, pos.y, pos.z, component.power(), component.createFire(), component.destructionType(), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE);
    explosion.collectBlocksAndDamageEntities();
    explosion.affectWorld(true);

    // 适用于 1.19.3，因为不是通过 world.createExplosion 实现的，没有向客户端发送消息，所以需要在这里手动发送
    if (!explosion.shouldDestroy()) {
      explosion.clearAffectedBlocks();
    }
    for (PlayerEntity playerEntity : world.getPlayers()) {
      ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
      if (serverPlayerEntity.squaredDistanceTo(pos.x, pos.y, pos.z) < 4096.0) {
        serverPlayerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(pos.x, pos.y, pos.z, component.power(), explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(serverPlayerEntity), component.destructionType(), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE));
      }
    }
    stack.damage((int) component.power(), user, LivingEntity.getSlotForHand(hand));
    if (user.isCreative()) {
      booleanRule.set(backup, null);
    }
    return TypedActionResult.success(stack);
  }

  @Override
  public Text getName(ItemStack stack) {
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT);
    return TextBridge.translatable(getTranslationKey(stack) + ".formatted", component.power(), TextBridge.translatable("item.mishanguc.explosion_tool.createFire." + component.createFire()), TextBridge.translatable("item.mishanguc.explosion_tool.destructionType." + component.destructionType().name().toLowerCase()));
  }

  public void appendToEntries(ItemGroup.Entries stacks) {
    stacks.add(new ItemStack(this));
    ItemStack stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL, new ExplosionToolComponent(4, true, Explosion.DestructionType.DESTROY));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL, new ExplosionToolComponent(4, false, Explosion.DestructionType.KEEP));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL, new ExplosionToolComponent(4, false, Explosion.DestructionType.DESTROY_WITH_DECAY));
    stacks.add(stack);

    stack = new ItemStack(this);
    stack.set(MishangucComponents.EXPLOSION_TOOL, new ExplosionToolComponent(4, false, Explosion.DestructionType.TRIGGER_BLOCK));
    stacks.add(stack);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.1", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.2").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.3").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.4").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.5").formatted(Formatting.GRAY));

    final ExplosionToolComponent component = stack.get(MishangucComponents.EXPLOSION_TOOL);
    if (component != null) {
      component.appendTooltip(context, tooltip::add, type);
    }
  }

  @Override
  public void onScroll(int selectedSlot, double scrollAmount, ServerPlayerEntity player, ItemStack stack) {
    final boolean creative = player.isCreative();
    final float power = MathHelper.clamp(stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT).power() - (float) scrollAmount, creative ? -128 : 0, creative ? 128 : 64);
    stack.apply(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT, c -> c.withPower(power));
  }

  @Override
  public ItemStack dispense(BlockPointer pointer, ItemStack stack) {
    final ServerWorld world = pointer.world();
    if (!world.getGameRules().get(MishangucRules.EXPLOSION_TOOL_ACCESS).get().hasAccess(null)) {
      return stack;
    }
    final BlockPos basePos = pointer.pos();
    final Direction direction = pointer.state().get(DispenserBlock.FACING);
    final ExplosionToolComponent component = stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL, ExplosionToolComponent.DEFAULT);
    for (int i = 1; i < 33; i++) {
      final BlockPos pos = basePos.offset(direction, i);
      if (world.getBlockState(pos).getCollisionShape(world, pos).isEmpty()
          && world.getEntitiesByClass(Entity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR.and(Entity::canHit).and(EntityFlagsPredicate.Builder.create().sneaking(false).build()::test)).isEmpty()
      ) {
        continue;
      }
      Explosion explosion = new Explosion(world, null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, component.power(), component.createFire(), component.destructionType());
      explosion.collectBlocksAndDamageEntities();
      explosion.affectWorld(true);
      // 适用于 1.19.3，因为不是通过 world.createExplosion 实现的，没有向客户端发送消息，所以需要在这里手动发送
      if (!explosion.shouldDestroy()) {
        explosion.clearAffectedBlocks();
      }
      for (ServerPlayerEntity playerEntity : world.getPlayers()) {
        if (playerEntity.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) < 4096.0) {
          playerEntity.networkHandler.sendPacket(new ExplosionS2CPacket(pos.getX(), pos.getY(), pos.getZ(), component.power(), explosion.getAffectedBlocks(), explosion.getAffectedPlayers().get(playerEntity), component.destructionType(), ParticleTypes.EXPLOSION, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.ENTITY_GENERIC_EXPLODE));
        }
      }
      stack.damage((int) component.power(), world.getRandom(), null, () -> {});
    }
    return stack;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    // 此物品的模型由普通的资源包手动提供，不在运行时的资源包中。
    return null;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeItemModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder itemModel = getItemModel();
    final Identifier itemModelId = getItemModelId();
    if (itemModel != null) pack.addModel(itemModelId, itemModel);
    final Identifier textureId = getTextureId();
    for (final String name : new String[]{
        "_fire",
        "_4", "_4_fire",
        "_8", "_8_fire",
        "_16", "_16_fire",
        "_32", "_32_fire",
        "_64", "_64_fire",
        "_128", "_128_fire",
    }) {
      pack.addModel(itemModelId.brrp_suffixed(name), ModelJsonBuilder.create(Models.HANDHELD).addTexture(TextureKey.LAYER0, textureId.brrp_suffixed(name)));
    }
  }
}
