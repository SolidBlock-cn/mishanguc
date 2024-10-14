package pers.solid.mishang.uc.data;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.annotations.MiningLevel;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static pers.solid.mishang.uc.MishangUtils.*;

public class MishangucTagProvider extends FabricTagProvider.BlockTagProvider {
  public final @NotNull MishangucItemTagProvider affiliate;

  @SuppressWarnings("deprecation")
  protected MishangucTagBuilder<Block> getMishangucTagBuilder(TagKey<Block> tag) {
    final TagBuilder tagBuilder = getTagBuilder(tag);
    return new MishangucTagBuilder<>(tag, tagBuilder, block -> block.getRegistryEntry().registryKey());
  }

  protected final Map<TagKey<Block>, TagKey<Item>> blockTagsWithItem = new HashMap<>();

  protected final MishangucTagBuilder<Block> pickaxeMineable = blockTagOnly(BlockTags.PICKAXE_MINEABLE);
  protected final MishangucTagBuilder<Block> shovelMineable = blockTagOnly(BlockTags.SHOVEL_MINEABLE);
  protected final MishangucTagBuilder<Block> axeMineable = blockTagOnly(BlockTags.AXE_MINEABLE);
  protected final MishangucTagBuilder<Block> hoeMineable = blockTagOnly(BlockTags.HOE_MINEABLE);
  protected final MishangucTagBuilder<Block> needsStoneTool = blockTagOnly(BlockTags.NEEDS_STONE_TOOL);
  protected final MishangucTagBuilder<Block> needsIronTool = blockTagOnly(BlockTags.NEEDS_IRON_TOOL);
  protected final MishangucTagBuilder<Block> needsDiamondTool = blockTagOnly(BlockTags.NEEDS_DIAMOND_TOOL);

  protected static final Map<DyeColor, @NotNull TagKey<Block>> dyedBlockTags = ImmutableMap.<DyeColor, TagKey<Block>>builder()
      .put(DyeColor.BLACK, ConventionalBlockTags.BLACK_DYED)
      .put(DyeColor.BLUE, ConventionalBlockTags.BLUE_DYED)
      .put(DyeColor.BROWN, ConventionalBlockTags.BROWN_DYED)
      .put(DyeColor.CYAN, ConventionalBlockTags.CYAN_DYED)
      .put(DyeColor.GRAY, ConventionalBlockTags.GRAY_DYED)
      .put(DyeColor.GREEN, ConventionalBlockTags.GREEN_DYED)
      .put(DyeColor.LIGHT_BLUE, ConventionalBlockTags.LIGHT_BLUE_DYED)
      .put(DyeColor.LIGHT_GRAY, ConventionalBlockTags.LIGHT_GRAY_DYED)
      .put(DyeColor.LIME, ConventionalBlockTags.LIME_DYED)
      .put(DyeColor.MAGENTA, ConventionalBlockTags.MAGENTA_DYED)
      .put(DyeColor.ORANGE, ConventionalBlockTags.ORANGE_DYED)
      .put(DyeColor.PINK, ConventionalBlockTags.PINK_DYED)
      .put(DyeColor.PURPLE, ConventionalBlockTags.PURPLE_DYED)
      .put(DyeColor.RED, ConventionalBlockTags.RED_DYED)
      .put(DyeColor.WHITE, ConventionalBlockTags.WHITE_DYED)
      .put(DyeColor.YELLOW, ConventionalBlockTags.YELLOW_DYED)
      .build();
  protected static final Map<DyeColor, @NotNull TagKey<Item>> dyedItemTags = ImmutableMap.<DyeColor, TagKey<Item>>builder()
      .put(DyeColor.BLACK, ConventionalItemTags.BLACK_DYED)
      .put(DyeColor.BLUE, ConventionalItemTags.BLUE_DYED)
      .put(DyeColor.BROWN, ConventionalItemTags.BROWN_DYED)
      .put(DyeColor.CYAN, ConventionalItemTags.CYAN_DYED)
      .put(DyeColor.GRAY, ConventionalItemTags.GRAY_DYED)
      .put(DyeColor.GREEN, ConventionalItemTags.GREEN_DYED)
      .put(DyeColor.LIGHT_BLUE, ConventionalItemTags.LIGHT_BLUE_DYED)
      .put(DyeColor.LIGHT_GRAY, ConventionalItemTags.LIGHT_GRAY_DYED)
      .put(DyeColor.LIME, ConventionalItemTags.LIME_DYED)
      .put(DyeColor.MAGENTA, ConventionalItemTags.MAGENTA_DYED)
      .put(DyeColor.ORANGE, ConventionalItemTags.ORANGE_DYED)
      .put(DyeColor.PINK, ConventionalItemTags.PINK_DYED)
      .put(DyeColor.PURPLE, ConventionalItemTags.PURPLE_DYED)
      .put(DyeColor.RED, ConventionalItemTags.RED_DYED)
      .put(DyeColor.WHITE, ConventionalItemTags.WHITE_DYED)
      .put(DyeColor.YELLOW, ConventionalItemTags.YELLOW_DYED)
      .build();

