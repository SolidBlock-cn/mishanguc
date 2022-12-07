package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.loottable.VanillaBlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredPillarBlock extends PillarBlock implements ColoredBlock, BlockResourceGenerator {
  private final JTextures textures;

  public ColoredPillarBlock(Settings settings, JTextures textures) {
    super(settings);
    this.textures = textures;
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
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel("mishanguc:block/colored_cube_column").textures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    ColoredBlock.super.writeBlockModel(pack);
    pack.addModel(getBlockModel().parent("mishanguc:block/colored_cube_column_horizontal"), getBlockModelId().brrp_append("_horizontal"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JBlockStates getBlockStates() {
    return JBlockStates.delegate(BlockStateModelGenerator.createAxisRotatedBlockState(this, getBlockModelId(), getBlockModelId().brrp_append("_horizontal")));
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
  }
}
