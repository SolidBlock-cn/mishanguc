package pers.solid.mishang.uc.blocks;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.*;

/**
 * <h1>墙上的告示牌方块</h1>
 * 包括一般的墙上告示牌和完整的墙上告示牌。二者对应不同的方块实体类型。
 */
public final class WallSignBlocks extends MishangucBlocks {

  /**
   * 隐形的告示牌。
   */
  @MiningLevel(MiningLevel.Tool.NONE)
  public static final FullWallSignBlock INVISIBLE_WALL_SIGN = registerFull("invisible_wall_sign", null, Block.Properties.of().mapColor(MapColor.NONE).noCollision().strength(0, 1f));

  @MiningLevel(MiningLevel.Tool.NONE)
  public static final FullWallSignBlock INVISIBLE_GLOWING_WALL_SIGN = registerFull("invisible_glowing_wall_sign", null, Block.Properties.of().mapColor(MapColor.NONE).noCollision().lightLevel(x -> 15).strength(0, 1f));

  // 木质
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock OAK_WOOD_WALL_SIGN = register("oak_wood_wall_sign", Blocks.OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock SPRUCE_WOOD_WALL_SIGN = register("spruce_wood_wall_sign", Blocks.SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock BIRCH_WOOD_WALL_SIGN = register("birch_wood_wall_sign", Blocks.BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock JUNGLE_WOOD_WALL_SIGN = register("jungle_wood_wall_sign", Blocks.JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock ACACIA_WOOD_WALL_SIGN = register("acacia_wood_wall_sign", Blocks.ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock CHERRY_WOOD_WALL_SIGN = register("cherry_wood_wall_sign", Blocks.CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock DARK_OAK_WOOD_WALL_SIGN = register("dark_oak_wood_wall_sign", Blocks.DARK_OAK_WOOD);
  public static final WallSignBlock PALE_OAK_WOOD_WALL_SIGN = register("pale_oak_wood_wall_sign", Blocks.PALE_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock MANGROVE_WOOD_WALL_SIGN = register("mangrove_wood_wall_sign", Blocks.MANGROVE_WOOD);

  public static final WallSignBlock CRIMSON_HYPHAE_WALL_SIGN = register("crimson_hyphae_wall_sign", Blocks.CRIMSON_HYPHAE);

  public static final WallSignBlock WARPED_HYPHAE_WALL_SIGN = register("warped_hyphae_wall_sign", Blocks.WARPED_HYPHAE);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_OAK_WOOD_WALL_SIGN = register("stripped_oak_wood_wall_sign", Blocks.STRIPPED_OAK_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_SPRUCE_WOOD_WALL_SIGN = register("stripped_spruce_wood_wall_sign", Blocks.STRIPPED_SPRUCE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_BIRCH_WOOD_WALL_SIGN = register("stripped_birch_wood_wall_sign", Blocks.STRIPPED_BIRCH_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_JUNGLE_WOOD_WALL_SIGN = register("stripped_jungle_wood_wall_sign", Blocks.STRIPPED_JUNGLE_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_ACACIA_WOOD_WALL_SIGN = register("stripped_acacia_wood_wall_sign", Blocks.STRIPPED_ACACIA_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_CHERRY_WOOD_WALL_SIGN = register("stripped_cherry_wood_wall_sign", Blocks.STRIPPED_CHERRY_WOOD);
  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_DARK_OAK_WOOD_WALL_SIGN = register("stripped_dark_oak_wood_wall_sign", Blocks.STRIPPED_DARK_OAK_WOOD);
  public static final WallSignBlock STRIPPED_PALE_OAK_WOOD_WALL_SIGN = register("stripped_pale_oak_wood_wall_sign", Blocks.STRIPPED_PALE_OAK_WOOD);

  @ApiStatus.AvailableSince("1.2.4")
  public static final WallSignBlock STRIPPED_MANGROVE_WOOD_WALL_SIGN = register("stripped_mangrove_wood_wall_sign", Blocks.STRIPPED_MANGROVE_WOOD, Block.Properties.ofFullCopy(Blocks.STRIPPED_MANGROVE_WOOD).mapColor(MapColor.COLOR_RED));

  public static final WallSignBlock STRIPPED_CRIMSON_HYPHAE_WALL_SIGN = register("stripped_crimson_hyphae_wall_sign", Blocks.STRIPPED_CRIMSON_HYPHAE);

  public static final WallSignBlock STRIPPED_WARPED_HYPHAE_WALL_SIGN = register("stripped_warped_hyphae_wall_sign", Blocks.STRIPPED_WARPED_HYPHAE);

  static {
    OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/oak_log");
    SPRUCE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/spruce_log");
    BIRCH_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/birch_log");
    JUNGLE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/jungle_log");
    ACACIA_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/acacia_log");
    CHERRY_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/cherry_log");
    DARK_OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/dark_oak_log");
    PALE_OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/pale_oak_log");
    MANGROVE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/mangrove_log");
    CRIMSON_HYPHAE_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/crimson_stem");
    WARPED_HYPHAE_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/warped_stem");
    STRIPPED_OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_oak_log");
    STRIPPED_SPRUCE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_spruce_log");
    STRIPPED_BIRCH_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_birch_log");
    STRIPPED_JUNGLE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_jungle_log");
    STRIPPED_ACACIA_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_acacia_log");
    STRIPPED_CHERRY_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_cherry_log");
    STRIPPED_DARK_OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_dark_oak_log");
    STRIPPED_PALE_OAK_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_pale_oak_log");
    STRIPPED_MANGROVE_WOOD_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_mangrove_log");
    STRIPPED_CRIMSON_HYPHAE_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_crimson_stem");
    STRIPPED_WARPED_HYPHAE_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/stripped_warped_stem");
  }

  public static final WallSignBlock OAK_WALL_SIGN = register("oak_wall_sign", Blocks.OAK_PLANKS);

  public static final WallSignBlock SPRUCE_WALL_SIGN = register("spruce_wall_sign", Blocks.SPRUCE_PLANKS);

  public static final WallSignBlock BIRCH_WALL_SIGN = register("birch_wall_sign", Blocks.BIRCH_PLANKS);

  public static final WallSignBlock JUNGLE_WALL_SIGN = register("jungle_wall_sign", Blocks.JUNGLE_PLANKS);

  public static final WallSignBlock ACACIA_WALL_SIGN = register("acacia_wall_sign", Blocks.ACACIA_PLANKS);
  @ApiStatus.AvailableSince("1.1.1-mc1.19.4")
  public static final WallSignBlock CHERRY_WALL_SIGN = register("cherry_wall_sign", Blocks.CHERRY_PLANKS);

  public static final WallSignBlock DARK_OAK_WALL_SIGN = register("dark_oak_wall_sign", Blocks.DARK_OAK_PLANKS);

  public static final WallSignBlock PALE_OAK_WALL_SIGN = register("pale_oak_wall_sign", Blocks.PALE_OAK_PLANKS);

  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final WallSignBlock MANGROVE_WALL_SIGN = register("mangrove_wall_sign", Blocks.MANGROVE_PLANKS);

  public static final WallSignBlock CRIMSON_WALL_SIGN = register("crimson_wall_sign", Blocks.CRIMSON_PLANKS);

  public static final WallSignBlock WARPED_WALL_SIGN = register("warped_wall_sign", Blocks.WARPED_PLANKS);

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_WALL_SIGN = register("bamboo_wall_sign", Blocks.BAMBOO_BLOCK, Block.Properties.ofFullCopy(Blocks.BAMBOO_BLOCK).mapColor(MapColor.PLANT));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_PLANK_WALL_SIGN = register("bamboo_plank_wall_sign", Blocks.BAMBOO_PLANKS, Block.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS));

  @ApiStatus.AvailableSince("1.0.4-mc1.19.3")
  @MiningLevel(MiningLevel.Tool.AXE)
  public static final WallSignBlock BAMBOO_MOSAIC_WALL_SIGN = register("bamboo_mosaic_wall_sign", Blocks.BAMBOO_MOSAIC, Block.Properties.ofFullCopy(Blocks.BAMBOO_MOSAIC));

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_WOODEN_WALL_SIGN = registerColored("colored_wooden_wall_sign", ColoredBlocks.COLORED_PLANKS);

  // 混凝土
  public static final ColorCollection<WallSignBlock> CONCRETE_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_concrete_wall_sign", Blocks.CONCRETE.pick(dyeColor)));

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_CONCRETE_WALL_SIGN = registerColored("colored_concrete_wall_sign", ColoredBlocks.COLORED_CONCRETE);

  public static final ColorCollection<WallSignBlock> DYED_TERRACOTTA_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> register(s + "_terracotta_wall_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  // 陶瓦
  public static final ColorCollection<GlowingWallSignBlock> GLOWING_CONCRETE_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_concrete_wall_sign", Blocks.CONCRETE.pick(dyeColor)));

  @ApiStatus.AvailableSince("0.2.2")
  public static final ColoredWallSignBlock COLORED_TERRACOTTA_WALL_SIGN = registerColored("colored_terracotta_wall_sign", ColoredBlocks.COLORED_TERRACOTTA);

  // 发光的混凝土
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_CONCRETE_WALL_SIGN = registerColoredGlowing("colored_glowing_concrete_wall_sign", ColoredBlocks.COLORED_CONCRETE);
  public static final ColorCollection<GlowingWallSignBlock> GLOWING_DYED_TERRACOTTA_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerGlowing("glowing_" + s + "_terracotta_wall_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  // 发光的陶瓦
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_TERRACOTTA_WALL_SIGN = registerColoredGlowing("colored_glowing_terracotta_wall_sign", ColoredBlocks.COLORED_TERRACOTTA);

  // 一些比较杂项的
  /// 石头
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_WALL_SIGN = register("stone_wall_sign", Blocks.STONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_WALL_SIGN = registerGlowing("glowing_stone_wall_sign", Blocks.STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_WALL_SIGN = registerColored("colored_stone_wall_sign", ColoredBlocks.COLORED_STONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_WALL_SIGN = registerColoredGlowing("colored_glowing_stone_wall_sign", ColoredBlocks.COLORED_STONE);
  /// 圆石
  @ApiStatus.AvailableSince("0.2.4")
  public static final WallSignBlock COBBLESTONE_WALL_SIGN = register("cobblestone_wall_sign", Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("0.2.4")
  public static final GlowingWallSignBlock GLOWING_COBBLESTONE_WALL_SIGN = registerGlowing("glowing_cobblestone_wall_sign", Blocks.COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_COBBLESTONE_WALL_SIGN = registerColored("colored_cobblestone_wall_sign", ColoredBlocks.COLORED_COBBLESTONE);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_COBBLESTONE_WALL_SIGN = registerColoredGlowing("colored_glowing_cobblestone_wall_sign", ColoredBlocks.COLORED_COBBLESTONE);
  /// 石砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock STONE_BRICK_WALL_SIGN = register("stone_brick_wall_sign", Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_STONE_BRICK_WALL_SIGN = registerGlowing("glowing_stone_brick_wall_sign", Blocks.STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredWallSignBlock COLORED_STONE_BRICK_WALL_SIGN = registerColored("colored_stone_brick_wall_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  @ApiStatus.AvailableSince("1.0.2")
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_STONE_BRICK_WALL_SIGN = registerColoredGlowing("colored_glowing_stone_brick_wall_sign", ColoredBlocks.COLORED_STONE_BRICKS);
  // 铁块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final WallSignBlock IRON_WALL_SIGN = register("iron_wall_sign", Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingWallSignBlock GLOWING_IRON_WALL_SIGN = registerGlowing("glowing_iron_wall_sign", Blocks.IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredWallSignBlock COLORED_IRON_WALL_SIGN = registerColored("colored_iron_wall_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  @ApiStatus.AvailableSince("1.0.2")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final ColoredGlowingWallSignBlock COLORED_GLOWING_IRON_WALL_SIGN = registerColoredGlowing("colored_glowing_iron_wall_sign", ColoredBlocks.COLORED_IRON_BLOCK);
  // 金块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock GOLD_WALL_SIGN = register("gold_wall_sign", Blocks.GOLD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_GOLD_WALL_SIGN = registerGlowing("glowing_gold_wall_sign", Blocks.GOLD_BLOCK);
  // 钻石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock DIAMOND_WALL_SIGN = register("diamond_wall_sign", Blocks.DIAMOND_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_DIAMOND_WALL_SIGN = registerGlowing("glowing_diamond_wall_sign", Blocks.DIAMOND_BLOCK);
  // 绿宝石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final WallSignBlock EMERALD_WALL_SIGN = register("emerald_wall_sign", Blocks.EMERALD_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.IRON)
  public static final GlowingWallSignBlock GLOWING_EMERALD_WALL_SIGN = registerGlowing("glowing_emerald_wall_sign", Blocks.EMERALD_BLOCK);
  // 青金石块
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final WallSignBlock LAPIS_WALL_SIGN = register("lapis_wall_sign", Blocks.LAPIS_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(level = MiningLevel.Level.STONE)
  public static final GlowingWallSignBlock GLOWING_LAPIS_WALL_SIGN = registerGlowing("glowing_lapis_wall_sign", Blocks.LAPIS_BLOCK);
  // 下界合金块
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock NETHERITE_WALL_SIGN = register("netherite_wall_sign", Blocks.NETHERITE_BLOCK);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_NETHERITE_WALL_SIGN = registerGlowing("glowing_netherite_wall_sign", Blocks.NETHERITE_BLOCK);
  // 黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock OBSIDIAN_WALL_SIGN = register("obsidian_wall_sign", Blocks.OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_OBSIDIAN_WALL_SIGN = registerGlowing("glowing_obsidian_wall_sign", Blocks.OBSIDIAN);
  // 哭泣的黑曜石
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final WallSignBlock CRYING_OBSIDIAN_WALL_SIGN = register("crying_obsidian_wall_sign", Blocks.CRYING_OBSIDIAN);
  @ApiStatus.AvailableSince("0.2.4")
  @MiningLevel(level = MiningLevel.Level.DIAMOND)
  public static final GlowingWallSignBlock GLOWING_CRYING_OBSIDIAN_WALL_SIGN = registerGlowing("glowing_crying_obsidian_wall_sign", Blocks.CRYING_OBSIDIAN);
  // 下界岩
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHERRACK_WALL_SIGN = register("netherrack_wall_sign", Blocks.NETHERRACK);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHERRACK_WALL_SIGN = registerGlowing("glowing_netherrack_wall_sign", Blocks.NETHERRACK);
  // 下界砖
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock NETHER_BRICK_WALL_SIGN = register("nether_brick_wall_sign", Blocks.NETHER_BRICKS);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_NETHER_BRICK_WALL_SIGN = registerGlowing("glowing_nether_brick_wall_sign", Blocks.NETHER_BRICKS);
  // 黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLACKSTONE_WALL_SIGN = register("blackstone_wall_sign", Blocks.BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLACKSTONE_WALL_SIGN = registerGlowing("glowing_blackstone_wall_sign", Blocks.BLACKSTONE);
  // 磨制黑石
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock POLISHED_BLACKSTONE_WALL_SIGN = register("polished_blackstone_wall_sign", Blocks.POLISHED_BLACKSTONE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_POLISHED_BLACKSTONE_WALL_SIGN = registerGlowing("glowing_polished_blackstone_wall_sign", Blocks.POLISHED_BLACKSTONE);

  static {
    GLOWING_NETHERRACK_WALL_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_NETHER_BRICK_WALL_SIGN.glowTexture = Identifier.withDefaultNamespace("block/lava_still");
    GLOWING_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
    GLOWING_POLISHED_BLACKSTONE_WALL_SIGN.glowTexture = Identifier.withDefaultNamespace("block/glowstone");
  }

  // 雪
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final WallSignBlock SNOW_WALL_SIGN = register("snow_wall_sign", Blocks.SNOW_BLOCK);
  @ApiStatus.AvailableSince("0.1.7")
  @MiningLevel(MiningLevel.Tool.SHOVEL)
  public static final GlowingWallSignBlock GLOWING_SNOW_WALL_SIGN = registerGlowing("glowing_snow_wall_sign", Blocks.SNOW_BLOCK);
  // 冰
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock ICE_WALL_SIGN = register("ice_wall_sign", Blocks.ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock PACKED_ICE_WALL_SIGN = register("packed_ice_wall_sign", Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_PACKED_ICE_WALL_SIGN = registerGlowing("glowing_packed_ice_wall_sign", Blocks.PACKED_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final WallSignBlock BLUE_ICE_WALL_SIGN = register("blue_ice_wall_sign", Blocks.BLUE_ICE);
  @ApiStatus.AvailableSince("0.1.7")
  public static final GlowingWallSignBlock GLOWING_BLUE_ICE_WALL_SIGN = registerGlowing("glowing_blue_ice_wall_sign", Blocks.BLUE_ICE);

  static {
    SNOW_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/snow");
    GLOWING_SNOW_WALL_SIGN.texture = Identifier.withDefaultNamespace("block/snow");
  }

  // 完整的混凝土

  public static final ColorCollection<FullWallSignBlock> FULL_CONCRETE_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerFull("full_" + s + "_concrete_wall_sign", Blocks.CONCRETE.pick(dyeColor)));

  // 完整的陶瓦

  public static final ColorCollection<FullWallSignBlock> FULL_DYED_TERRACOTTA_WALL_SIGN = ColorCollection.zipMap(ColorCollection.NAMES, ColorCollection.VALUES, (s, dyeColor) -> registerFull("full_" + s + "_terracotta_wall_sign", Blocks.DYED_TERRACOTTA.pick(dyeColor)));

  private WallSignBlocks() {
  }

  private static WallSignBlock register(String name, Block baseBlock, BlockBehaviour.Properties settings) {
    return MishangucBlocks.register(name, settings1 -> new WallSignBlock(baseBlock, settings1), settings);
  }

  private static WallSignBlock register(String name, Block baseBlock) {
    return register(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static FullWallSignBlock registerFull(String name, @Nullable Block baseBlock, BlockBehaviour.Properties settings) {
    return MishangucBlocks.register(name, settings1 -> new FullWallSignBlock(baseBlock, settings1), settings);
  }

  private static FullWallSignBlock registerFull(String name, Block baseBlock) {
    return registerFull(name, baseBlock, BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static GlowingWallSignBlock registerGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings1 -> new GlowingWallSignBlock(baseBlock, settings1), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static ColoredWallSignBlock registerColored(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredWallSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }

  private static ColoredGlowingWallSignBlock registerColoredGlowing(String name, Block baseBlock) {
    return MishangucBlocks.register(name, settings -> new ColoredGlowingWallSignBlock(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
  }
}
