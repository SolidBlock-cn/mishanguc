package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.util.TextureMapReference;

import java.util.List;

public class ColoredIceBlock extends IceBlock implements ColoredBlock {
  public static final MapCodec<ColoredIceBlock> CODEC = simpleCodec(settings1 -> new ColoredIceBlock(settings1, TextureMapReference.EMPTY));

  private final TextureMapReference textures;

  public ColoredIceBlock(Properties settings, TextureMapReference textures) {
    super(settings);
    this.textures = textures;
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
    final Identifier modelId = MishangucModels.COLORED_CUBE_ALL.create(this, textures.getTextureMap(), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(modelId, ColoredTintSource.INSTANCE));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSilkTouchOnlyTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredIceBlock> codec() {
    return CODEC;
  }
}
