package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.MUBlocks;
import pers.solid.mishang.uc.render.RendersBlockOutline;

import java.lang.reflect.Field;

public class MishangUcClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    for (Field field : MUBlocks.class.getFields()) {
      try {
        Block value = (Block) field.get(null);
        if (field.isAnnotationPresent(Cutout.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
        }
        if (field.isAnnotationPresent(Translucent.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getTranslucent());
        }
      } catch (IllegalAccessException | ClassCastException e) {
        e.printStackTrace();
      }
    }

    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
//        WorldRenderEvents.BLOCK_OUTLINE.register(new FastBuildingToolOutlineRenderer());
//        WorldRenderEvents.BLOCK_OUTLINE.register(new SlabToolOutlineRenderer());
//        final ForcePlacingToolOutlineRenderer forcePlacingToolOutlineRenderer = new ForcePlacingToolOutlineRenderer();
//        WorldRenderEvents.BLOCK_OUTLINE.register(forcePlacingToolOutlineRenderer);
//        final BlockToolOutlineRenderer blockToolOutlineRenderer = new BlockToolOutlineRenderer();
//        WorldRenderEvents.BLOCK_OUTLINE.register(blockToolOutlineRenderer);
  }
}
