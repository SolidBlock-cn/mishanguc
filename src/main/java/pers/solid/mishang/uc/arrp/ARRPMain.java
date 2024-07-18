package pers.solid.mishang.uc.arrp;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import pers.solid.brrp.v1.BRRPUtils;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.fabric.api.SidedRRPCallback;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

/**
 * @since 0.1.7 本类应当在 onInitialize 的入口点中执行，而非 pregen 中。
 */
public class ARRPMain implements ModInitializer {
  private static final RuntimeResourcePack PACK = RuntimeResourcePack.create(Identifier.of("mishanguc", "pack"));
  public static final VanillaBlockLootTableGenerator LOOT_TABLE_GENERATOR = new VanillaBlockLootTableGenerator(PACK.getRegistryLookup());

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
    addRecipesForInvisibleSigns();
    addRoadPalingRecipes();
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