  protected MishangucTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
    this.affiliate = new MishangucItemTagProvider(output, registriesFuture, this);
  }

  protected MishangucTagBuilder<Block> blockTagOnly(TagKey<Block> blockTagKey) {
    return getMishangucTagBuilder(blockTagKey);
  }

  protected MishangucTagBuilder<Block> blockTagOnly(String path) {
    return getMishangucTagBuilder(TagKey.of(RegistryKeys.BLOCK, Mishanguc.id(path)));
  }

  protected MishangucTagBuilder<Block> blockTagWithItem(TagKey<Block> blockTagKey, TagKey<Item> itemTagKey) {
    Preconditions.checkArgument(blockTagKey.id().equals(itemTagKey.id()));
    final var tag = getMishangucTagBuilder(blockTagKey);
    blockTagsWithItem.put(blockTagKey, itemTagKey);
    return tag;
  }

  protected MishangucTagBuilder<Block> blockTagWithItem(String path) {
    final TagKey<Block> tagKey = TagKey.of(RegistryKeys.BLOCK, Mishanguc.id(path));
    final var tag = getMishangucTagBuilder(tagKey);
    blockTagsWithItem.put(tagKey, TagKey.of(RegistryKeys.ITEM, Mishanguc.id(path)));
    return tag;
  }

  protected MishangucTagBuilder<Item> itemTag(TagKey<Item> tagKey) {
    return affiliate.getMishangucTagBuilder(tagKey);
  }

  protected MishangucTagBuilder<Item> itemTag(String path) {
    return affiliate.getMishangucTagBuilder(TagKey.of(RegistryKeys.ITEM, Mishanguc.id(path)));
  }

  protected void roads() {
    final var roadBlocks = blockTagWithItem("road_blocks");
    final var roadSlabs = blockTagWithItem("road_slabs"); // 数据包手动加入 #slabs 中
    // 上述两个标签手动加入 #mishanguc:roads 中
    final var roadMarks = blockTagWithItem("road_marks");

    MishangUtils.instanceStream(RoadBlocks.class, Block.class).forEach(roadBlocks::add);
    RoadSlabBlocks.SLABS.forEach(roadSlabs::add);
    MishangUtils.instanceStream(RoadMarkBlocks.class, Block.class).forEach(roadMarks::add);

    final var roads = blockTagWithItem("roads").addTag(roadBlocks, roadSlabs);
    pickaxeMineable.addTag(roads);
    blockTagOnly(BlockTags.SNOW_LAYER_CANNOT_SURVIVE_ON).addTag(roads);
  }

  protected void handrails() {
    // 混凝土栏杆部分
    final var simpleConcreteNormalHandrails = blockTagOnly("simple_concrete_normal_handrails");
    final var simpleConcreteCentralHandrails = blockTagOnly("simple_concrete_central_handrails");
    final var simpleConcreteCornerHandrails = blockTagOnly("simple_concrete_corner_handrails");
    final var simpleConcreteOuterHandrails = blockTagOnly("simple_concrete_outer_handrails");
    final var simpleConcreteStairHandrails = blockTagOnly("simple_concrete_stair_handrails");
    final var simpleConcreteHandrails = blockTagOnly("simple_concrete_handrails")
        .addTag(simpleConcreteNormalHandrails)
        .addTag(simpleConcreteCentralHandrails)
        .addTag(simpleConcreteCornerHandrails)
        .addTag(simpleConcreteOuterHandrails)
        .addTag(simpleConcreteStairHandrails);
    final var simpleConcreteHandrailItems = itemTag("simple_concrete_handrails");

    // 陶瓦栏杆部分
    final var simpleTerracottaNormalHandrails = blockTagOnly("simple_terracotta_normal_handrails");
    final var simpleTerracottaCentralHandrails = blockTagOnly("simple_terracotta_central_handrails");
    final var simpleTerracottaCornerHandrails = blockTagOnly("simple_terracotta_corner_handrails");
    final var simpleTerracottaOuterHandrails = blockTagOnly("simple_terracotta_outer_handrails");
    final var simpleTerracottaStairHandrails = blockTagOnly("simple_terracotta_stair_handrails");
    final var simpleTerracottaHandrails = blockTagOnly("simple_terracotta_handrails")
        .addTag(simpleTerracottaNormalHandrails)
        .addTag(simpleTerracottaCentralHandrails)
        .addTag(simpleTerracottaCornerHandrails)
        .addTag(simpleTerracottaOuterHandrails)
        .addTag(simpleTerracottaStairHandrails);
    final var simpleTerracottaHandrailItems = itemTag("simple_terracotta_handrails");

    // 染色玻璃栏杆部分
    final var simpleStainedGlassNormalHandrails = blockTagOnly("simple_stained_glass_normal_handrails");
    final var simpleStainedGlassCentralHandrails = blockTagOnly("simple_stained_glass_central_handrails");
    final var simpleStainedGlassCornerHandrails = blockTagOnly("simple_stained_glass_corner_handrails");
    final var simpleStainedGlassOuterHandrails = blockTagOnly("simple_stained_glass_outer_handrails");
    final var simpleStainedGlassStairHandrails = blockTagOnly("simple_stained_glass_stair_handrails");
    final var simpleStainedGlassHandrails = blockTagOnly("simple_stained_glass_handrails")
        .addTag(simpleStainedGlassNormalHandrails)
        .addTag(simpleStainedGlassCentralHandrails)
        .addTag(simpleStainedGlassCornerHandrails)
        .addTag(simpleStainedGlassOuterHandrails)
        .addTag(simpleStainedGlassStairHandrails);
    final var simpleStainedGlassHandrailItems = itemTag("simple_stained_glass_handrails");

    // 染色木头部分
    final var simpleWoodenNormalHandrails = blockTagOnly("simple_wooden_normal_handrails");
    final var simpleWoodenCentralHandrails = blockTagOnly("simple_wooden_central_handrails");
    final var simpleWoodenCornerHandrails = blockTagOnly("simple_wooden_corner_handrails");
    final var simpleWoodenOuterHandrails = blockTagOnly("simple_wooden_outer_handrails");
    final var simpleWoodenStairHandrails = blockTagOnly("simple_wooden_stair_handrails");
    final var simpleWoodenHandrails = blockTagOnly("simple_wooden_handrails")
        .addTag(simpleWoodenNormalHandrails, simpleWoodenCentralHandrails, simpleWoodenCornerHandrails, simpleWoodenOuterHandrails, simpleWoodenStairHandrails);
    final var simpleWoodenHandrailItems = itemTag("simple_wooden_handrails");

    // 所有的简单栏杆
    final var simpleNormalHandrails = blockTagOnly("simple_normal_handrails")
        .addTag(simpleConcreteNormalHandrails, simpleTerracottaNormalHandrails, simpleStainedGlassHandrails, simpleWoodenHandrails);
    final var simpleCentralHandrails = blockTagOnly("simple_central_handrails")
        .addTag(simpleConcreteCentralHandrails, simpleTerracottaCentralHandrails, simpleStainedGlassCentralHandrails, simpleWoodenCentralHandrails);
    final var simpleCornerHandrails = blockTagOnly("simple_corner_handrails")
        .addTag(simpleConcreteCornerHandrails, simpleTerracottaCornerHandrails, simpleStainedGlassCornerHandrails, simpleWoodenCornerHandrails);
    final var simpleOuterHandrails = blockTagOnly("simple_outer_handrails")
        .addTag(simpleConcreteOuterHandrails, simpleTerracottaOuterHandrails, simpleStainedGlassOuterHandrails, simpleWoodenOuterHandrails);
    final var simpleStairHandrails = blockTagOnly("simple_stair_handrails")
        .addTag(simpleConcreteStairHandrails, simpleTerracottaStairHandrails, simpleStainedGlassStairHandrails, simpleWoodenStairHandrails);
    final var simpleHandrails = blockTagOnly("simple_handrails")
        .addTag(simpleNormalHandrails, simpleCentralHandrails, simpleCornerHandrails, simpleOuterHandrails, simpleStairHandrails);
    final var simpleHandrailItems = itemTag("simple_handrails")
        .addTag(simpleConcreteHandrailItems, simpleTerracottaHandrailItems, simpleStainedGlassHandrailItems, simpleWoodenHandrailItems);

    // 玻璃栏杆部分
    final var glassNormalHandrails = blockTagOnly("glass_normal_handrails");
    final var glassCentralHandrails = blockTagOnly("glass_central_handrails");
    final var glassCornerHandrails = blockTagOnly("glass_corner_handrails");
    final var glassOuterHandrails = blockTagOnly("glass_outer_handrails");
    final var glassStairHandrails = blockTagOnly("glass_stair_handrails");
    final var glassHandrails = blockTagOnly("glass_handrails").addTag(glassNormalHandrails, glassCentralHandrails, glassCornerHandrails, glassOuterHandrails, glassStairHandrails);
    final var glassHandrailItems = itemTag("glass_handrails");

    final var normalHandrails = blockTagOnly("normal_handrails")
        .addTag(glassNormalHandrails)
        .addTag(simpleNormalHandrails);
    final var centralHandrails = blockTagOnly("central_handrails")
        .addTag(glassCentralHandrails)
        .addTag(simpleCentralHandrails);
    final var cornerHandrails = blockTagOnly("corner_handrails")
        .addTag(glassCornerHandrails)
        .addTag(simpleCornerHandrails);
    final var outerHandrails = blockTagOnly("outer_handrails")
        .addTag(glassOuterHandrails)
        .addTag(simpleOuterHandrails);
    final var stairHandrails = blockTagOnly("stair_handrails")
        .addTag(glassStairHandrails)
        .addTag(simpleStairHandrails);
    final var handrails = blockTagOnly("handrails")
        .addTag(normalHandrails)
        .addTag(centralHandrails)
        .addTag(cornerHandrails)
        .addTag(outerHandrails)
        .addTag(stairHandrails);
    final var handrailItems = itemTag("handrails")
        .addTag(simpleStainedGlassHandrailItems)
        .addTag(simpleTerracottaHandrailItems)
        .addTag(simpleConcreteHandrailItems)
        .addTag(simpleWoodenHandrailItems);

    MishangUtils.instanceEntryStream(HandrailBlocks.class, Block.class).forEach(entry -> {
      final Field field = entry.getKey();
      final Block block = entry.getValue();
      if (block instanceof final SimpleHandrailBlock simpleHandrailBlock) {
        if (MishangUtils.isStained_glass(simpleHandrailBlock.baseBlock)) {
          simpleStainedGlassNormalHandrails.add(simpleHandrailBlock);
          simpleStainedGlassCentralHandrails.add(simpleHandrailBlock.central);
          simpleStainedGlassCornerHandrails.add(simpleHandrailBlock.corner);
          simpleStainedGlassOuterHandrails.add(simpleHandrailBlock.outer);
          simpleStainedGlassStairHandrails.add(simpleHandrailBlock.stair);
          simpleStainedGlassHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isConcrete(simpleHandrailBlock.baseBlock)) {
          simpleConcreteNormalHandrails.add(simpleHandrailBlock);
          simpleConcreteCentralHandrails.add(simpleHandrailBlock.central);
          simpleConcreteCornerHandrails.add(simpleHandrailBlock.corner);
          simpleConcreteOuterHandrails.add(simpleHandrailBlock.outer);
          simpleConcreteStairHandrails.add(simpleHandrailBlock.stair);
          simpleConcreteHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isTerracotta(simpleHandrailBlock.baseBlock)) {
          simpleTerracottaNormalHandrails.add(simpleHandrailBlock);
          simpleTerracottaCentralHandrails.add(simpleHandrailBlock.central);
          simpleTerracottaCornerHandrails.add(simpleHandrailBlock.corner);
          simpleTerracottaOuterHandrails.add(simpleHandrailBlock.outer);
          simpleTerracottaStairHandrails.add(simpleHandrailBlock.stair);
          simpleTerracottaHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isWood(simpleHandrailBlock.baseBlock) || isPlanks(simpleHandrailBlock.baseBlock)) {
          simpleWoodenNormalHandrails.add(simpleHandrailBlock);
          simpleWoodenCentralHandrails.add(simpleHandrailBlock.central);
          simpleWoodenCornerHandrails.add(simpleHandrailBlock.corner);
          simpleWoodenOuterHandrails.add(simpleHandrailBlock.outer);
          simpleWoodenStairHandrails.add(simpleHandrailBlock.stair);
          simpleWoodenHandrailItems.add(simpleHandrailBlock.asItem());
        } else {
          simpleNormalHandrails.add(simpleHandrailBlock);
          simpleCentralHandrails.add(simpleHandrailBlock.central);
          simpleCornerHandrails.add(simpleHandrailBlock.corner);
          simpleOuterHandrails.add(simpleHandrailBlock.outer);
          simpleStairHandrails.add(simpleHandrailBlock.stair);
          simpleHandrailItems.add(simpleHandrailBlock.asItem());
          configureMineableTags(field, simpleHandrailBlock.selfAndVariants());
        }
      } else if (block instanceof GlassHandrailBlock glassHandrailBlock) {
        glassNormalHandrails.add(glassHandrailBlock);
        glassCentralHandrails.add(glassHandrailBlock.central());
        glassCornerHandrails.add(glassHandrailBlock.corner());
        glassOuterHandrails.add(glassHandrailBlock.outer());
        glassStairHandrails.add(glassHandrailBlock.stair());
        configureMineableTags(field, glassHandrailBlock.selfAndVariants());
      }
    });

    configureColoredTags(HandrailBlocks.SIMPLE_CONCRETE_HANDRAILS);
    configureColoredTags(HandrailBlocks.SIMPLE_TERRACOTTA_HANDRAILS);
    configureColoredTags(HandrailBlocks.SIMPLE_STAINED_GLASS_HANDRAILS);
    configureColoredTags(HandrailBlocks.DECORATED_IRON_HANDRAILS);

    axeMineable.addTag(simpleWoodenHandrails);
    pickaxeMineable.addTag(simpleConcreteHandrails);
    pickaxeMineable.addTag(simpleTerracottaHandrails);
  }

  protected void coloredBlocks() {
    final MishangucTagBuilder<Block> colored = blockTagWithItem("colored");
    MishangUtils.blocks().stream().filter(Predicates.instanceOf(ColoredBlock.class)).forEach(colored::add);
    MishangUtils.instanceEntryStream(ColoredBlocks.class, Block.class).forEach(entry -> configureMineableTags(entry.getKey(), entry.getValue()));

    blockTagWithItem(ConventionalBlockTags.GLASS_BLOCKS, ConventionalItemTags.GLASS_BLOCKS).add(ColoredBlocks.COLORED_GLASS);
    blockTagWithItem(ConventionalBlockTags.GLASS_PANES, ConventionalItemTags.GLASS_PANES).add(ColoredBlocks.COLORED_GLASS_PANE);

    blockTagOnly(BlockTags.AZALEA_ROOT_REPLACEABLE).add(ColoredBlocks.COLORED_SNOW_BLOCK);
    blockTagOnly(BlockTags.BEACON_BASE_BLOCKS).add(ColoredBlocks.COLORED_IRON_BLOCK);
    blockTagOnly(BlockTags.DRAGON_IMMUNE).add(ColoredBlocks.COLORED_END_STONE, ColoredBlocks.COLORED_END_STONE_STAIRS, ColoredBlocks.COLORED_END_STONE_SLAB);
    blockTagOnly(BlockTags.GOATS_SPAWNABLE_ON).add(ColoredBlocks.COLORED_STONE);
    blockTagOnly(BlockTags.HOGLIN_REPELLENTS).add(ColoredBlocks.COLORED_NETHER_PORTAL);
    blockTagWithItem(BlockTags.LEAVES, ItemTags.LEAVES).add(MishangUtils.instanceStream(ColoredBlocks.class, ColoredLeavesBlock.class).toArray(Block[]::new));
    blockTagOnly(BlockTags.PORTALS).add(ColoredBlocks.COLORED_NETHER_PORTAL);
    blockTagOnly(BlockTags.SCULK_REPLACEABLE).add(ColoredBlocks.COLORED_DIRT, ColoredBlocks.COLORED_TERRACOTTA, ColoredBlocks.COLORED_END_STONE, ColoredBlocks.COLORED_END_STONE_STAIRS, ColoredBlocks.COLORED_END_STONE_SLAB);
    blockTagOnly(BlockTags.SNAPS_GOAT_HORN).add(ColoredBlocks.COLORED_STONE);
    blockTagOnly(BlockTags.TERRACOTTA).add(ColoredBlocks.COLORED_TERRACOTTA);
    blockTagOnly(ConventionalBlockTags.CONCRETES).add(ColoredBlocks.COLORED_CONCRETE);
    blockTagOnly(BlockTags.WOOL).add(ColoredBlocks.COLORED_WOOL);

    blockTagOnly("tints_beacon_beams").add(ColoredBlocks.COLORED_GLASS, ColoredBlocks.COLORED_GLASS_PANE, ColoredBlocks.COLORED_ICE, ColoredBlocks.COLORED_NETHER_PORTAL);
  }

  protected void lights() {
    final var whiteStripWallLights = blockTagWithItem("white_strip_wall_lights");
    final var whiteWallLights = blockTagWithItem("white_wall_lights").addTag(whiteStripWallLights);
    final var whiteCornerLights = blockTagWithItem("white_corner_lights");
    final var whiteLightDecorations = blockTagWithItem("white_light_decorations");
    final var whiteColumnLights = blockTagWithItem("white_column_lights");
    final var whiteLight = blockTagWithItem("white_lights").addTag(whiteWallLights, whiteColumnLights, whiteColumnLights, whiteColumnLights).add(LightBlocks.WHITE_LIGHT, LightBlocks.WHITE_LIGHT_SLAB, LightBlocks.WHITE_LIGHT_COVER);

    final var yellowStripWallLights = blockTagWithItem("yellow_strip_wall_lights");
    final var yellowWallLights = blockTagWithItem("yellow_wall_lights").addTag(yellowStripWallLights);
    final var yellowCornerLights = blockTagWithItem("yellow_corner_lights");
    final var yellowLightDecorations = blockTagWithItem("yellow_light_decorations");
    final var yellowColumnLights = blockTagWithItem("yellow_column_lights");
    final var yellowLight = blockTagWithItem("yellow_lights").addTag(yellowWallLights, yellowColumnLights, yellowColumnLights, yellowColumnLights).add(LightBlocks.YELLOW_LIGHT, LightBlocks.YELLOW_LIGHT_SLAB, LightBlocks.YELLOW_LIGHT_COVER);

    final var orangeStripWallLights = blockTagWithItem("orange_strip_wall_lights");
    final var orangeWallLights = blockTagWithItem("orange_wall_lights").addTag(orangeStripWallLights);
    final var orangeCornerLights = blockTagWithItem("orange_corner_lights");
    final var orangeLightDecorations = blockTagWithItem("orange_light_decorations");
    final var orangeColumnLights = blockTagWithItem("orange_column_lights");
    final var orangeLight = blockTagWithItem("orange_lights").addTag(orangeWallLights, orangeColumnLights, orangeColumnLights, orangeColumnLights).add(LightBlocks.ORANGE_LIGHT, LightBlocks.ORANGE_LIGHT_SLAB, LightBlocks.ORANGE_LIGHT_COVER);

    final var greenStripWallLights = blockTagWithItem("green_strip_wall_lights");
    final var greenWallLights = blockTagWithItem("green_wall_lights").addTag(greenStripWallLights);
    final var greenCornerLights = blockTagWithItem("green_corner_lights");
    final var greenLightDecorations = blockTagWithItem("green_light_decorations");
    final var greenColumnLights = blockTagWithItem("green_column_lights");
    final var greenLight = blockTagWithItem("green_lights").addTag(greenWallLights, greenColumnLights, greenColumnLights, greenColumnLights).add(LightBlocks.GREEN_LIGHT, LightBlocks.GREEN_LIGHT_SLAB, LightBlocks.GREEN_LIGHT_COVER);

    final var cyanStripWallLights = blockTagWithItem("cyan_strip_wall_lights");
    final var cyanWallLights = blockTagWithItem("cyan_wall_lights").addTag(cyanStripWallLights);
    final var cyanCornerLights = blockTagWithItem("cyan_corner_lights");
    final var cyanLightDecorations = blockTagWithItem("cyan_light_decorations");
    final var cyanColumnLights = blockTagWithItem("cyan_column_lights");
    final var cyanLight = blockTagWithItem("cyan_lights").addTag(cyanWallLights, cyanColumnLights, cyanColumnLights, cyanColumnLights).add(LightBlocks.CYAN_LIGHT, LightBlocks.CYAN_LIGHT_SLAB, LightBlocks.CYAN_LIGHT_COVER);

    final var pinkStripWallLights = blockTagWithItem("pink_strip_wall_lights");
    final var pinkWallLights = blockTagWithItem("pink_wall_lights").addTag(pinkStripWallLights);
    final var pinkCornerLights = blockTagWithItem("pink_corner_lights");
    final var pinkLightDecorations = blockTagWithItem("pink_light_decorations");
    final var pinkColumnLights = blockTagWithItem("pink_column_lights");
    final var pinkLight = blockTagWithItem("pink_lights").addTag(pinkWallLights, pinkColumnLights, pinkColumnLights, pinkColumnLights).add(LightBlocks.PINK_LIGHT, LightBlocks.PINK_LIGHT_SLAB, LightBlocks.PINK_LIGHT_COVER);

    // 通过数据包手动加入到 #slabs 中
    final var lightSlabs = blockTagWithItem("light_slabs");
    final var lightCovers = blockTagWithItem("light_covers");

    // 其他的灯标签
    blockTagWithItem("wall_lights").addTag(whiteWallLights, yellowWallLights, cyanWallLights, greenWallLights, orangeWallLights, pinkWallLights);
    blockTagWithItem("column_lights").addTag(whiteColumnLights, yellowColumnLights, cyanColumnLights, greenColumnLights, orangeColumnLights, pinkColumnLights);
    blockTagWithItem("corner_lights").addTag(whiteCornerLights, yellowCornerLights, cyanCornerLights, greenCornerLights, orangeCornerLights, pinkCornerLights);
    blockTagWithItem("light_decorations").addTag(whiteLightDecorations, yellowLightDecorations, cyanLightDecorations, greenLightDecorations, orangeLightDecorations, pinkLightDecorations);
    blockTagWithItem("lights").addTag(whiteLight, yellowLight, cyanLight, greenLight, orangeLight, pinkLight);

    // 只指定颜色的标签，只指定形状的标签，以及不指定颜色和形状的标签，均在数据包中

    MishangUtils.instanceEntryStream(LightBlocks.class, Block.class).forEach(
        entry -> {
          final Block block = entry.getValue();
          if (block instanceof StripWallLightBlock) {
            switch (((StripWallLightBlock) block).lightColor) {
              case "white" -> whiteStripWallLights.add(block);
              case "yellow" -> yellowStripWallLights.add(block);
              case "cyan" -> cyanStripWallLights.add(block);
              case "orange" -> orangeStripWallLights.add(block);
              case "green" -> greenStripWallLights.add(block);
              case "pink" -> pinkStripWallLights.add(block);
            }
          } else if (block instanceof AutoConnectWallLightBlock) {
            switch (((AutoConnectWallLightBlock) block).lightColor) {
              case "white" -> whiteLightDecorations.add(block);
              case "yellow" -> yellowLightDecorations.add(block);
              case "cyan" -> cyanLightDecorations.add(block);
              case "orange" -> orangeLightDecorations.add(block);
              case "green" -> greenLightDecorations.add(block);
              case "pink" -> pinkLightDecorations.add(block);
            }
          } else if (block instanceof ColumnLightBlock || block instanceof ColumnWallLightBlock) {
            switch (block instanceof ColumnLightBlock ? ((ColumnLightBlock) block).lightColor : ((ColumnWallLightBlock) block).lightColor) {
              case "white" -> whiteColumnLights.add(block);
              case "yellow" -> yellowColumnLights.add(block);
              case "cyan" -> cyanColumnLights.add(block);
              case "orange" -> orangeColumnLights.add(block);
              case "green" -> greenColumnLights.add(block);
              case "pink" -> pinkColumnLights.add(block);
            }
          } else if (block instanceof WallLightBlock) {
            switch (((WallLightBlock) block).lightColor) {
              case "white" -> whiteWallLights.add(block);
              case "yellow" -> yellowWallLights.add(block);
              case "cyan" -> cyanWallLights.add(block);
              case "orange" -> orangeWallLights.add(block);
              case "green" -> greenWallLights.add(block);
              case "pink" -> pinkWallLights.add(block);
            }
          } else if (block instanceof CornerLightBlock) {
            switch (((CornerLightBlock) block).lightColor) {
              case "white" -> whiteCornerLights.add(block);
              case "yellow" -> yellowCornerLights.add(block);
              case "cyan" -> cyanCornerLights.add(block);
              case "orange" -> orangeCornerLights.add(block);
              case "green" -> greenCornerLights.add(block);
              case "pink" -> pinkCornerLights.add(block);
            }
          }
          if (block instanceof SlabBlock) {
            lightSlabs.add(block);
          } else if (block instanceof LightCoverBlock) {
            lightCovers.add(block);
          }
        }
    );
  }

  protected void signs() {
    wallSigns();
    hungSignsAndBars();
    standingSigns();
  }

  private void standingSigns() {
    final var woodenStandingSigns = blockTagWithItem("wooden_standing_signs");
    final var concreteStandingSigns = blockTagWithItem("concrete_standing_signs");
    final var terracottaStandingSigns = blockTagWithItem("terracotta_standing_signs");
    final var standingSigns = blockTagWithItem("standing_signs")
        .addTag(woodenStandingSigns, concreteStandingSigns, terracottaStandingSigns);

    final var glowingConcreteStandingSigns = blockTagWithItem("glowing_concrete_standing_signs");
    final var glowingTerracottaStandingSigns = blockTagWithItem("glowing_terracotta_standing_signs");
    final var glowingStandingSigns = blockTagWithItem("glowing_standing_signs")
        .addTag(glowingConcreteStandingSigns, glowingTerracottaStandingSigns);

    MishangUtils.instanceEntryStream(StandingSignBlocks.class, Block.class).forEach(entry -> {
      final Field field = entry.getKey();
      final Block block = entry.getValue();
      if (!(block instanceof StandingSignBlock standingSignBlock)) return;
      final Block baseBlock = standingSignBlock.baseBlock;
      if (block instanceof GlowingStandingSignBlock) {
        if (isConcrete(baseBlock)) {
          glowingConcreteStandingSigns.add(block);
        } else if (isTerracotta(baseBlock)) {
          glowingTerracottaStandingSigns.add(block);
        } else {
          glowingStandingSigns.add(block);
          configureMineableTags(field, block);
        }
      } else if (block instanceof StandingSignBlock) {
        if (isConcrete(baseBlock)) {
          concreteStandingSigns.add(block);
        } else if (isTerracotta(baseBlock)) {
          terracottaStandingSigns.add(block);
        } else if (isWooden(baseBlock)) {
          woodenStandingSigns.add(block);
        } else {
          standingSigns.add(block);
          configureMineableTags(field, block);
        }
      }
    });

    configureColoredTags(StandingSignBlocks.CONCRETE_STANDING_SIGNS);
    configureColoredTags(StandingSignBlocks.TERRACOTTA_STANDING_SIGNS);
    configureColoredTags(StandingSignBlocks.GLOWING_CONCRETE_STANDING_SIGNS);
    configureColoredTags(StandingSignBlocks.GLOWING_TERRACOTTA_STANDING_SIGNS);

    axeMineable.addTag(woodenStandingSigns);
    pickaxeMineable.addTag(concreteStandingSigns, glowingConcreteStandingSigns, terracottaStandingSigns, glowingTerracottaStandingSigns);
  }

  private void hungSignsAndBars() {
    final var woodenHungSigns = blockTagWithItem("wooden_hung_signs");
    final var concreteHungSigns = blockTagWithItem("concrete_hung_signs");
    final var terracottaHungSigns = blockTagWithItem("terracotta_hung_signs");
    final var hungSigns = blockTagWithItem("hung_signs")
        .addTag(woodenHungSigns, concreteHungSigns, terracottaHungSigns);

    final var glowingConcreteHungSigns = blockTagWithItem("glowing_concrete_hung_signs");
    final var glowingTerracottaHungSigns = blockTagWithItem("glowing_terracotta_hung_signs");
    final var glowingHungSigns = blockTagWithItem("glowing_hung_signs")
        .addTag(glowingConcreteHungSigns, glowingTerracottaHungSigns);

    final var woodenHungSignBars = blockTagWithItem("wooden_hung_sign_bars");
    final var concreteHungSignBars = blockTagWithItem("concrete_hung_sign_bars");
    final var terracottaHungSignBars = blockTagWithItem("terracotta_hung_sign_bars");
    final var hungSignBars = blockTagWithItem("hung_sign_bars")
        .addTag(woodenHungSignBars, concreteHungSignBars, terracottaHungSignBars);

    MishangUtils.instanceEntryStream(HungSignBlocks.class, Block.class).forEach(entry -> {
      final Field field = entry.getKey();
      final Block block = entry.getValue();
      if (block instanceof HungSignBlock hungSignBlock) {
        final Block baseBlock = hungSignBlock.baseBlock;
        if (block instanceof GlowingHungSignBlock) {
          if (isConcrete(baseBlock)) {
            glowingConcreteHungSigns.add(block);
          } else if (isTerracotta(baseBlock)) {
            glowingTerracottaHungSigns.add(block);
          } else {
            glowingHungSigns.add(block);
            configureMineableTags(field, block);
          }
        } else if (block instanceof HungSignBlock) {
          if (isConcrete(baseBlock)) {
            concreteHungSigns.add(block);
          } else if (isTerracotta(baseBlock)) {
            terracottaHungSigns.add(block);
          } else if (isWooden(baseBlock)) {
            woodenHungSigns.add(block);
          } else {
            hungSigns.add(block);
            configureMineableTags(field, block);
          }
        }
      } else if (block instanceof HungSignBarBlock hungSignBarBlock) {
        final Block baseBlock = hungSignBarBlock.baseBlock;
        if (isConcrete(baseBlock)) {
          concreteHungSignBars.add(block);
        } else if (isTerracotta(baseBlock)) {
          terracottaHungSignBars.add(block);
        } else if (isWood(baseBlock) || isStrippedWood(baseBlock)) {
          woodenHungSignBars.add(block);
        } else {
          hungSignBars.add(block);
          configureMineableTags(field, block);
        }
      }
    });
    configureColoredTags(HungSignBlocks.CONCRETE_HUNG_SIGNS);
    configureColoredTags(HungSignBlocks.CONCRETE_HUNG_SIGN_BARS);
    configureColoredTags(HungSignBlocks.GLOWING_CONCRETE_HUNG_SIGNS);
    configureColoredTags(HungSignBlocks.CONCRETE_HUNG_SIGN_BARS);
    configureColoredTags(HungSignBlocks.TERRACOTTA_HUNG_SIGNS);
    configureColoredTags(HungSignBlocks.TERRACOTTA_HUNG_SIGN_BARS);
    configureColoredTags(HungSignBlocks.GLOWING_TERRACOTTA_HUNG_SIGNS);
    configureColoredTags(HungSignBlocks.TERRACOTTA_HUNG_SIGN_BARS);

    axeMineable.addTag(woodenHungSigns, woodenHungSignBars);
    pickaxeMineable.addTag(concreteHungSigns, concreteHungSignBars, glowingConcreteHungSigns, terracottaHungSigns, terracottaHungSignBars, glowingTerracottaHungSigns);
  }

  private void wallSigns() {
    final var woodenWallSigns = blockTagWithItem("wooden_wall_signs");
    final var concreteWallSigns = blockTagWithItem("concrete_wall_signs");
    final var terracottaWallSigns = blockTagWithItem("terracotta_wall_signs");
    final var wallSigns = blockTagWithItem("wall_signs")
        .addTag(woodenWallSigns, concreteWallSigns, terracottaWallSigns);

    final var glowingConcreteWallSigns = blockTagWithItem("glowing_concrete_wall_signs");
    final var glowingTerracottaWallSigns = blockTagWithItem("glowing_terracotta_wall_signs");
    final var glowingWallSigns = blockTagWithItem("glowing_wall_signs")
        .addTag(glowingConcreteWallSigns, glowingTerracottaWallSigns);

    final var fullConcreteWallSigns = blockTagWithItem("full_concrete_wall_signs");
    final var fullTerracottaWallSigns = blockTagWithItem("full_terracotta_wall_signs");
    final var fullWallSigns = blockTagWithItem("full_wall_signs")
        .addTag(fullConcreteWallSigns, fullTerracottaWallSigns);

    MishangUtils.instanceEntryStream(WallSignBlocks.class, Block.class).forEach(entry -> {
      final Field field = entry.getKey();
      final Block block = entry.getValue();
      if (!(block instanceof WallSignBlock wallSignBlock)) return;
      final Block baseBlock = wallSignBlock.baseBlock;
      if (block instanceof GlowingWallSignBlock) {
        if (isConcrete(baseBlock)) {
          glowingConcreteWallSigns.add(block);
        } else if (isTerracotta(baseBlock)) {
          glowingTerracottaWallSigns.add(block);
        } else {
          glowingWallSigns.add(block);
          configureMineableTags(field, block);
        }
      } else if (block instanceof FullWallSignBlock) {
        if (isConcrete(baseBlock)) {
          fullConcreteWallSigns.add(block);
        } else if (isTerracotta(baseBlock)) {
          fullTerracottaWallSigns.add(block);
        } else {
          fullWallSigns.add(block);
          configureMineableTags(field, block);
        }
      } else if (block instanceof WallSignBlock) {
        if (isConcrete(baseBlock)) {
          concreteWallSigns.add(block);
        } else if (isTerracotta(baseBlock)) {
          terracottaWallSigns.add(block);
        } else if (isWooden(baseBlock)) {
          woodenWallSigns.add(block);
        } else {
          wallSigns.add(block);
          configureMineableTags(field, block);
        }
      }
    });

    configureColoredTags(WallSignBlocks.CONCRETE_WALL_SIGNS);
    configureColoredTags(WallSignBlocks.GLOWING_CONCRETE_WALL_SIGNS);
    configureColoredTags(WallSignBlocks.FULL_CONCRETE_WALL_SIGNS);
    configureColoredTags(WallSignBlocks.TERRACOTTA_WALL_SIGNS);
    configureColoredTags(WallSignBlocks.GLOWING_TERRACOTTA_WALL_SIGNS);
    configureColoredTags(WallSignBlocks.FULL_TERRACOTTA_WALL_SIGNS);

    axeMineable.addTag(woodenWallSigns);
    pickaxeMineable.addTag(concreteWallSigns, fullConcreteWallSigns, glowingConcreteWallSigns, terracottaWallSigns, fullTerracottaWallSigns, glowingTerracottaWallSigns);
  }

  protected void configureMineableTags(Field field, Block block) {
    final MiningLevel annotation = field.getAnnotation(MiningLevel.class);
    if (annotation == null) {
      pickaxeMineable.add(block);
    } else {
      final MiningLevel.Tool tool = annotation.value();
      switch (tool) {
        case PICKAXE -> pickaxeMineable.add(block);
        case SHOVEL -> shovelMineable.add(block);
        case AXE -> axeMineable.add(block);
        case HOE -> hoeMineable.add(block);
      }
      final MiningLevel.Level level = annotation.level();
      switch (level) {
        case STONE -> needsStoneTool.add(block);
        case IRON -> needsIronTool.add(block);
        case DIAMOND -> needsDiamondTool.add(block);
      }
    }
  }

  protected void configureMineableTags(Field field, Block[] blocks) {
    final MiningLevel annotation = field.getAnnotation(MiningLevel.class);
    if (annotation == null) {
      pickaxeMineable.add(blocks);
    } else {
      final MiningLevel.Tool tool = annotation.value();
      switch (tool) {
        case PICKAXE -> pickaxeMineable.add(blocks);
        case SHOVEL -> shovelMineable.add(blocks);
        case AXE -> axeMineable.add(blocks);
        case HOE -> hoeMineable.add(blocks);
      }
      final MiningLevel.Level level = annotation.level();
      switch (level) {
        case STONE -> needsStoneTool.add(blocks);
        case IRON -> needsIronTool.add(blocks);
        case DIAMOND -> needsDiamondTool.add(blocks);
      }
    }
  }

  protected void configureColoredTags(Map<DyeColor, ? extends Block> map) {
    map.forEach((dyeColor, block) -> {
      if (block instanceof HandrailBlock handrailBlock) {
        blockTagOnly(dyedBlockTags.get(dyeColor)).add(handrailBlock.selfAndVariants());
      } else {
        blockTagOnly(dyedBlockTags.get(dyeColor)).add(block);
      }
      itemTag(dyedItemTags.get(dyeColor)).add(block.asItem());
    });
  }

  protected void tools() {
    itemTag(ItemTags.PICKAXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.AXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SHOVELS).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.HOES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SWORDS).add(MishangucItems.OMNIPOTENT_TOOL);

    itemTag(ItemTags.DURABILITY_ENCHANTABLE).add(MishangucItems.ROTATING_TOOL, MishangucItems.MIRRORING_TOOL, MishangucItems.SLAB_TOOL, MishangucItems.TEXT_COPY_TOOL, MishangucItems.EXPLOSION_TOOL, MishangucItems.COLOR_TOOL, MishangucItems.ROAD_TOOL, MishangucItems.TP_TOOL, MishangucItems.GROWTH_TOOL, MishangucItems.ICE_SNOW_TOOL);
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
    roads();
    signs();
    lights();
    handrails();
    coloredBlocks();
    tools();

    blockTagWithItem(BlockTags.STAIRS, ItemTags.STAIRS).add(blocks().stream().filter(block -> block instanceof StairsBlock).toArray(Block[]::new));
    blockTagWithItem(BlockTags.SLABS, ItemTags.SLABS).add(blocks().stream().filter(block -> block instanceof SlabBlock).toArray(Block[]::new));
  }

  public class MishangucItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public MishangucItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @Nullable FabricTagProvider.BlockTagProvider blockTagProvider) {
      super(output, completableFuture, blockTagProvider);
    }

    @SuppressWarnings("deprecation")
    protected MishangucTagBuilder<Item> getMishangucTagBuilder(TagKey<Item> tag) {
      final TagBuilder tagBuilder = this.getTagBuilder(tag);
      return new MishangucTagBuilder<>(tag, tagBuilder, item -> item.getRegistryEntry().registryKey());
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
      blockTagsWithItem.forEach(this::copy);
    }
  }
}
