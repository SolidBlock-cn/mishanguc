package pers.solid.mishang.uc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.*;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

@Environment(EnvType.CLIENT)
public class MishangucClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // ??????????????? BlockLayer
    MishangUtils.blocks().forEach((field, value) -> {
      try {
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
      } catch (Throwable e) {
        Mishanguc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
      }
    });

    // ????????????????????????
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBeforeOutline.RENDERER);

    // ???????????????????????????
    BlockEntityRendererRegistry.register(
        MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.register(
        MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<pers.solid.mishang.uc.blockentity.WallSignBlockEntity>::new);
    BlockEntityRendererRegistry.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<FullWallSignBlockEntity>::new);

    // ?????????????????????
    ColorProviderRegistry.BLOCK.register(
        (state, world, pos, tintIndex) -> {
          if (world == null) return -1;
          final BlockEntity entity = world.getBlockEntity(pos);
          return entity instanceof ColoredBlockEntity coloredBlockEntity ? coloredBlockEntity.getColor() : -1;
        },
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN,
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN_BAR,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN_BAR
    );
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> {
          final NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
          if (nbt != null && nbt.contains("color", NbtElement.NUMBER_TYPE)) {
            return nbt.getInt("color");
          }
          final ClientPlayerEntity clientPlayer = MinecraftClient.getInstance().player;
          final BlockPos blockPos = clientPlayer == null ? new BlockPos(5, 5, 5) : clientPlayer.getBlockPos();
          return ColoredBlockEntity.getDefaultColorFromPos(blockPos);
        },
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN,
        HungSignBlocks.CUSTOM_CONCRETE_HUNG_SIGN_BAR,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN,
        HungSignBlocks.CUSTOM_TERRACOTTA_HUNG_SIGN_BAR
    );

    // ????????????????????????????????????
    ClientTickEvents.END_WORLD_TICK.register(Road.CHECK_MULTIPLIER::accept);

    // ????????????
    // ????????????????????????????????????????????????????????????????????????????????????????????????????????????
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

    // ?????????????????????
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
    ModelPredicateProviderRegistry.register(MishangucItems.FAST_BUILDING_TOOL, new Identifier("mishanguc", "fast_building_range"), (stack, world, entity, seed) -> MishangucItems.FAST_BUILDING_TOOL.getRange(stack) / 64f);
  }
}
