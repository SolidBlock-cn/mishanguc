package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.ColoredWallSignBlockEntity;

import java.util.List;

public class ColoredGlowingWallSignBlock extends GlowingWallSignBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlowingWallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, ColoredGlowingWallSignBlock::new));

  public ColoredGlowingWallSignBlock(@NotNull Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getPickStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ColoredWallSignBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredGlowingWallSignBlock> getCodec() {
    return CODEC;
  }
}
