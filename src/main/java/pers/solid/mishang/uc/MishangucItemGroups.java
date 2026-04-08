package pers.solid.mishang.uc;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.ObjectUtils;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.ColorToolItem;
import pers.solid.mishang.uc.item.ExplosionToolItem;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.util.ColorfulBlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MishangucItemGroups {
  public static final CreativeModeTab ROADS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
      Mishanguc.id("roads"),
      FabricCreativeModeTab.builder().icon(
          () -> new ItemStack(RoadBlocks.ROAD_WITH_WHITE_DOUBLE_LINE)).displayItems((displayContext, entries) -> {
        MishangUtils.instanceStream(RoadBlocks.class, Block.class).forEach(addEntries(entries));
        RoadSlabBlocks.SLABS.forEach(addEntries(entries));
        MishangUtils.instanceStream(RoadMarkBlocks.class, Block.class).forEach(addEntries(entries));
      }).title(Component.translatable("itemGroup.mishanguc.roads")).build());
  public static final CreativeModeTab LIGHTS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
      Mishanguc.id("lights"),
      FabricCreativeModeTab.builder().icon(() -> new ItemStack(LightBlocks.WHITE_LARGE_WALL_LIGHT)).displayItems((displayContext, entries) -> MishangUtils.instanceStream(LightBlocks.class, Block.class).forEach(addEntries(entries))).title(Component.translatable("itemGroup.mishanguc.lights")).build());
  public static final CreativeModeTab SIGNS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mishanguc.id("signs"),
      FabricCreativeModeTab.builder(
      ).icon(
          () -> new ItemStack(StandingSignBlocks.ACACIA_STANDING_SIGN)).displayItems((displayContext, entries) -> {
        MishangUtils.instanceStream(WallSignBlocks.class, Block.class).forEach(addEntries(entries));
        MishangUtils.instanceStream(HungSignBlocks.class, Block.class).forEach(addEntries(entries));
        MishangUtils.instanceStream(StandingSignBlocks.class, Block.class).forEach(addEntries(entries));
      }).title(Component.translatable("itemGroup.mishanguc.signs")).build());
  public static final CreativeModeTab TOOLS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mishanguc.id("tools"),
      FabricCreativeModeTab.builder().icon(() -> new ItemStack(MishangucItems.ROTATING_TOOL)).displayItems((displayContext, entries) -> MishangUtils.instanceStream(MishangucItems.class, ItemLike.class).forEach(item -> {
        if (item instanceof final ExplosionToolItem explosionToolItem) {
          explosionToolItem.appendToEntries(entries);
        } else if (item instanceof final FastBuildingToolItem fastBuildingToolItem) {
          fastBuildingToolItem.appendToEntries(entries);
        } else if (item instanceof final ColorToolItem colorToolItem) {
          colorToolItem.appendToEntries(entries);
        } else {
          entries.accept(item);
        }
      })).title(Component.translatable("itemGroup.mishanguc.tools")).build());
  public static final CreativeModeTab DECORATIONS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mishanguc.id("decorations"), FabricCreativeModeTab.builder().icon(() -> new ItemStack(HandrailBlocks.SIMPLE_ORANGE_CONCRETE_HANDRAIL)).displayItems((displayContext, entries) -> MishangUtils.instanceStream(HandrailBlocks.class, Block.class).forEach(addEntries(entries))).title(Component.translatable("itemGroup.mishanguc.decorations")).build());

  public static final CreativeModeTab COLORED_BLOCKS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Mishanguc.id("colored_blocks"), FabricCreativeModeTab.builder().icon(() -> new ItemStack(ColoredBlocks.COLORED_WOOL)).displayItems((displayContext, entries) -> MishangUtils.instanceStream(ColoredBlocks.class, Block.class).forEach(addEntries(entries))).title(Component.translatable("itemGroup.mishanguc.colored_blocks")).build());
  public static final List<DyeColor> FANCY_COLORS = List.of(
      DyeColor.WHITE,
      DyeColor.LIGHT_GRAY,
      DyeColor.GRAY,
      DyeColor.BLACK,
      DyeColor.BROWN,
      DyeColor.RED,
      DyeColor.ORANGE,
      DyeColor.YELLOW,
      DyeColor.LIME,
      DyeColor.GREEN,
      DyeColor.CYAN,
      DyeColor.LIGHT_BLUE,
      DyeColor.BLUE,
      DyeColor.PURPLE,
      DyeColor.MAGENTA,
      DyeColor.PINK
  );

  public static void init() {
    Preconditions.checkState(ObjectUtils.allNotNull(ROADS, LIGHTS, SIGNS, TOOLS, DECORATIONS, COLORED_BLOCKS));
  }

  private static <T extends Block> Consumer<T> addEntries(CreativeModeTab.Output entries) {
    return t -> {
      if (ColorfulBlockRegistry.WHITE_TO_COLORFUL.containsKey(t)) {
        final Map<DyeColor, ? extends Block> map = ColorfulBlockRegistry.WHITE_TO_COLORFUL.get(t);
        for (DyeColor color : FANCY_COLORS) {
          if (map.containsKey(color)) {
            entries.accept(map.get(color));
          }
        }
      } else if (!ColorfulBlockRegistry.COLORFUL_BLOCKS.contains(t)) {
        entries.accept(t);
      }
    };
  }
}
