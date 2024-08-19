package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blockentity.ColoredHungSignBlockEntity;

import java.util.List;

public class ColoredGlowingHungSignBlock extends GlowingHungSignBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlowingHungSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec()).apply(instance, ColoredGlowingHungSignBlock::new));

  public ColoredGlowingHungSignBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Override
  public @NotNull BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ColoredHungSignBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredGlowingHungSignBlock> getCodec() {
    return CODEC;
  }
}
