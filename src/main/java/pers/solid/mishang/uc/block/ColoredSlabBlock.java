package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BRRPSlabBlock;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredSlabBlock extends BRRPSlabBlock implements ColoredBlock {
  public static final MapCodec<ColoredSlabBlock> CODEC = BRRPUtils.createCodecWithBaseBlock(createSettingsCodec(), ColoredSlabBlock::new);

  public ColoredSlabBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public ColoredSlabBlock(@NotNull Block baseBlock) {
    super(baseBlock);
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
    return super.getBlockModel().parent(new Identifier("mishanguc", "block/colored_slab"));
  }


  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final ModelJsonBuilder model = getBlockModel();
    final Identifier id = getBlockModelId();
    pack.addModel(id, model);
    pack.addModel(id.brrp_suffixed("_top"), model.withParent(new Identifier("mishanguc", "block/colored_slab_top")));
    if (baseBlock == null) {
      pack.addModel(id.brrp_suffixed("_double"), model.withParent(new Identifier("mishanguc", "block/colored_cube_bottom_up")));
    }
  }

  @Override
  public LootTable.@NotNull Builder getLootTable() {
    return new VanillaBlockLootTableGenerator().slabDrops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.BUILDING_BLOCKS;
  }

  @Override
  public MapCodec<? extends ColoredSlabBlock> getCodec() {
    return CODEC;
  }
}
