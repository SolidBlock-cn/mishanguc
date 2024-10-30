package pers.solid.mishang.uc.item;

import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeGenerator;

public interface MishangucItem {
  default CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return null;
  }

  default String customRecipeCategory() {
    return null;
  }
}
