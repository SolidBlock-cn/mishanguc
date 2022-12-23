package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

@ApiStatus.AvailableSince("0.2.4")
public class GlassHandrailBlock extends HandrailBlock {
  public final String decorationTexture;
  private final CentralBlock central;
  private final CornerBlock corner;
  private final StairBlock stair;
  private final OuterBlock outer;
  private final Block baseBlock;
  private final String frameTexture;

  public GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture) {
    super(settings);
    this.baseBlock = baseBlock;
    this.frameTexture = frameTexture;
    this.decorationTexture = decorationTexture;
    this.central = new CentralBlock(this);
    this.corner = new CornerBlock(this);
    this.stair = new StairBlock(this);
    this.outer = new OuterBlock(this);
  }

  protected GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture, Function<GlassHandrailBlock, CentralBlock> centralProvider, Function<GlassHandrailBlock, CornerBlock> cornerProvider, Function<GlassHandrailBlock, StairBlock> stairProvider, Function<GlassHandrailBlock, OuterBlock> outerProvider) {
    super(settings.nonOpaque());
    this.baseBlock = baseBlock;
    this.frameTexture = frameTexture;
    this.decorationTexture = decorationTexture;
    central = centralProvider.apply(this);
    corner = cornerProvider.apply(this);
    stair = stairProvider.apply(this);
    outer = outerProvider.apply(this);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel("mishanguc:block/glass_handrail").textures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return new JModel().parent("mishanguc:block/glass_handrail_inventory").textures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JTextures getTextures() {
    return new JTextures().var("frame", frameTexture).var("glass", "mishanguc:block/glass_unframed").var("decoration", decorationTexture);
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
      final JTextures textures = baseHandrail.getTextures();
      final Identifier modelId = getBlockModelId();
      pack.addModel(new JModel(new Identifier("mishanguc", "block/glass_handrail_post")).textures(textures), modelId.brrp_append("_post"));
      pack.addModel(new JModel(new Identifier("mishanguc", "block/glass_handrail_post_side")).textures(textures), modelId.brrp_append("_post_side"));
      pack.addModel(new JModel(new Identifier("mishanguc", "block/glass_handrail_side")).textures(textures), modelId.brrp_append("_side"));
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
    public @NotNull JModel getBlockModel() {
      return baseHandrail.getBlockModel().parent("mishanguc:block/glass_handrail_corner");
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
      final JTextures textures = baseHandrail.getTextures();
      final Identifier modelIdentifier = getBlockModelId();
      pack.addModel(new JModel(new Identifier("mishanguc", "block/glass_handrail_stair_middle_center")).textures(textures), modelIdentifier);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          pack.addModel(new JModel(new Identifier("mishanguc", String.format("block/glass_handrail_stair_%s_%s", shape.asString(), position.asString()))).textures(textures), modelIdentifier.brrp_append("_" + shape.asString() + "_" + position.asString()));
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
    public @NotNull JModel getBlockModel() {
      return baseHandrail.getBlockModel().parent("mishanguc:block/glass_handrail_outer");
    }
  }
}
