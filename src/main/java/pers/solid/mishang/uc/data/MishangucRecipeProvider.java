package pers.solid.mishang.uc.data;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
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
          final StonecuttingRecipeJsonBuilder stonecuttingRecipe = r.getStonecuttingRecipe();
          if (stonecuttingRecipe != null) {
            stonecuttingRecipe.offerTo(exporter, r.getStonecuttingRecipeId());
          }
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
      final TagKey<Item> dyeKey = TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, "dyes/" + dyeColor.asString()));
      ShapedRecipeJsonBuilder.create(glassHandrailBlock.getRecipeCategory(), glassHandrailBlock, 4)
          .pattern("XXX")
          .pattern("oMo")
          .pattern("nnn")
          .input('X', ConventionalItemTags.IRON_INGOTS)
          .input('o', Items.GLASS_PANE)
          .input('M', dyeKey)
          .input('n', Items.IRON_NUGGET)
          .criterionFromItemTag("has_iron_ingot", ConventionalItemTags.IRON_INGOTS)
          .criterionFromItem(Items.GLASS_PANE)
          .criterionFromItemTag("has_dye", dyeKey)
          .criterionFromItem(Items.IRON_NUGGET)
          .setCustomRecipeCategory("handrails")
          .group("mishanguc:decorated_iron_handrail")
          .offerTo(exporter, glassHandrailBlock.getRecipeId());
    });

    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_CONCRETE, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_CONCRETE, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_CONCRETE, 4);

    addRecipeForGlassHandrail(exporter, HandrailBlocks.SNOW_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SNOW_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.BLUE_ICE, 6, null);

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
    for (GlassHandrailBlock output : List.of(
        HandrailBlocks.COLORED_DECORATED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CRIMSON_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_WARPED_HANDRAIL
    )) {
      addRecipeForGlassHandrail(exporter, output, output.baseBlock(), ColoredBlocks.COLORED_PLANKS, Items.STICK, 6, "colored_decorated_wooden_handrail");
    }

    addRecipeForGlassHandrail(exporter, HandrailBlocks.GLASS_BAMBOO_HANDRAIL, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS, Items.BAMBOO, 6, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.NETHERRACK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.SOUL_SOIL, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.MAGMA_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.MAGMA_BLOCK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.NETHERRACK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.SOUL_SOIL, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(exporter, HandrailBlocks.MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.MAGMA_BLOCK, Items.CRYING_OBSIDIAN, 8, null);
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, ItemConvertible frame, ItemConvertible decoration, ItemConvertible base, int outputCount, @Nullable String group) {
    ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterionFromItem(frame)
        .criterionFromItem(Items.GLASS_PANE)
        .criterionFromItem(decoration)
        .criterionFromItem(base)
        .setCustomRecipeCategory("handrails")
        .group(group)
        .offerTo(exporter, output.getRecipeId());
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, ItemConvertible base, int outputCount) {
    ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterionFromItemTag(frameCriterionName, frame)
        .criterionFromItem(Items.GLASS_PANE)
        .criterionFromItem(decoration)
        .criterionFromItem(base)
        .setCustomRecipeCategory("handrails")
        .offerTo(exporter, output.getRecipeId());
  }

  private static void addRecipeForGlassHandrail(RecipeExporter exporter, GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, int outputCount) {
    ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("XXX")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .criterionFromItemTag(frameCriterionName, frame)
        .criterionFromItem(Items.GLASS_PANE)
        .criterionFromItem(decoration)
        .setCustomRecipeCategory("handrails")
        .offerTo(exporter, output.getRecipeId());
  }

  private static void addRecipesForInvisibleSigns(RecipeExporter exporter) {
    // 隐形告示牌是合成其他告示牌的基础。
    ShapedRecipeJsonBuilder.create(WallSignBlocks.INVISIBLE_WALL_SIGN.getRecipeCategory(), WallSignBlocks.INVISIBLE_WALL_SIGN, 9)
        .pattern(".#.")
        .pattern("#o#")
        .pattern(".#.")
        .input('.', Items.IRON_NUGGET)
        .input('#', Items.FEATHER)
        .input('o', Items.GOLD_INGOT)
        .criterion("has_iron_nugget", RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
        .criterion("has_feather", RecipeProvider.conditionsFromItem(Items.FEATHER))
        .criterion("has_gold_ingot", RecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
        .setCustomRecipeCategory("signs")
        .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(WallSignBlocks.INVISIBLE_WALL_SIGN));
    ShapedRecipeJsonBuilder.create(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN.getRecipeCategory(), WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN, 3)
        .pattern("---")
        .pattern("###")
        .input('-', Items.GLOWSTONE_DUST)
        .input('#', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .criterion("has_base_block", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .setCustomRecipeCategory("signs")
        .offerTo(exporter, CraftingRecipeJsonBuilder.getItemId(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN));
  }

  private static void addRoadPalingRecipes(RecipeExporter exporter) {
    // 将带有标线的道路重置为不带标线的道路。
    final TagKey<Item> roadBlocks = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_blocks"));
    StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadBlocks), RoadBlocks.ROAD_BLOCK.getRecipeCategory(), RoadBlocks.ROAD_BLOCK)
        .criterionFromItemTag("has_road_block", roadBlocks)
        .setCustomRecipeCategory("roads")
        .offerTo(exporter, RoadBlocks.ROAD_BLOCK.getRecipeId().withSuffixedPath("_from_paling"));
    final TagKey<Item> roadSlabs = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_slabs"));
    final AbstractRoadSlabBlock roadSlabBlock = RoadSlabBlocks.BLOCK_TO_SLABS.get(RoadBlocks.ROAD_BLOCK);
    StonecuttingRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadSlabs), roadSlabBlock.getRecipeCategory(), roadSlabBlock)
        .criterionFromItemTag("has_road_slab", roadSlabs)
        .setCustomRecipeCategory("roads")
        .offerTo(exporter, roadSlabBlock.getRecipeId().withSuffixedPath("_from_paling"));
  }
}
