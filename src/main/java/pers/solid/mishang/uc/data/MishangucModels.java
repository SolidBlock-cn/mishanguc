package pers.solid.mishang.uc.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.GlassHandrailBlock;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public final class MishangucModels {
  public static final ModelTemplate LIGHT = createBlock("light", TextureSlot.ALL);
  public static final ModelTemplate LIGHT_SLAB = createBlock("light_slab", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);
  public static final ModelTemplate LIGHT_SLAB_TOP = createBlock("light_slab_top", "_top", TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.BOTTOM);

  public static final ModelTemplate GLASS_HANDRAIL = createBlock("glass_handrail", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_INVENTORY = createBlock("glass_handrail_inventory", "_inventory", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_POST = createBlock("glass_handrail_post", "_post", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_SIDE = createBlock("glass_handrail_side", "_side", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_POST_SIDE = createBlock("glass_handrail_post_side", "_post_side", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_CORNER = createBlock("glass_handrail_corner", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);
  public static final ModelTemplate GLASS_HANDRAIL_OUTER = createBlock("glass_handrail_outer", GlassHandrailBlock.TextureKeys.FRAME, GlassHandrailBlock.TextureKeys.GLASS, GlassHandrailBlock.TextureKeys.DECORATION);

  public static final ModelTemplate SIMPLE_HANDRAIL = createBlock("simple_handrail", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_INVENTORY = createBlock("simple_handrail_inventory", "_inventory", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_POST = createBlock("simple_handrail_post", "_post", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_SIDE = createBlock("simple_handrail_side", "_side", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_POST_SIDE = createBlock("simple_handrail_post_side", "_post_side", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_CORNER = createBlock("simple_handrail_corner", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);
  public static final ModelTemplate SIMPLE_HANDRAIL_OUTER = createBlock("simple_handrail_outer", TextureSlot.TEXTURE, TextureSlot.TOP, TextureSlot.BOTTOM);

  public static final ModelTemplate TEMPLATE_COLORED_GLASS_PANE_POST = createBlock("template_colored_glass_pane_post", "_post", TextureSlot.PANE, TextureSlot.EDGE);
  public static final ModelTemplate TEMPLATE_COLORED_GLASS_PANE_SIDE = createBlock("template_colored_glass_pane_side", "_side", TextureSlot.PANE, TextureSlot.EDGE);
  public static final ModelTemplate TEMPLATE_COLORED_GLASS_PANE_SIDE_ALT = createBlock("template_colored_glass_pane_side_alt", "_side_alt", TextureSlot.PANE, TextureSlot.EDGE);
  public static final ModelTemplate TEMPLATE_COLORED_GLASS_PANE_NOSIDE = createBlock("template_colored_glass_pane_noside", "_noside", TextureSlot.PANE, TextureSlot.EDGE);
  public static final ModelTemplate TEMPLATE_COLORED_GLASS_PANE_NOSIDE_ALT = createBlock("template_colored_glass_pane_noside_alt", "_noside_alt", TextureSlot.PANE, TextureSlot.EDGE);

  public static final ModelTemplate COLORED_CUBE_COLUMN = createBlock("colored_cube_column", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_CUBE_COLUMN_HORITONZAL = createBlock("colored_cube_column_horizontal", "_horizontal", TextureSlot.END, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_SLAB = createBlock("colored_slab", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_SLAB_TOP = createBlock("colored_slab_top", "_top", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_CUBE_BOTTOM_UP = createBlock("colored_cube_bottom_up", "_double", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);

  public static final ModelTemplate COLORED_STAIRS = createBlock("colored_stairs", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_INNER_STAIRS = createBlock("colored_inner_stairs", "_inner", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);
  public static final ModelTemplate COLORED_OUTER_STAIRS = createBlock("colored_outer_stairs", "_outer", TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.SIDE);

  public static final ModelTemplate ROAD_MARK = createBlock("road_mark", TextureSlot.TEXTURE);
  public static final ModelTemplate ROAD_MARK_ON_SLAB = createBlock("road_mark_on_slab", "_on_slab", TextureSlot.TEXTURE);
  public static final ModelTemplate ROAD_MARK_ROTATED = createBlock("road_mark_rotated", "_rotated", TextureSlot.TEXTURE);
  public static final ModelTemplate ROAD_MARK_ON_SLAB_ROTATED = createBlock("road_mark_on_slab_rotated", "_on_slab_rotated", TextureSlot.TEXTURE);

  public static final ModelTemplate WALL_SIGN = createBlock("wall_sign", TextureSlot.TEXTURE);
  public static final ModelTemplate FULL_WALL_SIGN = createBlock("full_wall_sign", TextureSlot.TEXTURE);
  public static final ModelTemplate GLOWING_WALL_SIGN = createBlock("glowing_wall_sign", TextureSlot.TEXTURE, MishangucTextureKeys.GLOW);

  public static final ModelTemplate STANDING_SIGN = createBlock("standing_sign", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_1 = createBlock("standing_sign_1", "_1", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_2 = createBlock("standing_sign_2", "_2", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_3 = createBlock("standing_sign_3", "_3", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_BARRED = createBlock("standing_sign_barred", "_barred", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_BARRED_1 = createBlock("standing_sign_barred_1", "_barred_1", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_BARRED_2 = createBlock("standing_sign_barred_2", "_barred_2", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate STANDING_SIGN_BARRED_3 = createBlock("standing_sign_barred_3", "_barred_3", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate GLOWING_STANDING_SIGN = createBlock("glowing_standing_sign", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_1 = createBlock("glowing_standing_sign_1", "_1", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_2 = createBlock("glowing_standing_sign_2", "_2", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_3 = createBlock("glowing_standing_sign_3", "_3", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_BARRED = createBlock("glowing_standing_sign_barred", "_barred", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_BARRED_1 = createBlock("glowing_standing_sign_barred_1", "_barred_1", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_BARRED_2 = createBlock("glowing_standing_sign_barred_2", "_barred_2", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_STANDING_SIGN_BARRED_3 = createBlock("glowing_standing_sign_barred_3", "_barred_3", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);

  public static final ModelTemplate HUNG_SIGN = createBlock("hung_sign", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.TEXTURE_TOP);
  public static final ModelTemplate HUNG_SIGN_BODY = createBlock("hung_sign_body", "_body", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.TEXTURE_TOP);
  public static final ModelTemplate HUNG_SIGN_TOP_BAR = createBlock("hung_sign_top_bar", "_top_bar", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate HUNG_SIGN_TOP_BAR_EDGE = createBlock("hung_sign_top_bar_edge", "_top_bar_edge", TextureSlot.TEXTURE, MishangucTextureKeys.BAR);
  public static final ModelTemplate GLOWING_HUNG_SIGN = createBlock("glowing_hung_sign", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final ModelTemplate GLOWING_HUNG_SIGN_BODY = createBlock("glowing_hung_sign_body", "_body", TextureSlot.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);

  public static final ModelTemplate HUNG_SIGN_BAR = createBlock("hung_sign_bar", TextureSlot.TEXTURE);
  public static final ModelTemplate HUNG_SIGN_BAR_CENTRAL = createBlock("hung_sign_bar_central", "_central", TextureSlot.TEXTURE);
  public static final ModelTemplate HUNG_SIGN_BAR_EDGE = createBlock("hung_sign_bar_edge", "_edge", TextureSlot.TEXTURE);
  /**
   * previously in {@link pers.solid.mishang.uc.block.ColoredCubeBlock}
   */
  public static final ModelTemplate COLORED_CUBE_ALL = createBlock("colored_cube_all", TextureSlot.ALL);
  /**
   * previously in {@link pers.solid.mishang.uc.block.ColoredCubeBlock}
   */
  public static final ModelTemplate COLORED_CUBE_BOTTOM_TOP = createBlock("colored_cube_bottom_top", TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.SIDE);
  /**
   * previously in {@link pers.solid.mishang.uc.block.ColoredCubeBlock}
   */
  public static final ModelTemplate COLORED_CUBE_MIRRORED_ALL = createBlock("colored_cube_mirrored_all", "_mirrored", TextureSlot.ALL);
  /**
   * previously in {@link pers.solid.mishang.uc.block.ColoredCubeBlock}
   */
  public static final ModelTemplate COLORED_CUBE_ALL_WITHOUT_SHADE = createBlock("colored_cube_all_without_shade", TextureSlot.ALL);


  public static Material material(String path) {
    return new Material(Mishanguc.id("block/" + path));
  }

  public static ModelTemplate createBlock(String name, TextureSlot... requiredTextureKeys) {
    return new ModelTemplate(Optional.of(Mishanguc.id("block/" + name)), Optional.empty(), requiredTextureKeys);
  }

  public static ModelTemplate createBlock(String name, String variant, TextureSlot... requiredTextureKeys) {
    return new ModelTemplate(Optional.of(Mishanguc.id("block/" + name)), Optional.of(variant), requiredTextureKeys);
  }
}
