package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.block.MUBlocks;

public class ItemGroups {
    public static final ItemGroup ROADS =
            FabricItemGroupBuilder.create(new Identifier("mishanguc","roads")).icon(()->new ItemStack(MUBlocks.ASPHALT_ROAD_WITH_WHITE_RIGHT_ANGLE_LINE)).build();
    public static final ItemGroup LIGHTS = FabricItemGroupBuilder.create(new Identifier("mishanguc","lights")).icon(()->new ItemStack(MUBlocks.WHITE_LAMP)).build();
}
