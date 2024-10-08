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
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.data.MishangucModels;
import pers.solid.mishang.uc.data.MishangucTextureKeys;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 发光的直立告示牌。
 */
public class GlowingStandingSignBlock extends StandingSignBlock {
  public static final MapCodec<GlowingStandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), createSettingsCodec()).apply(instance, GlowingStandingSignBlock::new));
  protected static final Identifier DEFAULT_GLOW_TEXTURE = Mishanguc.id("block/white_light");
  public Identifier glowTexture = DEFAULT_GLOW_TEXTURE;

  public GlowingStandingSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public GlowingStandingSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) return TextBridge.translatable("block.mishanguc.glowing_standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final TextureMap textures = TextureMap.texture(getBaseTexture()).put(MishangucTextureKeys.BAR, barTexture).put(MishangucTextureKeys.GLOW, glowTexture);
    final Identifier modelId = MishangucModels.GLOWING_STANDING_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier r1ModelId = MishangucModels.GLOWING_STANDING_SIGN_1.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier r2ModelId = MishangucModels.GLOWING_STANDING_SIGN_2.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier r3ModelId = MishangucModels.GLOWING_STANDING_SIGN_3.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier barredModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier barredR1ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_1.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier barredR2ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_2.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier barredR3ModelId = MishangucModels.GLOWING_STANDING_SIGN_BARRED_3.upload(this, textures, blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(modelId, r1ModelId, r2ModelId, r3ModelId, barredModelId, barredR1ModelId, barredR2ModelId, barredR3ModelId));
    blockStateModelGenerator.registerParentedItemModel(this, barredModelId);
  }

  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:glowing_concrete_standing_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:glowing_terracotta_standing_sign";
    if (baseBlock == Blocks.BLUE_ICE || baseBlock == Blocks.PACKED_ICE) {
      return "mishanguc:glowing_ice_standing_sign";
    }
    return null;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this, 4)
        .pattern("---")
        .pattern("###")
        .pattern(" | ")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN).input('|', Items.STICK)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(baseBlock))
        .criterion("has_sign", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN))
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends GlowingStandingSignBlock> getCodec() {
    return CODEC;
  }
}
