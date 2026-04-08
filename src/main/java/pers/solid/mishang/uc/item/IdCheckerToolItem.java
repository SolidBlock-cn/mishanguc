package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.EnvironmentInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class IdCheckerToolItem extends BlockToolItemWithEntity implements InteractsWithEntity, RendersBeforeOutline, WithMishangTooltip {
  public IdCheckerToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public InteractionResult getIdOf(Player player, Level world, BlockPos blockPos) {
    BlockState blockState = world.getBlockState(blockPos);
    if (player != null) {
      final Block block = blockState.getBlock();
      final Identifier identifier = BuiltInRegistries.BLOCK.getKey(block);
      final int rawId = BuiltInRegistries.BLOCK.getId(block);
      player.displayClientMessage(
          TextBridge.literal("")
              .append(TextBridge.translatable("debug.mishanguc.blockId.header", String.format(
                      "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .withStyle(ChatFormatting.YELLOW)), false);
      broadcastId(player, block.getName(), identifier, rawId);
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.SUCCESS;
  }

  /**
   * 发送一个方块、实体或其他事物的id。
   */
  private void broadcastId(
      Player player, Component name, @Nullable Identifier identifier, int rawId) {
    player.displayClientMessage(
        TextBridge.literal("  ").append(TextBridge.translatable("debug.mishanguc.id.name", name))
            .append("\n  ")
            .append(TextBridge.translatable("debug.mishanguc.id.id", identifier == null
                ? TextBridge.translatable("gui.none")
                : TextBridge.literal(identifier.toString())))
            .append("\n  ")
            .append(TextBridge.translatable("debug.mishanguc.id.rawId", TextBridge.literal(Integer.toString(rawId)))), false);
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    if (world.isClientSide()) return getIdOf(player, world, blockHitResult.getBlockPos());
    else return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClientSide()) return getIdOf(player, world, pos);
    else return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    if (world.isClientSide()) {
      final BlockPos blockPos = user.blockPosition();
      final Biome biome = user.level().getBiome(blockPos).value();
      final Registry<Biome> biomes = world.registryAccess().lookupOrThrow(Registries.BIOME);
      final Identifier identifier = biomes.getKey(biome);
      final int rawId = biomes.getId(biome);
      user.displayClientMessage(
          TextBridge.literal("").append(
              TextBridge.translatable("debug.mishanguc.biomeId.header", String.format(
                      "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                  .withStyle(ChatFormatting.YELLOW)), false);
      broadcastId(
          user,
          TextBridge.translatable(Util.makeDescriptionId("biome", identifier)),
          identifier,
          rawId);
    }
    return super.use(world, user, hand);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(
        TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.1")
            .withStyle(ChatFormatting.GRAY));
    final @Nullable Boolean includesFluid = includesFluid(stack);
    if (stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.INCLUDES_FLUID)) {
      if (includesFluid == null) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.2")
                .withStyle(ChatFormatting.GRAY));
      } else if (includesFluid) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.id_checker_tool.tooltip.3")
                .withStyle(ChatFormatting.GRAY));
      }
    }
  }

  @Override
  public InteractionResult attackEntityCallback(
      Player player,
      Level world,
      InteractionHand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return useEntityCallback(player, world, hand, entity, hitResult);
  }

  @Override
  public InteractionResult useEntityCallback(
      Player player,
      Level world,
      InteractionHand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (player.isSpectator()) return InteractionResult.PASS;
    if (!world.isClientSide()) return InteractionResult.SUCCESS;
    final BlockPos blockPos = entity.blockPosition();
    player.displayClientMessage(
        TextBridge.literal("").append(
            TextBridge.translatable("debug.mishanguc.entityId.header", String.format(
                    "%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .withStyle(ChatFormatting.YELLOW)), false);
    final EntityType<?> type = entity.getType();
    broadcastId(
        player,
        entity.getName(),
        BuiltInRegistries.ENTITY_TYPE.getKey(type),
        BuiltInRegistries.ENTITY_TYPE.getId(type));
    return InteractionResult.SUCCESS;
  }
}
