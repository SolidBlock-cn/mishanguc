package pers.solid.mishang.uc.arrp;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.SingleItemRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.fabric.api.SidedRRPCallback;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.blocks.*;

import java.util.List;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
public class ARRPMain implements ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create(new Identifier("mishanguc", "pack"));

  private static void addTags() {
    MishangucTagProvider.run(PACK);
  }

  /**
   * 为运行时资源包生成资源。在开发环境中，每次加载资源就会重新生成一次。在非开发环境中，游戏开始时生成一次，此后不再生成。
   */
  private static void generateResources(boolean includesClient, boolean includesServer) {
    if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
      Preconditions.checkArgument(!includesClient, "The parameter 'includesClient' cannot be true when in dedicated server!");
    }
    if (includesClient)
      PACK.clearResources(ResourceType.CLIENT_RESOURCES);
    if (includesServer)
      PACK.clearResources(ResourceType.SERVER_DATA);

    for (Block block : MishangUtils.blocks()) {
      if (block instanceof final BlockResourceGenerator generator) {
        generator.writeResources(PACK, includesClient, includesServer);
      }
    }
    for (Item item : MishangUtils.items()) {
      if (item instanceof final ItemResourceGenerator generator) {
        generator.writeResources(PACK, includesClient, includesServer);
      }
    }

    // 服务器部分
    if (includesServer) {
      addTags();
      addSpecialRecipes();
    }
  }

  /**
   * 为本模组内的物品添加配方。该方法只会生成部分配方，还有很多配方是在 {@link ItemResourceGenerator#writeRecipes(RuntimeResourcePack)} 的子方法中定义的。
   */
  private static void addSpecialRecipes() {
    addGlassHandrailsRecipes();
    addRecipesForInvisibleSigns();
    addRoadPalingRecipes();
  }

  private static void addGlassHandrailsRecipes() {
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_STONE_HANDRAIL, Items.STONE, ColoredBlocks.COLORED_CONCRETE, Items.STONE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_COBBLESTONE_HANDRAIL, Items.COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.COBBLESTONE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_MOSSY_COBBLESTONE_HANDRAIL, Items.MOSSY_COBBLESTONE, ColoredBlocks.COLORED_CONCRETE, Items.MOSSY_COBBLESTONE, 6, null);

    HandrailBlocks.DECORATED_IRON_HANDRAILS.forEach((dyeColor, glassHandrailBlock) -> {
      final TagKey<Item> dyeKey = TagKey.of(RegistryKeys.ITEM, new Identifier(TagUtil.C_TAG_NAMESPACE, "dyes/" + dyeColor.asString()));
      PACK.addRecipeAndAdvancement(glassHandrailBlock.getRecipeId(), ShapedRecipeJsonBuilder.create(glassHandrailBlock.getRecipeCategory(), glassHandrailBlock, 4)
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
          .group("mishanguc:decorated_iron_handrail"));
    });

    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL, ConventionalItemTags.IRON_INGOTS, "has_iron_ingot", ColoredBlocks.COLORED_CONCRETE, Items.IRON_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_GOLD_HANDRAIL, ConventionalItemTags.GOLD_INGOTS, "has_gold_ingot", ColoredBlocks.COLORED_CONCRETE, Items.GOLD_NUGGET, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_EMERALD_HANDRAIL, ConventionalItemTags.EMERALD_GEMS, "has_emerald", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_DIAMOND_HANDRAIL, ConventionalItemTags.DIAMOND_GEMS, "has_diamond", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_NETHERITE_HANDRAIL, ConventionalItemTags.NETHERITE_INGOTS, "has_netherite_ingot", ColoredBlocks.COLORED_CONCRETE, 4);
    addRecipeForGlassHandrail(HandrailBlocks.COLORED_DECORATED_LAPIS_HANDRAIL, ConventionalItemTags.LAPIS_GEMS, "has_lapis", ColoredBlocks.COLORED_CONCRETE, 4);

    addRecipeForGlassHandrail(HandrailBlocks.SNOW_DECORATED_PACKED_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.PACKED_ICE, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.SNOW_DECORATED_BLUE_ICE_HANDRAIL, Items.PACKED_ICE, ColoredBlocks.COLORED_SNOW_BLOCK, Items.BLUE_ICE, 6, null);

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
      addRecipeForGlassHandrail(output, wood, planks, Items.STICK, 6, "glass_wooden_handrail");
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
      addRecipeForGlassHandrail(output, output.baseBlock(), ColoredBlocks.COLORED_PLANKS, Items.STICK, 6, "colored_decorated_wooden_handrail");
    }

    addRecipeForGlassHandrail(HandrailBlocks.GLASS_BAMBOO_HANDRAIL, Items.BAMBOO_BLOCK, Items.BAMBOO_PLANKS, Items.BAMBOO, 6, null);
    addRecipeForGlassHandrail(HandrailBlocks.NETHERRACK_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.NETHERRACK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.SOUL_SOIL_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.SOUL_SOIL, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.MAGMA_DECORATED_OBSIDIAN_HANDRAIL, Items.OBSIDIAN, Items.MAGMA_BLOCK, Items.OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.NETHERRACK_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.NETHERRACK, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.SOUL_SOIL_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.SOUL_SOIL, Items.CRYING_OBSIDIAN, 8, null);
    addRecipeForGlassHandrail(HandrailBlocks.MAGMA_DECORATED_CRYING_OBSIDIAN_HANDRAIL, Items.CRYING_OBSIDIAN, Items.MAGMA_BLOCK, Items.CRYING_OBSIDIAN, 8, null);
  }

  private static void addRecipeForGlassHandrail(GlassHandrailBlock output, ItemConvertible frame, ItemConvertible decoration, ItemConvertible base, int outputCount, @Nullable String group) {
    PACK.addRecipeAndAdvancement(output.getRecipeId(), ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
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
        .group(group));
  }

  private static void addRecipeForGlassHandrail(GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, ItemConvertible base, int outputCount) {
    PACK.addRecipeAndAdvancement(output.getRecipeId(), ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
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
        .setCustomRecipeCategory("handrails"));
  }

  private static void addRecipeForGlassHandrail(GlassHandrailBlock output, TagKey<Item> frame, String frameCriterionName, ItemConvertible decoration, int outputCount) {
    PACK.addRecipeAndAdvancement(output.getRecipeId(), ShapedRecipeJsonBuilder.create(output.getRecipeCategory(), output, outputCount)
        .pattern("XXX")
        .pattern("oMo")
        .pattern("XXX")
        .input('X', frame)
        .input('o', Items.GLASS_PANE)
        .input('M', decoration)
        .criterionFromItemTag(frameCriterionName, frame)
        .criterionFromItem(Items.GLASS_PANE)
        .criterionFromItem(decoration)
        .setCustomRecipeCategory("handrails"));
  }

  private static void addRecipesForInvisibleSigns() {
    // 隐形告示牌是合成其他告示牌的基础。
    PACK.addRecipeAndAdvancement(BRRPUtils.getRecipeId(WallSignBlocks.INVISIBLE_WALL_SIGN),
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WallSignBlocks.INVISIBLE_WALL_SIGN, 9)
            .pattern(".#.")
            .pattern("#o#")
            .pattern(".#.")
            .input('.', Items.IRON_NUGGET)
            .input('#', Items.FEATHER)
            .input('o', Items.GOLD_INGOT)
            .criterion("has_iron_nugget", RecipeProvider.conditionsFromItem(Items.IRON_NUGGET))
            .criterion("has_feather", RecipeProvider.conditionsFromItem(Items.FEATHER))
            .criterion("has_gold_ingot", RecipeProvider.conditionsFromItem(Items.GOLD_INGOT))
            .setCustomRecipeCategory("signs"));
    PACK.addRecipeAndAdvancement(BRRPUtils.getRecipeId(WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN),
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN, 3)
            .pattern("---")
            .pattern("###")
            .input('-', Items.GLOWSTONE_DUST)
            .input('#', WallSignBlocks.INVISIBLE_WALL_SIGN)
            .criterion("has_base_block", RecipeProvider.conditionsFromItem(WallSignBlocks.INVISIBLE_WALL_SIGN))
            .setCustomRecipeCategory("signs"));
  }

  private static void addRoadPalingRecipes() {
    // 将带有标线的道路重置为不带标线的道路。
    final TagKey<Item> roadBlocks = TagKey.of(RegistryKeys.ITEM, new Identifier("mishanguc", "road_blocks"));
    PACK.addRecipeAndAdvancement(RoadBlocks.ROAD_BLOCK.getRecipeId().brrp_suffixed("_from_paling"), SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadBlocks), RecipeCategory.BUILDING_BLOCKS, RoadBlocks.ROAD_BLOCK)
        .criterionFromItemTag("has_road_block", roadBlocks)
        .setCustomRecipeCategory("roads"));
    final TagKey<Item> roadSlabs = TagKey.of(RegistryKeys.ITEM, new Identifier("mishanguc", "road_slabs"));
    final AbstractRoadSlabBlock roadSlabBlock = RoadSlabBlocks.BLOCK_TO_SLABS.get(RoadBlocks.ROAD_BLOCK);
    PACK.addRecipeAndAdvancement(roadSlabBlock.getRecipeId().brrp_suffixed("_from_paling"), SingleItemRecipeJsonBuilder.createStonecutting(Ingredient.fromTag(roadSlabs), RecipeCategory.BUILDING_BLOCKS, roadSlabBlock)
        .criterionFromItemTag("has_road_slab", roadSlabs)
        .setCustomRecipeCategory("roads"));
  }

  @Override
  public void onInitialize() {
    generateResources(FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT, true);
    PACK.setSidedRegenerationCallback(ResourceType.CLIENT_RESOURCES, () -> generateResources(true, false));
    PACK.setSidedRegenerationCallback(ResourceType.SERVER_DATA, () -> generateResources(false, true));
    SidedRRPCallback.BEFORE_VANILLA.register((resourceType, builder) -> builder.add(PACK));
  }
}
