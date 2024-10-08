package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryWrapper;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.mishang.uc.MishangUtils;

import java.util.concurrent.CompletableFuture;

public class MishangucBlockLootTableProvider extends FabricBlockLootTableProvider {
  protected MishangucBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof BlockResourceGenerator r) {
        final LootTable.Builder lootTable = r.getLootTable(this);
        lootTables.put(block.getLootTableKey(), lootTable);
      }
    }
  }
}
