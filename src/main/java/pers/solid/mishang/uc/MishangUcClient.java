package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.renderer.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.renderer.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

import java.lang.reflect.Field;

@Environment(EnvType.CLIENT)
public class MishangUcClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // 设置相应的 BlockLayer
    for (Field field : MishangucBlocks.class.getFields()) {
      try {
        if (!Block.class.isAssignableFrom(field.getType())) {
          continue;
        }
        Block value = (Block) field.get(null);
        if (field.isAnnotationPresent(Cutout.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
        }
        if (field.isAnnotationPresent(Translucent.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getTranslucent());
        }
      } catch (IllegalAccessException | ClassCastException e) {
        MishangUc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
      }
    }

    // 注册方块外观描绘
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);

    // 注册方块实体渲染器
    BlockEntityRendererRegistry.INSTANCE.register(
        MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(
        MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);

    // 网络通信
    // 客户端收到服务器发来的编辑告示牌的数据包时，打开编辑界面，允许用户编辑。
    ClientPlayNetworking.registerGlobalReceiver(
        new Identifier("mishanguc", "edit_sign"),
        (client, handler, buf, responseSender) -> {
          try {
            final BlockPos blockPos = buf.readBlockPos();
            final BlockEntity blockEntity =
                client.world != null ? client.world.getBlockEntity(blockPos) : null;
            if (blockEntity instanceof HungSignBlockEntity) {
              final Direction direction = buf.readEnumConstant(Direction.class);
              client.execute(
                  () ->
                      client.openScreen(
                          new HungSignBlockEditScreen(
                              (HungSignBlockEntity) blockEntity, direction, blockPos)));
            } else if (blockEntity instanceof WallSignBlockEntity) {
              client.execute(
                  () ->
                      client.openScreen(
                          new WallSignBlockEditScreen(
                              (WallSignBlockEntity) blockEntity, blockPos)));
            }
          } catch (NullPointerException | ClassCastException exception) {
            MishangUc.MISHANG_LOGGER.error("Error when creating sign edit screen:", exception);
          }
        });
  }
}
