package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BRRPSlabBlock;
import pers.solid.brrp.v1.model.ModelJsonBuilder;

@ApiStatus.AvailableSince("1.1.0")
public class LightSlabBlock extends BRRPSlabBlock {
  public static final MapCodec<LightSlabBlock> CODEC = BRRPUtils.createCodecWithBaseBlock(createSettingsCodec(), LightSlabBlock::new);

  public LightSlabBlock(@NotNull Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public LightSlabBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return super.getBlockModel().parent(Identifier.of("mishanguc", "block/light_slab"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder model = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(id, model);
    pack.addModel(id.brrp_suffixed("_top"), model.withParent(Identifier.of("mishanguc", "block/light_slab_top")));
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ((ShapedRecipeJsonBuilder) super.getCraftingRecipe()).setCustomRecipeCategory("light");
  }

  @Override
  public boolean shouldWriteStonecuttingRecipe() {
    return true;
  }

  @Override
  public SingleItemRecipeJsonBuilder getStonecuttingRecipe() {
    return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(baseBlock), getRecipeCategory(), this, 2)
        .criterionFromItem(baseBlock)
        .setCustomRecipeCategory("lights");
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.DECORATIONS;
  }

  @Override
  public MapCodec<? extends LightSlabBlock> getCodec() {
    return CODEC;
  }
}
