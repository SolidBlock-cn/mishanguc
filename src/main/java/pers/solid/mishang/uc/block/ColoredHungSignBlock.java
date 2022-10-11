package pers.solid.mishang.uc.block;

import com.google.common.annotations.Beta;
import net.devtech.arrp.json.loot.JLootTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.Tag;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.ColoredHungSignBlockEntity;

import java.util.List;

@Beta
public class ColoredHungSignBlock extends HungSignBlock implements ColoredBlock {
  public ColoredHungSignBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  public ColoredHungSignBlock(@NotNull Block baseBlock, Tag<Item> tag, int level) {
    super(baseBlock, tag, level);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Override
  public @NotNull BlockEntity createBlockEntity(BlockView world) {
    return new ColoredHungSignBlockEntity();
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(BlockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION).build());
  }
}
