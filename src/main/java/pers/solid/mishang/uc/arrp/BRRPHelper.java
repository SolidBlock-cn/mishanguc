package pers.solid.mishang.uc.arrp;

import com.google.gson.JsonObject;
import net.devtech.arrp.json.blockstate.*;
import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JFunction;
import net.devtech.arrp.json.loot.JLootTable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.loot.LootTable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.util.Map;

public final class BRRPHelper {
  /**
   * 根据模型 id，返回一个最简单的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
  @NotNull
  public static JState simpleState(@NotNull Identifier modelIdentifier) {
    return new JState().add(new JVariant().put("", new JBlockModel(modelIdentifier)));
  }

  /**
   * 返回一个垂直方向的方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @param uvlock          是否锁定纹理。
   * @return 方块状态。
   * @deprecated Please use {@link JBlockStates#simpleHorizontalFacing(Identifier, boolean)}.
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  @Deprecated
  public static JBlockStates stateForHorizontalFacingBlock(@NotNull Identifier modelIdentifier, boolean uvlock) {
    JVariants variant = new JVariants();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final JBlockModel model = new JBlockModel(modelIdentifier).y((int) direction.asRotation()).uvlock(uvlock);
      variant.addVariant("facing", direction.asString(), model);
    }
    return JBlockStates.ofVariants(variant);
  }

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

  /**
   * 简单的战利品表。
   *
   * @param name 掉落物名称，通常是方块id对应的字符串。
   * @return 战利品表。
   * @see JLootTable#simple(String)
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
  public static JLootTable simpleLootTable(String name) {
    return new JLootTable("minecraft:block")
        .pool(
            JLootTable.pool()
                .rolls(1)
                .entry(JLootTable.entry().type("minecraft:item").name(name))
                .condition(JLootTable.predicate("minecraft:survives_explosion")));
  }

  /**
   * 楼梯的简单战利品表。当楼梯为 {@code [type=double]} 时，掉落两份。
   *
   * @param name 掉落物名称，通常是方块id对应的字符串。
   * @return 战利品表。
   * @see JLootTable#delegate(LootTable)
   */
  @Deprecated
  @ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
  public static JLootTable simpleSlabLootTable(String name) {
    return JLootTable.loot("minecraft:block").pool(JLootTable.pool().rolls(1)
        .entry(JLootTable.entry()
            .type("minecraft:item")
            .name(name)
            .function(new JFunction("set_count")
                .condition(new JCondition("block_state_property")
                    .parameter("block", name)
                    .parameter("properties",
                        Util.make(new JsonObject(),
                            jsonObject ->
                                jsonObject.addProperty("type", "double"))))
                .parameter("count", 2))
            .function(new JFunction("explosion_decay"))));
  }
}
