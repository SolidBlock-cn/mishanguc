package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BRRPStairsBlock;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredStairsBlock extends BRRPStairsBlock implements ColoredBlock {
  public ColoredStairsBlock(Block baseBlock, Settings settings) {
    super(baseBlock, settings);
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

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return super.getBlockModel().parent(new Identifier("mishanguc", "block/colored_stairs"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel blockModel = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(blockModel, id);
    pack.addModel(blockModel.parent(new Identifier("mishanguc", "block/colored_inner_stairs")), id.brrp_append("_inner"));
    pack.addModel(blockModel.parent(new Identifier("mishanguc", "block/colored_outer_stairs")), id.brrp_append("_outer"));
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(BlockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION).build());
  }
}
