package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

public class ModItemGroups {
  public static final ItemGroup ROADS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "roads"),
          () -> new ItemStack(MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE));
  public static final ItemGroup LIGHTS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "lights"), () -> new ItemStack(MishangucBlocks.WHITE_LIGHT));
  public static final ItemGroup SIGNS =
      FabricItemGroupBuilder.build(
          new Identifier("mishanguc", "signs"),
          () -> new ItemStack(MishangucBlocks.GLOWING_BLACK_CONCRETE_HUNG_SIGN));
  public static final ItemGroup TOOLS =
      FabricItemGroupBuilder.create(new Identifier("mishanguc", "tools"))
          .icon(() -> new ItemStack(MishangucItems.ROAD_CONNECTION_STATE_DEBUGGING_TOOL))
          .build();
}
