package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.block.MUBlocks;

import java.lang.reflect.Field;

public class MishangUcClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        for (Field field : MUBlocks.class.getFields()) {
            try {
                Block value = (Block) field.get(new MUBlocks());
                if (field.isAnnotationPresent(Cutout.class) && Block.class.isAssignableFrom(field.getType())) {
                    BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
