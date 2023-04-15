package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.data.client.model.Texture;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

@ApiStatus.AvailableSince("0.2.4")
public class GlassHandrailBlock extends HandrailBlock {
  public final Identifier decorationTexture;
  private final CentralBlock central;
  private final CornerBlock corner;
  private final StairBlock stair;
  private final OuterBlock outer;
  private final Block baseBlock;
  private final Identifier frameTexture;

  public GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture) {
    super(settings);
    this.baseBlock = baseBlock;
    this.frameTexture = new Identifier(frameTexture);
    this.decorationTexture = new Identifier(decorationTexture);
    this.central = new CentralBlock(this);
    this.corner = new CornerBlock(this);
    this.stair = new StairBlock(this);
    this.outer = new OuterBlock(this);
  }

  protected GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture, Function<GlassHandrailBlock, CentralBlock> centralProvider, Function<GlassHandrailBlock, CornerBlock> cornerProvider, Function<GlassHandrailBlock, StairBlock> stairProvider, Function<GlassHandrailBlock, OuterBlock> outerProvider) {
    super(settings.nonOpaque());
    this.baseBlock = baseBlock;
    this.frameTexture = new Identifier(frameTexture);
    this.decorationTexture = new Identifier(decorationTexture);
    central = centralProvider.apply(this);
    corner = cornerProvider.apply(this);
    stair = stairProvider.apply(this);
    outer = outerProvider.apply(this);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/glass_handrail")).setTextures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/glass_handrail_inventory")).setTextures(getTextures());
  }

  public static final TextureKey FRAME = TextureKey.of("frame");
  public static final TextureKey GLASS = TextureKey.of("glass");
  public static final TextureKey DECORATION = TextureKey.of("decoration");

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull Texture getTextures() {
    return new Texture().put(FRAME, frameTexture).put(GLASS, new Identifier("mishanguc:block/glass_unframed")).put(DECORATION, decorationTexture);
  }

  @Override
  public HandrailCentralBlock<? extends HandrailBlock> central() {
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

  public static class CentralBlock extends HandrailCentralBlock<GlassHandrailBlock> {

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_central", baseHandrail.getName());
    }

    protected CentralBlock(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final Texture textures = baseHandrail.getTextures();
      final Identifier modelId = getBlockModelId();
      pack.addModel(modelId.brrp_suffixed("_post"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/glass_handrail_post")).setTextures(textures));
      pack.addModel(modelId.brrp_suffixed("_post_side"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/glass_handrail_post_side")).setTextures(textures));
      pack.addModel(modelId.brrp_suffixed("_side"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/glass_handrail_side")).setTextures(textures));
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<GlassHandrailBlock> {

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_corner", baseHandrail.getName());
    }

    protected CornerBlock(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return baseHandrail.getBlockModel().parent(new Identifier("mishanguc:block/glass_handrail_corner"));
    }
  }

  public static class StairBlock extends HandrailStairBlock<GlassHandrailBlock> {

    protected StairBlock(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_stair", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final Texture textures = baseHandrail.getTextures();
      final Identifier modelIdentifier = getBlockModelId();
      pack.addModel(modelIdentifier, ModelJsonBuilder.create(new Identifier("mishanguc", "block/glass_handrail_stair_middle_center")).setTextures(textures));
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          pack.addModel(modelIdentifier.brrp_suffixed("_" + shape.asString() + "_" + position.asString()), ModelJsonBuilder.create(new Identifier("mishanguc", String.format("block/glass_handrail_stair_%s_%s", shape.asString(), position.asString()))).setTextures(textures));
        }
      }
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<GlassHandrailBlock> {

    protected OuterBlock(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_outer", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @NotNull ModelJsonBuilder getBlockModel() {
      return baseHandrail.getBlockModel().parent(new Identifier("mishanguc:block/glass_handrail_outer"));
    }
  }
}
