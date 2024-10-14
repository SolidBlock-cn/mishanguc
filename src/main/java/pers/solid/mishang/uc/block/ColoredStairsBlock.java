package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public class ColoredStairsBlock extends StairsBlock implements ColoredBlock {
  public static final MapCodec<ColoredStairsBlock> CODEC = BRRPUtils.createCodecWithBaseBlock(createSettingsCodec(), ColoredStairsBlock::new);
  public final @NotNull Block baseBlock;

  public ColoredStairsBlock(@NotNull Block baseBlock, Settings settings) {
    super(baseBlock.getDefaultState(), settings);
    this.baseBlock = baseBlock;
  }

  public ColoredStairsBlock(@NotNull Block baseBlock) {
    this(baseBlock, Settings.copy(baseBlock));
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
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

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textureMap = baseBlock instanceof ColoredCubeBlock coloredCubeBlock ? coloredCubeBlock.textures : TextureMap.all(this);
    final Identifier regularModelId = MishangucModels.COLORED_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    final Identifier innerModelId = MishangucModels.COLORED_INNER_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    final Identifier outerModelId = MishangucModels.COLORED_OUTER_STAIRS.upload(this, textureMap, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(this, innerModelId, regularModelId, outerModelId));
    blockStateModelGenerator.registerParentedItemModel(this, regularModelId);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ((ShapedRecipeJsonBuilder) RecipeProvider.createStairsRecipe(this, Ingredient.ofItems(baseBlock)))
        .criterion(RecipeProvider.hasItem(baseBlock), RecipeProvider.conditionsFromItem(baseBlock))
        .setCustomRecipeCategory("colored_blocks");
  }

  @Override
  public MapCodec<? extends ColoredStairsBlock> getCodec() {
    return CODEC;
  }
}
