package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BRRPSlabBlock;
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

public class ColoredSlabBlock extends BRRPSlabBlock implements ColoredBlock {
  public ColoredSlabBlock(@Nullable Block baseBlock, Settings settings) {
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
  public BlockEntity createBlockEntity(BlockView world) {
    return new SimpleColoredBlockEntity();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return super.getBlockModel().parent(new Identifier("mishanguc", "block/colored_slab"));
  }


  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel model = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(model, id);
    pack.addModel(model.clone().parent(new Identifier("mishanguc", "block/colored_slab_top")), id.brrp_append("_top"));
    if (baseBlock == null) {
      pack.addModel(model.clone().parent(new Identifier("mishanguc", "block/colored_cube_bottom_up")), id.brrp_append("_double"));
    }
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(BlockLootTableGenerator.slabDrops(this).apply(COPY_COLOR_LOOT_FUNCTION).build());
  }
}
