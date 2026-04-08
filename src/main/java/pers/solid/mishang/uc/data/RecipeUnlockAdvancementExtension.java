package pers.solid.mishang.uc.data;

import org.jetbrains.annotations.Nullable;

public interface RecipeUnlockAdvancementExtension {
  @Nullable
  default String mishanguc$getCustomRecipeCategory() {
    throw new UnsupportedOperationException("Implemented via mixin");
  }

  default void mishanguc$setCustomRecipeCategory(@Nullable String customRecipeCategory) {
    throw new UnsupportedOperationException("Implemented via mixin");
  }
}
