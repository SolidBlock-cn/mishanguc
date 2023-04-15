package pers.solid.mishang.uc.block;

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
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.HashMap;
import java.util.Map;

public class GlowingHungSignBlock extends HungSignBlock {
  @ApiStatus.AvailableSince("0.1.7")
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  public String glowTexture;

  public GlowingHungSignBlock(@Nullable Block baseBlock, FabricBlockSettings settings) {
    super(baseBlock, settings.luminance(15));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.1.7")
  public GlowingHungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.glowing_hung_sign", baseBlock.getName());
    }
    return super.getName();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final Identifier id = getBlockModelId();
    final Identifier texture = getBaseTexture();
    final Map<String, String> textures = new HashMap<>(3);
    textures.put("texture", texture == null ? null : texture.toString());
    textures.put("bar", barTexture == null ? null : barTexture.toString());
    textures.put("texture_top", textureTop == null ? null : textureTop.toString());
    pack.addModel(
        id,
        ModelJsonBuilder.create(new Identifier("mishanguc", "block/glowing_hung_sign"))
            .setTextures(textures));
    pack.addModel(
        id.brrp_suffixed("_body"),
        ModelJsonBuilder.create(new Identifier("mishanguc", "block/glowing_hung_sign_body"))
            .setTextures(textures));
    pack.addModel(
        id.brrp_suffixed("_top_bar"),
        ModelJsonBuilder.create(new Identifier("mishanguc", "block/hung_sign_top_bar"))
            .setTextures(textures));
    pack.addModel(
        id.brrp_suffixed("_top_bar_edge"),
        ModelJsonBuilder.create(new Identifier("mishanguc", "block/hung_sign_top_bar_edge"))
            .setTextures(textures));
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this, 6)
        .patterns("-#-", "-#-", "-#-")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .criterionFromItem("has_base_block", baseBlock).criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN)
        .setCustomRecipeCategory("signs");
  }
}
