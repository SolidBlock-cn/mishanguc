package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ModelProvider;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
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

public class GlowingHungSignBlock extends HungSignBlock {
  public static final MapCodec<GlowingHungSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), createSettingsCodec()).apply(instance, GlowingHungSignBlock::new));
  @ApiStatus.AvailableSince("0.1.7")
  protected static final Identifier DEFAULT_GLOW_TEXTURE = Mishanguc.id("block/white_light");
  public Identifier glowTexture;

  public GlowingHungSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings.luminance(s -> 15));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.1.7")
  public GlowingHungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock).luminance(x -> 15));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.glowing_hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier texture = getBaseTexture();
    final TextureMap textures = TextureMap.texture(texture);
    if (barTexture != null) textures.put(MishangucTextureKeys.BAR, barTexture);
    if (textureTop != null) textures.put(MishangucTextureKeys.TEXTURE_TOP, textureTop);
    textures.put(MishangucTextureKeys.GLOW, glowTexture);

    final Identifier id = MishangucModels.GLOWING_HUNG_SIGN.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier bodyId = MishangucModels.GLOWING_HUNG_SIGN_BODY.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier topBarId = MishangucModels.HUNG_SIGN_TOP_BAR.upload(this, textures, blockStateModelGenerator.modelCollector);
    final Identifier topBarEdgeId = MishangucModels.HUNG_SIGN_TOP_BAR_EDGE.upload(this, textures, blockStateModelGenerator.modelCollector);

    blockStateModelGenerator.blockStateCollector.accept(createBlockStates(bodyId, topBarId, topBarEdgeId));
    blockStateModelGenerator.registerParentedItemModel(this, id);
  }


  private @Nullable String getRecipeGroup() {
    if (baseBlock instanceof ColoredBlock) return null;
    if (MishangUtils.isConcrete(baseBlock)) return "mishanguc:glowing_concrete_hung_sign";
    if (MishangUtils.isTerracotta(baseBlock)) return "mishanguc:glowing_terracotta_hung_sign";
    if (baseBlock == Blocks.BLUE_ICE || baseBlock == Blocks.PACKED_ICE) {
      return "mishanguc:glowing_ice_hung_sign";
    }
    return null;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 6)
        .patterns("-#-", "-#-", "-#-")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .criterionFromItem("has_base_block", baseBlock)
        .criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .setCustomRecipeCategory("signs")
        .group(getRecipeGroup());
  }

  @Override
  protected MapCodec<? extends GlowingHungSignBlock> getCodec() {
    return CODEC;
  }
}
