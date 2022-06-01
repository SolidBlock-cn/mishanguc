package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

@Environment(EnvType.CLIENT)
public class MishangucClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // 设置相应的 BlockLayer
    MishangUtils.blockStream().forEach(field -> {
      try {
        Block value = (Block) field.get(null);
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
      } catch (IllegalAccessException | ClassCastException e) {
        Mishanguc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
      }
    });

    // 注册方块外观描绘
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);

    // 注册方块实体渲染器
    BlockEntityRendererRegistry.INSTANCE.register(
        MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(
        MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);

    // 注册方块和颜色
    ColorProviderRegistry.BLOCK.register(
        (state, world, pos, tintIndex) -> {
          if (world == null) return -1;
          final BlockEntity entity = world.getBlockEntity(pos);
          return entity instanceof ColoredBlockEntity ? ((ColoredBlockEntity) entity).getColor() : -1;
        },
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN
    );
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> {
          final NbtCompound nbt = stack.getSubTag("BlockEntityTag");
          if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
            return nbt.getInt("color");
          }
          final ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
          final BlockPos blockPos = clientPlayer == null ? new BlockPos(5, 5, 5) : clientPlayer.getBlockPos();
          return ColoredBlockEntity.getDefaultColorFromPos(blockPos);
        },
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN
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
    ModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL,
        new Identifier("mishanguc", "explosion_power"),
        new UnclampedModelPredicateProvider() {
          @Override
          public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            return MishangucItems.EXPLOSION_TOOL.power(stack);
          }

          @SuppressWarnings("deprecation")
          @Override
          public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i) {
            return unclampedCall(itemStack, clientWorld, livingEntity, i);
          }
        });
    ModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL, new Identifier("mishanguc", "explosion_create_fire"), (stack, world, entity, seed) -> MishangucItems.EXPLOSION_TOOL.createFire(stack) ? 1 : 0);
  }
}
