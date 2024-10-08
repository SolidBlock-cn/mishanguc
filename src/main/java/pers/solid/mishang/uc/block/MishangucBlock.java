package pers.solid.mishang.uc.block;

import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.loot.LootTable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;

public interface MishangucBlock extends BlockResourceGenerator {
  @Override
  default LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return BlockResourceGenerator.super.getLootTable(blockLootTableGenerator);
  }

  @Override
  default CraftingRecipeJsonBuilder getCraftingRecipe() {
    return BlockResourceGenerator.super.getCraftingRecipe();
  }

  @Override
  default StonecuttingRecipeJsonBuilder getStonecuttingRecipe() {
    return BlockResourceGenerator.super.getStonecuttingRecipe();
  }

  @Override
  default boolean shouldWriteStonecuttingRecipe() {
    return BlockResourceGenerator.super.shouldWriteStonecuttingRecipe();
  }
}
