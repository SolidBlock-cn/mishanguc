package pers.solid.mishang.uc.block;

import com.google.common.annotations.Beta;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.impl.BRRPBlockLootTableGenerator;
import pers.solid.mishang.uc.blockentity.ColoredHungSignBlockEntity;

import java.util.List;

@Beta
public class ColoredHungSignBlock extends HungSignBlock implements ColoredBlock {
  public static final MapCodec<ColoredHungSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec()).apply(instance, ColoredHungSignBlock::new));

  public ColoredHungSignBlock(@NotNull Block baseBlock) {
    super(baseBlock);
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
  public @NotNull BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new ColoredHungSignBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable() {
    return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }
}
