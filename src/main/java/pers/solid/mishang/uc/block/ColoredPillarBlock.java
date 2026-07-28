package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.ModelProvider;
import net.minecraft.client.data.TextureMap;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.util.TextureMapReference;

import java.util.List;

public class ColoredPillarBlock extends PillarBlock implements ColoredBlock {
  public static final MapCodec<ColoredPillarBlock> CODEC = createCodec(settings1 -> new ColoredPillarBlock(settings1, TextureMapReference.EMPTY));
  private final TextureMapReference textures;

  public ColoredPillarBlock(Settings settings, @Nullable TextureMapReference textures) {
    super(settings);
    this.textures = textures;
  }

  @Override
  protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getPickStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textureMap = textures.getTextureMap();
    final Identifier modelId = MishangucModels.COLORED_CUBE_COLUMN.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    final Identifier horizontalModelId = MishangucModels.COLORED_CUBE_COLUMN_HORITONZAL.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createAxisRotatedBlockState(this, BlockStateModelGenerator.createWeightedVariant(modelId), BlockStateModelGenerator.createWeightedVariant(horizontalModelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.tinted(modelId, ColoredTintSource.INSTANCE));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredPillarBlock> getCodec() {
    return CODEC;
  }
}
