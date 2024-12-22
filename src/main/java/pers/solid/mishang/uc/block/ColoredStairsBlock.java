package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.ModelProvider;
import net.minecraft.client.data.TextureMap;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;

public class ColoredStairsBlock extends StairsBlock implements ColoredBlock {
  public static final MapCodec<ColoredStairsBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(o -> o.baseBlock),
      createSettingsCodec()
  ).apply(i, ColoredStairsBlock::new));
  public final @NotNull Block baseBlock;

  public ColoredStairsBlock(@NotNull Block baseBlock, Settings settings) {
    super(baseBlock.getDefaultState(), settings);
    this.baseBlock = baseBlock;
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textureMap = baseBlock instanceof ColoredCubeBlock coloredCubeBlock ? coloredCubeBlock.textures.getTextureMap() : TextureMap.all(this);
    final Identifier regularModelId = MishangucModels.COLORED_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    final Identifier innerModelId = MishangucModels.COLORED_INNER_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    final Identifier outerModelId = MishangucModels.COLORED_OUTER_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(this, innerModelId, regularModelId, outerModelId));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.tinted(regularModelId, ColoredTintSource.INSTANCE));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return ((ShapedRecipeJsonBuilder) recipeGenerator.createStairsRecipe(this, Ingredient.ofItems(baseBlock)))
        .criterion(RecipeGenerator.hasItem(baseBlock), recipeGenerator.conditionsFromItem(baseBlock));
  }

  @Override
  public MapCodec<? extends ColoredStairsBlock> getCodec() {
    return CODEC;
  }
}
