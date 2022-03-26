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
    super(settings);
    this.baseBlock = baseBlock;
    this.central = new CentralBlock(this);
    this.corner = new CornerBlock(this);
    this.stair = new StairBlock(this);
  }

  public SimpleHandrailBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return JModel.model(new Identifier("mishanguc", "block/simple_handrail")).textures(new JTextures().var("texture", getTexture()).var("top", top).var("bottom", bottom));
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
  public @Nullable Block baseBlock() {
    return baseBlock;
  }

  @Environment(EnvType.CLIENT)
  protected String getTexture() {
    return texture == null ? MishangUtils.identifierPrefix(Registry.BLOCK.getId(baseBlock), "block/").toString() : texture;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return new TranslatableText("block.mishanguc.simple_handrail", baseBlock.getName());
    } else return super.getName();
  }

  public static class CentralBlock extends HandrailCentralBlock<SimpleHandrailBlock> {
    public CentralBlock(@NotNull SimpleHandrailBlock baseBlock) {
      super(baseBlock);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final JTextures textures = new JTextures()
          .var("texture", baseHandrail.getTexture())
          .var("top", baseHandrail.top)
          .var("bottom", baseHandrail.bottom);
      final Identifier modelId = getBlockModelIdentifier();
      pack.addModel(JModel.model(new Identifier("mishanguc", "block/simple_handrail_post")).textures(textures), MishangUtils.identifierSuffix(modelId, "_post"));
      pack.addModel(JModel.model(new Identifier("mishanguc", "block/simple_handrail_side")).textures(textures), MishangUtils.identifierSuffix(modelId, "_side"));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : new TranslatableText("block.mishanguc.simple_handrail_central", block.getName());
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<SimpleHandrailBlock> {
    public CornerBlock(@NotNull SimpleHandrailBlock baseHandrail) {
      super(baseHandrail);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable JModel getBlockModel() {
      return baseHandrail.getBlockModel().parent("mishanguc:block/simple_handrail_corner");
    }

    @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : new TranslatableText("block.mishanguc.simple_handrail_corner", block.getName());
    }
  }

  public static class StairBlock extends HandrailStairBlock<SimpleHandrailBlock> {
    public StairBlock(@NotNull SimpleHandrailBlock baseRail) {
      super(baseRail);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final String texture = baseRail.texture == null ? MishangUtils.identifierPrefix(Registry.BLOCK.getId(baseRail.baseBlock), "block/").toString() : baseRail.texture;
      final JTextures textures = new JTextures().var("texture", texture).var("top", baseRail.top).var("bottom", baseRail.bottom);
      final Identifier modelIdentifier = getBlockModelIdentifier();
      pack.addModel(JModel.model(new Identifier("mishanguc", "block/simple_handrail_stair_middle_center")).textures(textures), modelIdentifier);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          pack.addModel(JModel.model(new Identifier("mishanguc", String.format("block/simple_handrail_stair_%s_%s", shape.asString(), position.asString()))).textures(textures), MishangUtils.identifierSuffix(modelIdentifier, "_" + shape.asString() + "_" + position.asString()));
        }
      }
    }

    @Environment(EnvType.CLIENT)
    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : new TranslatableText("block.mishanguc.simple_handrail_stair", block.getName());
    }
  }
}
