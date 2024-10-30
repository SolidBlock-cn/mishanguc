package pers.solid.mishang.uc.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.StonecuttingRecipeJsonBuilder;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
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

public class MishangucRecipeGenerator extends RecipeGenerator {

  protected MishangucRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
    super(registries, exporter);
  }

  public void generate() {
    addRegularRecipes();
    addSpecialRecipes();
  }

  private void addRegularRecipes() {
    for (Block block : MishangUtils.blocks()) {
      if (block instanceof MishangucBlock r) {
        r.writeRecipes(this, exporter);
      } else {
        throw new IllegalStateException();
      }
    }
    for (Item item : MishangUtils.items()) {
      if (item instanceof MishangucItem i) {
        final CraftingRecipeJsonBuilder craftingRecipe = i.getCraftingRecipe(this);
        if (craftingRecipe != null) {
          craftingRecipe.offerTo(exporter);
        }
      }
    }
  }


  /**
   * 生成模组的部分配方。
   */
  public void addSpecialRecipes() {
    addGlassHandrailsRecipes();
    addRecipesForInvisibleSigns();
    addRoadPalingRecipes();
  }

  private void addGlassHandrailsRecipes() {
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_STONE_HANDRAIL, Items.STONE, ColoredBlocks.COLORED_CONCRETE, Items.STONE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_COBBLESTONE_HANDRAIL, Items.COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.COBBLESTONE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_MOSSY_COBBLESTONE_HANDRAIL, Items.MOSSY_COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.MOSSY_COBBLESTONE, 6, null);

    HandrailBlocks.DECORATED_IRON_HANDRAILS.forEach((dyeColor, glassHandrailBlock) -> {
      final TagKey<Item> dyeKey = TagKey.of(RegistryKeys.ITEM, Identifier.of(TagUtil.C_TAG_NAMESPACE, "dyes/" + dyeColor.asString()));
      createShaped(RecipeCategory.DECORATIONS, glassHandrailBlock, 4)
          .pattern("XXX")
          .pattern("oMo")
          .pattern("nnn")
          .input('X', ConventionalItemTags.IRON_INGOTS)
          .input('o', Items.GLASS_PANE)
          .input('M', dyeKey)
          .input('n', Items.IRON_NUGGET)
          .criterion("has_iron_ingot", conditionsFromTag(ConventionalItemTags.IRON_INGOTS))
          .criterion(RecipeGenerator.hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
          .criterion("has_dye", conditionsFromTag(dyeKey))
          .criterion(RecipeGenerator.hasItem(Items.IRON_NUGGET), conditionsFromItem(Items.IRON_NUGGET))
          .group("mishanguc:decorated_iron_handrail")
          .offerTo(exporter);
    });

    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_CONCRETE, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_CONCRETE, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_CONCRETE, 4);

    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_LIGHT, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_LIGHT, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_LIGHT, 4);
    addRecipeForGlassHandrail(HandrailBlocks.GLOWING_COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_LIGHT, 4);

    addRecipeForGlassHandrail(HandrailBlocks.SNOW_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, Blocks.SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.SNOW_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, Blocks.SNOW_BLOCK, Items.BLUE_ICE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.BLUE_ICE, 6, null);

    for (GlassHandrailBlock output : List.of(
        HandrailBlocks.GLASS_OAK_HANDRAIL,
        HandrailBlocks.GLASS_SPRUCE_HANDRAIL,
        HandrailBlocks.GLASS_BIRCH_HANDRAIL,
        HandrailBlocks.GLASS_JUNGLE_HANDRAIL,
        HandrailBlocks.GLASS_ACACIA_HANDRAIL,
        HandrailBlocks.GLASS_CHERRY_HANDRAIL,
        HandrailBlocks.GLASS_DARK_OAK_HANDRAIL,
        HandrailBlocks.GLASS_PALE_OAK_HANDRAIL,
        HandrailBlocks.GLASS_MANGROVE_HANDRAIL,
        HandrailBlocks.GLASS_CRIMSON_HANDRAIL,
        HandrailBlocks.GLASS_WARPED_HANDRAIL)) {
      final Item wood = output.baseBlock().asItem();
      final Identifier woodId = Registries.ITEM.getId(wood);
      final Item planks = Registries.ITEM.get(woodId.withPath(woodId.getPath().replace("wood", "planks").replace("hyphae", "planks")));
      Preconditions.checkState(wood != planks);
      addRecipeForGlassHandrail(output, wood, planks, Items.STICK, 6, "glass_wooden_handrail");
    }
    for (GlassHandrailBlock output : ImmutableSet.of(
        HandrailBlocks.COLORED_DECORATED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_PALE_OAK_HANDRAIL,
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
        HandrailBlocks.COLORED_DECORATED_STRIPPED_PALE_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_CRIMSON_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_WARPED_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BAMBOO_HANDRAIL
    )) {
      addRecipeForGlassHandrail(output, output.baseBlock(), ColoredBlocks.COLORED_PLANKS, Items.STICK, 6, "colored_decorated_wooden_handrail");
    }

    addRecipeForGlassHandrail(HandrailBlocks.GLASS_BAMBOO_HANDRAIL, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS, Items.BAMBOO, 6, null);

    addRecipeForGlassHandrail(HandrailBlocks.NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.NETHERRACK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.NETHERRACK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.SOUL_SOIL, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.SOUL_SOIL, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.MAGMA_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.MAGMA_BLOCK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.MAGMA_BLOCK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, ColoredBlocks.COLORED_CONCRETE, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, ColoredBlocks.COLORED_CONCRETE, Items.CRYING_OBSIDIAN, 8, null);
  }

  private void addRecipeForGlassHandrail(GlassHandrailBlock output, ItemConvertible frame, ItemConvertible decoration, ItemConvertible base, int outputCount, @Nullable String group) {
    createShaped(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterion(hasItem(frame), conditionsFromItem(frame))
        .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
        .criterion(hasItem(decoration), conditionsFromItem(decoration))
        .criterion(hasItem(base), conditionsFromItem(base))
        .group(group)
        .offerTo(exporter);
  }

  private void addRecipeForGlassHandrail(GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, ItemConvertible base, int outputCount) {
    createShaped(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("nnn")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .input('n', base)
        .criterion(frameCriterionName, conditionsFromTag(frame))
        .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
        .criterion(hasItem(decoration), conditionsFromItem(decoration))
        .criterion(hasItem(base), conditionsFromItem(base))
        .offerTo(exporter);
  }

  private void addRecipeForGlassHandrail(GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, int outputCount) {
    createShaped(RecipeCategory.DECORATIONS, output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("XXX")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .criterion(frameCriterionName, conditionsFromTag(frame))
        .criterion(hasItem(Items.GLASS_PANE), conditionsFromItem(Items.GLASS_PANE))
        .criterion(hasItem(decoration), conditionsFromItem(decoration))
        .offerTo(exporter);
  }

  private void addRecipesForInvisibleSigns() {
    // 隐形告示牌是合成其他告示牌的基础。
    createShaped(RecipeCategory.DECORATIONS, WallSignBlocks.INVISIBLE_WALL_SIGN, 9)
        .pattern(".#.")
        .pattern("#o#")
        .pattern(".#.")
        .input('.', Items.IRON_NUGGET)
        .input('#', Items.FEATHER)
        .input('o', Items.GOLD_INGOT)
        .criterion("has_iron_nugget", conditionsFromItem(Items.IRON_NUGGET))
        .criterion("has_feather", conditionsFromItem(Items.FEATHER))
        .criterion("has_gold_ingot", conditionsFromItem(Items.GOLD_INGOT))
        .offerTo(exporter);
    createShaped(RecipeCategory.DECORATIONS, WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN, 3)
        .pattern("---")
        .pattern("###")
        .input('-', Items.GLOWSTONE_DUST)
        .input('#', WallSignBlocks.INVISIBLE_WALL_SIGN)
        .criterion("has_base_block", conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
        .offerTo(exporter);
  }

  private void addRoadPalingRecipes() {
    // 将带有标线的道路重置为不带标线的道路。
    final TagKey<Item> roadBlocks = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_blocks"));
    StonecuttingRecipeJsonBuilder.createStonecutting(ingredientFromTag(roadBlocks), RecipeCategory.BUILDING_BLOCKS, RoadBlocks.ROAD_BLOCK)
        .criterion("has_road_block", conditionsFromTag(roadBlocks))
        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, CraftingRecipeJsonBuilder.getItemId(RoadBlocks.ROAD_BLOCK).withSuffixedPath("_from_paling")));
    final TagKey<Item> roadSlabs = TagKey.of(RegistryKeys.ITEM, Mishanguc.id("road_slabs"));
    final AbstractRoadSlabBlock roadSlabBlock = RoadSlabBlocks.BLOCK_TO_SLABS.get(RoadBlocks.ROAD_BLOCK);
    StonecuttingRecipeJsonBuilder.createStonecutting(ingredientFromTag(roadSlabs), RecipeCategory.BUILDING_BLOCKS, roadSlabBlock)
        .criterion("has_road_slab", conditionsFromTag(roadSlabs))
        .offerTo(exporter, RegistryKey.of(RegistryKeys.RECIPE, CraftingRecipeJsonBuilder.getItemId(roadSlabBlock).withSuffixedPath("_from_paling")));
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
