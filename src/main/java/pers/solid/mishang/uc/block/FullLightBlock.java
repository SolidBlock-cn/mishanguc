package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import pers.solid.brrp.v1.generator.BRRPCubeBlock;

public class FullLightBlock extends BRRPCubeBlock {
  public static final MapCodec<FullLightBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      createSettingsCodec(),
      Registries.ITEM.getCodec().fieldOf("dye_ingredient").forGetter(b -> b.dyeIngredient),
      Registries.ITEM.getCodec().fieldOf("concrete_ingredient").forGetter(b -> b.concreteIngredient)
  ).apply(i, (settings1, item1, item2) -> new FullLightBlock(settings1, new TextureMap(), item1, item2)));
  private static final Identifier MODEL_PARENT_ID = Identifier.of("mishanguc", "block/light");
  private final Item dyeIngredient;
  private final Item concreteIngredient;

  public FullLightBlock(Settings settings, Identifier allTexture, Item dyeIngredient, Item concreteIngredient) {
    this(settings, TextureMap.all(allTexture), dyeIngredient, concreteIngredient);
  }

  public FullLightBlock(Settings settings, TextureMap textures, Item dyeIngredient, Item concreteIngredient) {
    super(settings, MODEL_PARENT_ID, textures);
    this.dyeIngredient = dyeIngredient;
    this.concreteIngredient = concreteIngredient;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 8)
        .pattern("*#*")
        .pattern("#C#")
        .pattern("*#*")
        .input('*', dyeIngredient)
        .input('#', Items.GLOWSTONE)
        .input('C', concreteIngredient)
        .criterionFromItem(dyeIngredient)
        .criterionFromItem(Items.GLOWSTONE)
        .criterionFromItem(concreteIngredient)
        .setCustomRecipeCategory("light");
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.DECORATIONS;
  }

  @Override
  protected MapCodec<? extends FullLightBlock> getCodec() {
    return CODEC;
  }
}
