package pers.solid.mishang.uc;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ObjectUtils;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.ExplosionToolItem;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.item.ForcePlacingToolItem;
import pers.solid.mishang.uc.item.MishangucItems;

public class MishangucItemGroups {
  public static final ItemGroup ROADS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "roads")).icon(
          () -> new ItemStack(RoadBlocks.ROAD_WITH_WHITE_DOUBLE_LINE)).entries((enabledFeatures, entries, operatorEnabled) -> {
        MishangUtils.instanceStream(RoadBlocks.class, ItemConvertible.class).forEach(entries::add);
        RoadSlabBlocks.SLABS.forEach(entries::add);
      }).build();
  public static final ItemGroup LIGHTS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "lights")).icon(() -> new ItemStack(LightBlocks.WHITE_LARGE_WALL_LIGHT)).entries((enabledFeatures, entries, operatorEnabled) -> {
        MishangUtils.instanceStream(LightBlocks.class, ItemConvertible.class).forEach(entries::add);
      }).build();
  public static final ItemGroup SIGNS =
      FabricItemGroup.builder(
          new Identifier("mishanguc", "signs")).icon(
          () -> new ItemStack(StandingSignBlocks.ACACIA_STANDING_SIGN)).entries((enabledFeatures, entries, operatorEnabled) -> {
        MishangUtils.instanceStream(HungSignBlocks.class, ItemConvertible.class).forEach(entries::add);
        MishangUtils.instanceStream(WallSignBlocks.class, ItemConvertible.class).forEach(entries::add);
        MishangUtils.instanceStream(StandingSignBlocks.class, ItemConvertible.class).forEach(entries::add);
      }).build();
  public static final ItemGroup TOOLS =
      FabricItemGroup.builder(new Identifier("mishanguc", "tools")).icon(() -> new ItemStack(MishangucItems.ROTATING_TOOL)).entries((enabledFeatures, entries, operatorEnabled) -> {
        MishangUtils.instanceStream(MishangucItems.class, ItemConvertible.class).forEach(item -> {
          if (item instanceof final ExplosionToolItem explosionToolItem) {
            explosionToolItem.appendToEntries(entries);
          } else if (item instanceof final FastBuildingToolItem fastBuildingToolItem) {
            fastBuildingToolItem.appendToEntries(entries);
          } else if (item instanceof final ForcePlacingToolItem forcePlacingToolItem) {
            forcePlacingToolItem.appendStacks(entries);
          } else {
            entries.add(item);
          }
        });
      }).build();
  public static final ItemGroup DECORATIONS = FabricItemGroup.builder(new Identifier("mishanguc", "decorations")).icon(() -> new ItemStack(HandrailBlocks.SIMPLE_ORANGE_CONCRETE_HANDRAIL)).entries((enabledFeatures, entries, operatorEnabled) -> {
    MishangUtils.instanceStream(HandrailBlocks.class, ItemConvertible.class).forEach(entries::add);
  }).build();

  public static final ItemGroup COLORED_BLOCKS = FabricItemGroup.builder(new Identifier("mishanguc", "colored_blocks")).icon(() -> new ItemStack(ColoredBlocks.COLORED_WOOL)).entries((enabledFeatures, entries, operatorEnabled) -> {
    MishangUtils.instanceStream(ColoredBlocks.class, ItemConvertible.class).forEach(entries::add);
  }).build();

  public static void init() {
    Preconditions.checkState(ObjectUtils.allNotNull(ROADS, LIGHTS, SIGNS, TOOLS, DECORATIONS, COLORED_BLOCKS));
  }
}
