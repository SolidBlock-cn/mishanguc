package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

/**
 * 此接口用于表示该方块（通常是方块）可以生成对应的资源包和数据包。
 */
@ApiStatus.AvailableSince("0.1.7")
public interface ARRPGenerator {
  /**
   * 该方块对应的方块状态定义文件。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。
   *
   * @return 方块状态。
   */
  @Environment(EnvType.CLIENT)
  JState getBlockStates();

  /**
   * 该方块对应的方块模型。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。
   *
   * @return 方块模型。
   */
  @Environment(EnvType.CLIENT)
  JModel getBlockModel();

  /**
   * 该方块对应的物品模型。默认直接继承其方块模型。<br>
   * 本方法仅在客户端执行，覆盖此方法时必须注解为 {@code @Environment(EnvType.CLIENT)}。
   *
   * @return 物品模型。
   */
  @Environment(EnvType.CLIENT)
  default JModel getItemModel() {
    return new JModel().parent(getBlockModelIdentifier().toString());
  }

  default Identifier getIdentifier() {
    if (!(this instanceof Block)) {
      throw new RuntimeException("The 'getIdentifier' method can only be used for block!");
    }
    final Block block = (Block) this;
    return Registry.BLOCK.getId(block);
  }

  default Identifier getBlockModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return new Identifier(identifier.getNamespace(), "block/" + identifier.getPath());
  }

  default Identifier getItemModelIdentifier() {
    final Identifier identifier = getIdentifier();
    return new Identifier(identifier.getNamespace(), "item/" + identifier.getPath());
  }
}
