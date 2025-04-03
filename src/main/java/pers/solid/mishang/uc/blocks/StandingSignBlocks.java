package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredGlowingStandingSignBlock;
import pers.solid.mishang.uc.block.ColoredStandingSignBlock;
import pers.solid.mishang.uc.block.GlowingStandingSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.mixin.AbstractBlockAccessor;
import pers.solid.mishang.uc.mixin.AbstractBlockSettingsAccessor;

/**
 * <h1>直立的告示牌方块</h1>
 * 此类包含本模组中的所有直立告示牌方块。
 *
 * @since 1.0.2
 */
@ApiStatus.AvailableSince("1.0.2")
public final class StandingSignBlocks extends MishangucBlocks {
  private StandingSignBlocks() {
  }

  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock OAK_WOOD_STANDING_SIGN = register("oak_wood_standing_sign", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock SPRUCE_WOOD_STANDING_SIGN = register("spruce_wood_standing_sign", Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock BIRCH_WOOD_STANDING_SIGN = register("birch_wood_standing_sign", Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock JUNGLE_WOOD_STANDING_SIGN = register("jungle_wood_standing_sign", Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock ACACIA_WOOD_STANDING_SIGN = register("acacia_wood_standing_sign", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock CHERRY_WOOD_STANDING_SIGN = register("cherry_wood_standing_sign", Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock DARK_OAK_WOOD_STANDING_SIGN = register("dark_oak_wood_standing_sign", Blocks.DARK_OAK_WOOD);
  public static final StandingSignBlock PALE_OAK_WOOD_STANDING_SIGN = register("pale_oak_wood_standing_sign", Blocks.PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock MANGROVE_WOOD_STANDING_SIGN = register("mangrove_wood_standing_sign", Blocks.MANGROVE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock CRIMSON_HYPHAE_STANDING_SIGN = register("crimson_hyphae_standing_sign", Blocks.CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock WARPED_HYPHAE_STANDING_SIGN = register("warped_hyphae_standing_sign", Blocks.WARPED_HYPHAE);

  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_OAK_WOOD_STANDING_SIGN = register("stripped_oak_wood_standing_sign", Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_SPRUCE_WOOD_STANDING_SIGN = register("stripped_spruce_wood_standing_sign", Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_BIRCH_WOOD_STANDING_SIGN = register("stripped_birch_wood_standing_sign", Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_JUNGLE_WOOD_STANDING_SIGN = register("stripped_jungle_wood_standing_sign", Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_ACACIA_WOOD_STANDING_SIGN = register("stripped_acacia_wood_standing_sign", Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_CHERRY_WOOD_STANDING_SIGN = register("stripped_cherry_wood_standing_sign", Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_DARK_OAK_WOOD_STANDING_SIGN = register("stripped_dark_oak_wood_standing_sign", Blocks.STRIPPED_DARK_OAK_WOOD);
  public static final StandingSignBlock STRIPPED_PALE_OAK_WOOD_STANDING_SIGN = register("stripped_pale_oak_wood_standing_sign", Blocks.STRIPPED_PALE_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_MANGROVE_WOOD_STANDING_SIGN = register("stripped_mangrove_wood_standing_sign", Blocks.STRIPPED_MANGROVE_WOOD, Block.Settings.copy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.RED));
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_CRIMSON_HYPHAE_STANDING_SIGN = register("stripped_crimson_hyphae_standing_sign", Blocks.STRIPPED_CRIMSON_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final StandingSignBlock STRIPPED_WARPED_HYPHAE_STANDING_SIGN = register("stripped_warped_hyphae_standing_sign", Blocks.STRIPPED_WARPED_HYPHAE);

  public static final StandingSignBlock OAK_STANDING_SIGN = register("oak_standing_sign", Blocks.OAK_PLANKS);
  public static final StandingSignBlock SPRUCE_STANDING_SIGN = register("spruce_standing_sign", Blocks.SPRUCE_PLANKS);
  public static final StandingSignBlock BIRCH_STANDING_SIGN = register("birch_standing_sign", Blocks.BIRCH_PLANKS);
  public static final StandingSignBlock JUNGLE_STANDING_SIGN = register("jungle_standing_sign", Blocks.JUNGLE_PLANKS);
  public static final StandingSignBlock ACACIA_STANDING_SIGN = register("acacia_standing_sign", Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final StandingSignBlock CHERRY_STANDING_SIGN = register("cherry_standing_sign", Blocks.CHERRY_PLANKS);
  public static final StandingSignBlock DARK_OAK_STANDING_SIGN = register("dark_oak_standing_sign", Blocks.DARK_OAK_PLANKS);
  public static final StandingSignBlock PALE_OAK_STANDING_SIGN = register("pale_oak_standing_sign", Blocks.PALE_OAK_PLANKS);
  public static final StandingSignBlock MANGROVE_STANDING_SIGN = register("mangrove_standing_sign", Blocks.MANGROVE_PLANKS);
  public static final StandingSignBlock CRIMSON_STANDING_SIGN = register("crimson_standing_sign", Blocks.CRIMSON_PLANKS);
  public static final StandingSignBlock WARPED_STANDING_SIGN = register("warped_standing_sign", Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final StandingSignBlock BAMBOO_STANDING_SIGN = register("bamboo_standing_sign", Blocks.BAMBOO_BLOCK, Block.Settings.copy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.DARK_GREEN));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final StandingSignBlock BAMBOO_PLANK_STANDING_SIGN = register("bamboo_plank_standing_sign", Blocks.BAMBOO_PLANKS, Block.Settings.copy(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final StandingSignBlock BAMBOO_MOSAIC_STANDING_SIGN = register("bamboo_mosaic_standing_sign", Blocks.BAMBOO_MOSAIC, Block.Settings.copy(Blocks.BAMBOO_MOSAIC));

  static {
    OAK_WOOD_STANDING_SIGN.baseTexture = OAK_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/oak_log");
    SPRUCE_WOOD_STANDING_SIGN.baseTexture = SPRUCE_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/spruce_log");
    BIRCH_WOOD_STANDING_SIGN.baseTexture = BIRCH_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/birch_log");
    JUNGLE_WOOD_STANDING_SIGN.baseTexture = JUNGLE_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/jungle_log");
    ACACIA_WOOD_STANDING_SIGN.baseTexture = ACACIA_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/acacia_log");
    CHERRY_WOOD_STANDING_SIGN.baseTexture = CHERRY_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/cherry_log");
    DARK_OAK_WOOD_STANDING_SIGN.baseTexture = DARK_OAK_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/dark_oak_log");
    PALE_OAK_WOOD_STANDING_SIGN.baseTexture = PALE_OAK_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/pale_oak_log");
    MANGROVE_WOOD_STANDING_SIGN.baseTexture = MANGROVE_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/mangrove_log");
    CRIMSON_HYPHAE_STANDING_SIGN.baseTexture = CRIMSON_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/crimson_stem");
    WARPED_HYPHAE_STANDING_SIGN.baseTexture = WARPED_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/warped_stem");
    STRIPPED_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_dark_oak_log");
    STRIPPED_PALE_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_WOOD_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/stripped_warped_stem");
    BAMBOO_PLANK_STANDING_SIGN.barTexture = BAMBOO_MOSAIC_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/bamboo_block");
  }

  public static final ImmutableMap<WoodType, StandingSignBlock> WOODEN_SIGNS = new ImmutableMap.Builder<WoodType, StandingSignBlock>()
      .put(WoodType.OAK, OAK_STANDING_SIGN)
      .put(WoodType.SPRUCE, SPRUCE_STANDING_SIGN)
      .put(WoodType.BIRCH, BIRCH_STANDING_SIGN)
      .put(WoodType.ACACIA, ACACIA_STANDING_SIGN)
      .put(WoodType.CHERRY, CHERRY_STANDING_SIGN)
      .put(WoodType.JUNGLE, JUNGLE_STANDING_SIGN)
      .put(WoodType.DARK_OAK, DARK_OAK_STANDING_SIGN)
      .put(WoodType.PALE_OAK, PALE_OAK_STANDING_SIGN)
      .put(WoodType.CRIMSON, CRIMSON_STANDING_SIGN)
      .put(WoodType.WARPED, WARPED_STANDING_SIGN)
      .put(WoodType.MANGROVE, MANGROVE_STANDING_SIGN)
      .build();

  public static final StandingSignBlock WHITE_CONCRETE_STANDING_SIGN = register("white_concrete_standing_sign", Blocks.WHITE_CONCRETE);
  public static final StandingSignBlock ORANGE_CONCRETE_STANDING_SIGN = register("orange_concrete_standing_sign", Blocks.ORANGE_CONCRETE);
  public static final StandingSignBlock MAGENTA_CONCRETE_STANDING_SIGN = register("magenta_concrete_standing_sign", Blocks.MAGENTA_CONCRETE);
  public static final StandingSignBlock LIGHT_BLUE_CONCRETE_STANDING_SIGN = register("light_blue_concrete_standing_sign", Blocks.LIGHT_BLUE_CONCRETE);
  public static final StandingSignBlock YELLOW_CONCRETE_STANDING_SIGN = register("yellow_concrete_standing_sign", Blocks.YELLOW_CONCRETE);
  public static final StandingSignBlock LIME_CONCRETE_STANDING_SIGN = register("lime_concrete_standing_sign", Blocks.LIME_CONCRETE);
  public static final StandingSignBlock PINK_CONCRETE_STANDING_SIGN = register("pink_concrete_standing_sign", Blocks.PINK_CONCRETE);
  public static final StandingSignBlock GRAY_CONCRETE_STANDING_SIGN = register("gray_concrete_standing_sign", Blocks.GRAY_CONCRETE);
  public static final StandingSignBlock LIGHT_GRAY_CONCRETE_STANDING_SIGN = register("light_gray_concrete_standing_sign", Blocks.LIGHT_GRAY_CONCRETE);
  public static final StandingSignBlock CYAN_CONCRETE_STANDING_SIGN = register("cyan_concrete_standing_sign", Blocks.CYAN_CONCRETE);
  public static final StandingSignBlock PURPLE_CONCRETE_STANDING_SIGN = register("purple_concrete_standing_sign", Blocks.PURPLE_CONCRETE);
  public static final StandingSignBlock BLUE_CONCRETE_STANDING_SIGN = register("blue_concrete_standing_sign", Blocks.BLUE_CONCRETE);
  public static final StandingSignBlock BROWN_CONCRETE_STANDING_SIGN = register("brown_concrete_standing_sign", Blocks.BROWN_CONCRETE);
  public static final StandingSignBlock GREEN_CONCRETE_STANDING_SIGN = register("green_concrete_standing_sign", Blocks.GREEN_CONCRETE);
  public static final StandingSignBlock RED_CONCRETE_STANDING_SIGN = register("red_concrete_standing_sign", Blocks.RED_CONCRETE);
  public static final StandingSignBlock BLACK_CONCRETE_STANDING_SIGN = register("black_concrete_standing_sign", Blocks.BLACK_CONCRETE);

  public static final ColoredStandingSignBlock COLORED_CONCRETE_STANDING_SIGN = registerColored("colored_concrete_standing_sign", ColoredBlocks.COLORED_CONCRETE);

  public static final ImmutableMap<DyeColor, StandingSignBlock> CONCRETE_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, WHITE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.ORANGE, ORANGE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.YELLOW, YELLOW_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIME, LIME_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PINK, PINK_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GRAY, GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.CYAN, CYAN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PURPLE, PURPLE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLUE, BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BROWN, BROWN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GREEN, GREEN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.RED, RED_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLACK, BLACK_CONCRETE_STANDING_SIGN)
      .build();
  public static final StandingSignBlock WHITE_TERRACOTTA_STANDING_SIGN = register("white_terracotta_standing_sign", Blocks.WHITE_TERRACOTTA);
  public static final StandingSignBlock ORANGE_TERRACOTTA_STANDING_SIGN = register("orange_terracotta_standing_sign", Blocks.ORANGE_TERRACOTTA);
  public static final StandingSignBlock MAGENTA_TERRACOTTA_STANDING_SIGN = register("magenta_terracotta_standing_sign", Blocks.MAGENTA_TERRACOTTA);
  public static final StandingSignBlock LIGHT_BLUE_TERRACOTTA_STANDING_SIGN = register("light_blue_terracotta_standing_sign", Blocks.LIGHT_BLUE_TERRACOTTA);
  public static final StandingSignBlock YELLOW_TERRACOTTA_STANDING_SIGN = register("yellow_terracotta_standing_sign", Blocks.YELLOW_TERRACOTTA);
  public static final StandingSignBlock LIME_TERRACOTTA_STANDING_SIGN = register("lime_terracotta_standing_sign", Blocks.LIME_TERRACOTTA);
  public static final StandingSignBlock PINK_TERRACOTTA_STANDING_SIGN = register("pink_terracotta_standing_sign", Blocks.PINK_TERRACOTTA);
  public static final StandingSignBlock GRAY_TERRACOTTA_STANDING_SIGN = register("gray_terracotta_standing_sign", Blocks.GRAY_TERRACOTTA);
  public static final StandingSignBlock LIGHT_GRAY_TERRACOTTA_STANDING_SIGN = register("light_gray_terracotta_standing_sign", Blocks.LIGHT_GRAY_TERRACOTTA);
  public static final StandingSignBlock CYAN_TERRACOTTA_STANDING_SIGN = register("cyan_terracotta_standing_sign", Blocks.CYAN_TERRACOTTA);
  public static final StandingSignBlock PURPLE_TERRACOTTA_STANDING_SIGN = register("purple_terracotta_standing_sign", Blocks.PURPLE_TERRACOTTA);
  public static final StandingSignBlock BLUE_TERRACOTTA_STANDING_SIGN = register("blue_terracotta_standing_sign", Blocks.BLUE_TERRACOTTA);
  public static final StandingSignBlock BROWN_TERRACOTTA_STANDING_SIGN = register("brown_terracotta_standing_sign", Blocks.BROWN_TERRACOTTA);
  public static final StandingSignBlock GREEN_TERRACOTTA_STANDING_SIGN = register("green_terracotta_standing_sign", Blocks.GREEN_TERRACOTTA);
  public static final StandingSignBlock RED_TERRACOTTA_STANDING_SIGN = register("red_terracotta_standing_sign", Blocks.RED_TERRACOTTA);
  public static final StandingSignBlock BLACK_TERRACOTTA_STANDING_SIGN = register("black_terracotta_standing_sign", Blocks.BLACK_TERRACOTTA);

  public static final ColoredStandingSignBlock COLORED_TERRACOTTA_STANDING_SIGN = registerColored("colored_terracotta_standing_sign", ColoredBlocks.COLORED_TERRACOTTA);

  public static final ImmutableMap<DyeColor, StandingSignBlock> TERRACOTTA_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, WHITE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.ORANGE, ORANGE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.MAGENTA, MAGENTA_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, LIGHT_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.YELLOW, YELLOW_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIME, LIME_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PINK, PINK_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GRAY, GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, LIGHT_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.CYAN, CYAN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PURPLE, PURPLE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLUE, BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BROWN, BROWN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GREEN, GREEN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.RED, RED_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLACK, BLACK_TERRACOTTA_STANDING_SIGN)
      .build();
  public static final GlowingStandingSignBlock GLOWING_WHITE_CONCRETE_STANDING_SIGN = registerGlowing("glowing_white_concrete_standing_sign", Blocks.WHITE_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_ORANGE_CONCRETE_STANDING_SIGN = registerGlowing("glowing_orange_concrete_standing_sign", Blocks.ORANGE_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_MAGENTA_CONCRETE_STANDING_SIGN = registerGlowing("glowing_magenta_concrete_standing_sign", Blocks.MAGENTA_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_LIGHT_BLUE_CONCRETE_STANDING_SIGN = registerGlowing("glowing_light_blue_concrete_standing_sign", Blocks.LIGHT_BLUE_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_YELLOW_CONCRETE_STANDING_SIGN = registerGlowing("glowing_yellow_concrete_standing_sign", Blocks.YELLOW_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_LIME_CONCRETE_STANDING_SIGN = registerGlowing("glowing_lime_concrete_standing_sign", Blocks.LIME_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_PINK_CONCRETE_STANDING_SIGN = registerGlowing("glowing_pink_concrete_standing_sign", Blocks.PINK_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_GRAY_CONCRETE_STANDING_SIGN = registerGlowing("glowing_gray_concrete_standing_sign", Blocks.GRAY_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_LIGHT_GRAY_CONCRETE_STANDING_SIGN = registerGlowing("glowing_light_gray_concrete_standing_sign", Blocks.LIGHT_GRAY_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_CYAN_CONCRETE_STANDING_SIGN = registerGlowing("glowing_cyan_concrete_standing_sign", Blocks.CYAN_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_PURPLE_CONCRETE_STANDING_SIGN = registerGlowing("glowing_purple_concrete_standing_sign", Blocks.PURPLE_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_BLUE_CONCRETE_STANDING_SIGN = registerGlowing("glowing_blue_concrete_standing_sign", Blocks.BLUE_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_BROWN_CONCRETE_STANDING_SIGN = registerGlowing("glowing_brown_concrete_standing_sign", Blocks.BROWN_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_GREEN_CONCRETE_STANDING_SIGN = registerGlowing("glowing_green_concrete_standing_sign", Blocks.GREEN_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_RED_CONCRETE_STANDING_SIGN = registerGlowing("glowing_red_concrete_standing_sign", Blocks.RED_CONCRETE);
  public static final GlowingStandingSignBlock GLOWING_BLACK_CONCRETE_STANDING_SIGN = registerGlowing("glowing_black_concrete_standing_sign", Blocks.BLACK_CONCRETE);

  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_CONCRETE_STANDING_SIGN = registerColoredGlowing("colored_glowing_concrete_standing_sign", ColoredBlocks.COLORED_CONCRETE);

  public static final ImmutableMap<DyeColor, StandingSignBlock> GLOWING_CONCRETE_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_CONCRETE_STANDING_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_CONCRETE_STANDING_SIGN)
      .put(DyeColor.RED, GLOWING_RED_CONCRETE_STANDING_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_CONCRETE_STANDING_SIGN)
      .build();
  public static final GlowingStandingSignBlock GLOWING_WHITE_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_white_terracotta_standing_sign", Blocks.WHITE_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_ORANGE_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_orange_terracotta_standing_sign", Blocks.ORANGE_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_MAGENTA_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_magenta_terracotta_standing_sign", Blocks.MAGENTA_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_LIGHT_BLUE_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_light_blue_terracotta_standing_sign", Blocks.LIGHT_BLUE_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_YELLOW_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_yellow_terracotta_standing_sign", Blocks.YELLOW_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_LIME_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_lime_terracotta_standing_sign", Blocks.LIME_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_PINK_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_pink_terracotta_standing_sign", Blocks.PINK_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_GRAY_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_gray_terracotta_standing_sign", Blocks.GRAY_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_LIGHT_GRAY_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_light_gray_terracotta_standing_sign", Blocks.LIGHT_GRAY_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_CYAN_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_cyan_terracotta_standing_sign", Blocks.CYAN_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_PURPLE_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_purple_terracotta_standing_sign", Blocks.PURPLE_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_BLUE_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_blue_terracotta_standing_sign", Blocks.BLUE_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_BROWN_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_brown_terracotta_standing_sign", Blocks.BROWN_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_GREEN_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_green_terracotta_standing_sign", Blocks.GREEN_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_RED_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_red_terracotta_standing_sign", Blocks.RED_TERRACOTTA);
  public static final GlowingStandingSignBlock GLOWING_BLACK_TERRACOTTA_STANDING_SIGN = registerGlowing("glowing_black_terracotta_standing_sign", Blocks.BLACK_TERRACOTTA);

  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_TERRACOTTA_STANDING_SIGN = registerColoredGlowing("colored_glowing_terracotta_standing_sign", ColoredBlocks.COLORED_TERRACOTTA);

  public static final ImmutableMap<DyeColor, StandingSignBlock> GLOWING_TERRACOTTA_STANDING_SIGNS = new ImmutableMap.Builder<DyeColor, StandingSignBlock>()
      .put(DyeColor.WHITE, GLOWING_WHITE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.ORANGE, GLOWING_ORANGE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.MAGENTA, GLOWING_MAGENTA_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_BLUE, GLOWING_LIGHT_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.YELLOW, GLOWING_YELLOW_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIME, GLOWING_LIME_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PINK, GLOWING_PINK_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GRAY, GLOWING_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.LIGHT_GRAY, GLOWING_LIGHT_GRAY_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.CYAN, GLOWING_CYAN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.PURPLE, GLOWING_PURPLE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLUE, GLOWING_BLUE_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BROWN, GLOWING_BROWN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.GREEN, GLOWING_GREEN_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.RED, GLOWING_RED_TERRACOTTA_STANDING_SIGN)
      .put(DyeColor.BLACK, GLOWING_BLACK_TERRACOTTA_STANDING_SIGN)
      .build();

  // 以下是一些比较杂项的。
  /// 石头
  public static final StandingSignBlock STONE_STANDING_SIGN = register("stone_standing_sign", Blocks.STONE);
  public static final GlowingStandingSignBlock GLOWING_STONE_STANDING_SIGN = registerGlowing("glowing_stone_standing_sign", Blocks.STONE);
  public static final ColoredStandingSignBlock COLORED_STONE_STANDING_SIGN = registerColored("colored_stone_standing_sign", ColoredBlocks.COLORED_STONE);
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_STONE_STANDING_SIGN = registerColoredGlowing("colored_glowing_stone_standing_sign", ColoredBlocks.COLORED_STONE);
  /// 圆石
  public static final StandingSignBlock COBBLESTONE_STANDING_SIGN = register("cobblestone_standing_sign", Blocks.COBBLESTONE);
  public static final GlowingStandingSignBlock GLOWING_COBBLESTONE_STANDING_SIGN = registerGlowing("glowing_cobblestone_standing_sign", Blocks.COBBLESTONE);
  public static final ColoredStandingSignBlock COLORED_COBBLESTONE_STANDING_SIGN = registerColored("colored_cobblestone_standing_sign", ColoredBlocks.COLORED_COBBLESTONE);
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_COBBLESTONE_STANDING_SIGN = registerColoredGlowing("colored_glowing_cobblestone_standing_sign", ColoredBlocks.COLORED_COBBLESTONE);
  /// 石砖
  public static final StandingSignBlock STONE_BRICK_STANDING_SIGN = register("stone_brick_standing_sign", Blocks.STONE_BRICKS);
  public static final GlowingStandingSignBlock GLOWING_STONE_BRICK_STANDING_SIGN = registerGlowing("glowing_stone_brick_standing_sign", Blocks.STONE_BRICKS);
  public static final ColoredStandingSignBlock COLORED_STONE_BRICK_STANDING_SIGN = registerColored("colored_stone_brick_standing_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_STONE_BRICK_STANDING_SIGN = registerColoredGlowing("colored_glowing_stone_brick_standing_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  /// 铁块
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final StandingSignBlock IRON_STANDING_SIGN = register("iron_standing_sign", Blocks.IRON_BLOCK);
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingStandingSignBlock GLOWING_IRON_STANDING_SIGN = registerGlowing("glowing_iron_standing_sign", Blocks.IRON_BLOCK);
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredStandingSignBlock COLORED_IRON_STANDING_SIGN = registerColored("colored_iron_standing_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_IRON_STANDING_SIGN = registerColoredGlowing("colored_glowing_iron_standing_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  /// 金块
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final StandingSignBlock GOLD_STANDING_SIGN = register("gold_standing_sign", Blocks.GOLD_BLOCK);
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingStandingSignBlock GLOWING_GOLD_STANDING_SIGN = registerGlowing("glowing_gold_standing_sign", Blocks.GOLD_BLOCK);
  /// 钻石块
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final StandingSignBlock DIAMOND_STANDING_SIGN = register("diamond_standing_sign", Blocks.DIAMOND_BLOCK);
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingStandingSignBlock GLOWING_DIAMOND_STANDING_SIGN = registerGlowing("glowing_diamond_standing_sign", Blocks.DIAMOND_BLOCK);

  // 绿宝石块
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final StandingSignBlock EMERALD_STANDING_SIGN = register("emerald_standing_sign", Blocks.EMERALD_BLOCK);
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingStandingSignBlock GLOWING_EMERALD_STANDING_SIGN = registerGlowing("glowing_emerald_standing_sign", Blocks.EMERALD_BLOCK);
  // 青金石块
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final StandingSignBlock LAPIS_STANDING_SIGN = register("lapis_standing_sign", Blocks.LAPIS_BLOCK);
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingStandingSignBlock GLOWING_LAPIS_STANDING_SIGN = registerGlowing("glowing_lapis_standing_sign", Blocks.LAPIS_BLOCK);
  // 下界合金块
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final StandingSignBlock NETHERITE_STANDING_SIGN = register("netherite_standing_sign", Blocks.NETHERITE_BLOCK);
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingStandingSignBlock GLOWING_NETHERITE_STANDING_SIGN = registerGlowing("glowing_netherite_standing_sign", Blocks.NETHERITE_BLOCK);
  // 黑曜石
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final StandingSignBlock OBSIDIAN_STANDING_SIGN = register("obsidian_standing_sign", Blocks.OBSIDIAN);
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingStandingSignBlock GLOWING_OBSIDIAN_STANDING_SIGN = registerGlowing("glowing_obsidian_standing_sign", Blocks.OBSIDIAN);
  // 哭泣的黑曜石
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final StandingSignBlock CRYING_OBSIDIAN_STANDING_SIGN = register("crying_obsidian_standing_sign", Blocks.CRYING_OBSIDIAN);
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingStandingSignBlock GLOWING_CRYING_OBSIDIAN_STANDING_SIGN = registerGlowing("glowing_crying_obsidian_standing_sign", Blocks.CRYING_OBSIDIAN);
  // 下界岩
  public static final StandingSignBlock NETHERRACK_STANDING_SIGN = register("netherrack_standing_sign", Blocks.NETHERRACK);
  public static final GlowingStandingSignBlock GLOWING_NETHERRACK_STANDING_SIGN = registerGlowing("glowing_netherrack_standing_sign", Blocks.NETHERRACK);
  // 下界砖
  public static final StandingSignBlock NETHER_BRICK_STANDING_SIGN = register("nether_brick_standing_sign", Blocks.NETHER_BRICKS);
  public static final GlowingStandingSignBlock GLOWING_NETHER_BRICK_STANDING_SIGN = registerGlowing("glowing_nether_brick_standing_sign", Blocks.NETHER_BRICKS);
  // 黑石
  public static final StandingSignBlock BLACKSTONE_STANDING_SIGN = register("blackstone_standing_sign", Blocks.BLACKSTONE);
  public static final GlowingStandingSignBlock GLOWING_BLACKSTONE_STANDING_SIGN = registerGlowing("glowing_blackstone_standing_sign", Blocks.BLACKSTONE);
  // 磨制黑石
  public static final StandingSignBlock POLISHED_BLACKSTONE_STANDING_SIGN = register("polished_blackstone_standing_sign", Blocks.POLISHED_BLACKSTONE);
  public static final GlowingStandingSignBlock GLOWING_POLISHED_BLACKSTONE_STANDING_SIGN = registerGlowing("glowing_polished_blackstone_standing_sign", Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_STANDING_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_NETHER_BRICK_STANDING_SIGN.glowTexture = Identifier.ofVanilla("block/lava_still");
    GLOWING_BLACKSTONE_STANDING_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_STANDING_SIGN.glowTexture = Identifier.ofVanilla("block/glowstone");
  }


  // 雪
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final StandingSignBlock SNOW_STANDING_SIGN = register("snow_standing_sign", Blocks.SNOW_BLOCK);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingStandingSignBlock GLOWING_SNOW_STANDING_SIGN = registerGlowing("glowing_snow_standing_sign", Blocks.SNOW_BLOCK);
  // 冰
  @Translucent
  public static final StandingSignBlock ICE_STANDING_SIGN = register("ice_standing_sign", Blocks.ICE);
  public static final StandingSignBlock PACKED_ICE_STANDING_SIGN = register("packed_ice_standing_sign", Blocks.PACKED_ICE);
  public static final GlowingStandingSignBlock GLOWING_PACKED_ICE_STANDING_SIGN = registerGlowing("glowing_packed_ice_standing_sign", Blocks.PACKED_ICE);
  public static final StandingSignBlock BLUE_ICE_STANDING_SIGN = register("blue_ice_standing_sign", Blocks.BLUE_ICE);
  public static final GlowingStandingSignBlock GLOWING_BLUE_ICE_STANDING_SIGN = registerGlowing("glowing_blue_ice_standing_sign", Blocks.BLUE_ICE);

  static {
    SNOW_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/snow");
    SNOW_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/packed_ice");
    GLOWING_SNOW_STANDING_SIGN.baseTexture = Identifier.ofVanilla("block/snow");
    GLOWING_SNOW_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/packed_ice");
    ICE_STANDING_SIGN.barTexture = Identifier.ofVanilla("block/blue_ice");
  }

  private static StandingSignBlock register(String name, Block baseBlock, AbstractBlock.Settings settings) {
    return MishangucBlocks.register(name, settings1 -> new StandingSignBlock(baseBlock, settings1), settings);
  }

  private static StandingSignBlock register(String name, Block baseBlock) {
    return register(name, baseBlock, AbstractBlock.Settings.copy(baseBlock));
  }

  private static GlowingStandingSignBlock registerGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new GlowingStandingSignBlock(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
  }

  private static ColoredStandingSignBlock registerColored(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredStandingSignBlock(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
  }

  private static ColoredGlowingStandingSignBlock registerColoredGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredGlowingStandingSignBlock(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
  }
}
