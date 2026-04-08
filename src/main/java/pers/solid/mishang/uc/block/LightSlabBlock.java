package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.data.MishangucModels;

@ApiStatus.AvailableSince("1.1.0")
public class LightSlabBlock extends SlabBlock implements MishangucBlock {
  public final Block baseBlock;
  public static final MapCodec<LightSlabBlock> CODEC = RecordCodecBuilder.mapCodec(
      i -> i.group(
          BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(b -> b.baseBlock),
          propertiesCodec()
      ).apply(i, LightSlabBlock::new)
  );

  public LightSlabBlock(Block baseBlock, Properties settings) {
    super(settings);
    this.baseBlock = baseBlock;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Identifier bottomModelId = MishangucModels.LIGHT_SLAB.create(this, TextureMapping.cube(baseBlock), blockStateModelGenerator.modelOutput);
    final Identifier topModelId = MishangucModels.LIGHT_SLAB_TOP.create(this, TextureMapping.cube(baseBlock), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSlab(this, BlockModelGenerators.plainVariant(bottomModelId), BlockModelGenerators.plainVariant(topModelId), BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(baseBlock))));
    blockStateModelGenerator.registerSimpleItemModel(this, bottomModelId);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return ((ShapedRecipeBuilder) recipeGenerator.slabBuilder(RecipeCategory.BUILDING_BLOCKS, this, Ingredient.of(baseBlock)))
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock));
  }

  @Override
  public boolean shouldWriteStonecuttingRecipe() {
    return true;
  }

  @Override
  public SingleItemRecipeBuilder getStonecuttingRecipe(RecipeProvider recipeGenerator) {
    return SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.DECORATIONS, this, 2)
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSlabItemTable(this);
  }

  @Override
  public MapCodec<? extends LightSlabBlock> codec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
