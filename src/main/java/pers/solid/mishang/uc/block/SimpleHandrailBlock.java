package pers.solid.mishang.uc.block;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Function;

/**
 * 简单的栏杆方块。基本上都是采用相同的纹理，如有使用也可以采用不同的纹理。其形状都是最基本的图形。
 */
public class SimpleHandrailBlock extends HandrailBlock {
  public static final MapCodec<SimpleHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(b -> b.baseBlock), createSettingsCodec()).apply(instance, (block, settings1) -> new SimpleHandrailBlock(block, settings1, false)));
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
  public @Nullable Identifier texture;
  /**
   * 栏杆顶部部分的纹理。
   */
  public @Nullable Identifier top;
  /**
   * 栏杆底部部分的纹理。
   */
  public @Nullable Identifier bottom;

  public SimpleHandrailBlock(@Nullable Block baseBlock, Settings settings) {
    this(baseBlock, settings, true);
  }

  private SimpleHandrailBlock(@Nullable Block baseBlock, Settings settings, boolean createAffiliatedBlocks) {
    super(settings.nonOpaque());
    this.baseBlock = baseBlock;
    this.central = createAffiliatedBlocks ? new CentralBlock(this) : null;
    this.corner = createAffiliatedBlocks ? new CornerBlock(this) : null;
    this.stair = createAffiliatedBlocks ? new StairBlock(this) : null;
    this.outer = createAffiliatedBlocks ? new OuterBlock(this) : null;
  }

  public SimpleHandrailBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc", "block/simple_handrail")).setTextures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create("mishanguc", "block/simple_handrail_inventory").setTextures(getTextures());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull TextureMap getTextures() {
    return TextureMap.texture(getTexture()).put(TextureKey.TOP, top).put(TextureKey.BOTTOM, bottom);
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
  protected Identifier getTexture() {
    return texture == null ? Registries.BLOCK.getId(baseBlock).brrp_prefixed("block/") : texture;
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.simple_handrail", baseBlock.getName());
    } else return super.getName();
  }

  protected static <B extends Block> MapCodec<B> createSubCodec(Function<B, SimpleHandrailBlock> baseGetter, Function<SimpleHandrailBlock, B> function) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_rail").flatXmap(block -> block instanceof SimpleHandrailBlock simpleHandrailBlock ? DataResult.success(simpleHandrailBlock) : DataResult.error(() -> block + "not instance of SimpleHandrailBlock"), DataResult::success).forGetter(baseGetter)).apply(instance, function));
  }

  @Override
  protected MapCodec<? extends SimpleHandrailBlock> getCodec() {
    return CODEC;
  }

  public static class CentralBlock extends HandrailCentralBlock<SimpleHandrailBlock> {
    public static final MapCodec<CentralBlock> CODEC = createSubCodec(b -> b.baseHandrail, CentralBlock::new);

    public CentralBlock(@NotNull SimpleHandrailBlock baseBlock) {
      super(baseBlock, FabricBlockSettings.copyOf(baseBlock).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier modelId = getBlockModelId();
      pack.addModel(modelId.brrp_suffixed("_post"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/simple_handrail_post")).setTextures(textures));
      pack.addModel(modelId.brrp_suffixed("_post_side"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/simple_handrail_post_side")).setTextures(textures));
      pack.addModel(modelId.brrp_suffixed("_side"), ModelJsonBuilder.create(new Identifier("mishanguc", "block/simple_handrail_side")).setTextures(textures));
    }

    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : TextBridge.translatable("block.mishanguc.simple_handrail_central", block.getName());
    }

    @Override
    protected MapCodec<? extends CentralBlock> getCodec() {
      return CODEC;
    }
  }

  public static class CornerBlock extends HandrailCornerBlock<SimpleHandrailBlock> {
    public static final MapCodec<CornerBlock> CODEC = createSubCodec(b -> b.baseHandrail, CornerBlock::new);

    public CornerBlock(@NotNull SimpleHandrailBlock baseHandrail) {
      super(baseHandrail, FabricBlockSettings.copyOf(baseHandrail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public @Nullable ModelJsonBuilder getBlockModel() {
      return baseHandrail.getBlockModel().withParent(new Identifier("mishanguc:block/simple_handrail_corner"));
    }

    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : TextBridge.translatable("block.mishanguc.simple_handrail_corner", block.getName());
    }

    @Override
    protected MapCodec<? extends CornerBlock> getCodec() {
      return CODEC;
    }
  }

  public static class StairBlock extends HandrailStairBlock<SimpleHandrailBlock> {
    public static final MapCodec<StairBlock> CODEC = createSubCodec(b -> b.baseHandrail, StairBlock::new);

    public StairBlock(@NotNull SimpleHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      final TextureMap textures = baseHandrail.getTextures();
      final Identifier modelIdentifier = getBlockModelId();
      pack.addModel(modelIdentifier, ModelJsonBuilder.create(new Identifier("mishanguc", "block/simple_handrail_stair_middle_center")).setTextures(textures));
      for (Shape shape : Shape.values()) {
        for (Position position : Position.values()) {
          pack.addModel(modelIdentifier.brrp_suffixed("_" + shape.asString() + "_" + position.asString()), ModelJsonBuilder.create(new Identifier("mishanguc", String.format("block/simple_handrail_stair_%s_%s", shape.asString(), position.asString()))).setTextures(textures));
        }
      }
    }

    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : TextBridge.translatable("block.mishanguc.simple_handrail_stair", block.getName());
    }

    @Override
    protected MapCodec<? extends StairBlock> getCodec() {
      return CODEC;
    }
  }

  public static class OuterBlock extends HandrailOuterBlock<SimpleHandrailBlock> {
    public static final MapCodec<OuterBlock> CODEC = createSubCodec(b -> b.baseHandrail, OuterBlock::new);

    public OuterBlock(@NotNull SimpleHandrailBlock baseRail) {
      super(baseRail, FabricBlockSettings.copyOf(baseRail).nonOpaque());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void writeBlockModel(RuntimeResourcePack pack) {
      pack.addModel(getBlockModelId(), ModelJsonBuilder.create(new Identifier("mishanguc:block/simple_handrail_outer")).setTextures(baseHandrail.getTextures()));
    }

    @Override
    public MutableText getName() {
      final Block block = baseBlock();
      return block == null ? super.getName() : TextBridge.translatable("block.mishanguc.simple_handrail_outer", block.getName());
    }

    @Override
    protected MapCodec<? extends OuterBlock> getCodec() {
      return CODEC;
    }
  }

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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.ofItems(baseBlock), getRecipeCategory(), this, 5)
        .criterionFromItem(baseBlock)
        .setCustomRecipeCategory("handrails")
        .group(getRecipeGroup());
  }
}
