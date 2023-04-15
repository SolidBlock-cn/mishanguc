package pers.solid.mishang.uc.block;

import com.google.common.annotations.Beta;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

@Beta
public class ColoredHungSignBarBlock extends HungSignBarBlock implements BlockEntityProvider, ColoredBlock {
  public ColoredHungSignBarBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
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

  @Override
  public LootTable.Builder getLootTable() {
    return BlockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }
}
