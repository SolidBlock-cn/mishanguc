package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
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
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;

public class ColoredSlabBlock extends SlabBlock implements ColoredBlock {
  public static final MapCodec<ColoredSlabBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(o -> o.baseBlock),
      propertiesCodec()
  ).apply(i, ColoredSlabBlock::new));

  public final Block baseBlock;

  public ColoredSlabBlock(@Nullable Block baseBlock, Properties settings) {
    super(settings);
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
    final TextureMapping textures = baseBlock instanceof ColoredCubeBlock coloredCubeBlock ? coloredCubeBlock.textures.getTextureMap() : TextureMapping.cube(this);
    final Identifier bottomModelId = MishangucModels.COLORED_SLAB.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier topModelId = MishangucModels.COLORED_SLAB_TOP.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier fullModelId;
    if (baseBlock == null) {
      fullModelId = MishangucModels.COLORED_CUBE_BOTTOM_UP.create(this, textures, blockStateModelGenerator.modelOutput);
    } else {
      fullModelId = ModelLocationUtils.getModelLocation(baseBlock);
    }

    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSlab(this, BlockModelGenerators.plainVariant(bottomModelId), BlockModelGenerators.plainVariant(topModelId), BlockModelGenerators.plainVariant(fullModelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(bottomModelId, ColoredTintSource.INSTANCE));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSlabItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return ((ShapedRecipeBuilder) recipeGenerator.slabBuilder(RecipeCategory.BUILDING_BLOCKS, this, Ingredient.of(baseBlock)))
        .unlockedBy(RecipeProvider.getHasName(this.baseBlock), recipeGenerator.has(this.baseBlock));
  }

  @Override
  public MapCodec<? extends ColoredSlabBlock> codec() {
    return CODEC;
  }
}
