package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 发光的直立告示牌。
 */
public class GlowingStandingSignBlock extends StandingSignBlock {
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  public String glowTexture = DEFAULT_GLOW_TEXTURE;

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

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    final String texture = getBaseTexture();
    final JTextures textures = new JTextures().var("texture", texture).var("bar", barTexture).var("glow", glowTexture);
    return new JModel("mishanguc:block/glowing_standing_sign").textures(textures);
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("---", "###", " | ")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN).addKey("|", Items.STICK)
        .resultCount(4)
        .recipeCategory(getRecipeCategory());
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN);
    return recipe;
  }
}
