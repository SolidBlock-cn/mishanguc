package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MishangucDataGeneration implements DataGeneratorEntrypoint {
  @Override
  public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
    final FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
    pack.addProvider(MishangucBlockLootTableProvider::new);
    pack.addProvider(MishangucRecipeProvider::new);
    pack.addProvider(MishangucModelProvider::new);
    final MishangucTagProvider mishangucTagProvider = pack.addProvider(MishangucTagProvider::new);
    pack.addProvider((output, registriesFuture) -> mishangucTagProvider.affiliate);
  }
}
