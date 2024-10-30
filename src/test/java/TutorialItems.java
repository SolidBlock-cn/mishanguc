import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public final class TutorialItems {
  private TutorialItems() {
  }

  public static final Item CUSTOM_ITEM = register("custom_item", CustomItem::new, new Item.Settings()
      .component(DataComponentTypes.UNBREAKABLE, new UnbreakableComponent(true)));

  public static Item register(String path, Function<Item.Settings, Item> factory, Item.Settings settings) {
    final RegistryKey<Item> registryKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("tutorial", path));
    return Items.register(registryKey, factory, settings);
  }

  public static final Block EXAMPLE_BLOCK = register("example_block", Block::new, Block.Settings.create().strength(4.0f));

  private static Block register(String path, Function<AbstractBlock.Settings, Block> factory, AbstractBlock.Settings settings) {
    final Identifier identifier = Identifier.of("tutorial", path);
    final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

    final Block block = Blocks.register(registryKey, factory, settings);
    Items.register(block);
    return block;
  }

  public static void initialize() {
    FuelRegistryEvents.BUILD.register((builder, context) -> {
      builder.add(CUSTOM_ITEM, 300);
    });
  }
}