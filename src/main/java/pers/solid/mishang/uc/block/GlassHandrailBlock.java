package pers.solid.mishang.uc.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.function.BiFunction;
import java.util.function.Function;

@ApiStatus.AvailableSince("0.2.4")
public class GlassHandrailBlock extends HandrailBlock {
  public static final MapCodec<GlassHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(GlassHandrailBlock::baseBlock), propertiesCodec()).apply(instance, (block, settings1) -> new GlassHandrailBlock(block, settings1, null, null, BuiltInRegistries.BLOCK.getKey(block), false)));
  public final Identifier decorationTexture;
  private final CentralBlock central;
  private final CornerBlock corner;
  private final StairBlock stair;
  private final OuterBlock outer;
  private final Block baseBlock;
  private final Identifier frameTexture;

  public GlassHandrailBlock(Block baseBlock, Properties settings, String frameTexture, String decorationTexture, Identifier identifier) {
    this(baseBlock, settings, frameTexture, decorationTexture, identifier, true);
  }

  protected GlassHandrailBlock(Block baseBlock, Properties settings, String frameTexture, String decorationTexture, Identifier identifier, boolean createAffiliateBlocks) {
    super(settings.noOcclusion().setId(ResourceKey.create(Registries.BLOCK, identifier)));
    this.baseBlock = baseBlock;
    this.frameTexture = frameTexture == null ? null : Identifier.parse(frameTexture);
    this.decorationTexture = decorationTexture == null ? null : Identifier.parse(decorationTexture);
    this.central = createAffiliateBlocks ? new CentralBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_central")))) : null;
    this.corner = createAffiliateBlocks ? new CornerBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_corner")))) : null;
    this.stair = createAffiliateBlocks ? new StairBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_stair")))) : null;
    this.outer = createAffiliateBlocks ? new OuterBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_outer")))) : null;
  }

  protected GlassHandrailBlock(Block baseBlock, Properties settings, String frameTexture, String decorationTexture, BiFunction<GlassHandrailBlock, Properties, CentralBlock> centralProvider, BiFunction<GlassHandrailBlock, Properties, CornerBlock> cornerProvider, BiFunction<GlassHandrailBlock, Properties, StairBlock> stairProvider, BiFunction<GlassHandrailBlock, Properties, OuterBlock> outerProvider, Identifier identifier) {
    super(settings.noOcclusion().setId(ResourceKey.create(Registries.BLOCK, identifier)));
    this.baseBlock = baseBlock;
    this.frameTexture = Identifier.parse(frameTexture);
    this.decorationTexture = Identifier.parse(decorationTexture);
    central = centralProvider.apply(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_central"))));
    corner = cornerProvider.apply(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_corner"))));
    stair = stairProvider.apply(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_stair"))));
    outer = outerProvider.apply(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_outer"))));
  }

  protected static <B extends Block> MapCodec<B> createSubCodec(Function<B, GlassHandrailBlock> baseGetter, BiFunction<GlassHandrailBlock, Properties, B> function) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_rail").flatXmap(block -> block instanceof GlassHandrailBlock glassHandrailBlock ? DataResult.success(glassHandrailBlock) : DataResult.error(() -> block + " not instance of " + GlassHandrailBlock.class.getName()), DataResult::success).forGetter(baseGetter), propertiesCodec()).apply(instance, function));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = getTextures();
    final Identifier modelId = MishangucModels.GLASS_HANDRAIL.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier itemModelId = MishangucModels.GLASS_HANDRAIL_INVENTORY.create(ModelLocationUtils.getModelLocation(asItem()), textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    if (this instanceof ColoredBlock) {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(itemModelId, ColoredTintSource.INSTANCE));
    } else {
      blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.plainModel(itemModelId));
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public TextureMapping getTextures() {
    return new TextureMapping().put(TextureKeys.FRAME, new Material(frameTexture)).put(TextureKeys.GLASS, new Material(Mishanguc.id("block/glass_unframed"))).put(TextureKeys.DECORATION, new Material(decorationTexture));
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
  protected MapCodec<? extends GlassHandrailBlock> codec() {
    return CODEC;
  }

  @Environment(EnvType.CLIENT)
  public static class TextureKeys {
    public static final TextureSlot FRAME = TextureSlot.create("frame");
    public static final TextureSlot GLASS = TextureSlot.create("glass");
    public static final TextureSlot DECORATION = TextureSlot.create("decoration");

    private TextureKeys() {
    }
  }

  public static class CentralBlock extends HandrailCentralBlock<GlassHandrailBlock> {
    public static final MapCodec<CentralBlock> CODEC = createSubCodec(b -> b.baseHandrail, CentralBlock::new);

    protected CentralBlock(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableComponent getName() {
      return Component.translatable("block.mishanguc.handrail_central", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = baseHandrail.getTextures();
      final Identifier postModelId = MishangucModels.GLASS_HANDRAIL_POST.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier sideModelId = MishangucModels.GLASS_HANDRAIL_SIDE.create(this, textures, blockStateModelGenerator.modelOutput);
      final Identifier postSideModelId = MishangucModels.GLASS_HANDRAIL_POST_SIDE.create(this, textures, blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(postModelId, postSideModelId, sideModelId));
    }

    @Override
    protected MapCodec<? extends CentralBlock> codec() {
      return CODEC;
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<GlassHandrailBlock> {
    public static final MapCodec<CornerBlock> CODEC = createSubCodec(b -> b.baseHandrail, CornerBlock::new);

    protected CornerBlock(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableComponent getName() {
      return Component.translatable("block.mishanguc.handrail_corner", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_CORNER.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    }

    @Override
    protected MapCodec<? extends CornerBlock> codec() {
      return CODEC;
    }
  }

  public static class StairBlock extends HandrailStairBlock<GlassHandrailBlock> {
    public static final MapCodec<StairBlock> CODEC = createSubCodec(b -> b.baseHandrail, StairBlock::new);

    protected StairBlock(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableComponent getName() {
      return Component.translatable("block.mishanguc.handrail_stair", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = baseHandrail.getTextures();
      final Identifier baseModelId = MishangucModels.createBlock("glass_handrail_stair_middle_center", TextureKeys.FRAME, TextureKeys.GLASS, TextureKeys.DECORATION).create(this, textures, blockStateModelGenerator.modelOutput);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          MishangucModels.createBlock(String.format("glass_handrail_stair_%s_%s", shape.getSerializedName(), position.getSerializedName()), "_" + shape.getSerializedName() + "_" + position.getSerializedName(), TextureKeys.FRAME, TextureKeys.GLASS, TextureKeys.DECORATION).create(this, textures, blockStateModelGenerator.modelOutput);
        }
      }
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(baseModelId));
    }

    @Override
    protected MapCodec<? extends StairBlock> codec() {
      return CODEC;
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<GlassHandrailBlock> {
    public static final MapCodec<OuterBlock> CODEC = createSubCodec(b -> b.baseHandrail, OuterBlock::new);

    protected OuterBlock(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public MutableComponent getName() {
      return Component.translatable("block.mishanguc.handrail_outer", baseHandrail.getName());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.GLASS_HANDRAIL_OUTER.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    }

    @Override
    protected MapCodec<? extends OuterBlock> codec() {
      return CODEC;
    }
  }
}
