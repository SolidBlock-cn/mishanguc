package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BRRPStairsBlock;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.brrp.v1.recipe.RecipeJsonBuilderExtension;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredStairsBlock extends BRRPStairsBlock implements ColoredBlock {
  public ColoredStairsBlock(Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public ColoredStairsBlock(Block baseBlock) {
    super(baseBlock);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @UnknownNullability ModelJsonBuilder getBlockModel() {
    return super.getBlockModel().parent(new Identifier("mishanguc", "block/colored_stairs"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder blockModel = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(id, blockModel);
    pack.addModel(id.brrp_suffixed("_inner"), blockModel.parent(new Identifier("mishanguc", "block/colored_inner_stairs")));
    pack.addModel(id.brrp_suffixed("_outer"), blockModel.parent(new Identifier("mishanguc", "block/colored_outer_stairs")));
  }

  @Override
  public LootTable.@NotNull Builder getLootTable() {
    return new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    final CraftingRecipeJsonBuilder recipe = super.getCraftingRecipe();
    if (recipe instanceof RecipeJsonBuilderExtension<?> s) s.setCustomRecipeCategory("colored_blocks");
    return recipe;
  }
}
