package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ModelProvider;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public interface MishangucBlock {
  default LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops((ItemConvertible) this);
  }

  default CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return null;
  }

  default StonecuttingRecipeJsonBuilder getStonecuttingRecipe(RecipeGenerator recipeGenerator) {
    return null;
  }

  default RegistryKey<Recipe<?>> getStonecuttingRecipeKey() {
    return RegistryKey.of(RegistryKeys.RECIPE, CraftingRecipeJsonBuilder.getItemId((ItemConvertible) this).withSuffixedPath("_from_stonecutting"));
  }

  default boolean shouldWriteStonecuttingRecipe() {
    return false;
  }

  default void writeRecipes(RecipeGenerator recipeGenerator, RecipeExporter exporter) {
    final CraftingRecipeJsonBuilder craftingRecipe = getCraftingRecipe(recipeGenerator);
    if (craftingRecipe != null) {
      craftingRecipe.offerTo(exporter);
    }
    if (shouldWriteStonecuttingRecipe()) {
      final StonecuttingRecipeJsonBuilder stonecuttingRecipe = getStonecuttingRecipe(recipeGenerator);
      if (stonecuttingRecipe != null) {
        stonecuttingRecipe.offerTo(exporter, getStonecuttingRecipeKey());
      }
    }
  }

  void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator);


  default Identifier getTexture(TextureKey key) {
    return TextureMap.getId(((Block) this));
  }

  default String customRecipeCategory() {
    return null;
  }
}
