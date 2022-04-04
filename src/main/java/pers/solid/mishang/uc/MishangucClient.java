package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.blockentity.FullWallSignBlockEntity;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.renderer.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.renderer.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

@Environment(EnvType.CLIENT)
public class MishangucClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // 设置相应的 BlockLayer
    MishangUtils.blockStream()
        .forEach(
            field -> {
              try {
                Block value = (Block) field.get(null);
                if (field.isAnnotationPresent(Cutout.class)) {
                  BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
                  if (value instanceof final HandrailBlock handrailBlock) {
                    BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), handrailBlock.central(), handrailBlock.corner(), handrailBlock.stair(), handrailBlock.outer());
                  }
                }
                if (field.isAnnotationPresent(Translucent.class)) {
                  BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getTranslucent());
                  if (value instanceof final HandrailBlock handrailBlock) {
                    BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), handrailBlock.central(), handrailBlock.corner(), handrailBlock.stair(), handrailBlock.outer());
                  }
                }
              } catch (IllegalAccessException | ClassCastException e) {
                Mishanguc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
              }
            });

    // 注册方块外观描绘
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);

    // 注册方块实体渲染器
    BlockEntityRendererRegistry.register(
        MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.register(
        MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<WallSignBlockEntity>::new);
    BlockEntityRendererRegistry.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<FullWallSignBlockEntity>::new);

    // 注册客户端实体事件
    ClientEntityEvents.ENTITY_LOAD.register(
        new Identifier("mishanguc", "notice"),
        (entity, world) ->
            entity.sendSystemMessage(
                new TranslatableText("notice.mishanguc.load")
                    .styled(style -> style.withColor(TextColor.fromRgb(0xd2e877))),
                Util.NIL_UUID));

    // 网络通信
    // 客户端收到服务器发来的编辑告示牌的数据包时，打开编辑界面，允许用户编辑。
    ClientPlayNetworking.registerGlobalReceiver(
        new Identifier("mishanguc", "edit_sign"),
        (client, handler, buf, responseSender) -> {
          try {
            final BlockPos blockPos = buf.readBlockPos();
            final BlockEntity blockEntity =
                client.world != null ? client.world.getBlockEntity(blockPos) : null;
            if (blockEntity instanceof final HungSignBlockEntity hungSignBlockEntity) {
              final Direction direction = buf.readEnumConstant(Direction.class);
              client.execute(
                  () ->
                      client.setScreen(
                          new HungSignBlockEditScreen(
                              hungSignBlockEntity, direction, blockPos)));
            } else if (blockEntity instanceof final WallSignBlockEntity wallSignBlockEntity) {
              client.execute(
                  () ->
                      client.setScreen(
                          new WallSignBlockEditScreen(
                              wallSignBlockEntity, blockPos)));
            }
          } catch (NullPointerException | ClassCastException exception) {
            Mishanguc.MISHANG_LOGGER.error("Error when creating sign edit screen:", exception);
          }
        });
    ClientPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "get_block_data"), new DataTagToolItem.BlockDataReceiver());
    ClientPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "get_entity_data"), new DataTagToolItem.EntityDataReceiver());
  }
}