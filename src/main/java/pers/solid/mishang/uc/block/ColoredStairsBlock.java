package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;

public class ColoredStairsBlock extends StairBlock implements ColoredBlock {
  public static final MapCodec<ColoredStairsBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(o -> o.baseBlock),
      propertiesCodec()
  ).apply(i, ColoredStairsBlock::new));
  public final Block baseBlock;

  public ColoredStairsBlock(Block baseBlock, Properties settings) {
    super(baseBlock.defaultBlockState(), settings);
    this.baseBlock = baseBlock;
  }

  @Override
  public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textureMap = baseBlock instanceof ColoredCubeBlock coloredCubeBlock ? coloredCubeBlock.textures.getTextureMap() : TextureMapping.cube(this);
    final Identifier regularModelId = MishangucModels.COLORED_STAIRS.create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier innerModelId = MishangucModels.COLORED_INNER_STAIRS.create(this, textureMap, blockStateModelGenerator.modelOutput);
    final Identifier outerModelId = MishangucModels.COLORED_OUTER_STAIRS.create(this, textureMap, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createStairs(this, BlockModelGenerators.plainVariant(innerModelId), BlockModelGenerators.plainVariant(regularModelId), BlockModelGenerators.plainVariant(outerModelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(regularModelId, ColoredTintSource.INSTANCE));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return ((ShapedRecipeBuilder) recipeGenerator.stairBuilder(this, Ingredient.of(baseBlock)))
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock));
  }

  @Override
  public MapCodec<? extends ColoredStairsBlock> codec() {
    return CODEC;
  }
}
