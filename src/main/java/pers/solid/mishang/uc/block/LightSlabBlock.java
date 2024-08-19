package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelIds;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.data.MishangucModels;

@ApiStatus.AvailableSince("1.1.0")
public class LightSlabBlock extends SlabBlock implements MishangucBlock {
  public final Block baseBlock;

  public LightSlabBlock(@NotNull Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
  }

  public LightSlabBlock(@NotNull Block baseBlock) {
    super(Settings.copy(baseBlock));
    this.baseBlock = baseBlock;
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier bottomModelId = MishangucModels.LIGHT_SLAB.upload(this, TextureMap.all(baseBlock), blockStateModelGenerator.modelCollector);
    final Identifier topModelId = MishangucModels.LIGHT_SLAB_TOP.upload(this, TextureMap.all(baseBlock), blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(this, bottomModelId, topModelId, ModelIds.getBlockModelId(baseBlock)));
    blockStateModelGenerator.registerParentedItemModel(this, bottomModelId);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ((ShapedRecipeJsonBuilder) RecipeProvider.createSlabRecipe(RecipeCategory.BUILDING_BLOCKS, this, Ingredient.ofItems(baseBlock)))
        .criterion(RecipeProvider.hasItem(baseBlock), RecipeProvider.conditionsFromItem(baseBlock));
  }

  @Override
  public boolean shouldWriteStonecuttingRecipe() {
    return true;
  }

  @Override
  public SingleItemRecipeJsonBuilder getStonecuttingRecipe() {
    return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(baseBlock), RecipeCategory.DECORATIONS, this, 2)
        .criterion(RecipeProvider.hasItem(baseBlock), RecipeProvider.conditionsFromItem(baseBlock));
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
