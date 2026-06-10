package pers.solid.mishang.uc.data;

import com.google.common.base.Predicates;
import it.unimi.dsi.fastutil.floats.FloatObjectPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.MishangucBlock;
import pers.solid.mishang.uc.blocks.StandingSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.item.*;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MishangucModelProvider extends FabricModelProvider {
  public MishangucModelProvider(FabricPackOutput output) {
    super(output);
  }

  @Override
  public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
    MishangUtils.blocks().forEach(block -> {
      if (block instanceof MishangucBlock mishangucBlock) {
        mishangucBlock.registerModels(this, blockStateModelGenerator);
      }
    });
  }

  @Override
  public void generateItemModels(ItemModelGenerators itemModelGenerator) {
    itemModelGenerator.generateFlatItem(WallSignBlocks.INVISIBLE_WALL_SIGN.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN.asItem(), ModelTemplates.FLAT_HANDHELD_ITEM);

    itemModelGenerator.generateFlatItem(MishangucItems.BLOCK_STATE_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.FLUID_STATE_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    registerCarryingTool(itemModelGenerator, MishangucItems.CARRYING_TOOL);
    registerColorTool(itemModelGenerator, MishangucItems.COLOR_TOOL);
    itemModelGenerator.generateFlatItem(MishangucItems.COLUMN_BUILDING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.DATA_TAG_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    registerExplosionToolVariants(itemModelGenerator, MishangucItems.EXPLOSION_TOOL);
    itemModelGenerator.generateFlatItem(MishangucItems.FORCE_PLACING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.FLUID_FORCE_PLACING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.GROWTH_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.ICE_SNOW_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.ID_CHECKER_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.FLUID_ID_CHECKER_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.MIRRORING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.OMNIPOTENT_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.ROAD_CONNECTION_STATE_DEBUGGING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.ROAD_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.ROTATING_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.SLAB_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.TEXT_COPY_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    itemModelGenerator.generateFlatItem(MishangucItems.TP_TOOL, ModelTemplates.FLAT_HANDHELD_ITEM);
    registerFastBuildingTool(itemModelGenerator, MishangucItems.FAST_BUILDING_TOOL);
  }

  private void registerFastBuildingTool(ItemModelGenerators itemModelGenerator, Item item) {
    final Identifier modelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(item, TextureMapping.layer0(item), itemModelGenerator.modelOutput);
    final Identifier darkModelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, "_dark"), TextureMapping.layer0(TextureMapping.getItemTexture(item, "_dark")), itemModelGenerator.modelOutput);

    itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.rangeSelect(FastBuildingRangeProperty.INSTANCE, 1 / 64f, ItemModelUtils.plainModel(modelId), ItemModelUtils.override(ItemModelUtils.plainModel(darkModelId), 0.5f)));
  }

  private void registerCarryingTool(ItemModelGenerators itemModelGenerator, Item item) {
    final Identifier modelId = ModelTemplates.FLAT_HANDHELD_ITEM.create(item, TextureMapping.layer0(item), itemModelGenerator.modelOutput);
    final Identifier withBlock = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, "_with_block"), TextureMapping.layer0(TextureMapping.getItemTexture(item, "_with_block")), itemModelGenerator.modelOutput);
    final Identifier withEntity = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, "_with_entity"), TextureMapping.layer0(TextureMapping.getItemTexture(item, "_with_entity")), itemModelGenerator.modelOutput);
    itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.select(CarryingToolTypeProperty.INSTANCE,
        ItemModelUtils.plainModel(modelId),
        ItemModelUtils.when((short) 0, ItemModelUtils.plainModel(withBlock)),
        ItemModelUtils.when((short) 1, ItemModelUtils.plainModel(withEntity))
    ));
  }

  private void registerExplosionToolVariants(ItemModelGenerators itemModelGenerator, Item item) {
    record ExplosionToolEntry(String suffix, float power, boolean createFire) {}
    final List<ExplosionToolEntry> entries = List.of(
        new ExplosionToolEntry("", 0, false),
        new ExplosionToolEntry("_fire", 0, true),
        new ExplosionToolEntry("_4", 4, false),
        new ExplosionToolEntry("_4_fire", 4, true),
        new ExplosionToolEntry("_8", 8, false),
        new ExplosionToolEntry("_8_fire", 8, true),
        new ExplosionToolEntry("_16", 16, false),
        new ExplosionToolEntry("_16_fire", 16, true),
        new ExplosionToolEntry("_32", 32, false),
        new ExplosionToolEntry("_32_fire", 32, true),
        new ExplosionToolEntry("_64", 64, false),
        new ExplosionToolEntry("_64_fire", 64, true),
        new ExplosionToolEntry("_128", 128, false),
        new ExplosionToolEntry("_128_fire", 128, true)
    );
    for (final ExplosionToolEntry entry : entries) {
      ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, entry.suffix), TextureMapping.layer0(TextureMapping.getItemTexture(item, entry.suffix)), itemModelGenerator.modelOutput);
    }
    itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.conditional(ExplosionCreateFireProperty.INSTANCE,
        ItemModelUtils.rangeSelect(ExplosionPowerProperty.INSTANCE,
            ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_fire")),
            entries.stream()
                .filter(ExplosionToolEntry::createFire)
                .filter(entry -> entry.power != 0)
                .map(entry -> ItemModelUtils.override(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, entry.suffix)), entry.power))
                .toList()),
        ItemModelUtils.rangeSelect(ExplosionPowerProperty.INSTANCE,
            ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)),
            entries.stream()
                .filter(Predicates.not(ExplosionToolEntry::createFire))
                .filter(entry -> entry.power != 0)
                .map(entry -> ItemModelUtils.override(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, entry.suffix)), entry.power))
                .toList())));
  }

  private void registerColorTool(ItemModelGenerators itemModelGenerator, Item item) {
    final List<FloatObjectPair<String>> opacities = List.of(
        FloatObjectPair.of(0.1f, "_opacity_10"),
        FloatObjectPair.of(0.25f, "_opacity_25"),
        FloatObjectPair.of(0.5f, "_opacity_50"),
        FloatObjectPair.of(0.75f, "_opacity_75")
    );
    for (FloatObjectPair<String> opacity : opacities) {
      ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, opacity.right()), TextureMapping.layer0(TextureMapping.getItemTexture(item, opacity.right())), itemModelGenerator.modelOutput);
    }
    for (ColorMixtureType colorMixtureType : ColorMixtureType.values()) {
      if (colorMixtureType == ColorMixtureType.NORMAL) continue;
      ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item, "_" + colorMixtureType.getSerializedName()), TextureMapping.layer0(TextureMapping.getItemTexture(item, "_" + colorMixtureType.getSerializedName())), itemModelGenerator.modelOutput);
    }

    itemModelGenerator.itemModelOutput.accept(item, ItemModelUtils.select(ColorMixtureTypeProperty.INSTANCE,
        ItemModelUtils.rangeSelect(TransparencyPropertyProperty.INSTANCE,
            ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item)),
            opacities.stream()
                .map(pair -> ItemModelUtils.override(ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, pair.right())), 1 - pair.leftFloat()))
                .toList()),
        Arrays.stream(ColorMixtureType.values())
            .filter(colorMixtureType -> colorMixtureType != ColorMixtureType.NORMAL)
            .map(colorMixtureType -> ItemModelUtils.when(colorMixtureType, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(item, "_" + colorMixtureType.getSerializedName()))))
            .toList()));

    ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), itemModelGenerator.modelOutput);
  }
}
