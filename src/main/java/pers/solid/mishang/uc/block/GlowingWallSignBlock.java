package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

public class GlowingWallSignBlock extends WallSignBlock {
  public static final MapCodec<GlowingWallSignBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createBaseBlockCodec(), createSettingsCodec()).apply(instance, GlowingWallSignBlock::new));
  @ApiStatus.AvailableSince("0.1.7")
  protected static final Identifier DEFAULT_GLOW_TEXTURE = new Identifier("mishanguc:block/white_light");
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

  @Override
  public @Nullable CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 6)
        .patterns("---", "###", "---")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .criterionFromItem("has_base_block", baseBlock).criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .setCustomRecipeCategory("signs");
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/glowing_wall_sign")).addTexture("texture", getBaseTexture()).addTexture("glow", glowTexture);
  }

  @Override
  protected MapCodec<? extends GlowingWallSignBlock> getCodec() {
    return CODEC;
  }
}
