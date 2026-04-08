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
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.data.MishangucModels;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 简单的栏杆方块。基本上都是采用相同的纹理，如有使用也可以采用不同的纹理。其形状都是最基本的图形。
 */
public class SimpleHandrailBlock extends HandrailBlock {
  public static final MapCodec<SimpleHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(b -> b.baseBlock), propertiesCodec()).apply(instance, (block, settings1) -> new SimpleHandrailBlock(block, settings1, BuiltInRegistries.BLOCK.getKey(block), false)));
  /**
   * 该栏杆的基础方块。
   */
  public final @Nullable Block baseBlock;

  /**
   * 该方块对应的中间位置版本。
   */
  public final @Nullable CentralBlock central;
  /**
   * 该方块对应的角落位置版本。
   */
  public final @Nullable CornerBlock corner;
  /**
   * 该方块对应的楼梯扶手方块。
   */
  public final @Nullable StairBlock stair;
  /**
   * 该方块对应的外角方块。
   */
  public final @Nullable OuterBlock outer;

  /**
   * 栏杆的纹理。若为 {@code null}，则默认根据 {@link #baseBlock} 推断纹理。
   */
  public @Nullable Identifier texture;
  /**
   * 栏杆顶部部分的纹理。
   */
  public @Nullable Material top;
  /**
   * 栏杆底部部分的纹理。
   */
  public @Nullable Material bottom;

  public SimpleHandrailBlock(@Nullable Block baseBlock, Properties settings, Identifier identifier) {
    this(baseBlock, settings, identifier, true);
  }

  protected SimpleHandrailBlock(@Nullable Block baseBlock, Properties settings, Identifier identifier, boolean createAffiliatedBlocks) {
    super(settings.noOcclusion().setId(ResourceKey.create(Registries.BLOCK, identifier)));
    this.baseBlock = baseBlock;
    this.central = createAffiliatedBlocks ? new CentralBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_central")))) : null;
    this.corner = createAffiliatedBlocks ? new CornerBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_corner")))) : null;
    this.stair = createAffiliatedBlocks ? new StairBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_stair")))) : null;
    this.outer = createAffiliatedBlocks ? new OuterBlock(this, Properties.ofFullCopy(this).setId(ResourceKey.create(Registries.BLOCK, identifier.withSuffix("_outer")))) : null;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final TextureMapping textures = getTextures();
    final Identifier modelId = MishangucModels.SIMPLE_HANDRAIL.create(this, textures, blockStateModelGenerator.modelOutput);
    final Identifier itemModelId = MishangucModels.SIMPLE_HANDRAIL_INVENTORY.create(ModelLocationUtils.getModelLocation(asItem()), textures, blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.plainModel(itemModelId));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public TextureMapping getTextures() {
    return TextureMapping.cube(getMaterial()).put(TextureSlot.TOP, top).put(TextureSlot.BOTTOM, bottom);
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
  protected Material getMaterial() {
    return texture == null ? TextureMapping.getBlockTexture(baseBlock) : new Material(texture);
  }

  @Override
  public MutableComponent getName() {
    if (baseBlock != null) {
      return Component.translatable("block.mishanguc.simple_handrail", baseBlock.getName());
    } else return super.getName();
  }

  protected static <B extends Block> MapCodec<B> createSubCodec(Function<B, SimpleHandrailBlock> baseGetter, BiFunction<SimpleHandrailBlock, Properties, B> function) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_rail").flatXmap(block -> block instanceof SimpleHandrailBlock simpleHandrailBlock ? DataResult.success(simpleHandrailBlock) : DataResult.error(() -> block + "not instance of SimpleHandrailBlock"), DataResult::success).forGetter(baseGetter), propertiesCodec()).apply(instance, function));
  }

  @Override
  protected MapCodec<? extends SimpleHandrailBlock> codec() {
    return CODEC;
  }

  public static class CentralBlock extends HandrailCentralBlock<SimpleHandrailBlock> {
    public static final MapCodec<CentralBlock> CODEC = createSubCodec(b -> b.baseHandrail, CentralBlock::new);

    public CentralBlock(SimpleHandrailBlock baseBlock, Properties settings) {
      super(baseBlock, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final Identifier postModelId = MishangucModels.SIMPLE_HANDRAIL_POST.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      final Identifier sideModelId = MishangucModels.SIMPLE_HANDRAIL_SIDE.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      final Identifier postSideModelId = MishangucModels.SIMPLE_HANDRAIL_POST_SIDE.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(postModelId, postSideModelId, sideModelId));
    }

    @Override
    public MutableComponent getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : Component.translatable("block.mishanguc.simple_handrail_central", block.getName());
    }

    @Override
    protected MapCodec<? extends CentralBlock> codec() {
      return CODEC;
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<SimpleHandrailBlock> {
    public static final MapCodec<CornerBlock> CODEC = createSubCodec(b -> b.baseHandrail, CornerBlock::new);

    public CornerBlock(SimpleHandrailBlock baseHandrail, Properties settings) {
      super(baseHandrail, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.SIMPLE_HANDRAIL_CORNER.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    }

    @Override
    public MutableComponent getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : Component.translatable("block.mishanguc.simple_handrail_corner", block.getName());
    }

    @Override
    protected MapCodec<? extends CornerBlock> codec() {
      return CODEC;
    }
  }

  public static class StairBlock extends HandrailStairBlock<SimpleHandrailBlock> {
    public static final MapCodec<StairBlock> CODEC = createSubCodec(b -> b.baseHandrail, StairBlock::new);

    public StairBlock(SimpleHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final TextureMapping textures = baseHandrail.getTextures();
      final Identifier baseModelId = MishangucModels.createBlock("simple_handrail_stair_middle_center", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM).create(this, textures, blockStateModelGenerator.modelOutput);
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          MishangucModels.createBlock(String.format("simple_handrail_stair_%s_%s", shape.getSerializedName(), position.getSerializedName()), "_" + shape.getSerializedName() + "_" + position.getSerializedName(), TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM).create(this, textures, blockStateModelGenerator.modelOutput);
        }
      }
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(baseModelId));
    }

    @Override
    public MutableComponent getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : Component.translatable("block.mishanguc.simple_handrail_stair", block.getName());
    }

    @Override
    protected MapCodec<? extends StairBlock> codec() {
      return CODEC;
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<SimpleHandrailBlock> {
    public static final MapCodec<OuterBlock> CODEC = createSubCodec(b -> b.baseHandrail, OuterBlock::new);

    public OuterBlock(SimpleHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
      final Identifier modelId = MishangucModels.SIMPLE_HANDRAIL_OUTER.create(this, baseHandrail.getTextures(), blockStateModelGenerator.modelOutput);
      blockStateModelGenerator.blockStateOutput.accept(createBlockStates(modelId));
    }

    @Override
    public MutableComponent getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : Component.translatable("block.mishanguc.simple_handrail_outer", block.getName());
    }

    @Override
    protected MapCodec<? extends OuterBlock> codec() {
      return CODEC;
    }
  }

  @Nullable
  private String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:simple_concrete_handrail";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:simple_terracotta_handrail";
    if (MishangUtils.isStained_glass(baseBlock)) return "mishanguc:simple_stained_glass_handrail";
    if (MishangUtils.isWood(baseBlock)) return "mishanguc:simple_wood_handrail";
    if (MishangUtils.isPlanks(baseBlock)) return "mishanguc:simple_plank_handrail";
    if (baseBlock == Blocks.ICE || baseBlock == Blocks.PACKED_ICE || baseBlock == Blocks.BLUE_ICE) {
      return "mishanguc:simple_ice_handrail";
    }
    return null;
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return SingleItemRecipeBuilder.stonecutting(Ingredient.of(baseBlock), RecipeCategory.DECORATIONS, this, 5)
        .unlockedBy(RecipeProvider.getHasName(baseBlock), recipeGenerator.has(baseBlock))
        .group(getRecipeGroup());
  }
}
