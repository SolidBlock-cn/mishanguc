package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.ColoredWallSignBlockEntity;

import java.util.List;

public class ColoredGlowingWallSignBlock extends GlowingWallSignBlock implements ColoredBlock {
  public ColoredGlowingWallSignBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ColoredWallSignBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable() {
    return BlockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }
}
