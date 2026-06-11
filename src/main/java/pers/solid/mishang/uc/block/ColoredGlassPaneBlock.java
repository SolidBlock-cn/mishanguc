package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;

public class ColoredGlassPaneBlock extends IronBarsBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlassPaneBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      Identifier.CODEC.fieldOf("pane_material").forGetter(b -> b.paneTexture),
      Identifier.CODEC.fieldOf("edge_material").forGetter(b -> b.edgeTexture),
      propertiesCodec()
  ).apply(i, ColoredGlassPaneBlock::new));
  private final Identifier paneTexture;
  private final Identifier edgeTexture;

  public ColoredGlassPaneBlock(Identifier paneTexture, Identifier edgeTexture, Properties properties) {
    super(properties);
    this.paneTexture = paneTexture;
    this.edgeTexture = edgeTexture;
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
    TextureMapping textures = TextureMapping.singleSlot(TextureSlot.PANE, new Material(paneTexture)).put(TextureSlot.EDGE, new Material(edgeTexture));
    final Identifier postId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_POST.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier sideId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_SIDE.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier SideAltId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_SIDE_ALT.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier nosideId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_NOSIDE.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier nosideAltId = MishangucModels.TEMPLATE_COLORED_GLASS_PANE_NOSIDE_ALT.create(this, textures, blockStateModelGenerator.modelOutput);

    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(postId, sideId, SideAltId, nosideId, nosideAltId));
    final Identifier itemModelId = ModelTemplates.FLAT_ITEM.create(asItem(), TextureMapping.layer0(new Material(paneTexture)), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(itemModelId, ColoredTintSource.INSTANCE));
  }

  @Environment(EnvType.CLIENT)
  public BlockModelDefinitionGenerator createBlockStates(Identifier postId, Identifier sideId, Identifier sideAltId, Identifier nosideId, Identifier nosideAltId) {
    return MultiPartGenerator.multiPart(this)
        .with(BlockModelGenerators.plainVariant(postId))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.NORTH, true),
            BlockModelGenerators.plainVariant(sideId))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.EAST, true),
            BlockModelGenerators.plainVariant(sideId)
                .with(BlockModelGenerators.Y_ROT_90))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.SOUTH, true),
            BlockModelGenerators.plainVariant(sideAltId))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.WEST, true),
            BlockModelGenerators.plainVariant(sideAltId)
                .with(BlockModelGenerators.Y_ROT_90))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.NORTH, false),
            BlockModelGenerators.plainVariant(nosideId))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.EAST, false),
            BlockModelGenerators.plainVariant(nosideAltId))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.SOUTH, false),
            BlockModelGenerators.plainVariant(nosideAltId)
                .with(BlockModelGenerators.Y_ROT_90))
        .with(BlockModelGenerators.condition().term(BlockStateProperties.WEST, false),
            BlockModelGenerators.plainVariant(nosideId)
                .with(BlockModelGenerators.Y_ROT_270));
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSilkTouchOnlyTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredGlassPaneBlock> codec() {
    return CODEC;
  }
}
