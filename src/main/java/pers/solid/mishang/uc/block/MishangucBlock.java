package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;

public interface MishangucBlock {
  default LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSingleItemTable((ItemLike) this);
  }

  default RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return null;
  }

  default SingleItemRecipeBuilder getStonecuttingRecipe(RecipeProvider recipeGenerator) {
    return null;
  }

  default ResourceKey<Recipe<?>> getStonecuttingRecipeKey() {
    return ResourceKey.create(Registries.RECIPE, RecipeBuilder.getDefaultRecipeId((ItemLike) this).withSuffix("_from_stonecutting"));
  }

  default boolean shouldWriteStonecuttingRecipe() {
    return false;
  }

  default void writeRecipes(RecipeProvider recipeGenerator, RecipeOutput exporter) {
    final RecipeBuilder craftingRecipe = getCraftingRecipe(recipeGenerator);
    if (craftingRecipe != null) {
      craftingRecipe.save(exporter);
    }
    if (shouldWriteStonecuttingRecipe()) {
      final SingleItemRecipeBuilder stonecuttingRecipe = getStonecuttingRecipe(recipeGenerator);
      if (stonecuttingRecipe != null) {
        stonecuttingRecipe.save(exporter, getStonecuttingRecipeKey());
      }
    }
  }

  @Environment(EnvType.CLIENT)
  void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator);

  @Environment(EnvType.CLIENT)
  default Identifier getTexture(TextureSlot key) {
    return TextureMapping.getBlockTexture(((Block) this));
  }

  default String customRecipeCategory() {
    return null;
  }
}
