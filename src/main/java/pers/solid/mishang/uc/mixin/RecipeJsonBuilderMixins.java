package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.data.recipe.StonecuttingRecipeJsonBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.data.MishangucRecipeGenerator;

/**
 * 此 mixin 同时修改多个类的自定义 recipeCategory。需确保 mixin 对每个类有效。
 */
@Mixin({ShapedRecipeJsonBuilder.class, ShapelessRecipeJsonBuilder.class, StonecuttingRecipeJsonBuilder.class})
public abstract class RecipeJsonBuilderMixins {

  @ModifyExpressionValue(method = "offerTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/recipe/book/RecipeCategory;getName()Ljava/lang/String;"))
  private String redirectGetCategoryName(String original) {
    final String customRecipeCategory = MishangucRecipeGenerator.getCustomRecipeCategory(((CraftingRecipeJsonBuilder) this).getOutputItem());
    return customRecipeCategory != null ? customRecipeCategory : original;
  }
}
