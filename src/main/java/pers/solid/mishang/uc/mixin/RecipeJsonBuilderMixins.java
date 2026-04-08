package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.data.MishangucRecipeGenerator;

/**
 * 此 mixin 同时修改多个类的自定义 recipeCategory。需确保 mixin 对每个类有效。
 */
@Mixin({ShapedRecipeBuilder.class, ShapelessRecipeBuilder.class, SingleItemRecipeBuilder.class})
public abstract class RecipeJsonBuilderMixins {

  @ModifyExpressionValue(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/recipes/RecipeCategory;getFolderName()Ljava/lang/String;"))
  private String redirectGetCategoryName(String original) {
    final String customRecipeCategory = MishangucRecipeGenerator.getCustomRecipeCategory(((RecipeBuilder) this).getResult());
    return customRecipeCategory != null ? customRecipeCategory : original;
  }
}
