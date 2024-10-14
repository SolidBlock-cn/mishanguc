package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;

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

  default void writeRecipes(RecipeExporter exporter) {
    final CraftingRecipeJsonBuilder craftingRecipe = getCraftingRecipe();
    if (craftingRecipe != null) {
      craftingRecipe.offerTo(exporter);
    }
    if (shouldWriteStonecuttingRecipe()) {
      final StonecuttingRecipeJsonBuilder stonecuttingRecipe = getStonecuttingRecipe();
      if (stonecuttingRecipe != null) {
        stonecuttingRecipe.offerTo(exporter, getStonecuttingRecipeId());
      }
    }
  }

  @Deprecated(forRemoval = true)
  @Override
  default void writeRecipes(RuntimeResourcePack pack) {
    BlockResourceGenerator.super.writeRecipes(pack);
  }

  void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator);

  @Deprecated(forRemoval = true)
  @Override
  default BlockStateSupplier getBlockStates() {
    return BlockResourceGenerator.super.getBlockStates();
  }

  @Deprecated(forRemoval = true)
  @Override
  default ModelJsonBuilder getBlockModel() {
    return BlockResourceGenerator.super.getBlockModel();
  }

  @Deprecated(forRemoval = true)
  @Override
  default ModelJsonBuilder getItemModel() {
    return BlockResourceGenerator.super.getItemModel();
  }

  @Deprecated(forRemoval = true)
  @Override
  default Identifier getBlockModelId() {
    return BlockResourceGenerator.super.getBlockModelId();
  }

  @Deprecated(forRemoval = true)
  @Override
  default Identifier getItemModelId() {
    return BlockResourceGenerator.super.getItemModelId();
  }

  @Deprecated(forRemoval = true)
  @Override
  default void writeBlockModel(RuntimeResourcePack pack) {
    BlockResourceGenerator.super.writeBlockModel(pack);
  }

  @Deprecated(forRemoval = true)
  @Override
  default void writeItemModel(RuntimeResourcePack pack) {
    BlockResourceGenerator.super.writeItemModel(pack);
  }

  default Identifier getTexture(TextureKey key) {
    return TextureMap.getId(((Block) this));
  }
}
