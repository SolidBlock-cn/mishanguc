package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.arrp.ARRPMain;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassBlock extends TransparentBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlassBlock> CODEC = createCodec(settings1 -> new ColoredGlassBlock(settings1, new TextureMap()));
  private final TextureMap textures;

  public ColoredGlassBlock(Settings settings, TextureMap textures) {
    super(settings);
    this.textures = textures;
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(Identifier.of("mishanguc:block/colored_cube_all")).setTextures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createSingletonBlockState(this, getBlockModelId());
  }

  @Override
  public LootTable.@NotNull Builder getLootTable() {
    return ARRPMain.LOOT_TABLE_GENERATOR.dropsWithSilkTouch(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredGlassBlock> getCodec() {
    return CODEC;
  }
}
