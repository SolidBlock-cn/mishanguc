package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.blocks.ColoredBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.util.ModelReference;
import pers.solid.mishang.uc.util.TextureMapReference;

import java.util.List;

public class ColoredCubeBlock extends Block implements ColoredBlock {
  protected final ModelReference model;
  protected final TextureMapReference textures;
  public static final MapCodec<ColoredCubeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createSettingsCodec()).apply(instance, (settings1) -> new ColoredCubeBlock(settings1, null, TextureMapReference.EMPTY)));

  @ApiStatus.Internal
  public ColoredCubeBlock(Settings settings, ModelReference model, TextureMapReference textures) {
    super(settings);
    this.model = model;
    this.textures = textures;
  }

  public static ColoredCubeBlock cubeAll(Settings settings, String allTexture) {
    return new ColoredCubeBlock(settings, ModelReference.COLORED_CUBE_ALL, TextureMapReference.all(Identifier.of(allTexture)));
  }

  public static ColoredCubeBlock cubeBottomTop(Settings settings, String topTexture, String sideTexture, String bottomTexture) {
    return new ColoredCubeBlock(settings, ModelReference.COLORED_CUBE_BOTTOM_TOP, TextureMapReference.topSideBottom(Identifier.of(topTexture), Identifier.of(sideTexture), Identifier.of(bottomTexture)));
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
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

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    if (this == ColoredBlocks.COLORED_PACKED_ICE) {
      return blockLootTableGenerator.dropsWithSilkTouch(this).apply(COPY_COLOR_LOOT_FUNCTION);
    } else if (this == ColoredBlocks.COLORED_STONE) {
      return blockLootTableGenerator.drops(this, ColoredBlocks.COLORED_COBBLESTONE).apply(COPY_COLOR_LOOT_FUNCTION);
    }
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textureMap = textures.getTextureMap();
    if (this == ColoredBlocks.COLORED_STONE) {
      final Identifier modelId = MishangucModels.COLORED_CUBE_ALL.upload(this, textureMap, blockStateModelGenerator.modelCollector);
      final Identifier mirroredModelId = MishangucModels.COLORED_CUBE_MIRRORED_ALL.upload(this, textureMap, blockStateModelGenerator.modelCollector);

      blockStateModelGenerator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(this, BlockStateModelGenerator.modelWithMirroring(new ModelVariant(modelId), new ModelVariant(mirroredModelId))));
      return;
    }
    final Identifier modelId = model.getModel().upload(this, textureMap, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, BlockStateModelGenerator.createWeightedVariant(modelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.tinted(modelId, ColoredTintSource.INSTANCE));
  }

  @Override
  protected MapCodec<? extends ColoredCubeBlock> getCodec() {
    return CODEC;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier getTexture(TextureKey key) {
    return textures.getTextureMap().getTexture(key);
  }
}
