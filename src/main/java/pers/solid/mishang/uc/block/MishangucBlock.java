package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;

public interface MishangucBlock {
  default LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops((ItemConvertible) this);
  }

  default CraftingRecipeJsonBuilder getCraftingRecipe() {
    return null;
  }

  default StonecuttingRecipeJsonBuilder getStonecuttingRecipe() {
    return null;
  }

  default Identifier getStonecuttingRecipeId() {
    return CraftingRecipeJsonBuilder.getItemId((ItemConvertible) this).withSuffixedPath("_from_stonecutting");
  }

  default boolean shouldWriteStonecuttingRecipe() {
    return false;
  }

  default void writeRecipes(RecipeExporter exporter) {
    final CraftingRecipeJsonBuilder craftingRecipe = getCraftingRecipe();
    if (craftingRecipe != null) {
      craftingRecipe.offerTo(exporter);
    }
    if (shouldWriteStonecuttingRecipe()) {
      final StonecuttingRecipeJsonBuilder stonecuttingRecipe = getStonecuttingRecipe();
      if (stonecuttingRecipe != null) {
        stonecuttingRecipe.offerTo(exporter, getStonecuttingRecipeId());
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
