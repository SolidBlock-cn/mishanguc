package pers.solid.mishang.uc.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.ColoredGlowingStandingSignBlock;
import pers.solid.mishang.uc.block.ColoredStandingSignBlock;
import pers.solid.mishang.uc.block.GlowingStandingSignBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;

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
  public static final StandingSignBlock STRIPPED_MANGROVE_WOOD_STANDING_SIGN = register("stripped_mangrove_wood_standing_sign", Blocks.STRIPPED_MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.COLOR_RED));
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
  public static final StandingSignBlock BAMBOO_STANDING_SIGN = register("bamboo_standing_sign", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final StandingSignBlock BAMBOO_PLANK_STANDING_SIGN = register("bamboo_plank_standing_sign", Blocks.BAMBOO_PLANKS, Block.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));
  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final StandingSignBlock BAMBOO_MOSAIC_STANDING_SIGN = register("bamboo_mosaic_standing_sign", Blocks.BAMBOO_MOSAIC, Block.Properties.ofFullCopy(Blocks.BAMBOO_MOSAIC));

  static {
    OAK_WOOD_STANDING_SIGN.baseTexture = OAK_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/oak_log");
    SPRUCE_WOOD_STANDING_SIGN.baseTexture = SPRUCE_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/spruce_log");
    BIRCH_WOOD_STANDING_SIGN.baseTexture = BIRCH_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/birch_log");
    JUNGLE_WOOD_STANDING_SIGN.baseTexture = JUNGLE_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/jungle_log");
    ACACIA_WOOD_STANDING_SIGN.baseTexture = ACACIA_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/acacia_log");
    CHERRY_WOOD_STANDING_SIGN.baseTexture = CHERRY_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/cherry_log");
    DARK_OAK_WOOD_STANDING_SIGN.baseTexture = DARK_OAK_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/dark_oak_log");
    PALE_OAK_WOOD_STANDING_SIGN.baseTexture = PALE_OAK_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/pale_oak_log");
    MANGROVE_WOOD_STANDING_SIGN.baseTexture = MANGROVE_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/mangrove_log");
    CRIMSON_HYPHAE_STANDING_SIGN.baseTexture = CRIMSON_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/crimson_stem");
    WARPED_HYPHAE_STANDING_SIGN.baseTexture = WARPED_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/warped_stem");
    STRIPPED_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_dark_oak_log");
    STRIPPED_PALE_OAK_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_WOOD_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/stripped_warped_stem");
    BAMBOO_PLANK_STANDING_SIGN.barTexture = BAMBOO_MOSAIC_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/bamboo_block");
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

  public static final ColorCollection<StandingSignBlock> CONCRETE_STANDING_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_concrete_standing_sign", Blocks.CONCRETE.pick(dyeColor)));

  public static final ColoredStandingSignBlock COLORED_CONCRETE_STANDING_SIGN = registerColored("colored_concrete_standing_sign", ColoredBlocks.COLORED_CONCRETE);

  public static final ColorCollection<StandingSignBlock> DYED_TERRACOTTA_STANDING_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_terracotta_standing_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  public static final ColoredStandingSignBlock COLORED_TERRACOTTA_STANDING_SIGN = registerColored("colored_terracotta_standing_sign", ColoredBlocks.COLORED_TERRACOTTA);

  public static final ColorCollection<GlowingStandingSignBlock> GLOWING_CONCRETE_STANDING_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_concrete_standing_sign", Blocks.CONCRETE.pick(dyeColor)));

  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_CONCRETE_STANDING_SIGN = registerColoredGlowing("colored_glowing_concrete_standing_sign", ColoredBlocks.COLORED_CONCRETE);

  public static final ColorCollection<GlowingStandingSignBlock> GLOWING_DYED_TERRACOTTA_STANDING_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_terracotta_standing_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  public static final ColoredGlowingStandingSignBlock COLORED_GLOWING_TERRACOTTA_STANDING_SIGN = registerColoredGlowing("colored_glowing_terracotta_standing_sign", ColoredBlocks.COLORED_TERRACOTTA);

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

  // 硫黄和朱砂
  public static final StandingSignBlock SULFUR_STANDING_SIGN = register("sulfur_standing_sign", Blocks.SULFUR);
  public static final GlowingStandingSignBlock GLOWING_SULFUR_STANDING_SIGN = registerGlowing("glowing_sulfur_standing_sign", Blocks.SULFUR);

  public static final StandingSignBlock CINNABAR_STANDING_SIGN = register("cinnabar_standing_sign", Blocks.CINNABAR);
  public static final GlowingStandingSignBlock GLOWING_CINNABAR_STANDING_SIGN = registerColoredGlowing("glowing_cinnabar_standing_sign", Blocks.CINNABAR);

  static {
    GLOWING_NETHERRACK_STANDING_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_NETHER_BRICK_STANDING_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_BLACKSTONE_STANDING_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_STANDING_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
  }


  // 雪
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final StandingSignBlock SNOW_STANDING_SIGN = register("snow_standing_sign", Blocks.SNOW_BLOCK);
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingStandingSignBlock GLOWING_SNOW_STANDING_SIGN = registerGlowing("glowing_snow_standing_sign", Blocks.SNOW_BLOCK);
  // 冰
  public static final StandingSignBlock ICE_STANDING_SIGN = register("ice_standing_sign", Blocks.ICE);
  public static final StandingSignBlock PACKED_ICE_STANDING_SIGN = register("packed_ice_standing_sign", Blocks.PACKED_ICE);
  public static final GlowingStandingSignBlock GLOWING_PACKED_ICE_STANDING_SIGN = registerGlowing("glowing_packed_ice_standing_sign", Blocks.PACKED_ICE);
  public static final StandingSignBlock BLUE_ICE_STANDING_SIGN = register("blue_ice_standing_sign", Blocks.BLUE_ICE);
  public static final GlowingStandingSignBlock GLOWING_BLUE_ICE_STANDING_SIGN = registerGlowing("glowing_blue_ice_standing_sign", Blocks.BLUE_ICE);

  static {
    SNOW_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/snow");
    SNOW_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/packed_ice");
    GLOWING_SNOW_STANDING_SIGN.baseTexture = Identifier.withDefaultNamespace("block/snow");
    GLOWING_SNOW_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/packed_ice");
    ICE_STANDING_SIGN.barTexture = Identifier.withDefaultNamespace("block/blue_ice");
  }

  private static StandingSignBlock register(String name, Block baseBlock, BlockBehaviour.Properties settings) {
    return MishangucBlocks.register(name, settings1 -> new StandingSignBlock(baseBlock, settings1), settings);
  }

  private static StandingSignBlock register(String name, Block baseBlock) {
    return register(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static GlowingStandingSignBlock registerGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new GlowingStandingSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static ColoredStandingSignBlock registerColored(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredStandingSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static ColoredGlowingStandingSignBlock registerColoredGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredGlowingStandingSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }
}
