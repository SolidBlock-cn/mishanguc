package pers.solid.mishang.uc.item;

import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;

public interface MishangucItem {
  default CraftingRecipeJsonBuilder getCraftingRecipe() {
    return null;
  }

  default String customRecipeCategory() {
    return null;
  }
}
