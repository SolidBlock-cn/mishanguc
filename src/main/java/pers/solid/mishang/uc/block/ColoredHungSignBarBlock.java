package pers.solid.mishang.uc.block;

import com.google.common.annotations.Beta;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.impl.BRRPBlockLootTableGenerator;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

@Beta
public class ColoredHungSignBarBlock extends HungSignBarBlock implements BlockEntityProvider, ColoredBlock {
  public static final MapCodec<ColoredHungSignBarBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec()).apply(instance, ColoredHungSignBarBlock::new));

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
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Override
  public LootTable.Builder getLootTable() {
    return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredHungSignBarBlock> getCodec() {
    return CODEC;
  }
}
