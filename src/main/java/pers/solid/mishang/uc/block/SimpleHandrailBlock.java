package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 简单的栏杆方块。基本上都是采用相同的纹理，如有使用也可以采用不同的纹理。其形状都是最基本的图形。
 */
public class SimpleHandrailBlock extends HandrailBlock {
  /**
   * 该栏杆的基础方块。
   */
  public final @Nullable Block baseBlock;

  /**
   * 该方块对应的中间位置版本。
   */
  public final CentralBlock central;
  /**
   * 该方块对应的角落位置版本。
   */
  public final CornerBlock corner;
  /**
   * 该方块对应的楼梯扶手方块。
   */
  public final StairBlock stair;
  /**
   * 该方块对应的外角方块。
   */
  public final OuterBlock outer;

  /**
   * 栏杆的纹理。若为 {@code null}，则默认根据 {@link #baseBlock} 推断纹理。
   */
  public @Nullable String texture;
  /**
   * 栏杆顶部部分的纹理。
   */
  public @Nullable String top;
  /**
   * 栏杆底部部分的纹理。
   */
  public @Nullable String bottom;

  public SimpleHandrailBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings.nonOpaque());
    this.baseBlock = baseBlock;
    this.central = new CentralBlock(this);
    this.corner = new CornerBlock(this);
    this.stair = new StairBlock(this);
    this.outer = new OuterBlock(this);
  }

  public SimpleHandrailBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel(new Identifier("mishanguc", "block/simple_handrail")).textures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getItemModel() {
    return new JModel().parent("mishanguc:block/simple_handrail_inventory").textures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JTextures getTextures() {
    return new JTextures().var("texture", getTexture()).var("top", top).var("bottom", bottom);
  }

  @Override
  public final HandrailCentralBlock<SimpleHandrailBlock> central() {
    return central;
  }

  @Override
  public HandrailCornerBlock<? extends HandrailBlock> corner() {
    return corner;
  }

  @Override
  public HandrailStairBlock<? extends HandrailBlock> stair() {
    return stair;
  }

  @Override
  public HandrailOuterBlock<? extends HandrailBlock> outer() {
    return outer;
  }

  @Override
  public @Nullable Block baseBlock() {
    return baseBlock;
  }

  /**
   * @return 该方块的基础纹理变量。
   */
  @Environment(EnvType.CLIENT)
  protected String getTexture() {
    return texture == null ? Registry.BLOCK.getId(baseBlock).brrp_prepend("block/").toString() : texture;
  }

  // 不要注解为 @Environment(EnvType.CLIENT)
  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.simple_handrail", MishangUtils.getBlockName(baseBlock));
    } else return new TranslatableText(getTranslationKey());
  }

  public static class CentralBlock extends HandrailCentralBlock<SimpleHandrailBlock> {
    public CentralBlock(@NotNull SimpleHandrailBlock baseBlock) {
      super(baseBlock, FabricBlockSettings.copyOf(baseBlock).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final JTextures textures = baseHandrail.getTextures();
      final Identifier modelId = getBlockModelId();
      pack.addModel(new JModel(new Identifier("mishanguc", "block/simple_handrail_post")).textures(textures), modelId.brrp_append("_post"));
      pack.addModel(new JModel(new Identifier("mishanguc", "block/simple_handrail_post_side")).textures(textures), modelId.brrp_append("_post_side"));
      pack.addModel(new JModel(new Identifier("mishanguc", "block/simple_handrail_side")).textures(textures), modelId.brrp_append("_side"));
    }

    // 不要注解为 @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? new TranslatableText(getTranslationKey()) : TextBridge.translatable("block.mishanguc.simple_handrail_central", MishangUtils.getBlockName(block));
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<SimpleHandrailBlock> {
    public CornerBlock(@NotNull SimpleHandrailBlock baseHandrail) {
      super(baseHandrail, FabricBlockSettings.copyOf(baseHandrail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable JModel getBlockModel() {
      return baseHandrail.getBlockModel().parent("mishanguc:block/simple_handrail_corner");
    }

    // 不要注解为 @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? new TranslatableText(getTranslationKey()) : TextBridge.translatable("block.mishanguc.simple_handrail_corner", MishangUtils.getBlockName(block));
    }
  }

  public static class StairBlock extends HandrailStairBlock<SimpleHandrailBlock> {
    public StairBlock(@NotNull SimpleHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final JTextures textures = baseHandrail.getTextures();
      final Identifier modelIdentifier = getBlockModelId();
      pack.addModel(new JModel(new Identifier("mishanguc", "block/simple_handrail_stair_middle_center")).textures(textures), modelIdentifier);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          pack.addModel(new JModel(new Identifier("mishanguc", String.format("block/simple_handrail_stair_%s_%s", shape.asString(), position.asString()))).textures(textures), modelIdentifier.brrp_append("_" + shape.asString() + "_" + position.asString()));
        }
      }
    }

    // 不要注解为 @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? new TranslatableText(getTranslationKey()) : TextBridge.translatable("block.mishanguc.simple_handrail_stair", MishangUtils.getBlockName(block));
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<SimpleHandrailBlock> {
    public OuterBlock(@NotNull SimpleHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      pack.addModel(new JModel("mishanguc:block/simple_handrail_outer").textures(baseHandrail.getTextures()), getBlockModelId());
    }

    // 不要注解为 @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? new TranslatableText(getTranslationKey()) : TextBridge.translatable("block.mishanguc.simple_handrail_outer", MishangUtils.getBlockName(block));
    }
  }
}
