package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.text.MutableText;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.brrp.v1.util.RecipeJsonFactory;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.util.TextBridge;

public class GlowingWallSignBlock extends WallSignBlock {
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

  @ApiStatus.AvailableSince("0.2.4, 1.16")
  public GlowingWallSignBlock(@NotNull Block baseBlock, Tag<Item> tag) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).breakByTool(tag).luminance(15));
  }

  @ApiStatus.AvailableSince("0.2.4, 1.16")
  public GlowingWallSignBlock(@NotNull Block baseBlock, Tag<Item> tag, int level) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock).breakByTool(tag, level).luminance(15));
  }

  // 不要注解为 @Environment(EnvType.CLIENT)
  @Override
  public MutableText getName() {
    return TextBridge.translatable("block.mishanguc.glowing_wall_sign", MishangUtils.getBlockName(baseBlock));
  }

  @Override
  public @Nullable RecipeJsonFactory getCraftingRecipe() {
    if (baseBlock == null) return null;
    return ShapedRecipeJsonFactory.create(this, 6)
        .patterns("---", "###", "---")
        .input('#', baseBlock).input('-', WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
        .criterionFromItem("has_base_block", baseBlock).criterionFromItem("has_sign", WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)::offerTo;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/glowing_wall_sign")).addTexture("texture", getBaseTexture()).addTexture("glow", glowTexture);
  }
}
