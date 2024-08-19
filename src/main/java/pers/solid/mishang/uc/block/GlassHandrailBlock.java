package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.data.MishangucModels;
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

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = getTextures();
    final Identifier modelId = MishangucModels.GLASS_HANDRAIL.upload(this, textures, blockStateModelGenerator.modelCollector);
    MishangucModels.GLASS_HANDRAIL_INVENTORY.upload(ModelIds.getItemModelId(asItem()), textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
  }

  public static final TextureKey FRAME = TextureKey.of("frame");
  public static final TextureKey GLASS = TextureKey.of("glass");
  public static final TextureKey DECORATION = TextureKey.of("decoration");

  @Override
  public @NotNull TextureMap getTextures() {
    return new TextureMap().put(FRAME, frameTexture).put(GLASS, Mishanguc.id("block/glass_unframed")).put(DECORATION, decorationTexture);
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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier postModelId = MishangucModels.GLASS_HANDRAIL_POST.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier sideModelId = MishangucModels.GLASS_HANDRAIL_SIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier postSideModelId = MishangucModels.GLASS_HANDRAIL_POST_SIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(postModelId, postSideModelId, sideModelId));
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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_CORNER.upload(this, baseHandrail.getTextures(), blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier baseModelId = MishangucModels.createBlock("glass_handrail_stair_middle_center", FRAME, GLASS, DECORATION).upload(this, textures, blockStateModelGenerator.modelCollector);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          MishangucModels.createBlock(String.format("glass_handrail_stair_%s_%s", shape.asString(), position.asString()), "_" + shape.asString() + "_" + position.asString(), FRAME, GLASS, DECORATION).upload(this, textures, blockStateModelGenerator.modelCollector);
        }
      }
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(baseModelId));
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

    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_OUTER.upload(this, baseHandrail.getTextures(), blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
    }
  }
}
