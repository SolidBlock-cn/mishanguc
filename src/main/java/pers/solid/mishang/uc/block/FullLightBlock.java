package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.data.MishangucModels;

public class FullLightBlock extends Block implements MishangucBlock {
  private final Item dyeIngredient;
  private final Item concreteIngredient;

  public FullLightBlock(Settings settings, Item dyeIngredient, Item concreteIngredient) {
    super(settings);
    this.dyeIngredient = dyeIngredient;
    this.concreteIngredient = concreteIngredient;
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier modelId = MishangucModels.LIGHT.upload(this, TextureMap.all(this), blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, modelId));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 8)
        .pattern("*#*")
        .pattern("#C#")
        .pattern("*#*")
        .input('*', dyeIngredient)
        .input('#', Items.GLOWSTONE)
        .input('C', concreteIngredient)
        .criterion(RecipeProvider.hasItem(dyeIngredient), RecipeProvider.conditionsFromItem(dyeIngredient))
        .criterion(RecipeProvider.hasItem(Items.GLOWSTONE), RecipeProvider.conditionsFromItem(Items.GLOWSTONE))
        .criterion(RecipeProvider.hasItem(concreteIngredient), RecipeProvider.conditionsFromItem(concreteIngredient));
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
