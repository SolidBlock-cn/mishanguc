package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Unit;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class OmnipotentToolItem extends Item implements MishangucItem, InteractsWithEntity, WithMishangTooltip {
  protected static final ToolMaterial MATERIAL = new ToolMaterial(TagKey.of(RegistryKeys.BLOCK, Mishanguc.id("incorrect_for_omnipotent_tool")), Integer.MAX_VALUE, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Integer.MAX_VALUE, TagKey.of(RegistryKeys.ITEM, Mishanguc.id("omnipotent_repair_items")));

  public OmnipotentToolItem(Settings settings) {
    super(settings);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(TextBridge.translatable("item.mishanguc.omnipotent_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.omnipotent_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
  }

  @Override
  public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
    if (world instanceof ServerWorld serverWorld) {
      serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 32, 0.5, 0.5, 0.5, 0);
    }
    return super.postMine(stack, world, state, pos, miner);
  }

  @Override
  public @NotNull ActionResult useEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (entity instanceof LivingEntity livingEntity) {
      livingEntity.heal(Float.POSITIVE_INFINITY);
      if (world instanceof ServerWorld serverWorld) {
        serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 32, 0.5, 0.5, 0.5, 0.5);
      }
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
    if (world instanceof ServerWorld serverWorld) {
      serverWorld.spawnParticles(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY(), entity.getZ(), 32, 0.5, 0.5, 0.5, 0.5);
    }
    return ActionResult.PASS;
  }

  @Override
  public ItemStack getDefaultStack() {
    final ItemStack defaultStack = super.getDefaultStack();
    defaultStack.set(DataComponentTypes.UNBREAKABLE, Unit.INSTANCE);
    return defaultStack;
  }

  @Override
  public float getMiningSpeed(ItemStack stack, BlockState state) {
    return Float.POSITIVE_INFINITY;
  }

  @Override
  public boolean isCorrectForDrops(ItemStack stack, BlockState state) {
    return true;
  }
}
