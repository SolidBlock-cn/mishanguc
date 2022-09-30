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
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.BooleanUtils;
import pers.solid.mishang.uc.annotations.Cutout;
import pers.solid.mishang.uc.annotations.Translucent;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.item.HoldingToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.render.HungSignBlockEntityRenderer;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.RendersBlockOutline;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;

import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class MishangucClient implements ClientModInitializer {
  /**
   * @see MishangucRules#FORCE_PLACING_TOOL_ACCESS
   */
  public static final AtomicReference<MishangucRules.ToolAccess> CLIENT_FORCE_PLACING_TOOL_ACCESS = new AtomicReference<>(MishangucRules.ToolAccess.CREATIVE_ONLY);
  /**
   * @see MishangucRules#CARRYING_TOOL_ACCESS
   */
  public static final AtomicReference<MishangucRules.ToolAccess> CLIENT_CARRYING_TOOL_ACCESS = new AtomicReference<>(MishangucRules.ToolAccess.ALL);

  @Override
  public void onInitializeClient() {
    registerBlockLayers();

    registerRenderEvents();

    registerBlockEntityRenderers();

    registerBlockColors();

    // 玩家踩在道路方块上时加速
    ClientTickEvents.END_WORLD_TICK.register(Road.CHECK_MULTIPLIER::accept);

    registerNetworking();

    registerModelPredicateProviders();
  }

  private static void registerModelPredicateProviders() {
    // 模型谓词提供器
    FabricModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL,
        new Identifier("mishanguc", "explosion_power"),
        (stack, world, entity) -> MishangucItems.EXPLOSION_TOOL.power(stack));
    FabricModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL, new Identifier("mishanguc", "explosion_create_fire"), (stack, world, entity) -> MishangucItems.EXPLOSION_TOOL.createFire(stack) ? 1 : 0);
    FabricModelPredicateProviderRegistry.register(MishangucItems.FAST_BUILDING_TOOL, new Identifier("mishanguc", "fast_building_range"), (stack, world, entity) -> MishangucItems.FAST_BUILDING_TOOL.getRange(stack) / 64f);
    FabricModelPredicateProviderRegistry.register(MishangucItems.CARRYING_TOOL, new Identifier("mishanguc", "is_holding_block"), (stack, world, entity) -> BooleanUtils.toInteger(HoldingToolItem.hasHoldingBlockState(stack)));
    FabricModelPredicateProviderRegistry.register(MishangucItems.CARRYING_TOOL, new Identifier("mishanguc", "is_holding_entity"), (stack, world, entity) -> BooleanUtils.toInteger(HoldingToolItem.hasHoldingEntity(stack)));
  }

  private static void registerNetworking() {
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
    ClientPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "rule_changed"), MishangucRules::handle);
  }

  private static void registerBlockColors() {
    // 注册方块和颜色
    final Block[] coloredBlocks = MishangUtils.blocks().values().stream().filter(Predicates.instanceOf(ColoredBlock.class))
        .flatMap(block -> block instanceof HandrailBlock ? Stream.of(((HandrailBlock) block), ((HandrailBlock) block).central(), ((HandrailBlock) block).corner(), ((HandrailBlock) block).stair(), ((HandrailBlock) block).outer()) : Stream.of(block))  // since 0.2.4 用于可着色的栏杆方块及其变种
        .toArray(Block[]::new);
    ColorProviderRegistry.BLOCK.register(
        (state, world, pos, tintIndex) -> {
          if (world == null || pos == null) return -1;
          BlockEntity entity = world.getBlockEntity(pos);
          // 考虑到玩家掉落产生粒子时，坐标会向上偏离一格。
          if (entity == null) entity = world.getBlockEntity(pos.down());
          if (entity instanceof ColoredBlockEntity) {
            return ((ColoredBlockEntity) entity).getColor();
          } else {
            // 考虑到坐标本身的位置没有方块颜色，因此根据附近坐标来推断方块颜色。
            // 受部分渲染器影响，方块颜色会与周围插值，故需确保有自定义颜色的方块周围也会带有相同的自定义颜色。
            int accumulatedNum = 0;
            int accumulatedRed = 0;
            int accumulatedGreen = 0;
            int accumulatedBlue = 0;
            for (BlockPos outPos : BlockPos.iterateOutwards(pos, 1, 1, 1)) {
              if (outPos.equals(pos)) continue;
              final BlockEntity outEntity = world.getBlockEntity(outPos);
              if (outEntity instanceof ColoredBlockEntity) {
                final int color = ((ColoredBlockEntity) outEntity).getColor();
                accumulatedNum += 1;
                accumulatedRed += color >> 16 & 255;
                accumulatedGreen += color >> 8 & 255;
                accumulatedBlue += color & 255;
              }
            }
            if (accumulatedNum > 0) {
              return (accumulatedRed / accumulatedNum << 16) + (accumulatedGreen / accumulatedNum << 8) + accumulatedBlue / accumulatedNum;
            } else {
              return -1;
            }
          }
        },
        coloredBlocks
    );
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> {
          final NbtCompound nbt = stack.getSubTag("BlockEntityTag");
          if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
            return nbt.getInt("color"); // 此处忽略 colorRemembered
          }
          return Color.HSBtoRGB(Util.getMeasuringTimeMs() / 4096f + (stack.getItem().hashCode() >> 16) / 64f, 0.5f, 0.95f);
        },
        coloredBlocks
    );
  }

  private static void registerBlockEntityRenderers() {
    // 注册方块实体渲染器
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererRegistry.INSTANCE.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
  }

  private static void registerRenderEvents() {
    // 注册方块外观描绘
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBeforeOutline.RENDERER);
  }

  private static void registerBlockLayers() {
    // 设置相应的 BlockLayer
    MishangUtils.blocks().forEach((field, value) -> {
      try {
        if (field.isAnnotationPresent(Cutout.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getCutout());
          if (value instanceof HandrailBlock) {
            HandrailBlock handrailBlock = (HandrailBlock) value;
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), handrailBlock.central(), handrailBlock.corner(), handrailBlock.stair(), handrailBlock.outer());
          }
        }
        if (field.isAnnotationPresent(Translucent.class)) {
          BlockRenderLayerMap.INSTANCE.putBlock(value, RenderLayer.getTranslucent());
          if (value instanceof HandrailBlock) {
            HandrailBlock handrailBlock = (HandrailBlock) value;
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getTranslucent(), handrailBlock.central(), handrailBlock.corner(), handrailBlock.stair(), handrailBlock.outer());
          }
        }
      } catch (Throwable e) {
        Mishanguc.MISHANG_LOGGER.warn("Error when setting BlockLayers:", e);
      }
    });
  }
}
