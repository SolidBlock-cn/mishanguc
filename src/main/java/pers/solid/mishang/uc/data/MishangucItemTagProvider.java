package pers.solid.mishang.uc.data;

import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagBuilder;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.SimpleHandrailBlock;
import pers.solid.mishang.uc.blocks.HandrailBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

import static pers.solid.mishang.uc.MishangUtils.*;

public class MishangucItemTagProvider extends FabricTagProvider.ItemTagProvider {
  private final MishangucBlockTagProvider blockTagProvider;

  public MishangucItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture, @NotNull MishangucBlockTagProvider blockTagProvider) {
    super(output, completableFuture, blockTagProvider);
    this.blockTagProvider = blockTagProvider;
  }

  @SuppressWarnings("deprecation")
  protected MishangucTagBuilder<Item> getMishangucTagBuilder(TagKey<Item> tag) {
    final TagBuilder tagBuilder = this.getTagBuilder(tag);
    return new MishangucTagBuilder<>(tag, tagBuilder, item -> item.getRegistryEntry().registryKey());
  }

  protected void tools() {
    itemTag(ItemTags.PICKAXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.AXES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SHOVELS).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.HOES).add(MishangucItems.OMNIPOTENT_TOOL);
    itemTag(ItemTags.SWORDS).add(MishangucItems.OMNIPOTENT_TOOL);
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
  }

  @Override
  protected void configure(RegistryWrapper.WrapperLookup lookup) {
    tools();
    handrailItems();
    coloredItems();
    blockTagProvider.blockTagsWithItem.forEach(this::copy);
    final MishangucTagBuilder<Item> colored = itemTag("colored"); // 因为涉及染色栏杆的要特殊处理，所以这里先这样。
    MishangUtils.blocks().stream().filter(Predicates.instanceOf(ColoredBlock.class)).map(Block::asItem).distinct().forEach(colored::add);
  }

  protected MishangucTagBuilder<Item> itemTag(TagKey<Item> tagKey) {
    return getMishangucTagBuilder(tagKey);
  }

  protected MishangucTagBuilder<Item> itemTag(String path) {
    return getMishangucTagBuilder(TagKey.of(RegistryKeys.ITEM, Mishanguc.id(path)));
  }
}
