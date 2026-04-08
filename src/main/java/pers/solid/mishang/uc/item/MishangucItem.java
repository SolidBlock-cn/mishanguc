package pers.solid.mishang.uc.item;

import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;

public interface MishangucItem {
  default RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return null;
  }

  default String customRecipeCategory() {
    return null;
  }
}
