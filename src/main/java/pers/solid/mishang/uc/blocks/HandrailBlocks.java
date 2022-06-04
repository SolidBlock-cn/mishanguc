package pers.solid.mishang.uc.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.annotations.RegisterIdentifier;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.SimpleHandrailBlock;

import java.util.EnumMap;
import java.util.Map;

@ApiStatus.AvailableSince("0.1.7")
public final class HandrailBlocks extends MishangucBlocks {

  // 简单的混凝土栏杆

  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_WHITE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_ORANGE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_YELLOW_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIME_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PINK_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GRAY_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_CYAN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PURPLE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLUE_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BROWN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GREEN_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_RED_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_CONCRETE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLACK_CONCRETE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_CONCRETE);

  /**
   * 颜色及其对应的简单混凝土栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_CONCRETE_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_CONCRETE_HANDRAIL);
    SIMPLE_CONCRETE_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_CONCRETE_HANDRAIL);
  }


  // 简单的陶瓦栏杆

  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_WHITE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_ORANGE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_YELLOW_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIME_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PINK_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GRAY_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_CYAN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PURPLE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLUE_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BROWN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GREEN_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_RED_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_TERRACOTTA);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLACK_TERRACOTTA_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_TERRACOTTA);

  /**
   * 颜色及其对应的简单陶瓦栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_TERRACOTTA_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_TERRACOTTA_HANDRAIL);
    SIMPLE_TERRACOTTA_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_TERRACOTTA_HANDRAIL);
  }

  // 冰雪。
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.ICE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PACKED_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.PACKED_ICE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLUE_ICE_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_ICE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_SNOW_HANDRAIL = new SimpleHandrailBlock(Blocks.SNOW_BLOCK);

  static {
    SIMPLE_SNOW_HANDRAIL.texture = "block/snow";
  }

  // 木头
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_OAK_HANDRAIL = new SimpleHandrailBlock(Blocks.OAK_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_SPRUCE_HANDRAIL = new SimpleHandrailBlock(Blocks.SPRUCE_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BIRCH_HANDRAIL = new SimpleHandrailBlock(Blocks.BIRCH_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_JUNGLE_HANDRAIL = new SimpleHandrailBlock(Blocks.JUNGLE_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_ACACIA_HANDRAIL = new SimpleHandrailBlock(Blocks.ACACIA_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_DARK_OAK_HANDRAIL = new SimpleHandrailBlock(Blocks.DARK_OAK_WOOD);
  @RegisterIdentifier
  @ApiStatus.AvailableSince("0.2.0-mc1.19")
  public static final SimpleHandrailBlock SIMPLE_MANGROVE_HANDRAIL = new SimpleHandrailBlock(Blocks.MANGROVE_WOOD);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_CRIMSON_HANDRAIL = new SimpleHandrailBlock(Blocks.CRIMSON_STEM);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_WARPED_HANDRAIL = new SimpleHandrailBlock(Blocks.CRIMSON_STEM);

  static {
    SIMPLE_OAK_HANDRAIL.texture = "block/oak_log";
    SIMPLE_SPRUCE_HANDRAIL.texture = "block/spruce_log";
    SIMPLE_BIRCH_HANDRAIL.texture = "block/birch_log";
    SIMPLE_JUNGLE_HANDRAIL.texture = "block/jungle_log";
    SIMPLE_ACACIA_HANDRAIL.texture = "block/acacia_log";
    SIMPLE_DARK_OAK_HANDRAIL.texture = "block/dark_oak_log";
    SIMPLE_MANGROVE_HANDRAIL.texture = "block/mangrove_log";
    SIMPLE_CRIMSON_HANDRAIL.texture = "block/crimson_stem";
    SIMPLE_WARPED_HANDRAIL.texture = "block/warped_stem";
  }

  // 染色玻璃。

  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_DIRT_HANDRAIL = new SimpleHandrailBlock(Blocks.DIRT);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_STONE_HANDRAIL = new SimpleHandrailBlock(Blocks.STONE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_COBBLESTONE_HANDRAIL = new SimpleHandrailBlock(Blocks.COBBLESTONE);
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_MOSSY_COBBLESTONE_HANDRAIL = new SimpleHandrailBlock(Blocks.MOSSY_COBBLESTONE);

  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_WHITE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.WHITE_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_ORANGE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.ORANGE_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_MAGENTA_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.MAGENTA_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_BLUE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_BLUE_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_YELLOW_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.YELLOW_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIME_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIME_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PINK_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.PINK_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GRAY_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.GRAY_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_LIGHT_GRAY_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.LIGHT_GRAY_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_CYAN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.CYAN_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_PURPLE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.PURPLE_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLUE_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BLUE_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BROWN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BROWN_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_GREEN_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.GREEN_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_RED_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.RED_STAINED_GLASS);
  @Translucent
  @RegisterIdentifier
  public static final SimpleHandrailBlock SIMPLE_BLACK_STAINED_GLASS_HANDRAIL = new SimpleHandrailBlock(Blocks.BLACK_STAINED_GLASS);

  /**
   * 颜色及其对应的染色玻璃栏杆组成的映射。
   */
  public static final Map<DyeColor, SimpleHandrailBlock> SIMPLE_STAINED_GLASS_HANDRAILS = new EnumMap<>(DyeColor.class);

  static {
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.WHITE, SIMPLE_WHITE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.ORANGE, SIMPLE_ORANGE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.MAGENTA, SIMPLE_MAGENTA_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIGHT_BLUE, SIMPLE_LIGHT_BLUE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.YELLOW, SIMPLE_YELLOW_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIME, SIMPLE_LIME_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.PINK, SIMPLE_PINK_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.GRAY, SIMPLE_GRAY_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.LIGHT_GRAY, SIMPLE_LIGHT_GRAY_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.CYAN, SIMPLE_CYAN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.PURPLE, SIMPLE_PURPLE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BLUE, SIMPLE_BLUE_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BROWN, SIMPLE_BROWN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.GREEN, SIMPLE_GREEN_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.RED, SIMPLE_RED_STAINED_GLASS_HANDRAIL);
    SIMPLE_STAINED_GLASS_HANDRAILS.put(DyeColor.BLACK, SIMPLE_BLACK_STAINED_GLASS_HANDRAIL);
  }
}
