package pers.solid.mishang.uc.data;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.SimpleHandrailBlock;
import pers.solid.mishang.uc.blocks.HandrailBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static pers.solid.mishang.uc.MishangUtils.*;

public class MishangucItemTagProvider extends FabricTagProvider.ItemTagProvider {
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
  private final MishangucBlockTagProvider blockTagProvider;

  public MishangucItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @NotNull MishangucBlockTagProvider blockTagProvider) {
    super(output, completableFuture, blockTagProvider);
    this.blockTagProvider = blockTagProvider;
  }

  protected MishangucTagBuilder<Item> getMishangucTagBuilder(TagKey<Item> tag) {
    return new MishangucTagBuilder<>(tag, valueLookupBuilder(tag));
  }

  protected void tools() {
    itemTag(ItemTags.PICKAXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.AXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SHOVELS).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.HOES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SWORDS).add(MishangucItems.OMNIPOTENT_TOOL);

    itemTag(ItemTags.DURABILITY_ENCHANTABLE).add(MishangucItems.ROTATING_TOOL, MishangucItems.MIRRORING_TOOL, MishangucItems.SLAB_TOOL, MishangucItems.TEXT_COPY_TOOL, MishangucItems.EXPLOSION_TOOL, MishangucItems.COLOR_TOOL, MishangucItems.ROAD_TOOL, MishangucItems.TP_TOOL, MishangucItems.GROWTH_TOOL, MishangucItems.ICE_SNOW_TOOL);
  }

  protected void handrailItems() {
    final var simpleConcreteHandrailItems = itemTag("simple_concrete_handrails");
    final var simpleTerracottaHandrailItems = itemTag("simple_terracotta_handrails");
    final var simpleStainedGlassHandrailItems = itemTag("simple_stained_glass_handrails");
    final var simpleWoodenHandrailItems = itemTag("simple_wooden_handrails");
    final var simpleHandrailItems = itemTag("simple_handrails")
        .addTag(simpleConcreteHandrailItems, simpleTerracottaHandrailItems, simpleStainedGlassHandrailItems, simpleWoodenHandrailItems);

    final var glassHandrailItems = itemTag("glass_handrails");
    final var handrailItems = itemTag("handrails")
        .addTag(simpleStainedGlassHandrailItems)
        .addTag(simpleTerracottaHandrailItems)
        .addTag(simpleConcreteHandrailItems)
        .addTag(simpleWoodenHandrailItems)
        .addTag(glassHandrailItems);

    MishangUtils.instanceEntryStream(HandrailBlocks.class, Block.class).forEach(entry -> {
      final Field field = entry.getKey();
      final Block block = entry.getValue();
      if (block instanceof final SimpleHandrailBlock simpleHandrailBlock) {
        if (MishangUtils.isStained_glass(simpleHandrailBlock.baseBlock)) {
          simpleStainedGlassHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isConcrete(simpleHandrailBlock.baseBlock)) {
          simpleConcreteHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isTerracotta(simpleHandrailBlock.baseBlock)) {
          simpleTerracottaHandrailItems.add(simpleHandrailBlock.asItem());
        } else if (isWood(simpleHandrailBlock.baseBlock) || isPlanks(simpleHandrailBlock.baseBlock)) {
          simpleWoodenHandrailItems.add(simpleHandrailBlock.asItem());
        } else {
          simpleHandrailItems.add(simpleHandrailBlock.asItem());
        }
      } else if (block instanceof GlassHandrailBlock glassHandrailBlock) {
        glassHandrailItems.add(glassHandrailBlock.asItem());
      }
    });
  }

  protected void coloredItems() {
    blockTagProvider.coloredItems.asMap().forEach((dyeColor, items) -> {
      final MishangucTagBuilder<Item> builder = getMishangucTagBuilder(dyedItemTags.get(dyeColor));
      items.forEach(builder::add);
    });
  }

  @Override
  protected void configure(RegistryWrapper.@NonNull WrapperLookup lookup) {
    tools();
    handrailItems();
    coloredItems();
    blockTagProvider.blockTagsWithItem.forEach(this::copy);
    final MishangucTagBuilder<Item> colored = itemTag("colored"); // 因为涉及染色栏杆的要特殊处理，所以这里先这样。
    MishangUtils.blocks().stream().filter(Predicates.instanceOf(ColoredBlock.class)).map(Block::asItem).distinct().forEach(colored::add);

    itemTag("omnipotent_repair_items").add(Items.BEDROCK);
    itemTag("road_materials").add(Items.WHITE_CONCRETE, Items.GRAY_CONCRETE, Items.LIGHT_GRAY_CONCRETE, Items.BLACK_CONCRETE);
  }

  protected MishangucTagBuilder<Item> itemTag(TagKey<Item> tagKey) {
    return getMishangucTagBuilder(tagKey);
  }

  protected MishangucTagBuilder<Item> itemTag(String path) {
    return getMishangucTagBuilder(TagKey.of(RegistryKeys.ITEM, Mishanguc.id(path)));
  }
}
