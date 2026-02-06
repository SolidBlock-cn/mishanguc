package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryWrapper;
import org.jspecify.annotations.NonNull;

public class MishangucDataGeneration implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(MishangucBlockLootTableProvider::new);
    pack.addProvider((fabricDataOutput, completableFuture) -> new FabricRecipeProvider(fabricDataOutput, completableFuture) {
      @Override
      protected @NonNull RecipeGenerator getRecipeGenerator(RegistryWrapper.@NonNull WrapperLookup wrapperLookup, @NonNull RecipeExporter recipeExporter) {
        return new MishangucRecipeGenerator(wrapperLookup, recipeExporter);
      }

      @Override
      public String getName() {
        return "";
      }
    });
    pack.addProvider(MishangucModelProvider::new);
    final MishangucBlockTagProvider mishangucBlockTagProvider = pack.addProvider(MishangucBlockTagProvider::new);
    pack.addProvider((output, registriesFuture) -> mishangucBlockTagProvider.affiliate);
  }
}
