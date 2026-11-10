package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.crafting.Recipe;

public class MishangucDataGeneration implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(MishangucBlockLootTableProvider::new);
    pack.addProvider((fabricDataOutput, completableFuture) -> new FabricRecipeProvider(fabricDataOutput, completableFuture) {

      @Override
      protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new MishangucRecipeGenerator(recipes, advancements);
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
