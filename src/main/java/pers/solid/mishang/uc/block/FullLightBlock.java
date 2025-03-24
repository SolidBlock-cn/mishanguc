package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ModelProvider;
import net.minecraft.client.data.TextureMap;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.data.MishangucModels;

public class FullLightBlock extends Block implements MishangucBlock {
  public static final MapCodec<FullLightBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      createSettingsCodec(),
      Registries.ITEM.getCodec().fieldOf("dye_ingredient").forGetter(b -> b.dyeIngredient),
      Registries.ITEM.getCodec().fieldOf("concrete_ingredient").forGetter(b -> b.concreteIngredient)
  ).apply(i, FullLightBlock::new));
  private final Item dyeIngredient;
  private final Item concreteIngredient;

  public FullLightBlock(Settings settings, Item dyeIngredient, Item concreteIngredient) {
    super(settings);
    this.dyeIngredient = dyeIngredient;
    this.concreteIngredient = concreteIngredient;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier modelId = MishangucModels.LIGHT.upload(this, TextureMap.all(this), blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, BlockStateModelGenerator.createWeightedVariant(modelId)));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return recipeGenerator.createShaped(RecipeCategory.DECORATIONS, this, 8)
        .pattern("*#*")
        .pattern("#C#")
        .pattern("*#*")
        .input('*', dyeIngredient)
        .input('#', Items.GLOWSTONE)
        .input('C', concreteIngredient)
        .criterion(RecipeGenerator.hasItem(dyeIngredient), recipeGenerator.conditionsFromItem(dyeIngredient))
        .criterion(RecipeGenerator.hasItem(Items.GLOWSTONE), recipeGenerator.conditionsFromItem(Items.GLOWSTONE))
        .criterion(RecipeGenerator.hasItem(concreteIngredient), recipeGenerator.conditionsFromItem(concreteIngredient));
  }

  @Override
  protected MapCodec<? extends FullLightBlock> getCodec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "light";
  }
}
