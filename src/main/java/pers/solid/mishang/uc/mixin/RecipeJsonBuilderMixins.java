package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.data.MishangucRecipeGenerator;
import pers.solid.mishang.uc.data.RecipeUnlockAdvancementExtension;

/**
 * 此 mixin 同时修改多个类的自定义 recipeCategory。需确保 mixin 对每个类有效。
 */
@Mixin(RecipeUnlockAdvancementBuilder.class)
public abstract class RecipeJsonBuilderMixins implements RecipeUnlockAdvancementExtension {
  @Unique
  private @Nullable String customRecipeCategory;

  @Override
  public @Nullable String mishanguc$getCustomRecipeCategory() {
    return this.customRecipeCategory;
  }

  @Override
  public void mishanguc$setCustomRecipeCategory(@Nullable String customRecipeCategory) {
    this.customRecipeCategory = customRecipeCategory;
  }

  @Mixin(ShapedRecipeBuilder.class)
  private abstract static class Shaped {
    @Shadow
    @Final
    private ItemStackTemplate result;

    @Shadow
    @Final
    private RecipeUnlockAdvancementBuilder advancementBuilder;

    @Inject(method = "save", at = @At("HEAD"))
    private void recordCustomRecipeCategory(RecipeOutput output, ResourceKey<Recipe<?>> id, CallbackInfo ci) {
      final Item result = this.result.item().value();
      final String customRecipeCategory = MishangucRecipeGenerator.getCustomRecipeCategory(result);
      ((RecipeUnlockAdvancementExtension) advancementBuilder).mishanguc$setCustomRecipeCategory(customRecipeCategory);
    }
  }

  @Mixin(ShapelessRecipeBuilder.class)
  private abstract static class Shapeless {
    @Shadow
    @Final
    private ItemStackTemplate result;

    @Shadow
    @Final
    private RecipeUnlockAdvancementBuilder advancementBuilder;

    @Inject(method = "save", at = @At("HEAD"))
    private void recordCustomRecipeCategory(RecipeOutput output, ResourceKey<Recipe<?>> id, CallbackInfo ci) {
      final Item result = this.result.item().value();
      final String customRecipeCategory = MishangucRecipeGenerator.getCustomRecipeCategory(result);
      ((RecipeUnlockAdvancementExtension) advancementBuilder).mishanguc$setCustomRecipeCategory(customRecipeCategory);
    }
  }

  @Mixin(SingleItemRecipeBuilder.class)
  private abstract static class SingleItem {
    @Shadow
    @Final
    private ItemStackTemplate result;

    @Shadow
    @Final
    private RecipeUnlockAdvancementBuilder advancementBuilder;

    @Inject(method = "save", at = @At("HEAD"))
    private void recordCustomRecipeCategory(RecipeOutput output, ResourceKey<Recipe<?>> id, CallbackInfo ci) {
      final Item result = this.result.item().value();
      final String customRecipeCategory = MishangucRecipeGenerator.getCustomRecipeCategory(result);
      ((RecipeUnlockAdvancementExtension) advancementBuilder).mishanguc$setCustomRecipeCategory(customRecipeCategory);
    }
  }

  @ModifyExpressionValue(method = "build", at = @At(value = "INVOKE", target = "Lnet/minecraft/data/recipes/RecipeCategory;getFolderName()Ljava/lang/String;"))
  private String addCustomRecipeCategory(String original) {
    final String customRecipeCategory = mishanguc$getCustomRecipeCategory();
    return customRecipeCategory != null ? customRecipeCategory : original;
  }
}
