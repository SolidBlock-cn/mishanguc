package pers.solid.mishang.uc.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class TextPadBlockItem extends BlockItem {
  public TextPadBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  protected boolean postPlacement(
      BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
    return super.postPlacement(pos, world, player, stack, state);
  }
}
