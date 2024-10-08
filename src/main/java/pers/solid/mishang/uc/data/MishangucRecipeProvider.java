package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.registry.RegistryWrapper;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.mishang.uc.MishangUtils;

import java.util.concurrent.CompletableFuture;

public class MishangucRecipeProvider extends FabricRecipeProvider {
  public MishangucRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  public void generate(RecipeExporter exporter) {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof BlockResourceGenerator r) {
        final CraftingRecipeJsonBuilder craftingRecipe = r.getCraftingRecipe();
        if (craftingRecipe != null) {
          craftingRecipe.offerTo(exporter);
        }
        if (r.shouldWriteStonecuttingRecipe()) {
          final StonecuttingRecipeJsonBuilder stonecuttingRecipe = r.getStonecuttingRecipe();
          if (stonecuttingRecipe != null) {
            stonecuttingRecipe.offerTo(exporter, r.getStonecuttingRecipeId());
          }
        }
      }
    }
  }
}
