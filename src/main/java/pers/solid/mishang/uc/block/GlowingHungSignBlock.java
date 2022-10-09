package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

public class GlowingHungSignBlock extends HungSignBlock {
  @ApiStatus.AvailableSince("0.1.7")
  protected static final String DEFAULT_GLOW_TEXTURE = "mishanguc:block/white_light";
  public String glowTexture;

  public GlowingHungSignBlock(@Nullable Block baseBlock, FabricBlockSettings settings) {
    super(baseBlock, settings.luminance(15));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.2.4, 1.16")
  public GlowingHungSignBlock(@NotNull Block baseBlock, Tag<Item> tag) {
    super(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15).breakByTool(tag));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.2.4, 1.16")
  public GlowingHungSignBlock(@NotNull Block baseBlock, Tag<Item> tag, int level) {
    super(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15).breakByTool(tag, level));
    this.glowTexture = DEFAULT_GLOW_TEXTURE;
  }

  @ApiStatus.AvailableSince("0.1.7")
  public GlowingHungSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).luminance(15));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) {
      return TextBridge.translatable("block.mishanguc.glowing_hung_sign", MishangUtils.getBlockName(baseBlock));
    }
    return new TranslatableText(getTranslationKey());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final Identifier id = getBlockModelId();
    final String texture = getBaseTexture();
    final JTextures textures = new JTextures().var("texture", texture).var("glow", glowTexture).var("bar", barTexture).var("texture_top", textureTop);
    pack.addModel(
        new JModel(new Identifier("mishanguc", "block/glowing_hung_sign"))
            .textures(textures),
        new Identifier(id.getNamespace(), id.getPath()));
    pack.addModel(
        new JModel(new Identifier("mishanguc", "block/glowing_hung_sign_body"))
            .textures(textures),
        id.brrp_append("_body"));
    pack.addModel(
        new JModel(new Identifier("mishanguc", "block/hung_sign_top_bar"))
            .textures(textures),
        id.brrp_append("_top_bar"));
    pack.addModel(
        new JModel(new Identifier("mishanguc", "block/hung_sign_top_bar_edge"))
            .textures(textures),
        id.brrp_append("_top_bar_edge"));
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("-#-", "-#-", "-#-")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .resultCount(6);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
    return recipe;
  }
}
