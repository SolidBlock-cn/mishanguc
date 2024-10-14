package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredIceBlock extends IceBlock implements ColoredBlock, BlockResourceGenerator {
  public static final MapCodec<ColoredIceBlock> CODEC = createCodec(settings1 -> new ColoredIceBlock(settings1, null));

  private final @Nullable TextureMap textures;

  public ColoredIceBlock(Settings settings, @Nullable TextureMap textures) {
    super(settings);
    this.textures = textures;
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
    final Identifier modelId = ColoredCubeBlock.COLORED_CUBE_ALL.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, modelId));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }
  
  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredIceBlock> getCodec() {
    return CODEC;
  }
}
