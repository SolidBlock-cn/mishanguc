package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.List;

public class ColoredGlassPaneBlock extends PaneBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlassPaneBlock> CODEC = createCodec(settings1 -> new ColoredGlassPaneBlock(null, null, settings1));
  private final Identifier paneTexture;
  private final Identifier edgeTexture;

  public ColoredGlassPaneBlock(Identifier paneTexture, Identifier edgeTexture, Settings settings) {
    super(settings);
    this.paneTexture = paneTexture;
    this.edgeTexture = edgeTexture;
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

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    TextureMap textures = TextureMap.of(TextureKey.PANE, paneTexture).put(TextureKey.EDGE, edgeTexture);
    final Identifier postId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_POST.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier sideId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_SIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier SideAltId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_SIDE_ALT.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier nosideId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_NOSIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier nosideAltId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_NOSIDE_ALT.upload(this, textures, blockStateModelGenerator.modelCollector);

    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(postId, sideId, SideAltId, nosideId, nosideAltId));
    Models.GENERATED.upload(ModelIds.getItemModelId(asItem()), TextureMap.layer0(paneTexture), blockStateModelGenerator.modelCollector);
  }

  public @NotNull BlockStateSupplier createBlockStates(Identifier postId, Identifier sideId, Identifier sideAltId, Identifier nosideId, Identifier nosideAltId) {
    return MultipartBlockStateSupplier.create(this)
        .with(BlockStateVariant.create()
            .put(VariantSettings.MODEL, postId))
        .with(When.create().set(Properties.NORTH, true),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideId))
        .with(When.create().set(Properties.EAST, true),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
        .with(When.create().set(Properties.SOUTH, true),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideAltId))
        .with(When.create().set(Properties.WEST, true),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, sideAltId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
        .with(When.create().set(Properties.NORTH, false),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, nosideId))
        .with(When.create().set(Properties.EAST, false),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, nosideAltId))
        .with(When.create().set(Properties.SOUTH, false),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, nosideAltId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R90))
        .with(When.create().set(Properties.WEST, false),
            BlockStateVariant.create()
                .put(VariantSettings.MODEL, nosideId)
                .put(VariantSettings.Y, VariantSettings.Rotation.R270));
  }

  @Override
  public LootTable.@NotNull Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    return BlockLootTableGenerator.dropsWithSilkTouch(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredGlassPaneBlock> getCodec() {
    return CODEC;
  }
}
