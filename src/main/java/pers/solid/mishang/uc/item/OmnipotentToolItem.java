package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class OmnipotentToolItem extends Item implements MishangucItem, InteractsWithEntity, WithMishangTooltip {
  protected static final ToolMaterial MATERIAL = new ToolMaterial(TagKey.create(Registries.BLOCK, Mishanguc.id("incorrect_for_omnipotent_tool")), Integer.MAX_VALUE, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Integer.MAX_VALUE, TagKey.create(Registries.ITEM, Mishanguc.id("omnipotent_repair_items")));

  public OmnipotentToolItem(Properties settings) {
    super(settings.tool(MATERIAL, BlockTags.MINEABLE_WITH_PICKAXE, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 0f).component(DataComponents.UNBREAKABLE, Unit.INSTANCE));
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.omnipotent_tool.tooltip.1", TextBridge.keybind("key.attack").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.omnipotent_tool.tooltip.2", TextBridge.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public boolean mineBlock(ItemStack stack, Level world, BlockState state, BlockPos pos, LivingEntity miner) {
    if (world instanceof ServerLevel serverWorld) {
      serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 32, 0.5, 0.5, 0.5, 0);
    }
    return super.mineBlock(stack, world, state, pos, miner);
  }

  @Override
  public InteractionResult useEntityCallback(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (entity instanceof LivingEntity livingEntity) {
      livingEntity.heal(Float.POSITIVE_INFINITY);
      if (world instanceof ServerLevel serverWorld) {
        serverWorld.sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 32, 0.5, 0.5, 0.5, 0.5);
      }
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult attackEntityCallback(Player player, Level world, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (world instanceof ServerLevel serverWorld) {
      serverWorld.sendParticles(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY(), entity.getZ(), 32, 0.5, 0.5, 0.5, 0.5);
    }
    return InteractionResult.PASS;
  }

  @Override
  public ItemStack getDefaultInstance() {
    final ItemStack defaultStack = super.getDefaultInstance();
    defaultStack.set(DataComponents.UNBREAKABLE, Unit.INSTANCE);
    return defaultStack;
  }

  @Override
  public float getDestroySpeed(ItemStack stack, BlockState state) {
    return Float.POSITIVE_INFINITY;
  }

  @Override
  public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
    return true;
  }
}
