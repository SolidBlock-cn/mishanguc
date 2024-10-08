package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public class ColoredCubeBlock extends Block implements ColoredBlock {
  protected final Model model;
  protected final TextureMap textures;
  public static final MapCodec<ColoredCubeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createSettingsCodec()).apply(instance, (settings1) -> new ColoredCubeBlock(settings1, null, new TextureMap())));
  public static final Model COLORED_CUBE_ALL = MishangucModels.createBlock("colored_cube_all", TextureKey.ALL);
  public static final Model COLORED_CUBE_BOTTOM_TOP = MishangucModels.createBlock("colored_cube_bottom_top", TextureKey.TOP, TextureKey.BOTTOM, TextureKey.SIDE);
  public static final Model COLORED_CUBE_MIRRORED_ALL = MishangucModels.createBlock("colored_cube_mirrored_all", "_mirrored", TextureKey.ALL);
  public static final Model COLORED_CUBE_ALL_WITHOUT_SHADE = MishangucModels.createBlock("colored_cube_all_without_shade", TextureKey.ALL);

  @ApiStatus.Internal
  public ColoredCubeBlock(Settings settings, Model model, TextureMap textures) {
    super(settings);
    this.model = model;
    this.textures = textures;
  }

  public static ColoredCubeBlock cubeAll(Settings settings, String allTexture) {
    return new ColoredCubeBlock(settings, COLORED_CUBE_ALL, TextureMap.all(new Identifier(allTexture)));
  }

  public static ColoredCubeBlock cubeBottomTop(Settings settings, String topTexture, String sideTexture, String bottomTexture) {
    return new ColoredCubeBlock(settings, COLORED_CUBE_BOTTOM_TOP, TextureMap.of(TextureKey.TOP, new Identifier(topTexture)).put(TextureKey.SIDE, new Identifier(sideTexture)).put(TextureKey.BOTTOM, new Identifier(bottomTexture)));
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

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return blockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier modelId = model.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, modelId));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  @Override
  protected MapCodec<? extends ColoredCubeBlock> getCodec() {
    return CODEC;
  }

  @Override
  public Identifier getTexture(TextureKey key) {
    return textures.getTexture(key);
  }
}
