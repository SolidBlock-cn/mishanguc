package pers.solid.mishang.uc.data;

import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.GlassHandrailBlock;

import java.util.Optional;

public final class MishangucModels {
  public static final Model LIGHT = createBlock("light", TextureKey.ALL);
  public static final Model LIGHT_SLAB = createBlock("light_slab", TextureKey.TOP, TextureKey.SIDE, TextureKey.END);
  public static final Model LIGHT_SLAB_TOP = createBlock("light_slab_top", "_top", TextureKey.TOP, TextureKey.SIDE, TextureKey.END);

  public static final Model GLASS_HANDRAIL = createBlock("glass_handrail", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_INVENTORY = createBlock("glass_handrail_inventory", "_inventory", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_POST = createBlock("glass_handrail_post", "_post", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_SIDE = createBlock("glass_handrail_side", "_side", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_POST_SIDE = createBlock("glass_handrail_post_side", "_post_side", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_CORNER = createBlock("glass_handrail_corner", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);
  public static final Model GLASS_HANDRAIL_OUTER = createBlock("glass_handrail_outer", GlassHandrailBlock.FRAME, GlassHandrailBlock.GLASS, GlassHandrailBlock.DECORATION);

  public static final Model SIMPLE_HANDRAIL = createBlock("simple_handrail", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_INVENTORY = createBlock("simple_handrail_inventory", "_inventory", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_POST = createBlock("simple_handrail_post", "_post", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_SIDE = createBlock("simple_handrail_side", "_side", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_POST_SIDE = createBlock("simple_handrail_post_side", "_post_side", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_CORNER = createBlock("simple_handrail_corner", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);
  public static final Model SIMPLE_HANDRAIL_OUTER = createBlock("simple_handrail_outer", TextureKey.TEXTURE, TextureKey.TOP, TextureKey.BOTTOM);

  public static final Model TEMPLATE_COLORED_GLASS_PANE_POST = createBlock("template_colored_glass_pane_post", "_post", TextureKey.PANE, TextureKey.EDGE);
  public static final Model TEMPLATE_COLORED_GLASS_PANE_SIDE = createBlock("template_colored_glass_pane_side", "_side", TextureKey.PANE, TextureKey.EDGE);
  public static final Model TEMPLATE_COLORED_GLASS_PANE_SIDE_ALT = createBlock("template_colored_glass_pane_side_alt", "_side_alt", TextureKey.PANE, TextureKey.EDGE);
  public static final Model TEMPLATE_COLORED_GLASS_PANE_NOSIDE = createBlock("template_colored_glass_pane_noside", "_noside", TextureKey.PANE, TextureKey.EDGE);
  public static final Model TEMPLATE_COLORED_GLASS_PANE_NOSIDE_ALT = createBlock("template_colored_glass_pane_noside_alt", "_noside_alt", TextureKey.PANE, TextureKey.EDGE);

  public static final Model COLORED_CUBE_COLUMN = createBlock("colored_cube_column", TextureKey.END, TextureKey.SIDE);
  public static final Model COLORED_CUBE_COLUMN_HORITONZAL = createBlock("colored_cube_column_horizontal", "_horizontal", TextureKey.END, TextureKey.SIDE);
  public static final Model COLORED_SLAB = createBlock("colored_slab", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);
  public static final Model COLORED_SLAB_TOP = createBlock("colored_slab_top", "_top", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);
  public static final Model COLORED_CUBE_BOTTOM_UP = createBlock("colored_cube_bottom_up", "_double", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);

  public static final Model COLORED_STAIRS = createBlock("colored_stairs", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);
  public static final Model COLORED_INNER_STAIRS = createBlock("colored_inner_stairs", "_inner", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);
  public static final Model COLORED_OUTER_STAIRS = createBlock("colored_stairs", "_outer", TextureKey.BOTTOM, TextureKey.TOP, TextureKey.SIDE);

  public static final Model ROAD_MARK = createBlock("road_mark", TextureKey.TEXTURE);
  public static final Model ROAD_MARK_ON_SLAB = createBlock("road_mark_on_slab", "_on_slab", TextureKey.TEXTURE);
  public static final Model ROAD_MARK_ROTATED = createBlock("road_mark_rotated", "_rotated", TextureKey.TEXTURE);
  public static final Model ROAD_MARK_ON_SLAB_ROTATED = createBlock("road_mark_on_slab_rotated", "_on_slab_rotated", TextureKey.TEXTURE);

  public static final Model WALL_SIGN = createBlock("wall_sign", TextureKey.TEXTURE);
  public static final Model FULL_WALL_SIGN = createBlock("full_wall_sign", TextureKey.TEXTURE);
  public static final Model GLOWING_WALL_SIGN = createBlock("glowing_wall_sign", TextureKey.TEXTURE, MishangucTextureKeys.GLOW);

  public static final Model STANDING_SIGN = createBlock("standing_sign", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_1 = createBlock("standing_sign_1", "_1", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_2 = createBlock("standing_sign_2", "_2", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_3 = createBlock("standing_sign_3", "_3", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_BARRED = createBlock("standing_sign_barred", "_barred", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_BARRED_1 = createBlock("standing_sign_barred_1", "_barred_1", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_BARRED_2 = createBlock("standing_sign_barred_2", "_barred_2", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model STANDING_SIGN_BARRED_3 = createBlock("standing_sign_barred_3", "_barred_3", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model GLOWING_STANDING_SIGN = createBlock("glowing_standing_sign", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_1 = createBlock("glowing_standing_sign_1", "_1", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_2 = createBlock("glowing_standing_sign_2", "_2", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_3 = createBlock("glowing_standing_sign_3", "_3", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_BARRED = createBlock("glowing_standing_sign_barred", "_barred", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_BARRED_1 = createBlock("glowing_standing_sign_barred_1", "_barred_1", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_BARRED_2 = createBlock("glowing_standing_sign_barred_2", "_barred_2", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_STANDING_SIGN_BARRED_3 = createBlock("glowing_standing_sign_barred_3", "_barred_3", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);

  public static final Model HUNG_SIGN = createBlock("hung_sign", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model HUNG_SIGN_BODY = createBlock("hung_sign_body", "_body", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model HUNG_SIGN_TOP_BAR = createBlock("hung_sign_top_bar", "_top_bar", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model HUNG_SIGN_TOP_BAR_EDGE = createBlock("hung_sign_top_bar_edge", "_top_bar_edge", TextureKey.TEXTURE, MishangucTextureKeys.BAR);
  public static final Model GLOWING_HUNG_SIGN = createBlock("glowing_hung_sign", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);
  public static final Model GLOWING_HUNG_SIGN_BODY = createBlock("glowing_hung_sign_body", "_body", TextureKey.TEXTURE, MishangucTextureKeys.BAR, MishangucTextureKeys.GLOW);

  public static final Model HUNG_SIGN_BAR = createBlock("hung_sign_bar", TextureKey.TEXTURE);
  public static final Model HUNG_SIGN_BAR_CENTRAL = createBlock("hung_sign_bar_central", "_central", TextureKey.TEXTURE);
  public static final Model HUNG_SIGN_BAR_EDGE = createBlock("hung_sign_bar_edge", "_edge", TextureKey.TEXTURE);

  public static Identifier texture(String path) {
    return Mishanguc.id("block/" + path);
  }

  public static Model createBlock(String name, TextureKey... requiredTextureKeys) {
    return new Model(Optional.of(Mishanguc.id("block/" + name)), Optional.empty(), requiredTextureKeys);
  }

  public static Model createBlock(String name, String variant, TextureKey... requiredTextureKeys) {
    return new Model(Optional.of(Mishanguc.id("block/" + name)), Optional.of(variant), requiredTextureKeys);
  }
}
