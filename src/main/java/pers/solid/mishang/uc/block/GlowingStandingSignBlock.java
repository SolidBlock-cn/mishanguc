package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 发光的直立告示牌。
 */
public class GlowingStandingSignBlock extends StandingSignBlock {
  public static final MapCodec<GlowingStandingSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(baseBlockCodec(), createSettingsCodec()).apply(instance, GlowingStandingSignBlock::new));
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  public String glowTexture = DEFAULT_GLOW_TEXTURE;

  public GlowingStandingSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  public GlowingStandingSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, Block.Settings.copy(baseBlock).luminance(x -> 15));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) return TextBridge.translatable("block.mishanguc.glowing_standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    final Identifier texture = getBaseTexture();
    return ModelJsonBuilder.create(Identifier.of("mishanguc:block/glowing_standing_sign")).addTexture("texture", texture).addTexture("bar", barTexture).addTexture("glow", glowTexture);
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 4)
        .patterns("---", "###", " | ")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN).input('|', Items.STICK)
        .criterionFromItem("has_base_block", baseBlock)
        .criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .setCustomRecipeCategory("signs");
  }

  @Override
  protected MapCodec<? extends GlowingStandingSignBlock> getCodec() {
    return CODEC;
  }
}
