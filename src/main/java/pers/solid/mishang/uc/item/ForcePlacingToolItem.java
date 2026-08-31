package pers.solid.mishang.uc.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.ForcePlacingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class ForcePlacingToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline, WithMishangTooltip {

  public ForcePlacingToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      // 仅限特定情况下使用。
      return InteractionResult.CONSUME;
    }
    BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, player.getItemInHand(hand), blockHitResult, fluidIncluded);
    blockPlacementContext.playSound();
    // 放置方块。对客户端和服务器均生效。
    int flags = getFlags(stack);
    blockPlacementContext.setBlockState(flags);
    blockPlacementContext.setBlockEntity();
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      // 仅限特定情况下使用。
      return InteractionResult.CONSUME;
    }
    final BlockState blockState = world.getBlockState(pos);
    world.levelEvent(player, 2001, pos, Block.getId(world.getBlockState(pos)));
    FluidState fluidState = blockState.getFluidState();
    // 在破坏时，直接先将其内容清除。
    world.removeBlockEntity(pos);
    int flags = getFlags(stack);
    world.setBlock(pos, fluidIncluded ? Blocks.AIR.defaultBlockState() : fluidState.createLegacyBlock(), flags);
    return InteractionResult.SUCCESS;
  }

  private static int getFlags(ItemStack stack) {
    return 0b1101011010;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(
        Component.translatable("item.mishanguc.force_placing_tool.tooltip.1")
            .withStyle(ChatFormatting.GRAY));
    tooltip.add(
        Component.translatable("item.mishanguc.force_placing_tool.tooltip.2")
            .withStyle(ChatFormatting.GRAY));
    if (Boolean.TRUE.equals(includesFluid(stack)) && stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.INCLUDES_FLUID)) {
      tooltip.add(
          Component.translatable("item.mishanguc.force_placing_tool.tooltip.fluids")
              .withStyle(ChatFormatting.GRAY));
    }
    tooltip.add(
        Component.translatable("item.mishanguc.force_placing_tool.tooltip.3")
            .withStyle(ChatFormatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable ForcePlacingToolState getMishangRenderState(LocalPlayer player, InteractionHand hand, ItemStack stack, LevelExtractionContext context, @Nullable HitResult result) {
    if (!hasAccess(player, context.level(), false)) {
      // 只有在符合条件的情况下，才会绘制边框。
      return null;
    } else {
      final Item item = player.getMainHandItem().getItem();
      if (hand == InteractionHand.OFF_HAND && (item instanceof BlockItem || item instanceof CarryingToolItem)) {
        // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
        return null;
      }
    }

    final ForcePlacingToolState state = new ForcePlacingToolState();

    if (result instanceof EntityHitResult entityHitResult) {
      final Entity entity = entityHitResult.getEntity();
      final DeltaTracker deltaTracker = Minecraft.getInstance().getDeltaTracker();
      final TickRateManager tickRateManager = context.level().tickRateManager();
      float entityPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(entity));
      state.hitEntityPos = entity.getPosition(entityPartialTicks);
      state.hitEntityBoundingBox = entity.getBoundingBox().move(state.hitEntityPos.subtract(entity.position()));
    }

    final BlockHitResult blockHitResult;
    if (result instanceof BlockHitResult) {
      blockHitResult = (BlockHitResult) result;
    } else {
      return state;
    }

    final boolean includesFluid = this.includesFluid(stack, player.isShiftKeyDown());
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            context.level(),
            blockHitResult.getBlockPos(),
            player,
            stack,
            blockHitResult,
            includesFluid);

    state.cyanShape = blockPlacementContext.stateToPlace.getShape(blockPlacementContext.world, blockPlacementContext.posToPlace, CollisionContext.of(player));
    state.cyanPos = blockPlacementContext.posToPlace;

    if (includesFluid) {
      state.blueShape = blockPlacementContext.stateToPlace.getFluidState().getShape(blockPlacementContext.world, blockPlacementContext.posToPlace);
      state.bluePos = blockPlacementContext.posToPlace;
    }
    if (hand == InteractionHand.MAIN_HAND) {
      // 只有当主手持有此物品时，才绘制红色边框。
      state.redShape = blockPlacementContext.hitState.getShape(blockPlacementContext.world, blockPlacementContext.blockPos, CollisionContext.of(player));
      state.redPos = blockPlacementContext.blockPos;
      if (includesFluid) {
        state.yellowShape = blockPlacementContext.hitState.getFluidState().getShape(blockPlacementContext.world, blockPlacementContext.blockPos);
        state.yellowPos = blockPlacementContext.blockPos;
      }
    }

    return state;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      Player player,
      ItemStack itemStack,
      LevelRenderContext context,
      BlockOutlineRenderState outlineRenderState) {
    final LevelRenderState worldRenderState = context.levelState();
    if (!(worldRenderState.getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof ForcePlacingToolState state)) {
      return true;
    }

    final PoseStack poseStack = context.poseStack();
    final Vec3 cameraPos = worldRenderState.cameraRenderState.pos;
    double cameraX = cameraPos.x;
    double cameraY = cameraPos.y;
    double cameraZ = cameraPos.z;
    if (state.cyanShape != null && state.cyanPos != null) {
      poseStack.pushPose();
      poseStack.translate(state.cyanPos.getX() - cameraX, state.cyanPos.getY() - cameraY, state.cyanPos.getZ() - cameraZ);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          state.cyanShape,
          RenderTypes.lines(),
          OUTLINE_COLOR_CYAN,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          outlineRenderState.isTranslucent());
      poseStack.popPose();
    }

    if (state.blueShape != null && state.bluePos != null) {
      poseStack.pushPose();
      poseStack.translate(state.bluePos.getX() - cameraX, state.bluePos.getY() - cameraY, state.bluePos.getZ() - cameraZ);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          state.blueShape,
          RenderTypes.lines(),
          OUTLINE_COLOR_BLUE,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          outlineRenderState.isTranslucent());
      poseStack.popPose();
    }
    if (state.redShape != null && state.redPos != null) {
      poseStack.pushPose();
      poseStack.translate(state.redPos.getX() - cameraX, state.redPos.getY() - cameraY, state.redPos.getZ() - cameraZ);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          state.redShape,
          RenderTypes.lines(),
          OUTLINE_COLOR_RED,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          outlineRenderState.isTranslucent());
      poseStack.popPose();
    }
    if (state.yellowShape != null && state.yellowPos != null) {
      poseStack.pushPose();
      poseStack.translate(state.yellowPos.getX() - cameraX, state.yellowPos.getY() - cameraY, state.yellowPos.getZ() - cameraZ);
      context.submitNodeCollector().submitShapeOutline(
          poseStack,
          state.yellowShape,
          RenderTypes.lines(),
          OUTLINE_COLOR_ORANGE,
          Minecraft.getInstance().getWindow().getAppropriateLineWidth(),
          outlineRenderState.isTranslucent());
      poseStack.popPose();
    }
    return false;
  }

  @Override
  public InteractionResult attackEntityCallback(
      Player player,
      Level world,
      InteractionHand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!hasAccess(player, world, true)) return InteractionResult.PASS;
    if (world instanceof ServerLevel serverWorld) {
      if (entity instanceof Player) {
        entity.kill(serverWorld);
      } else {
        entity.remove(Entity.RemovalReason.KILLED);
      }
      if (entity instanceof EnderDragonPart enderDragonPart) {
        enderDragonPart.parentMob.kill(serverWorld);
      }
    }
    return InteractionResult.SUCCESS;
  }

  /**
   * 玩家是否有权使用此物品。
   */
  @ApiStatus.AvailableSince("1.0.0")
  private static boolean hasAccess(Player player, Level world, boolean warn) {
    if (!(world instanceof ServerLevel serverWorld)) {
      return MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = serverWorld.getGameRules().get(MishangucRules.FORCE_PLACING_TOOL_ACCESS);
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(LocalPlayer player, ItemStack stack, LevelRenderContext context) {
    // 只在使用主手持有此物品时进行渲染。
    final PoseStack poseStack = context.poseStack();

    if (!(context.levelState().getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof ForcePlacingToolState state)) {
      return;
    }

    final Vec3 cameraPos = context.levelState().cameraRenderState.pos; // 检查 cameraPos 是否需要
    if (state.hitEntityPos != null && state.hitEntityBoundingBox != null) {
      poseStack.pushPose();
      poseStack.translate(state.hitEntityPos.x - cameraPos.x, state.hitEntityPos.y - cameraPos.y, state.hitEntityPos.z - cameraPos.z);
      context.submitNodeCollector().submitShapeOutline(poseStack, Shapes.create(state.hitEntityBoundingBox.move(-state.hitEntityPos.x, -state.hitEntityPos.y, -state.hitEntityPos.z)), RenderTypes.lines(), OUTLINE_COLOR_RED, Minecraft.getInstance().getWindow().getAppropriateLineWidth(), true); // todo 检查 afterTerrain
      poseStack.popPose();
    }
  }
}
