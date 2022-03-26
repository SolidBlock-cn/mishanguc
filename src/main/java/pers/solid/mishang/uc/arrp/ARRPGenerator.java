package pers.solid.mishang.uc.arrp;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.blockstate.JVariant;
import net.devtech.arrp.json.loot.JCondition;
import net.devtech.arrp.json.loot.JFunction;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.mixin.JBlockModelAccessor;
import pers.solid.mishang.uc.mixin.JStateAccessor;
import pers.solid.mishang.uc.mixin.JVariantAccessor;
import pers.solid.mishang.uc.util.HorizontalCornerDirection;

import java.util.List;
import java.util.Map;

/**
 * 此接口用于表示该方块（通常是方块）可以生成对应的运行时的资源包和数据包。<br>
 * 实现此方法，然后你就可以在 {@link ARRPMain} 中使用其对应的功能。
 */
@ApiStatus.AvailableSince("0.1.7")
public interface ARRPGenerator {
  /**
   * 根据模型 id，返回一个最简单的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  static JState simpleState(@NotNull Identifier modelIdentifier) {
    return new JState().add(new JVariant().put("", new JBlockModel(modelIdentifier)));
  }

  /**
   * 返回一个垂直方向的方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  static JState stateForHorizontalFacingBlock(@NotNull Identifier modelIdentifier) {
    JVariant variant = new JVariant();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      variant.put(
          "facing",
          direction,
          new JBlockModel(modelIdentifier).y((int) direction.asRotation()));
    }
    return JState.state(variant);
  }

  /**
   * 返回水平角落方向方块的方块状态。
   *
   * @param modelIdentifier 模型 id。
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  static JState stateForHorizontalCornerFacingBlock(@NotNull Identifier modelIdentifier) {
    JVariant variant = new JVariant();
    for (HorizontalCornerDirection direction : HorizontalCornerDirection.values()) {
      variant.put(
          "facing",
          direction,
          new JBlockModel(modelIdentifier).y(direction.asRotation() - 45));
    }
    return JState.state(variant);
  }

  static String slabOf(String string) {
    if (string.contains("road")) {
      return string.replaceFirst("road", "road_slab");
    } else {
      return string + "_slab";
    }
  }

  @Environment(EnvType.CLIENT)
  static JState composeStateForSlab(@NotNull JState stateForFull) {
    final List<JVariant> variants = ((JStateAccessor) (Object) stateForFull).getVariants();
    final List<JVariant> slabVariants = Lists.newArrayList();
    for (JVariant variant : variants) {
      final Map<String, JBlockModel> models = ((JVariantAccessor) (Object) variant).getModels();
      final JVariant slabVariant = new JVariant();
      for (Map.Entry<String, JBlockModel> entry : models.entrySet()) {
        final String key = entry.getKey();
        final JBlockModel value = entry.getValue();
        final Identifier model = ((JBlockModelAccessor) value).getModel();
        slabVariant
            .put(
                key.isEmpty() ? "type=bottom" : key + ",type=bottom",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(
                                new Identifier(model.getNamespace(), slabOf(model.getPath())))))
            .put(
                key.isEmpty() ? "type=top" : key + ",type=top",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(
                                new Identifier(
                                    model.getNamespace(), slabOf(model.getPath()) + "_top"))))
            .put(
                key.isEmpty() ? "type=double" : key + ",type=double",
                Util.make(
                    value.clone(),
                    jBlockModel ->
                        ((JBlockModelAccessor) jBlockModel)
                            .setModel(new Identifier(model.getNamespace(), (model.getPath())))));
      }
      slabVariants.add(slabVariant);
    }
    return JState.state(slabVariants.toArray(new JVariant[]{}));
  }

  static Identifier slabOf(Identifier identifier) {
    return new Identifier(identifier.getNamespace(), slabOf(identifier.getPath()));
  }


  /**
   * 该方块对应的方块状态定义文件。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。<br>
   * 若为 {@code null}，则不产生文件。
   *
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  @Nullable
  default JState getBlockStates() {
    return null;
  }

  /**
   * 该方块对应的方块模型。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。
   *
   * @return 方块模型。
   */
  @Environment(EnvType.CLIENT)
  @Nullable
  default JModel getBlockModel() {
    return null;
  }

  /**
   * 该方块对应的物品模型。默认直接继承其方块模型。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。
   *
   * @return 物品模型。
   */
  @Environment(EnvType.CLIENT)
  @Nullable
  default JModel getItemModel() {
    return new JModel().parent(getBlockModelIdentifier().toString());
  }

  /**
   * 该方块的战利品表。默认情况下则是最简单的战利品表。
   *
   * @return 战利品表。
   */
  @Nullable
  default JLootTable getLootTable() {
    return simpleLootTable(getItemIdentifier().toString());
  }

  default Identifier getItemIdentifier() {
    return Registry.ITEM.getId(((ItemConvertible) this).asItem());
  }

  /**
   * 简单的战利品表。
   *
   * @param name 掉落物名称，通常是方块id对应的字符串。
   * @return 战利品表。
   */
  static JLootTable simpleLootTable(String name) {
    return JLootTable.loot("minecraft:block")
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
   */
  static JLootTable simpleSlabLootTable(String name) {
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

  /**
   * 将该方块的方块状态文件写入运行时资源包。若 {@link #getBlockStates()} 返回 {@code null}，则不执行操作。
   */
  @Environment(EnvType.CLIENT)
  default void writeBlockStates(RuntimeResourcePack pack) {
    final JState blockStates = getBlockStates();
    if (blockStates != null) pack.addBlockState(blockStates, getIdentifier());
  }

  /**
   * 将该方块的方块模型文件写入运行时资源包。若 {@link #getBlockModel()} 返回 {@code null}，则不执行操作。<br>
   * 注意：有时候一个方块拥有多个方块模型，这时候就需要写多次。
   */
  @Environment(EnvType.CLIENT)
  default void writeBlockModel(RuntimeResourcePack pack) {
    final JModel blockModel = getBlockModel();
    if (blockModel != null) pack.addModel(blockModel, getBlockModelIdentifier());
  }

  /**
   * 将该方块的物品模型文件写入运行时资源包。若 {@link #getItemModel()} 返回 {@code null}，则不执行操作。
   */
  @Environment(EnvType.CLIENT)
  default void writeItemModel(RuntimeResourcePack pack) {
    final JModel itemModel = getItemModel();
    if (itemModel != null) pack.addModel(itemModel, getItemModelIdentifier());
  }

  /**
   * 将该方块的战利品表写入运行时资源包。若 {@link #getLootTable()} 返回 {@code null}，则不执行操作。
   */
  default void writeLootTable(RuntimeResourcePack pack) {
    final JLootTable lootTable = getLootTable();
    if (lootTable != null) pack.addLootTable(((Block) this).getLootTableId(), lootTable);
  }

  /**
   * 获取方块的 id。如需获取对应物品 id，应使用 {@link #getItemIdentifier()}。
   *
   * @return 方块的 id。
   */
  default Identifier getIdentifier() {
    if (!(this instanceof Block)) {
      throw new RuntimeException("The 'getIdentifier' method can only be used for block!");
    }
    final Block block = (Block) this;
    return Registry.BLOCK.getId(block);
  }

  default Identifier getBlockModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return MishangUtils.identifierPrefix(identifier, "block/");
  }

  default Identifier getItemModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return MishangUtils.identifierPrefix(identifier, "item/");
  }
}
