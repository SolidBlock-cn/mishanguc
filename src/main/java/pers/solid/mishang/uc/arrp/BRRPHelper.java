package pers.solid.mishang.uc.arrp;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class BRRPHelper {

  /**
   * 返回水平角落方向方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @param uvlock          是否锁定纹理。
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  public static BlockStateSupplier stateForHorizontalCornerFacingBlock(@NotNull Block block, @NotNull Identifier modelIdentifier, boolean uvlock) {
    return VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(MishangucProperties.HORIZONTAL_CORNER_FACING).register(direction -> {
      return BlockStateVariant.create().put(VariantSettings.MODEL, modelIdentifier).put(MishangUtils.INT_Y_VARIANT, direction.asRotation() - 45).put(VariantSettings.UVLOCK, uvlock);
    }));
  }

  public static String slabOf(String string) {
    if (string.endsWith("_block")) {
      return string.replaceFirst("_block$", "_slab");
    } else if (string.contains("road")) {
      return string.replaceFirst("road", "road_slab");
    } else {
      return string + "_slab";
    }
  }

  @Environment(EnvType.CLIENT)
  public static BlockStateSupplier composeStateForSlab(@NotNull BlockStateSupplier stateForFull) {
    final JsonObject variants = stateForFull.get().getAsJsonObject().getAsJsonObject("variants");
    final JsonObject slabVariant = new JsonObject();
    for (Map.Entry<String, JsonElement> entry : variants.entrySet()) {
      final String key = entry.getKey();
      final List<JsonObject> models;
      if (entry.getValue() instanceof JsonArray jsonArray) {
        models = Lists.transform(jsonArray.asList(), JsonElement::getAsJsonObject);
      } else {
        models = Collections.singletonList(entry.getValue().getAsJsonObject());
      }
      for (JsonObject blockModel : models) {
        final Identifier modelId = new Identifier(blockModel.get("model").getAsString());
        JsonObject bottomModel = blockModel.deepCopy();
        bottomModel.addProperty("model", slabOf(modelId).toString());
        slabVariant.add(
            key.isEmpty() ? "type=bottom" : key + ",type=bottom",
            bottomModel);
        JsonObject topModel = blockModel.deepCopy();
        topModel.addProperty("mode", slabOf(modelId).toString());
        slabVariant.add(
            key.isEmpty() ? "type=top" : key + ",type=top",
            topModel);
        JsonObject doubleModel = blockModel.deepCopy();
        doubleModel.addProperty("model", modelId.toString());
        slabVariant.add(
            key.isEmpty() ? "type=double" : key + ",type=double",
            doubleModel);
      }
    }
    return new BlockStateSupplier() {
      @Override
      public Block getBlock() {
        return null;
      }

      @Override
      public JsonElement get() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("variants", slabVariant);
        return jsonObject;
      }
    };
  }

  public static Identifier slabOf(Identifier identifier) {
    return new Identifier(identifier.getNamespace(), slabOf(identifier.getPath()));
  }

  @ApiStatus.AvailableSince("0.2.4")
  public static void addModelWithSlab(RuntimeResourcePack pack, ModelJsonBuilder model, Identifier id, @Nullable Identifier slabModelId) {
    pack.addModel(id, model);
    if (slabModelId != null) {
      final Identifier slabParent = slabOf(model.parentId);
      pack.addModel(slabModelId, model.withParent(slabParent));
      pack.addModel(slabModelId.brrp_suffixed("_top"), model.withParent(slabParent.brrp_suffixed("_top")));
    }
  }

  @ApiStatus.AvailableSince("1.1.0")
  public static void addModelWithSlab(RuntimeResourcePack pack, AbstractRoadBlock block) {
    final AbstractRoadSlabBlock roadSlab = block.getRoadSlab();
    addModelWithSlab(pack, block.getBlockModel(), block.getBlockModelId(), roadSlab == null ? null : roadSlab.getBlockModelId());
  }

  @ApiStatus.AvailableSince("1.1.0")
  public static void addModelWithSlabWithMirrored(RuntimeResourcePack pack, ModelJsonBuilder model, Identifier id, @Nullable Identifier slabModelId) {
    addModelWithSlab(pack, model, id, slabModelId);
    if (slabModelId != null) {
      addModelWithSlab(pack, model.withParent(model.parentId.brrp_suffixed("_mirrored")), id.brrp_suffixed("_mirrored"), slabModelId.brrp_suffixed("_mirrored"));
    }
  }

  @ApiStatus.AvailableSince("1.1.0")
  public static void addModelWithSlabWithMirrored(RuntimeResourcePack pack, AbstractRoadBlock block) {
    final AbstractRoadSlabBlock roadSlab = block.getRoadSlab();
    addModelWithSlabWithMirrored(pack, block.getBlockModel(), block.getBlockModelId(), roadSlab == null ? null : roadSlab.getBlockModelId());
  }
}
