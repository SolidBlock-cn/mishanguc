package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.item.MishangucItems;

public class ModItemGroups {
  public static final ItemGroup ROADS =
      FabricItemGroupBuilder.create(new Identifier("mishanguc", "roads"))
          .icon(() -> new ItemStack(MishangucBlocks.ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE))
          .build();
  public static final ItemGroup LIGHTS =
      FabricItemGroupBuilder.create(new Identifier("mishanguc", "lights"))
          .icon(() -> new ItemStack(MishangucBlocks.WHITE_LIGHT))
          .build();
  public static final ItemGroup TOOLS =
      FabricItemGroupBuilder.create(new Identifier("mishanguc", "tools"))
          .icon(() -> new ItemStack(MishangucItems.ROAD_CONNECTION_STATE_DEBUGGING_TOOL))
          .build();
}
