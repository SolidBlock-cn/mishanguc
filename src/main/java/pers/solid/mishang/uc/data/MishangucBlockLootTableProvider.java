package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.MishangucBlock;

import java.util.concurrent.CompletableFuture;

public class MishangucBlockLootTableProvider extends FabricBlockLootSubProvider {
  protected MishangucBlockLootTableProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof MishangucBlock r) {
        final LootTable.Builder lootTable = r.getLootTable(this);
        if (lootTable == null) {
          continue;
        }
        add(block, lootTable);
      } else {
        throw new IllegalStateException();
      }
    }
  }
}
