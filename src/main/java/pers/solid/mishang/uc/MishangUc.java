package pers.solid.mishang.uc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import pers.solid.mishang.uc.block.MUBlocks;
import pers.solid.mishang.uc.item.MUItems;
import pers.solid.mishang.uc.render.BuildingToolOutlineRenderer;
import pers.solid.mishang.uc.render.SlabToolOutlineRenderer;

public class MishangUc implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("Hello Fabric world!");
		new MUBlocks();
		new MUItems();

		WorldRenderEvents.BLOCK_OUTLINE.register(new BuildingToolOutlineRenderer());
		WorldRenderEvents.BLOCK_OUTLINE.register(new SlabToolOutlineRenderer());
	}
}
