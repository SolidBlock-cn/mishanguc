package pers.solid.mishang.uc.data;

import com.google.common.base.Predicates;
import it.unimi.dsi.fastutil.floats.FloatObjectPair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.MishangucBlock;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.item.*;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.Arrays;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MishangucModelProvider extends FabricModelProvider {
  public MishangucModelProvider(FabricDataOutput output) {
    super(output);
  }

  @Override
  public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    MishangUtils.blocks().forEach(block -> {
      if (block instanceof MishangucBlock mishangucBlock) {
        mishangucBlock.registerModels(this, blockStateModelGenerator);
      }
    });
  }

  @Override
  public void generateItemModels(ItemModelGenerator itemModelGenerator) {
    itemModelGenerator.register(WallSignBlocks.INVISIBLE_WALL_SIGN.asItem(), Models.HANDHELD);
    itemModelGenerator.register(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN.asItem(), Models.HANDHELD);

    itemModelGenerator.register(MishangucItems.BLOCK_STATE_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.FLUID_STATE_TOOL, Models.HANDHELD);
    registerCarryingTool(itemModelGenerator, MishangucItems.CARRYING_TOOL);
    registerColorTool(itemModelGenerator, MishangucItems.COLOR_TOOL);
    itemModelGenerator.register(MishangucItems.COLUMN_BUILDING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.DATA_TAG_TOOL, Models.HANDHELD);
    registerExplosionToolVariants(itemModelGenerator, MishangucItems.EXPLOSION_TOOL);
    itemModelGenerator.register(MishangucItems.FORCE_PLACING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.FLUID_FORCE_PLACING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.GROWTH_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.ICE_SNOW_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.ID_CHECKER_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.FLUID_ID_CHECKER_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.MIRRORING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.OMNIPOTENT_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.ROAD_CONNECTION_STATE_DEBUGGING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.ROAD_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.ROTATING_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.SLAB_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.TEXT_COPY_TOOL, Models.HANDHELD);
    itemModelGenerator.register(MishangucItems.TP_TOOL, Models.HANDHELD);
    registerFastBuildingTool(itemModelGenerator, MishangucItems.FAST_BUILDING_TOOL);
  }

  private void registerFastBuildingTool(ItemModelGenerator itemModelGenerator, Item item) {
    final Identifier modelId = Models.HANDHELD.upload(item, TextureMap.layer0(item), itemModelGenerator.modelCollector);
    final Identifier darkModelId = Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, "_dark"), TextureMap.layer0(TextureMap.getSubId(item, "_dark")), itemModelGenerator.modelCollector);

    itemModelGenerator.output.accept(item, ItemModels.rangeDispatch(FastBuildingRangeProperty.INSTANCE, 1 / 64f, ItemModels.basic(modelId), ItemModels.rangeDispatchEntry(ItemModels.basic(darkModelId), 0.5f)));
  }

  private void registerCarryingTool(ItemModelGenerator itemModelGenerator, Item item) {
    final Identifier modelId = Models.HANDHELD.upload(item, TextureMap.layer0(item), itemModelGenerator.modelCollector);
    final Identifier withBlock = Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, "_with_block"), TextureMap.layer0(TextureMap.getSubId(item, "_with_block")), itemModelGenerator.modelCollector);
    final Identifier withEntity = Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, "_with_entity"), TextureMap.layer0(TextureMap.getSubId(item, "_with_entity")), itemModelGenerator.modelCollector);
    itemModelGenerator.output.accept(item, ItemModels.select(CarryingToolTypeProperty.INSTANCE,
        ItemModels.basic(modelId),
        ItemModels.switchCase((short) 0, ItemModels.basic(withBlock)),
        ItemModels.switchCase((short) 1, ItemModels.basic(withEntity))
    ));
  }

  private void registerExplosionToolVariants(ItemModelGenerator itemModelGenerator, Item item) {
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
      Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, entry.suffix), TextureMap.layer0(TextureMap.getSubId(item, entry.suffix)), itemModelGenerator.modelCollector);
    }
    itemModelGenerator.output.accept(item, ItemModels.condition(ExplosionCreateFireProperty.INSTANCE,
        ItemModels.rangeDispatch(ExplosionPowerProperty.INSTANCE,
            ItemModels.basic(ModelIds.getItemSubModelId(item, "_fire")),
            entries.stream()
                .filter(ExplosionToolEntry::createFire)
                .filter(entry -> entry.power != 0)
                .map(entry -> ItemModels.rangeDispatchEntry(ItemModels.basic(ModelIds.getItemSubModelId(item, entry.suffix)), entry.power))
                .toList()),
        ItemModels.rangeDispatch(ExplosionPowerProperty.INSTANCE,
            ItemModels.basic(ModelIds.getItemModelId(item)),
            entries.stream()
                .filter(Predicates.not(ExplosionToolEntry::createFire))
                .filter(entry -> entry.power != 0)
                .map(entry -> ItemModels.rangeDispatchEntry(ItemModels.basic(ModelIds.getItemSubModelId(item, entry.suffix)), entry.power))
                .toList())));
  }

  private void registerColorTool(ItemModelGenerator itemModelGenerator, Item item) {
    final List<FloatObjectPair<String>> opacities = List.of(
        FloatObjectPair.of(0.1f, "_opacity_10"),
        FloatObjectPair.of(0.25f, "_opacity_25"),
        FloatObjectPair.of(0.5f, "_opacity_50"),
        FloatObjectPair.of(0.75f, "_opacity_75")
    );
    for (FloatObjectPair<String> opacity : opacities) {
      Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, opacity.right()), TextureMap.layer0(TextureMap.getSubId(item, opacity.right())), itemModelGenerator.modelCollector);
    }
    for (ColorMixtureType colorMixtureType : ColorMixtureType.values()) {
      if (colorMixtureType == ColorMixtureType.NORMAL) continue;
      Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, "_" + colorMixtureType.asString()), TextureMap.layer0(TextureMap.getSubId(item, "_" + colorMixtureType.asString())), itemModelGenerator.modelCollector);
    }

    itemModelGenerator.output.accept(item, ItemModels.select(ColorMixtureTypeProperty.INSTANCE,
        ItemModels.rangeDispatch(TransparencyPropertyProperty.INSTANCE,
            ItemModels.basic(ModelIds.getItemModelId(item)),
            opacities.stream()
                .map(pair -> ItemModels.rangeDispatchEntry(ItemModels.basic(ModelIds.getItemSubModelId(item, pair.right())), 1 - pair.leftFloat()))
                .toList()),
        Arrays.stream(ColorMixtureType.values())
            .filter(colorMixtureType -> colorMixtureType != ColorMixtureType.NORMAL)
            .map(colorMixtureType -> ItemModels.switchCase(colorMixtureType, ItemModels.basic(ModelIds.getItemSubModelId(item, "_" + colorMixtureType.asString()))))
            .toList()));

    Models.HANDHELD.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), itemModelGenerator.modelCollector);
  }
}
