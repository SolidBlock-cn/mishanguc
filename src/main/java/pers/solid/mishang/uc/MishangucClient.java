package pers.solid.mishang.uc;

import com.google.common.base.Predicates;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.StandingSignBlock;
import pers.solid.mishang.uc.blockentity.*;
import pers.solid.mishang.uc.blocks.MishangucBlocks;
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.FastBuildingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.item.DataTagToolItem;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.networking.EditSignPayload;
import pers.solid.mishang.uc.networking.GetBlockDataPayload;
import pers.solid.mishang.uc.networking.GetEntityDataPayload;
import pers.solid.mishang.uc.networking.RuleChangedPayload;
import pers.solid.mishang.uc.render.*;
import pers.solid.mishang.uc.screen.HungSignBlockEditScreen;
import pers.solid.mishang.uc.screen.StandingSignBlockEditScreen;
import pers.solid.mishang.uc.screen.WallSignBlockEditScreen;
import pers.solid.mishang.uc.util.ColorMixtureType;

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

    registerNetworking();

    registerModelPredicateProviders();
  }

  private static void registerModelPredicateProviders() {
    // 模型谓词提供器
    ModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL,
        Mishanguc.id("explosion_power"),
        new ClampedModelPredicateProvider() {
          @Override
          public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
            return stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT).power();
          }

          @SuppressWarnings("deprecation")
          @Override
          public float call(ItemStack itemStack, @Nullable ClientWorld clientWorld, @Nullable LivingEntity livingEntity, int i) {
            return unclampedCall(itemStack, clientWorld, livingEntity, i);
          }
        });
    ModelPredicateProviderRegistry.register(MishangucItems.EXPLOSION_TOOL, Mishanguc.id("explosion_create_fire"), (stack, world, entity, seed) -> stack.getOrDefault(MishangucComponents.EXPLOSION_TOOL_DATA, ExplosionToolComponent.DEFAULT).createFire() ? 1 : 0);
    ModelPredicateProviderRegistry.register(MishangucItems.FAST_BUILDING_TOOL, Mishanguc.id("fast_building_range"), (stack, world, entity, seed) -> stack.getOrDefault(MishangucComponents.FAST_BUILDING_TOOL_DATA, FastBuildingToolData.DEFAULT).range() / 64f);
    ModelPredicateProviderRegistry.register(MishangucItems.CARRYING_TOOL, Mishanguc.id("is_holding_block"), (stack, world, entity, seed) -> BooleanUtils.toInteger(stack.get(MishangucComponents.CARRYING_TOOL_DATA) instanceof CarryingToolData.HoldingBlockState));
    ModelPredicateProviderRegistry.register(MishangucItems.CARRYING_TOOL, Mishanguc.id("is_holding_entity"), (stack, world, entity, seed) -> BooleanUtils.toInteger(stack.get(MishangucComponents.CARRYING_TOOL_DATA) instanceof CarryingToolData.HoldingEntity));

    ModelPredicateProviderRegistry.register(MishangucItems.COLOR_TOOL, Mishanguc.id("transparency"), (stack, world, entity, seed) -> {
      final float opacity = stack.getOrDefault(MishangucComponents.OPACITY, 1f);
      return 1 - opacity;
    });
    ModelPredicateProviderRegistry.register(MishangucItems.COLOR_TOOL, Mishanguc.id("color_mixture_type"), (stack, world, entity, seed) -> stack.getOrDefault(MishangucComponents.COLOR_MIXTURE_TYPE, ColorMixtureType.NORMAL).ordinal() * 0.1f);
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
        .flatMap(block -> block instanceof HandrailBlock handrailBlock ? Stream.of(handrailBlock, handrailBlock.central(), handrailBlock.corner(), handrailBlock.stair(), handrailBlock.outer()) : Stream.of(block))  // since 0.2.4 用于可着色的栏杆方块及其变种
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
    ColorProviderRegistry.ITEM.register(
        (stack, tintIndex) -> {
          final Integer color = stack.get(MishangucComponents.COLOR);
          if (color != null) {
            return color; // 此处忽略 colorRemembered
          }
          return Color.HSBtoRGB(Util.getMeasuringTimeMs() / 4096f + (stack.getItem().hashCode() >> 16) / 64f, 0.5f, 0.95f);
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
    WorldRenderEvents.BLOCK_OUTLINE.register(RendersBlockOutline.RENDERER);
    WorldRenderEvents.BEFORE_BLOCK_OUTLINE.register(RendersBeforeOutline.RENDERER);
  }

  private static void registerBlockLayers() {
    // 设置相应的 BlockLayer
    Validate.notEmpty(MishangucBlocks.translucentBlocks).forEach(block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getTranslucent()));
    Validate.notEmpty(MishangucBlocks.cutoutBlocks).forEach(block -> {
      BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
      if (block instanceof AbstractRoadBlock roadBlock && roadBlock.getRoadSlab() != null) {
        BlockRenderLayerMap.INSTANCE.putBlock(roadBlock.getRoadSlab(), RenderLayer.getCutout());
      }
    });
    MishangucBlocks.translucentBlocks = null;
    MishangucBlocks.cutoutBlocks = null;
  }
}
