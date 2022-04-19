package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * 此接口用于表示该方块（通常是方块）可以生成对应的运行时的资源包和数据包。<br>
 * 实现此方法，然后你就可以在 {@link ARRPMain} 中使用其对应的功能。
 */
@Deprecated(forRemoval = true)
@ApiStatus.AvailableSince("0.1.7")
@ApiStatus.ScheduledForRemoval(inVersion = "0.2.0")
public interface ARRPGenerator {
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
    return JLootTable.simple(getItemIdentifier().toString());
  }

  default Identifier getItemIdentifier() {
    return Registry.ITEM.getId(((ItemConvertible) this).asItem());
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
    if (!(this instanceof final Block block)) {
      throw new RuntimeException("The 'getIdentifier' method can only be used for block!");
    }
    return Registry.BLOCK.getId(block);
  }

  default Identifier getBlockModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return identifier.brrp_prepend("block/");
  }

  default Identifier getItemModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return identifier.brrp_prepend("item/");
  }
}
