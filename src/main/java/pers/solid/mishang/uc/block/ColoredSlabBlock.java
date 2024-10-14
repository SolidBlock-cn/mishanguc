package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelIds;
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
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public class ColoredSlabBlock extends SlabBlock implements ColoredBlock {
  public static final MapCodec<ColoredSlabBlock> CODEC = BRRPUtils.createCodecWithBaseBlock(createSettingsCodec(), ColoredSlabBlock::new);

  public final Block baseBlock;

  public ColoredSlabBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
  }

  public ColoredSlabBlock(@NotNull Block baseBlock) {
    super(Settings.copy(baseBlock));
    this.baseBlock = baseBlock;
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
    final TextureMap textures = baseBlock instanceof ColoredCubeBlock coloredCubeBlock ? coloredCubeBlock.textures : TextureMap.all(this);
    final Identifier bottomModelId = MishangucModels.COLORED_SLAB.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier topModelId = MishangucModels.COLORED_SLAB_TOP.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier fullModelId;
    if (baseBlock == null) {
      fullModelId = MishangucModels.COLORED_CUBE_BOTTOM_UP.upload(this, textures, blockStateModelGenerator.modelCollector);
    } else {
      fullModelId = ModelIds.getBlockModelId(baseBlock);
    }

    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(this, bottomModelId, topModelId, fullModelId));
    blockStateModelGenerator.registerParentedItemModel(this, bottomModelId);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.slabDrops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ((ShapedRecipeJsonBuilder) RecipeProvider.createSlabRecipe(getRecipeCategory(), this, Ingredient.ofItems(baseBlock)))
        .criterion(RecipeProvider.hasItem(this.baseBlock), RecipeProvider.conditionsFromItem(this.baseBlock))
        .setCustomRecipeCategory("colored_blocks");
  }

  @Override
  public MapCodec<? extends ColoredSlabBlock> getCodec() {
    return CODEC;
  }
}
