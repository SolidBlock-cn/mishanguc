package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.TextBridge;

public class GlowingWallSignBlock extends WallSignBlock {
  public static final MapCodec<GlowingWallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, GlowingWallSignBlock::new));
  @ApiStatus.AvailableSince("0.1.7")
  protected static final Identifier DEFAULT_GLOW_TEXTURE = Mishanguc.id("block/white_light");
  /**
   * 告示牌发光部分的纹理。默认为 {@link #DEFAULT_GLOW_TEXTURE}。
   */
  public @Nullable Identifier glowTexture = DEFAULT_GLOW_TEXTURE;

  public GlowingWallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public GlowingWallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  public MutableText getName() {
    return TextBridge.translatable("block.mishanguc.glowing_wall_sign", baseBlock.getName());
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:glowing_concrete_wall_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:glowing_terracotta_wall_sign";
    if (baseBlock == Blocks.BLUE_ICE || baseBlock == Blocks.PACKED_ICE) {
      return "mishanguc:glowing_ice_wall_sign";
    }
    return null;
  }

  @Override
  public @Nullable CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this, 6)
        .pattern("---")
        .pattern("###")
        .pattern("---")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(baseBlock)).criterion("has_sign", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.texture(getBaseTexture()).put(MishangucTextureKeys.GLOW, glowTexture);
    final Identifier modelId = MishangucModels.GLOWING_WALL_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId));
    blockStateModelGenerator.registerParentedItemModel(this, modelId);
  }

  @Override
  protected MapCodec<? extends GlowingWallSignBlock> getCodec() {
    return CODEC;
  }
}
