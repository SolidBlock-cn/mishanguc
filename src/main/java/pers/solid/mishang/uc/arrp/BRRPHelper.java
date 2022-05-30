package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

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
  public static JBlockStates stateForHorizontalCornerFacingBlock(@NotNull Identifier modelIdentifier, boolean uvlock) {
    JVariants variant = new JVariants();
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      final JBlockModel model = new JBlockModel(modelIdentifier).y(direction.asRotation() - 45);
      if (uvlock) model.uvlock();
      variant.addVariant("facing", direction.asString(), model);
    }
    return JBlockStates.ofVariants(variant);
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
  public static JBlockStates composeStateForSlab(@NotNull JBlockStates stateForFull) {
    final JVariants variants = stateForFull.variants;
    final JVariants slabVariant = new JVariants();
    for (Map.Entry<String, JBlockModel[]> entry : variants.entrySet()) {
      final String key = entry.getKey();
      final JBlockModel[] value = entry.getValue();
      for (JBlockModel blockModel : value) {
        final Identifier modelId = blockModel.model;
        slabVariant
            .addVariant(
                key.isEmpty() ? "type=bottom" : key + ",type=bottom",
                blockModel.clone().modelId(
                    new Identifier(modelId.getNamespace(), slabOf(modelId.getPath()))))
            .addVariant(
                key.isEmpty() ? "type=top" : key + ",type=top",
                blockModel.clone().modelId(
                    new Identifier(
                        modelId.getNamespace(), slabOf(modelId.getPath()) + "_top")))
            .addVariant(
                key.isEmpty() ? "type=double" : key + ",type=double",
                blockModel.clone().modelId(new Identifier(modelId.getNamespace(), (modelId.getPath()))));
      }
    }
    return JBlockStates.ofVariants(slabVariant);
  }

  public static Identifier slabOf(Identifier identifier) {
    return new Identifier(identifier.getNamespace(), slabOf(identifier.getPath()));
  }
}
