package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

public class MishangucDataGeneration implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(MishangucBlockLootTableProvider::new);
    pack.addProvider((fabricDataOutput, completableFuture) -> new FabricRecipeProvider(fabricDataOutput, completableFuture) {
      @Override
      protected RecipeProvider createRecipeProvider(HolderLookup.Provider wrapperLookup, RecipeOutput recipeExporter) {
        return new MishangucRecipeGenerator(wrapperLookup, recipeExporter);
      }

      @Override
      public String getName() {
        return "Recipes";
      }
    });
    pack.addProvider(MishangucModelProvider::new);
    final MishangucBlockTagProvider mishangucBlockTagProvider = pack.addProvider(MishangucBlockTagProvider::new);
    pack.addProvider((output, registriesFuture) -> mishangucBlockTagProvider.affiliate);
  }
}
