package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

import java.util.List;

public class ColoredHungSignBlock extends HungSignBlock {
  public ColoredHungSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    final ItemStack stack = super.getPickStack(world, pos, state);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      stack.getOrCreateSubNbt("BlockEntityTag").putInt("color", coloredBlockEntity.getColor());
    }
    return stack;
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlockEntity.appendColorTooltip(stack, tooltip);
  }
}
