package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.arrp.ARRPGenerator;

/**
 * 和 {@link Block} 基本类似，可以搭配 ARRP 生成数据。ARRP 生成该方块的数据时，将其视为完整的方块。其中，纹理变量默认为方块id对应的纹理，但也可以指定不同。
 */
@ApiStatus.AvailableSince("0.1.7")
public class CubeAllBlock extends Block implements ARRPGenerator {
  /**
   * 方块各面的纹理。默认为 {@code null}，这种情况下，会根据方块id来获取其纹理。如果方块还没有注册，则获取纹理就会失败。<br>
   * 注意：该字段没有注解为 {@code Environment(EnvType.CLIENT)} 是因为编译时可能存在问题，实际上在客户端环境中不应该使用该值。
   */
  public final @Nullable String texture;

  public CubeAllBlock(Settings settings) {
    this(null, settings);
  }

  public CubeAllBlock(@Nullable String texture, Settings settings) {
    super(settings);
    this.texture = texture;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JState getBlockStates() {
    return ARRPGenerator.simpleState(getBlockModelIdentifier());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return JModel.model("block/cube_all").textures(new JTextures().var("all", getAllTexture()));
  }

  /**
   * @return 方块各面使用的纹理，即资源包中的纹理 #all 变量。
   */
  @Environment(EnvType.CLIENT)
  @NotNull
  public String getAllTexture() {
    if (texture == null) {
      return getBlockModelIdentifier().toString();
    } else {
      return texture;
    }
  }
}
