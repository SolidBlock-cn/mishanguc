package pers.solid.mishang.uc;

import com.google.common.base.Predicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.item.property.bool.BooleanProperties;
import net.minecraft.client.render.item.property.numeric.NumericProperties;
import net.minecraft.client.render.item.property.select.SelectProperties;
import net.minecraft.client.render.item.tint.TintSourceTypes;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.Validate;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.*;
import pers.solid.mishang.uc.blocks.MishangucBlocks;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.item.*;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.networking.GetBlockDataPayload;
import pers.solid.mishang.uc.networking.GetEntityDataPayload;
import pers.solid.mishang.uc.networking.RuleChangedPayload;
import pers.solid.mishang.uc.render.*;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.StandingSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.concurrent.atomic.AtomicReference;

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

    registerNetworking();

    registerItemProperties();

    ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
      if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof WithMishangTooltip withMishangTooltip) {
        withMishangTooltip.getMishangTooltip(itemStack, tooltipContext, list, tooltipType);
      }
      if (itemStack.getItem() instanceof WithMishangTooltip withMishangTooltip) {
        withMishangTooltip.getMishangTooltip(itemStack, tooltipContext, list, tooltipType);
      }
    });

    ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
      TooltipDisplayComponent tooltipDisplayComponent = itemStack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT);
      itemStack.appendComponentTooltip(MishangucComponents.CARRYING_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
      itemStack.appendComponentTooltip(MishangucComponents.EXPLOSION_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
      itemStack.appendComponentTooltip(MishangucComponents.FAST_BUILDING_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
    });
  }

  private static void registerItemProperties() {
    SelectProperties.ID_MAPPER.put(Mishanguc.id("color_mixture_type"), ColorMixtureTypeProperty.TYPE);
    SelectProperties.ID_MAPPER.put(Mishanguc.id("carrying_tool_type"), CarryingToolTypeProperty.TYPE);
    NumericProperties.ID_MAPPER.put(Mishanguc.id("transparency"), TransparencyPropertyProperty.CODEC);
    NumericProperties.ID_MAPPER.put(Mishanguc.id("explosion_power"), ExplosionPowerProperty.CODEC);
    BooleanProperties.ID_MAPPER.put(Mishanguc.id("explosion_create_fire"), ExplosionCreateFireProperty.CODEC);
    NumericProperties.ID_MAPPER.put(Mishanguc.id("fast_building_range"), FastBuildingRangeProperty.CODEC);
    TintSourceTypes.ID_MAPPER.put(Mishanguc.id("color"), ColoredTintSource.CODEC);
  }

  private static void registerNetworking() {
    // 网络通信
    // 客户端收到服务器发来的编辑告示牌的数据包时，打开编辑界面，允许用户编辑。
    ClientPlayNetworking.registerGlobalReceiver(
        EditSignPayload.ID,
        (payload, context) -> {
          final BlockPos blockPos = payload.blockPos();
          final MinecraftClient client = context.client();
          final BlockEntity blockEntity = client.world != null ? client.world.getBlockEntity(blockPos) : null;
          if (blockEntity instanceof final HungSignBlockEntity hungSignBlockEntity) {
            final Direction direction = payload.direction().orElseThrow();
            client.execute(() ->
                client.setScreen(new HungSignBlockEditScreen(client.world.getRegistryManager(), blockPos, direction, hungSignBlockEntity)));
          } else if (blockEntity instanceof final WallSignBlockEntity wallSignBlockEntity) {
            client.execute(() ->
                client.setScreen(new WallSignBlockEditScreen(client.world.getRegistryManager(), wallSignBlockEntity, blockPos)));
          } else if (blockEntity instanceof final StandingSignBlockEntity standingSignBlockEntity) {
            final BlockHitResult blockHitResult = payload.blockHitResult().orElseThrow();
            final Boolean isFront = StandingSignBlock.getHitSide(blockEntity.getCachedState(), blockHitResult);
            if (isFront != null) {
              client.execute(() -> client.setScreen(new StandingSignBlockEditScreen(client.world.getRegistryManager(), standingSignBlockEntity, blockPos, isFront)));
            }
          }
        });
    ClientPlayNetworking.registerGlobalReceiver(GetBlockDataPayload.ID, new DataTagToolItem.BlockDataReceiver());
    ClientPlayNetworking.registerGlobalReceiver(GetEntityDataPayload.ID, new DataTagToolItem.EntityDataReceiver());
    ClientPlayNetworking.registerGlobalReceiver(RuleChangedPayload.ID, MishangucRules::handle);
  }

  private static void registerBlockColors() {
    // 注册方块和颜色
    final Block[] coloredBlocks = MishangUtils.blocks().stream().filter(Predicates.instanceOf(ColoredBlock.class))
        .toArray(Block[]::new);
    ColorProviderRegistry.BLOCK.register(
        (state, world, pos, tintIndex) -> {
          if (world == null || pos == null) return -1;
          BlockEntity entity = world.getBlockEntity(pos);
          // 考虑到玩家掉落产生粒子时，坐标会向上偏离一格。
          if (entity == null) entity = world.getBlockEntity(pos.down());
          if (entity instanceof ColoredBlockEntity coloredBlockEntity) {
            return coloredBlockEntity.getColor();
          } else {
            // 考虑到坐标本身的位置没有方块颜色，因此根据附近坐标来推断方块颜色。
            // 受部分渲染器影响，方块颜色会与周围插值，故需确保有自定义颜色的方块周围也会带有相同的自定义颜色。
            int accumulatedNum = 0;
            int accumulatedRed = 0;
            int accumulatedGreen = 0;
            int accumulatedBlue = 0;
            for (BlockPos outPos : BlockPos.iterateOutwards(pos, 1, 1, 1)) {
              if (outPos.equals(pos)) continue;
              if (world.getBlockEntity(outPos) instanceof ColoredBlockEntity coloredBlockEntity) {
                final int color = coloredBlockEntity.getColor();
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
  }

  private static void registerBlockEntityRenderers() {
    // 注册方块实体渲染器
    BlockEntityRendererFactories.register(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<FullWallSignBlockEntity>::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.STANDING_SIGN_BLOCK_ENTITY, StandingSignBlockEntityRenderer::new);
    BlockEntityRendererFactories.register(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY, StandingSignBlockEntityRenderer::new);
  }

  private static void registerRenderEvents() {
    // 注册方块外观描绘
    WorldRenderEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register(MishangRenderStateProvider.MISHANG_EXTRACTION);
    WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    WorldRenderEvents.BEFORE_DEBUG_RENDER.register(RendersBeforeOutline.DEBUG_RENDER);
  }

  private static void registerBlockLayers() {
    // 设置相应的 BlockLayer
    Validate.notEmpty(MishangucBlocks.translucentBlocks).forEach(block -> BlockRenderLayerMap.putBlock(block, BlockRenderLayer.TRANSLUCENT));
    Validate.notEmpty(MishangucBlocks.cutoutBlocks).forEach(block -> {
      BlockRenderLayerMap.putBlock(block, BlockRenderLayer.CUTOUT);
      if (block instanceof AbstractRoadBlock roadBlock && roadBlock.getRoadSlab() != null) {
        BlockRenderLayerMap.putBlock(roadBlock.getRoadSlab(), BlockRenderLayer.CUTOUT);
      }
    });
    MishangucBlocks.translucentBlocks = null;
    MishangucBlocks.cutoutBlocks = null;
  }
}
