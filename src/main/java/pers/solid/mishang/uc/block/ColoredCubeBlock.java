package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
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
  public static final MapCodec<ColoredCubeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(propertiesCodec()).apply(instance, (settings1) -> new ColoredCubeBlock(settings1, null, TextureMapReference.EMPTY)));

  @ApiStatus.Internal
  public ColoredCubeBlock(Properties settings, ModelReference model, TextureMapReference textures) {
    super(settings);
    this.model = model;
    this.textures = textures;
  }

  public static ColoredCubeBlock cubeAll(Properties settings, String allTexture) {
    return new ColoredCubeBlock(settings, ModelReference.COLORED_CUBE_ALL, TextureMapReference.all(Identifier.parse(allTexture)));
  }

  public static ColoredCubeBlock cubeBottomTop(Properties settings, String topTexture, String sideTexture, String bottomTexture) {
    return new ColoredCubeBlock(settings, ModelReference.COLORED_CUBE_BOTTOM_TOP, TextureMapReference.topSideBottom(Identifier.parse(topTexture), Identifier.parse(sideTexture), Identifier.parse(bottomTexture)));
  }

  @Override
  public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    if (this == ColoredBlocks.COLORED_PACKED_ICE) {
      return blockLootTableGenerator.createSilkTouchOnlyTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
    } else if (this == ColoredBlocks.COLORED_STONE) {
      return blockLootTableGenerator.createSingleItemTableWithSilkTouch(this, ColoredBlocks.COLORED_COBBLESTONE).apply(COPY_COLOR_LOOT_FUNCTION);
    }
    return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textureMap = textures.getTextureMap();
    if (this == ColoredBlocks.COLORED_STONE) {
      final Identifier modelId = MishangucModels.COLORED_CUBE_ALL.create(this, textureMap, blockStateModelGenerator.modelOutput);
      final Identifier mirroredModelId = MishangucModels.COLORED_CUBE_MIRRORED_ALL.create(this, textureMap, blockStateModelGenerator.modelOutput);

      blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this, BlockModelGenerators.createRotatedVariants(new Variant(modelId), new Variant(mirroredModelId))));
      return;
    }
    final Identifier modelId = model.getModel().create(this, textureMap, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(modelId, ColoredTintSource.INSTANCE));
  }

  @Override
  protected MapCodec<? extends ColoredCubeBlock> codec() {
    return CODEC;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public Identifier getTexture(TextureSlot key) {
    return textures.getTextureMap().get(key);
  }
}
