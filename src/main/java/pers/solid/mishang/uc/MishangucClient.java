package pers.solid.mishang.uc;

import com.google.common.base.Predicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.BlockColorRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperties;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperties;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.*;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.item.*;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.networking.GetBlockDataPayload;
import pers.solid.mishang.uc.networking.GetEntityDataPayload;
import pers.solid.mishang.uc.networking.RuleChangedPayload;
import pers.solid.mishang.uc.render.*;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.SignPresets;
import pers.solid.mishang.uc.screen.StandingSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;
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

    SignPresets.loadAll();

    ClientCommandRegistrationCallback.EVENT.register(SignPresetCommand.INSTANCE);

    ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
      if (itemStack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof WithMishangTooltip withMishangTooltip) {
        withMishangTooltip.getMishangTooltip(itemStack, tooltipContext, list, tooltipType);
      }
      if (itemStack.getItem() instanceof WithMishangTooltip withMishangTooltip) {
        withMishangTooltip.getMishangTooltip(itemStack, tooltipContext, list, tooltipType);
      }
    });

    ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
      TooltipDisplay tooltipDisplayComponent = itemStack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
      itemStack.addToTooltip(MishangucComponents.CARRYING_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
      itemStack.addToTooltip(MishangucComponents.EXPLOSION_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
      itemStack.addToTooltip(MishangucComponents.FAST_BUILDING_TOOL_DATA, tooltipContext, tooltipDisplayComponent, list::add, tooltipType);
    });
  }

  private static void registerItemProperties() {
    SelectItemModelProperties.ID_MAPPER.put(Mishanguc.id("color_mixture_type"), ColorMixtureTypeProperty.TYPE);
    SelectItemModelProperties.ID_MAPPER.put(Mishanguc.id("carrying_tool_type"), CarryingToolTypeProperty.TYPE);
    RangeSelectItemModelProperties.ID_MAPPER.put(Mishanguc.id("transparency"), TransparencyPropertyProperty.CODEC);
    RangeSelectItemModelProperties.ID_MAPPER.put(Mishanguc.id("explosion_power"), ExplosionPowerProperty.CODEC);
    ConditionalItemModelProperties.ID_MAPPER.put(Mishanguc.id("explosion_create_fire"), ExplosionCreateFireProperty.CODEC);
    RangeSelectItemModelProperties.ID_MAPPER.put(Mishanguc.id("fast_building_range"), FastBuildingRangeProperty.CODEC);
    ItemTintSources.ID_MAPPER.put(Mishanguc.id("color"), ColoredTintSource.CODEC);
  }

  private static void registerNetworking() {
    // 网络通信
    // 客户端收到服务器发来的编辑告示牌的数据包时，打开编辑界面，允许用户编辑。
    ClientPlayNetworking.registerGlobalReceiver(
        EditSignPayload.ID,
        (payload, context) -> {
          final BlockPos blockPos = payload.blockPos();
          final Minecraft client = context.client();
          final BlockEntity blockEntity = client.level != null ? client.level.getBlockEntity(blockPos) : null;
          if (blockEntity instanceof final HungSignBlockEntity hungSignBlockEntity) {
            final Direction direction = payload.direction().orElseThrow();
            client.execute(() ->
                client.setScreenAndShow(new HungSignBlockEditScreen(client.level.registryAccess(), blockPos, direction, hungSignBlockEntity)));
          } else if (blockEntity instanceof final WallSignBlockEntity wallSignBlockEntity) {
            client.execute(() ->
                client.setScreenAndShow(new WallSignBlockEditScreen(client.level.registryAccess(), wallSignBlockEntity, blockPos)));
          } else if (blockEntity instanceof final StandingSignBlockEntity standingSignBlockEntity) {
            final BlockHitResult blockHitResult = payload.blockHitResult().orElseThrow();
            final Boolean isFront = StandingSignBlock.getHitSide(blockEntity.getBlockState(), blockHitResult);
            if (isFront != null) {
              client.execute(() -> client.setScreenAndShow(new StandingSignBlockEditScreen(client.level.registryAccess(), standingSignBlockEntity, blockPos, isFront)));
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
    BlockColorRegistry.register(List.of(new BlockTintSource() {
      @Override
      public int color(BlockState state) {
        return state.getBlock().defaultMapColor().col;
      }

      @Override
      public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        BlockEntity entity = level.getBlockEntity(pos);
        // 考虑到玩家掉落产生粒子时，坐标会向上偏离一格。
        if (entity == null) entity = level.getBlockEntity(pos.below());
        if (entity instanceof ColoredBlockEntity coloredBlockEntity) {
          return coloredBlockEntity.getColor();
        } else {
          // 考虑到坐标本身的位置没有方块颜色，因此根据附近坐标来推断方块颜色。
          // 受部分渲染器影响，方块颜色会与周围插值，故需确保有自定义颜色的方块周围也会带有相同的自定义颜色。
          int accumulatedNum = 0;
          int accumulatedRed = 0;
          int accumulatedGreen = 0;
          int accumulatedBlue = 0;
          for (BlockPos outPos : BlockPos.withinManhattan(pos, 1, 1, 1)) {
            if (outPos.equals(pos)) continue;
            if (level.getBlockEntity(outPos) instanceof ColoredBlockEntity coloredBlockEntity) {
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
      }
    }), coloredBlocks);
  }

  private static void registerBlockEntityRenderers() {
    // 注册方块实体渲染器
    BlockEntityRenderers.register(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRenderers.register(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, HungSignBlockEntityRenderer::new);
    BlockEntityRenderers.register(MishangucBlockEntities.WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRenderers.register(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer::new);
    BlockEntityRenderers.register(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, WallSignBlockEntityRenderer<FullWallSignBlockEntity>::new);
    BlockEntityRenderers.register(MishangucBlockEntities.STANDING_SIGN_BLOCK_ENTITY, StandingSignBlockEntityRenderer::new);
    BlockEntityRenderers.register(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY, StandingSignBlockEntityRenderer::new);
  }

  private static void registerRenderEvents() {
    // 注册方块外观描绘
    LevelExtractionEvents.AFTER_BLOCK_OUTLINE_EXTRACTION.register(MishangRenderStateProvider.MISHANG_EXTRACTION);
    LevelRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    LevelRenderEvents.BEFORE_GIZMOS.register(RendersBeforeOutline.DEBUG_RENDER);
  }

  private static void registerBlockLayers() {
  }
}
