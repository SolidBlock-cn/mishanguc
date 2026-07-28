package pers.solid.mishang.uc.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.BiFunction;
import java.util.function.Function;

@ApiStatus.AvailableSince("0.2.4")
public class GlassHandrailBlock extends HandrailBlock {
  public static final MapCodec<GlassHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(GlassHandrailBlock::baseBlock), createSettingsCodec()).apply(instance, (block, settings1) -> new GlassHandrailBlock(block, settings1, null, null, Registries.BLOCK.getId(block), false)));
  public final Identifier decorationTexture;
  private final CentralBlock central;
  private final CornerBlock corner;
  private final StairBlock stair;
  private final OuterBlock outer;
  private final Block baseBlock;
  private final Identifier frameTexture;

  public GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture, Identifier identifier) {
    this(baseBlock, settings, frameTexture, decorationTexture, identifier, true);
  }

  protected GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture, Identifier identifier, boolean createAffiliateBlocks) {
    super(settings.nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier)));
    this.baseBlock = baseBlock;
    this.frameTexture = frameTexture == null ? null : Identifier.of(frameTexture);
    this.decorationTexture = decorationTexture == null ? null : Identifier.of(decorationTexture);
    this.central = createAffiliateBlocks ? new CentralBlock(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_central")))) : null;
    this.corner = createAffiliateBlocks ? new CornerBlock(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_corner")))) : null;
    this.stair = createAffiliateBlocks ? new StairBlock(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_stair")))) : null;
    this.outer = createAffiliateBlocks ? new OuterBlock(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_outer")))) : null;
  }

  protected GlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture, BiFunction<GlassHandrailBlock, Settings, CentralBlock> centralProvider, BiFunction<GlassHandrailBlock, Settings, CornerBlock> cornerProvider, BiFunction<GlassHandrailBlock, Settings, StairBlock> stairProvider, BiFunction<GlassHandrailBlock, Settings, OuterBlock> outerProvider, Identifier identifier) {
    super(settings.nonOpaque().registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier)));
    this.baseBlock = baseBlock;
    this.frameTexture = Identifier.of(frameTexture);
    this.decorationTexture = Identifier.of(decorationTexture);
    central = centralProvider.apply(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_central"))));
    corner = cornerProvider.apply(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_corner"))));
    stair = stairProvider.apply(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_stair"))));
    outer = outerProvider.apply(this, Settings.copy(this).registryKey(RegistryKey.of(RegistryKeys.BLOCK, identifier.withSuffixedPath("_outer"))));
  }

  protected static <B extends Block> MapCodec<B> createSubCodec(Function<B, GlassHandrailBlock> baseGetter, BiFunction<GlassHandrailBlock, Settings, B> function) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_rail").flatXmap(block -> block instanceof GlassHandrailBlock glassHandrailBlock ? DataResult.success(glassHandrailBlock) : DataResult.error(() -> block + " not instance of " + GlassHandrailBlock.class.getName()), DataResult::success).forGetter(baseGetter), createSettingsCodec()).apply(instance, function));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = getTextures();
    final Identifier modelId = MishangucModels.GLASS_HANDRAIL.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier itemModelId = MishangucModels.GLASS_HANDRAIL_INVENTORY.upload(ModelIds.getItemModelId(asItem()), textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.tinted(itemModelId, ColoredTintSource.INSTANCE));
    } else {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.basic(itemModelId));
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull TextureMap getTextures() {
    return new TextureMap().put(TextureKeys.FRAME, frameTexture).put(TextureKeys.GLASS, Mishanguc.id("block/glass_unframed")).put(TextureKeys.DECORATION, decorationTexture);
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

  @Override
  protected MapCodec<? extends GlassHandrailBlock> getCodec() {
    return CODEC;
  }

  @Environment(EnvType.CLIENT)
  public static class TextureKeys {
    public static final TextureKey FRAME = TextureKey.of("frame");
    public static final TextureKey GLASS = TextureKey.of("glass");
    public static final TextureKey DECORATION = TextureKey.of("decoration");

    private TextureKeys() {
    }
  }

  public static class CentralBlock extends HandrailCentralBlock<GlassHandrailBlock> {
    public static final MapCodec<CentralBlock> CODEC = createSubCodec(b -> b.baseHandrail, CentralBlock::new);

    protected CentralBlock(@NotNull GlassHandrailBlock baseRail, Settings settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_central", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier postModelId = MishangucModels.GLASS_HANDRAIL_POST.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier sideModelId = MishangucModels.GLASS_HANDRAIL_SIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
      final Identifier postSideModelId = MishangucModels.GLASS_HANDRAIL_POST_SIDE.upload(this, textures, blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(postModelId, postSideModelId, sideModelId));
    }

    @Override
    protected MapCodec<? extends CentralBlock> getCodec() {
      return CODEC;
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<GlassHandrailBlock> {
    public static final MapCodec<CornerBlock> CODEC = createSubCodec(b -> b.baseHandrail, CornerBlock::new);

    protected CornerBlock(@NotNull GlassHandrailBlock baseRail, Settings settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_corner", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_CORNER.upload(this, baseHandrail.getTextures(), blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
    }

    @Override
    protected MapCodec<? extends CornerBlock> getCodec() {
      return CODEC;
    }
  }

  public static class StairBlock extends HandrailStairBlock<GlassHandrailBlock> {
    public static final MapCodec<StairBlock> CODEC = createSubCodec(b -> b.baseHandrail, StairBlock::new);

    protected StairBlock(@NotNull GlassHandrailBlock baseRail, Settings settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_stair", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier baseModelId = MishangucModels.createBlock("glass_handrail_stair_middle_center", TextureKeys.FRAME, TextureKeys.GLASS, TextureKeys.DECORATION).upload(this, textures, blockStateModelGenerator.modelCollector);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          MishangucModels.createBlock(String.format("glass_handrail_stair_%s_%s", shape.asString(), position.asString()), "_" + shape.asString() + "_" + position.asString(), TextureKeys.FRAME, TextureKeys.GLASS, TextureKeys.DECORATION).upload(this, textures, blockStateModelGenerator.modelCollector);
        }
      }
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(baseModelId));
    }

    @Override
    protected MapCodec<? extends StairBlock> getCodec() {
      return CODEC;
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<GlassHandrailBlock> {
    public static final MapCodec<OuterBlock> CODEC = createSubCodec(b -> b.baseHandrail, OuterBlock::new);

    protected OuterBlock(@NotNull GlassHandrailBlock baseRail, Settings settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableText getName() {
      return TextBridge.translatable("block.mishanguc.handrail_outer", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_OUTER.upload(this, baseHandrail.getTextures(), blockStateModelGenerator.modelCollector);
      blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
    }

    @Override
    protected MapCodec<? extends OuterBlock> getCodec() {
      return CODEC;
    }
  }
}
