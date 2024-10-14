package pers.solid.mishang.uc.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.MishangucBlock;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
public class MishangucRecipeProvider extends FabricRecipeProvider {
  public MishangucRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
    super(output, registriesFuture);
  }

  @Override
  public void generate(RecipeExporter exporter) {
    addRegularRecipes(exporter);
    addSpecialRecipes(exporter);
  }

  private static void addRegularRecipes(RecipeExporter exporter) {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof MishangucBlock r) {
        final CraftingRecipeJsonBuilder craftingRecipe = r.getCraftingRecipe();
        if (craftingRecipe != null) {
          craftingRecipe.offerTo(exporter);
        }
        if (r.shouldWriteStonecuttingRecipe()) {
          final SingleItemRecipeJsonBuilder stonecuttingRecipe = r.getStonecuttingRecipe();
          if (stonecuttingRecipe != null) {
            stonecuttingRecipe.offerTo(exporter, r.getStonecuttingRecipeId());
          }
        }
      } else {
        throw new IllegalStateException();
      }
    }
    for (Item item : MishangUtils.items()) {
      if (item instanceof MishangucItem i) {
        final CraftingRecipeJsonBuilder craftingRecipe = i.getCraftingRecipe();
        if (craftingRecipe != null) {
          craftingRecipe.offerTo(exporter);
        }
      }
    }
  }


  /**
   * 生成模组的部分配方。
   */
  public static void addSpecialRecipes(RecipeExporter exporter) {
    addGlassHandrailsRecipes(exporter);
    addRecipesForInvisibleSigns(exporter);
    addRoadPalingRecipes(exporter);
  }

  private static void addGlassHandrailsRecipes(RecipeExporter exporter) {
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_STONE_HANDRAIL, Items.STONE, ColoredBlocks.COLORED_CONCRETE, Items.STONE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_COBBLESTONE_HANDRAIL, Items.COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.COBBLESTONE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_MOSSY_COBBLESTONE_HANDRAIL, Items.MOSSY_COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.MOSSY_COBBLESTONE, 6, null);

    HandrailBlocks.DECORATED_IRON_HANDRAILS.forEach((dyeColor, glassHandrailBlock) -> {
      final TagKey<Item> dyeKey = TagKey.of(RegistryKeys.ITEM, new Identifier(TagUtil.C_TAG_NAMESPACE, "dyes/" + dyeColor.asString()));
      ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, glassHandrailBlock, 4)
          .pattern("XXX")
          .pattern("oMo")
          .pattern("nnn")
          .input('X', ConventionalItemTags.IRON_INGOTS)
          .input('o', Items.GLASS_PANE)
          .input('M', dyeKey)
          .input('n', Items.IRON_NUGGET)
          .criterion("has_iron_ingot", RecipeProvider.conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
          .criterion(RecipeProvider.hasItem(Items.GLASS_PANE), RecipeProvider.conditionsFromItem(Items.GLASS_PANE))
          .criterion("has_dye", RecipeProvider.conditionsFromTag(dyeKey))
          .criterion(RecipeProvider.hasItem(Items.IRON_NUGGET), RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
          .group("mishanguc:decorated_iron_handrail")
          .offerTo(exporter);
    });

    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_CONCRETE, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_CONCRETE, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_CONCRETE, 4);

    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_LIGHT, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_LIGHT, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLOWING_COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_LIGHT, 4);

    addRecipeForGlassHandrail(exporter, HandrailBlocks.SNOW_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, Blocks.SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SNOW_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, Blocks.SNOW_BLOCK, Items.BLUE_ICE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.BLUE_ICE, 6, null);

    for (GlassHandrailBlock output : List.of(
        HandrailBlocks.GLASS_OAK_HANDRAIL,
        HandrailBlocks.GLASS_SPRUCE_HANDRAIL,
        HandrailBlocks.GLASS_BIRCH_HANDRAIL,
        HandrailBlocks.GLASS_JUNGLE_HANDRAIL,
        HandrailBlocks.GLASS_ACACIA_HANDRAIL,
        HandrailBlocks.GLASS_CHERRY_HANDRAIL,
        HandrailBlocks.GLASS_DARK_OAK_HANDRAIL,
        HandrailBlocks.GLASS_MANGROVE_HANDRAIL,
        HandrailBlocks.GLASS_CRIMSON_HANDRAIL,
        HandrailBlocks.GLASS_WARPED_HANDRAIL)) {
      final Item wood = output.baseBlock().asItem();
      final Identifier woodId = Registries.ITEM.getId(wood);
      final Item planks = Registries.ITEM.get(woodId.withPath(woodId.getPath().replace("wood", "planks").replace("hyphae", "planks")));
      Preconditions.checkState(wood != planks);
      addRecipeForGlassHandrail(exporter, output, wood, planks, Items.STICK, 6, "glass_wooden_handrail");
    }
    for (GlassHandrailBlock output : ImmutableSet.of(
        HandrailBlocks.COLORED_DECORATED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CRIMSON_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_WARPED_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BAMBOO_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_CRIMSON_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_WARPED_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BAMBOO_HANDRAIL
    )) {
      addRecipeForGlassHandrail(exporter, output, output.baseBlock(), ColoredBlocks.COLORED_PLANKS, Items.STICK, 6, "colored_decorated_wooden_handrail");
    }

    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLASS_BAMBOO_HANDRAIL, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS, Items.BAMBOO, 6, null);

    addRecipeForGlassHandrail(exporter, HandrailBlocks.NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.NETHERRACK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.NETHERRACK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.SOUL_SOIL, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.SOUL_SOIL, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.MAGMA_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.MAGMA_BLOCK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.MAGMA_BLOCK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, ColoredBlocks.COLORED_CONCRETE, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, ColoredBlocks.COLORED_CONCRETE, Items.CRYING_OBSIDIAN, 8, null);
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, ItemConvertible frame, ItemConvertible decoration, ItemConvertible base, int outputCount, @Nullable String group) {
    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterion(RecipeProvider.hasItem(frame), RecipeProvider.conditionsFromItem(frame))
        .criterion(RecipeProvider.hasItem(Items.GLASS_PANE), RecipeProvider.conditionsFromItem(Items.GLASS_PANE))
        .criterion(RecipeProvider.hasItem(decoration), RecipeProvider.conditionsFromItem(decoration))
        .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base))
        .group(group)
        .offerTo(exporter);
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, ItemConvertible base, int outputCount) {
    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterion(frameCriterionName, RecipeProvider.conditionsFromTag(frame))
        .criterion(RecipeProvider.hasItem(Items.GLASS_PANE), RecipeProvider.conditionsFromItem(Items.GLASS_PANE))
        .criterion(RecipeProvider.hasItem(decoration), RecipeProvider.conditionsFromItem(decoration))
        .criterion(RecipeProvider.hasItem(base), RecipeProvider.conditionsFromItem(base))
        .offerTo(exporter);
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, int outputCount) {
    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("XXX")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .criterion(frameCriterionName, RecipeProvider.conditionsFromTag(frame))
        .criterion(RecipeProvider.hasItem(Items.GLASS_PANE), RecipeProvider.conditionsFromItem(Items.GLASS_PANE))
        .criterion(RecipeProvider.hasItem(decoration), RecipeProvider.conditionsFromItem(decoration))
        .offerTo(exporter);
  }

  private static void addRecipesForInvisibleSigns(RecipeExporter exporter) {
    // 隐形告示牌是合成其他告示牌的基础。
    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, WallSignBlocks.INVISIBLE_WALL_SIGN, 9)
        .pattern(".#.")
        .pattern("#o#")
        .pattern(".#.")
        .input('.', Items.IRON_NUGGET)
        .input('#', Items.FEATHER)
        .input('o', Items.GOLD_INGOT)
        .criterion("has_iron_nugget", RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
        .criterion("has_feather", RecipeProvider.conditionsFromItem(Items.FEATHER))
        .criterion("has_gold_ingot", RecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
        .offerTo(exporter);
    ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN, 3)
        .pattern("---")
        .pattern("###")
        .input('-', Items.GLOWSTONE_DUST)
        .input('#', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .offerTo(exporter);
  }

  private static void addRoadPalingRecipes(RecipeExporter exporter) {
    // 将带有标线的道路重置为不带标线的道路。
    final TagKey<Item> roadBlocks = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_blocks"));
    SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadBlocks), RecipeCategory.BUILDING_BLOCKS, RoadBlocks.ROAD_BLOCK)
        .criterion("has_road_block", RecipeProvider.conditionsFromTag(roadBlocks))
        .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(RoadBlocks.ROAD_BLOCK).withSuffixedPath("_from_paling"));
    final TagKey<Item> roadSlabs = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_slabs"));
    final AbstractRoadSlabBlock roadSlabBlock = RoadSlabBlocks.BLOCK_TO_SLABS.get(RoadBlocks.ROAD_BLOCK);
    SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadSlabs), RecipeCategory.BUILDING_BLOCKS, roadSlabBlock)
        .criterion("has_road_slab", RecipeProvider.conditionsFromTag(roadSlabs))
        .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(roadSlabBlock).withSuffixedPath("_from_paling"));
  }

  public static @Nullable String getCustomRecipeCategory(Item outputItem) {
    if (outputItem instanceof BlockItem blockItem && blockItem.getBlock() instanceof MishangucBlock mishangucBlock) {
      return mishangucBlock.customRecipeCategory();
    } else if (outputItem instanceof MishangucItem mishangucItem) {
      return mishangucItem.customRecipeCategory();
    }
    return null;
  }
}
