package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.MishangucItems;

public class MishangucItemGroups {
  public static final ItemGroup ROADS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "roads"),
          () -> new ItemStack(RoadBlocks.ROAD_WITH_WHITE_RA_LINE));
  public static final ItemGroup LIGHTS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "lights"), () -> new ItemStack(LightBlocks.YELLOW_LIGHT));
  public static final ItemGroup SIGNS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "signs"),
          () -> new ItemStack(HungSignBlocks.GLOWING_BLACK_CONCRETE_HUNG_SIGN));
  public static final ItemGroup TOOLS =
      FabricItemGroupBuilder.build(new Identifier("mishanguc", "tools"), () -> new ItemStack(MishangucItems.ROAD_CONNECTION_STATE_DEBUGGING_TOOL));
  public static final ItemGroup DECORATIONS = FabricItemGroupBuilder.build(new Identifier("mishanguc", "decorations"), () -> new ItemStack(HandrailBlocks.SIMPLE_ORANGE_CONCRETE_HANDRAIL));

  public static final ItemGroup COLORED_BLOCKS = FabricItemGroupBuilder.build(new Identifier("mishanguc", "colored_blocks"), () -> new ItemStack(ColoredBlocks.COLORED_WOOL));
}
