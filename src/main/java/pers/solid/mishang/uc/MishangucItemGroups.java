package pers.solid.mishang.uc;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ObjectUtils;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.ExplosionToolItem;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.item.ForcePlacingToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.util.ColorfulBlockRegistry;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class MishangucItemGroups {
  public static final ItemGroup ROADS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "roads")).icon(
          () -> new ItemStack(RoadBlocks.ROAD_WITH_WHITE_DOUBLE_LINE)).entries((displayContext, entries) -> {
        MishangUtils.instanceStream(RoadBlocks.class, Block.class).forEach(addEntries(entries));
        RoadSlabBlocks.SLABS.forEach(addEntries(entries));
      }).build();
  public static final ItemGroup LIGHTS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "lights")).icon(() -> new ItemStack(LightBlocks.WHITE_LARGE_WALL_LIGHT)).entries((displayContext, entries) -> MishangUtils.instanceStream(LightBlocks.class, Block.class).forEach(addEntries(entries))).build();
  public static final ItemGroup SIGNS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "signs")).icon(
          () -> new ItemStack(StandingSignBlocks.ACACIA_STANDING_SIGN)).entries((displayContext, entries) -> {
        MishangUtils.instanceStream(HungSignBlocks.class, Block.class).forEach(addEntries(entries));
        MishangUtils.instanceStream(WallSignBlocks.class, Block.class).forEach(addEntries(entries));
        MishangUtils.instanceStream(StandingSignBlocks.class, Block.class).forEach(addEntries(entries));
      }).build();
  public static final ItemGroup TOOLS =
      FabricItemGroup.builder(new Identifier("mishanguc", "tools")).icon(() -> new ItemStack(MishangucItems.ROTATING_TOOL)).entries((displayContext, entries) -> MishangUtils.instanceStream(MishangucItems.class, ItemConvertible.class).forEach(item -> {
        if (item instanceof final ExplosionToolItem explosionToolItem) {
          explosionToolItem.appendToEntries(entries);
        } else if (item instanceof final FastBuildingToolItem fastBuildingToolItem) {
          fastBuildingToolItem.appendToEntries(entries);
        } else if (item instanceof final ForcePlacingToolItem forcePlacingToolItem) {
          forcePlacingToolItem.appendStacks(entries);
        } else {
          entries.add(item);
        }
      })).build();
  public static final ItemGroup DECORATIONS = FabricItemGroup.builder(new Identifier("mishanguc", "decorations")).icon(() -> new ItemStack(HandrailBlocks.SIMPLE_ORANGE_CONCRETE_HANDRAIL)).entries((displayContext, entries) -> MishangUtils.instanceStream(HandrailBlocks.class, Block.class).forEach(addEntries(entries))).build();

  public static final ItemGroup COLORED_BLOCKS = FabricItemGroup.builder(new Identifier("mishanguc", "colored_blocks")).icon(() -> new ItemStack(ColoredBlocks.COLORED_WOOL)).entries((displayContext, entries) -> MishangUtils.instanceStream(ColoredBlocks.class, Block.class).forEach(addEntries(entries))).build();
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

  private static <T extends Block> Consumer<T> addEntries(ItemGroup.Entries entries) {
    return t -> {
      if (ColorfulBlockRegistry.WHITE_TO_COLORFUL.containsKey(t)) {
        final Map<DyeColor, ? extends Block> map = ColorfulBlockRegistry.WHITE_TO_COLORFUL.get(t);
        for (DyeColor color : FANCY_COLORS) {
          if (map.containsKey(color)) {
            entries.add(map.get(color));
          }
        }
      } else if (!ColorfulBlockRegistry.COLORFUL_BLOCKS.contains(t)) {
        entries.add(t);
      }
    };
  }
}
