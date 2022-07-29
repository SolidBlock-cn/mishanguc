package pers.solid.mishang.uc;

import com.google.common.base.Predicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.*;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

import java.awt.*;

@Environment(EnvType.CLIENT)
public class MishangucClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // 设置相应的 BlockLayer
    MishangUtils.blocks().forEach((field, value) -> {
      try {
        if (field.isAnnotationPresent(Cutout.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
          if (value instanceof HandrailBlock) {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ((HandrailBlock) value).central(), ((HandrailBlock) value).corner(), ((HandrailBlock) value).stair(), ((HandrailBlock) value).outer());
          }
        }
        if (field.isAnnotationPresent(Translucent.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getTranslucent());
          if (value instanceof HandrailBlock) {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), ((HandrailBlock) value).central(), ((HandrailBlock) value).corner(), ((HandrailBlock) value).stair(), ((HandrailBlock) value).outer());
          }
        }
      } catch (Throwable e) {
        Mishanguc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
      }
    });

    // 注册方块外观描绘
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBeforeOutline.RENDERER);

    // 注册方块实体渲染器
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<FullWallSignBlockEntity>::new);

    // 注册方块和颜色
    final Block[] coloredBlocks = MishangUtils.blocks().values().stream().filter(Predicates.instanceOf(ColoredBlock.class)).toArray(Block[]::new);
    ColorProviderRegistry.BLOCK.register(
        (state, world, pos, tintIndex) -> {
          if (world == null || pos == null) return -1;
          BlockEntity entity = world.getBlockEntity(pos);
          // 考虑到玩家掉落产生粒子时，坐标会向上偏离一格。
          if (entity == null) entity = world.getBlockEntity(pos.down());
          return entity instanceof ColoredBlockEntity coloredBlockEntity ? coloredBlockEntity.getColor() : -1;
        },
        coloredBlocks
    );
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> {
          final NbtCompound nbt = stack.getSubTag("BlockEntityTag");
          if (nbt != null && nbt.contains("color", NbtElement.NUMBER_TYPE)) {
            return nbt.getInt("color"); // 此处忽略 colorRemembered
          }
          return Color.HSBtoRGB(Util.getMeasuringTimeMs() / 8000f, 0.5f, 0.95f);
        },
        coloredBlocks
    );

    // 玩家踩在道路方块上时加速
    ClientTickEvents.END_WORLD_TICK.register(Road.CHECK_MULTIPLIER::accept);

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
            Mishanguc.MISHANG_LOGGER.error("Error when creating sign edit screen:", exception);
          }
        });
    ClientPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "get_block_data"), new DataTagToolItem.BlockDataReceiver());
    ClientPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "get_entity_data"), new DataTagToolItem.EntityDataReceiver());

    // 模型谓词提供器
    FabricModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL,
        new Identifier("mishanguc", "explosion_power"),
        (itemStack, clientWorld, livingEntity) -> MishangucItems.EXPLOSION_TOOL.power(itemStack));
    FabricModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL, new Identifier("mishanguc", "explosion_create_fire"), (stack, world, entity) -> MishangucItems.EXPLOSION_TOOL.createFire(stack) ? 1 : 0);
    FabricModelPredicateProviderRegistry.register(MishangucItems.FAST_BUILDING_TOOL, new Identifier("mishanguc", "fast_building_range"), (stack, world, entity) -> MishangucItems.FAST_BUILDING_TOOL.getRange(stack) / 64f);
  }
}
