package pers.solid.mishang.uc.item;

import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;

public interface MishangucItem {
  default CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return null;
  }

  default String customRecipeCategory() {
    return null;
  }
}
