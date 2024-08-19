package pers.solid.mishang.uc.data;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucProperties;
import pers.solid.mishang.uc.block.MishangucBlock;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class ModelHelper {

  /**
   * 返回水平角落方向方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @param uvlock          是否锁定纹理。
   * @return 方块状态。
   */
  @NotNull
  public static BlockStateSupplier stateForHorizontalCornerFacingBlock(@NotNull Block block, @NotNull Identifier modelIdentifier, boolean uvlock) {
    return VariantsBlockStateSupplier.create(block).coordinate(BlockStateVariantMap.create(MishangucProperties.HORIZONTAL_CORNER_FACING).register(direction -> BlockStateVariant.create().put(VariantSettings.MODEL, modelIdentifier).put(MishangUtils.INT_Y_VARIANT, direction.asRotation() - 45).put(VariantSettings.UVLOCK, uvlock)));
  }

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
        bottomModel.addProperty("model", modelId.toString());
        slabVariant.add(
            key.isEmpty() ? "type=bottom" : key + ",type=bottom",
            bottomModel);
        JsonObject topModel = blockModel.deepCopy();
        topModel.addProperty("model", modelId.withSuffixedPath("_top").toString());
        slabVariant.add(
            key.isEmpty() ? "type=top" : key + ",type=top",
            topModel);
        JsonObject doubleModel = blockModel.deepCopy();
        doubleModel.addProperty("model", modelId.withPath(s -> s.endsWith("_slab") ? s.replace("_slab", "_block") : s.replace("_slab", "")).toString());
        slabVariant.add(
            key.isEmpty() ? "type=double" : key + ",type=double",
            doubleModel);
      }
    }
    return new BlockStateSupplier() {
      @Override
      public Block getBlock() {
        return stateForFull.getBlock();
      }

      @Override
      public JsonElement get() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.add("variants", slabVariant);
        return jsonObject;
      }
    };
  }

  public static Identifier getTextureOf(Block block) {
    if (block instanceof MishangucBlock mishangucBlock) {
      return mishangucBlock.getTexture(TextureKey.TEXTURE);
    } else {
      return TextureMap.getId(block);
    }
  }
}
