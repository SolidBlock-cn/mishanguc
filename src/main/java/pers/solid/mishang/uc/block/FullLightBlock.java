package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import pers.solid.mishang.uc.data.MishangucModels;

public class FullLightBlock extends Block implements MishangucBlock {
  private final Item dyeIngredient;
  private final Item concreteIngredient;

  public FullLightBlock(Properties settings, Item dyeIngredient, Item concreteIngredient) {
    super(settings);
    this.dyeIngredient = dyeIngredient;
    this.concreteIngredient = concreteIngredient;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Identifier modelId = MishangucModels.LIGHT.create(this, TextureMapping.cube(this), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId)));
    blockStateModelGenerator.registerSimpleItemModel(this, modelId);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.shaped(RecipeCategory.DECORATIONS, this, 8)
        .pattern("*#*")
        .pattern("#C#")
        .pattern("*#*")
        .define('*', dyeIngredient)
        .define('#', Items.GLOWSTONE)
        .define('C', concreteIngredient)
        .unlockedBy(RecipeProvider.getHasName(dyeIngredient), recipeGenerator.has(dyeIngredient))
        .unlockedBy(RecipeProvider.getHasName(Items.GLOWSTONE), recipeGenerator.has(Items.GLOWSTONE))
        .unlockedBy(RecipeProvider.getHasName(concreteIngredient), recipeGenerator.has(concreteIngredient));
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
