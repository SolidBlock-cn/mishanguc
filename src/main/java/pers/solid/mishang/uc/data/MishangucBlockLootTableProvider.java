package pers.solid.mishang.uc.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryWrapper;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.MishangucBlock;

import java.util.concurrent.CompletableFuture;

public class MishangucBlockLootTableProvider extends FabricBlockLootTableProvider {
  protected MishangucBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
    super(dataOutput, registryLookup);
  }

  @Override
  public void generate() {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof MishangucBlock r) {
        if (r instanceof HandrailBlock handrailBlock) {
          for (Block i : handrailBlock.selfAndVariants()) {
            final LootTable.Builder lootTable = ((MishangucBlock) i).getLootTable(this);
            lootTables.put(i.getLootTableKey(), lootTable);
          }
        } else {
          final LootTable.Builder lootTable = r.getLootTable(this);
          lootTables.put(block.getLootTableKey(), lootTable);
        }
      } else {
        throw new IllegalStateException();
      }
    }
  }
}
