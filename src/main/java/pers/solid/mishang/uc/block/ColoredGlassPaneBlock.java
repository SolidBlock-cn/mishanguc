package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassPaneBlock extends PaneBlock implements ColoredBlock {
  private final Identifier paneTexture;
  private final Identifier edgeTexture;

  public ColoredGlassPaneBlock(Identifier paneTexture, Identifier edgeTexture, Settings settings) {
    super(settings);
    this.paneTexture = paneTexture;
    this.edgeTexture = edgeTexture;
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
  public void writeBlockModel(RuntimeResourcePack pack) {
    ImmutableList<String> suffixes = ImmutableList.of("_post", "_side", "_side_alt", "_noside", "_noside_alt");
    TextureMap textureMap = TextureMap.of(TextureKey.PANE, paneTexture).put(TextureKey.EDGE, edgeTexture);
    for (String suffix : suffixes) {
      pack.addModel(getBlockModelId().brrp_suffixed(suffix), ModelJsonBuilder.create(new Identifier("mishanguc", "block/template_colored_glass_pane" + suffix)).setTextures(textureMap));
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull BlockStateSupplier getBlockStates() {
    Identifier baseIdentifier = getBlockModelId();
    Identifier postIdentifier = baseIdentifier.brrp_suffixed("_post");
    Identifier sideIdentifier = baseIdentifier.brrp_suffixed("_side");
    Identifier sideAltIdentifier = baseIdentifier.brrp_suffixed("_side_alt");
    Identifier noSideIdentifier = baseIdentifier.brrp_suffixed("_noside");
    Identifier noSideAltIdentifier = baseIdentifier.brrp_suffixed("_noside_alt");
    return MultipartBlockStateSupplier.create(this).with(BlockStateVariant.create().put(VariantSettings.MODEL, postIdentifier)).with(When.create().set(Properties.NORTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, sideIdentifier)).with(When.create().set(Properties.EAST, true), BlockStateVariant.create().put(VariantSettings.MODEL, sideIdentifier).put(VariantSettings.Y, VariantSettings.Rotation.R90)).with(When.create().set(Properties.SOUTH, true), BlockStateVariant.create().put(VariantSettings.MODEL, sideAltIdentifier)).with(When.create().set(Properties.WEST, true), BlockStateVariant.create().put(VariantSettings.MODEL, sideAltIdentifier).put(VariantSettings.Y, VariantSettings.Rotation.R90)).with(When.create().set(Properties.NORTH, false), BlockStateVariant.create().put(VariantSettings.MODEL, noSideIdentifier)).with(When.create().set(Properties.EAST, false), BlockStateVariant.create().put(VariantSettings.MODEL, noSideAltIdentifier)).with(When.create().set(Properties.SOUTH, false), BlockStateVariant.create().put(VariantSettings.MODEL, noSideAltIdentifier).put(VariantSettings.Y, VariantSettings.Rotation.R90)).with(When.create().set(Properties.WEST, false), BlockStateVariant.create().put(VariantSettings.MODEL, noSideIdentifier).put(VariantSettings.Y, VariantSettings.Rotation.R270));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.GENERATED).addTexture(TextureKey.LAYER0, paneTexture);
  }

  @Override
  public LootTable.@NotNull Builder getLootTable() {
    return BlockLootTableGenerator.dropsWithSilkTouch(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }
}
