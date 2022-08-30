package pers.solid.mishang.uc.block;

import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassBlock extends AbstractGlassBlock implements ColoredBlock, BlockResourceGenerator {
  private final JTextures textures;

  public ColoredGlassBlock(Settings settings, JTextures textures) {
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

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel("mishanguc:block/colored_cube_all").textures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    return JBlockStates.simple(getBlockModelId());
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(BlockLootTableGenerator.dropsWithSilkTouch(this).apply(COPY_COLOR_LOOT_FUNCTION).build());
  }
}
