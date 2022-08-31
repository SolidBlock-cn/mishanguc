package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

public class GlowingWallSignBlock extends WallSignBlock {
  @ApiStatus.AvailableSince("0.1.7")
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  /**
   * 告示牌发光部分的纹理。默认为 {@link #DEFAULT_GLOW_TEXTURE}。
   */
  public @Nullable String glowTexture;

  public GlowingWallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  public GlowingWallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  public MutableText getName() {
    return TextBridge.translatable("block.mishanguc.glowing_wall_sign", baseBlock.getName());
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("---", "###", "---")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .resultCount(6);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
    return recipe;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @Nullable JModel getBlockModel() {
    return new JModel("mishanguc:block/glowing_wall_sign").textures(new JTextures().var("texture", getBaseTexture()).var("glow", glowTexture));
  }
}
