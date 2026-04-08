package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.MishangucBlock;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MishangucBlockLootTableProvider extends FabricBlockLootTableProvider {
  protected MishangucBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof MishangucBlock r) {
        final Optional<ResourceKey<LootTable>> lootTableKey = block.getLootTable();
        if (lootTableKey.isEmpty()) {
          continue;
        }
        final LootTable.Builder lootTable = r.getLootTable(this);
        map.put(lootTableKey.get(), lootTable);
      } else {
        throw new IllegalStateException();
      }
    }
  }
}
