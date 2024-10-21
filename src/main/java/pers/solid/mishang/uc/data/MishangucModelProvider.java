package pers.solid.mishang.uc.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.floats.FloatObjectPair;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.MishangucBlock;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.List;

public class MishangucModelProvider extends FabricModelProvider {
  public MishangucModelProvider(FabricDataOutput output) {
    super(output);
  }

  @Override
  public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    MishangUtils.blocks().forEach(block -> {
      if (block instanceof MishangucBlock mishangucBlock) {
        mishangucBlock.registerModels(this, blockStateModelGenerator);
        if (block instanceof HandrailBlock handrailBlock) {
          handrailBlock.central().registerModels(this, blockStateModelGenerator);
          handrailBlock.corner().registerModels(this, blockStateModelGenerator);
          handrailBlock.stair().registerModels(this, blockStateModelGenerator);
          handrailBlock.outer().registerModels(this, blockStateModelGenerator);
        }
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
  }

  private void registerCarryingTool(ItemModelGenerator itemModelGenerator, Item item) {
    Models.HANDHELD.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), itemModelGenerator.writer, (id, textures) -> {
      final JsonObject json = Models.HANDHELD.createJson(id, textures);
      final JsonArray overrides = new JsonArray();
      json.add("overrides", overrides);
      final JsonObject withBlock = new JsonObject();
      overrides.add(withBlock);
      final JsonObject withBlockPredicate = new JsonObject();
      withBlock.add("predicate", withBlockPredicate);
      withBlockPredicate.addProperty("mishanguc:is_holding_block", 1f);
      withBlock.addProperty("model", ModelIds.getItemSubModelId(item, "_with_block").toString());
      final JsonObject withEntity = new JsonObject();
      overrides.add(withEntity);
      final JsonObject withEntityPredicate = new JsonObject();
      withEntity.add("predicate", withEntityPredicate);
      withEntityPredicate.addProperty("mishanguc:is_holding_entity", 1f);
      withEntity.addProperty("model", ModelIds.getItemSubModelId(item, "_with_entity").toString());
      return json;
    });
    itemModelGenerator.register(item, "_with_block", Models.HANDHELD);
    itemModelGenerator.register(item, "_with_entity", Models.HANDHELD);
  }

  private void registerExplosionToolVariants(ItemModelGenerator itemModelGenerator, Item item) {
    for (final String name : new String[]{
        "_fire",
        "_4", "_4_fire",
        "_8", "_8_fire",
        "_16", "_16_fire",
        "_32", "_32_fire",
        "_64", "_64_fire",
        "_128", "_128_fire",
    }) {
      itemModelGenerator.register(item, name, Models.HANDHELD);
    }
  }

  private void registerColorTool(ItemModelGenerator itemModelGenerator, Item item) {
    final List<FloatObjectPair<String>> opacities = List.of(
        FloatObjectPair.of(0.1f, "_opacity_10"),
        FloatObjectPair.of(0.25f, "_opacity_25"),
        FloatObjectPair.of(0.5f, "_opacity_50"),
        FloatObjectPair.of(0.75f, "_opacity_75")
    );
    for (FloatObjectPair<String> opacity : opacities) {
      Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, opacity.right()), TextureMap.layer0(TextureMap.getSubId(item, opacity.right())), itemModelGenerator.writer);
    }
    for (ColorMixtureType colorMixtureType : ColorMixtureType.values()) {
      if (colorMixtureType == ColorMixtureType.NORMAL) continue;
      Models.HANDHELD.upload(ModelIds.getItemSubModelId(item, "_" + colorMixtureType.asString()), TextureMap.layer0(TextureMap.getSubId(item, "_" + colorMixtureType.asString())), itemModelGenerator.writer);
    }

    Models.HANDHELD.upload(ModelIds.getItemModelId(item), TextureMap.layer0(item), itemModelGenerator.writer, (id, textures) -> {
      final JsonObject json = Models.HANDHELD.createJson(id, textures);
      final JsonArray overrides = new JsonArray();
      json.add("overrides", overrides);

      for (FloatObjectPair<String> pair : opacities) {
        final JsonObject override = new JsonObject();
        overrides.add(override);
        final JsonObject predicate = new JsonObject();
        override.add("predicate", predicate);
        predicate.addProperty("mishanguc:transparency", 1 - pair.leftFloat());
        predicate.addProperty("mishanguc:color_mixture_type", 0);
        override.addProperty("model", ModelIds.getItemSubModelId(item, pair.right()).toString());
      }
      for (ColorMixtureType colorMixtureType : ColorMixtureType.values()) {
        if (colorMixtureType == ColorMixtureType.NORMAL) continue;
        final JsonObject override = new JsonObject();
        overrides.add(override);
        final JsonObject predicate = new JsonObject();
        override.add("predicate", predicate);
        predicate.addProperty("mishanguc:color_mixture_type", colorMixtureType.ordinal() * 0.1f);
        override.addProperty("model", ModelIds.getItemSubModelId(item, "_" + colorMixtureType.asString()).toString());
      }
      return json;
    });
  }
}
