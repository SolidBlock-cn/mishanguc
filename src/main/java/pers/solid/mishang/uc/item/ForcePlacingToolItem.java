package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.state.OutlineRenderState;
import net.minecraft.client.render.state.WorldRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.tick.TickManager;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.render.state.ForcePlacingToolState;
import pers.solid.mishang.uc.render.state.MishangRenderStateProvider;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class ForcePlacingToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline, WithMishangTooltip {

  public ForcePlacingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      // 仅限特定情况下使用。
      return ActionResult.PASS;
    }
    BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockHitResult.getBlockPos(), player, player.getStackInHand(hand), blockHitResult, fluidIncluded);
    blockPlacementContext.playSound();
    // 放置方块。对客户端和服务器均生效。
    int flags = getFlags(stack);
    blockPlacementContext.setBlockState(flags);
    blockPlacementContext.setBlockEntity();
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!hasAccess(player, world, true)) {
      // 仅限特定情况下使用。
      return ActionResult.PASS;
    }
    final BlockState blockState = world.getBlockState(pos);
    world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(world.getBlockState(pos)));
    FluidState fluidState = blockState.getFluidState();
    // 在破坏时，直接先将其内容清除。
    world.removeBlockEntity(pos);
    int flags = getFlags(stack);
    world.setBlockState(pos, fluidIncluded ? Blocks.AIR.getDefaultState() : fluidState.getBlockState(), flags);
    return ActionResult.SUCCESS;
  }

  private static int getFlags(ItemStack stack) {
    return 0b1101011010;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(
        TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.2")
            .formatted(Formatting.GRAY));
    if (Boolean.TRUE.equals(includesFluid(stack)) && stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT).shouldDisplay(MishangucComponents.INCLUDES_FLUID)) {
      tooltip.add(
          TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.fluids")
              .formatted(Formatting.GRAY));
    }
    tooltip.add(
        TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.3")
            .formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable ForcePlacingToolState getMishangRenderState(ClientPlayerEntity player, Hand hand, ItemStack stack, WorldExtractionContext context, @Nullable HitResult result) {
    if (!hasAccess(player, context.world(), false)) {
      // 只有在符合条件的情况下，才会绘制边框。
      return null;
    } else {
      final Item item = player.getMainHandStack().getItem();
      if (hand == Hand.OFF_HAND && (item instanceof BlockItem || item instanceof CarryingToolItem)) {
        // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
        return null;
      }
    }

    final ForcePlacingToolState state = new ForcePlacingToolState();

    if (result instanceof EntityHitResult entityHitResult) {
      final Entity entity = entityHitResult.getEntity();
      state.hitEntityPos = entity.getEntityPos();
      final RenderTickCounter deltaTracker = MinecraftClient.getInstance().getRenderTickCounter();
      final TickManager tickRateManager = context.world().getTickManager();
      final float tickProgress = deltaTracker.getTickProgress(!tickRateManager.shouldSkipTick(entity));
      state.hitEntityBoundingBox = entity.getBoundingBox().offset(entity.getLerpedPos(tickProgress).subtract(state.hitEntityPos));
    }

    final BlockHitResult blockHitResult;
    if (result instanceof BlockHitResult) {
      blockHitResult = (BlockHitResult) result;
    } else {
      return state;
    }

    final boolean includesFluid = this.includesFluid(stack, player.isSneaking());
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            context.world(),
            blockHitResult.getBlockPos(),
            player,
            stack,
            blockHitResult,
            includesFluid);

    state.cyanShape = blockPlacementContext.stateToPlace.getOutlineShape(blockPlacementContext.world, blockPlacementContext.posToPlace, ShapeContext.of(player));
    state.cyanPos = blockPlacementContext.posToPlace;

    if (includesFluid) {
      state.blueShape = blockPlacementContext.stateToPlace.getFluidState().getShape(blockPlacementContext.world, blockPlacementContext.posToPlace);
      state.bluePos = blockPlacementContext.posToPlace;
    }
    if (hand == Hand.MAIN_HAND) {
      // 只有当主手持有此物品时，才绘制红色边框。
      state.redShape = blockPlacementContext.hitState.getOutlineShape(blockPlacementContext.world, blockPlacementContext.blockPos, ShapeContext.of(player));
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
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext context,
      OutlineRenderState outlineRenderState) {
    final WorldRenderState worldRenderState = context.worldState();
    if (!(worldRenderState.getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof ForcePlacingToolState state)) {
      return true;
    }

    final MatrixStack matrices = context.matrices();
    final VertexConsumer vertexConsumer = context.consumers().getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = worldRenderState.cameraRenderState.pos;
    double cameraX = cameraPos.x;
    double cameraY = cameraPos.y;
    double cameraZ = cameraPos.z;
    if (state.cyanShape != null && state.cyanPos != null) {
      VertexRendering.drawOutline(
          matrices,
          vertexConsumer,
          state.cyanShape,
          state.cyanPos.getX() - cameraX,
          state.cyanPos.getY() - cameraY,
          state.cyanPos.getZ() - cameraZ,
          OUTLINE_COLOR_CYAN);
    }

    if (state.blueShape != null && state.bluePos != null) {
      VertexRendering.drawOutline(
          matrices,
          vertexConsumer,
          state.blueShape,
          state.bluePos.getX() - cameraX,
          state.bluePos.getY() - cameraY,
          state.bluePos.getZ() - cameraZ,
          OUTLINE_COLOR_BLUE);
    }
    if (state.redShape != null && state.redPos != null) {
      VertexRendering.drawOutline(
          matrices,
          vertexConsumer,
          state.redShape,
          state.redPos.getX() - cameraX,
          state.redPos.getY() - cameraY,
          state.redPos.getZ() - cameraZ,
          OUTLINE_COLOR_RED);
    }
    if (state.yellowShape != null && state.yellowPos != null) {
      VertexRendering.drawOutline(
          matrices,
          vertexConsumer,
          state.yellowShape,
          state.yellowPos.getX() - cameraX,
          state.yellowPos.getY() - cameraY,
          state.yellowPos.getZ() - cameraZ,
          OUTLINE_COLOR_ORANGE);

    }
    return false;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!hasAccess(player, world, true)) return ActionResult.PASS;
    if (world instanceof ServerWorld serverWorld) {
      if (entity instanceof PlayerEntity) {
        entity.kill(serverWorld);
      } else {
        entity.remove(Entity.RemovalReason.KILLED);
      }
      if (entity instanceof EnderDragonPart enderDragonPart) {
        enderDragonPart.owner.kill(serverWorld);
      }
    }
    return ActionResult.SUCCESS;
  }

  /**
   * 玩家是否有权使用此物品。
   */
  @ApiStatus.AvailableSince("1.0.0")
  private static boolean hasAccess(PlayerEntity player, World world, boolean warn) {
    if (!(world instanceof ServerWorld serverWorld)) {
      return MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = serverWorld.getGameRules().get(MishangucRules.FORCE_PLACING_TOOL_ACCESS).get();
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(ClientPlayerEntity player, ItemStack stack, WorldRenderContext context) {
    // 只在使用主手持有此物品时进行渲染。
    final MatrixStack matrices = context.matrices();
    final VertexConsumerProvider consumers = context.consumers();
    if (consumers == null) return;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());

    if (!(context.worldState().getData(MishangRenderStateProvider.MISHANG_BLOCK_OUTLINE) instanceof ForcePlacingToolState state)) {
      return;
    }

    final Vec3d cameraPos = context.worldState().cameraRenderState.pos;
    if (state.hitEntityPos != null && state.hitEntityBoundingBox != null) {
      VertexRendering.drawOutline(matrices, vertexConsumer, VoxelShapes.cuboid(state.hitEntityBoundingBox.offset(-state.hitEntityPos.x, -state.hitEntityPos.y, -state.hitEntityPos.z)), state.hitEntityPos.x - cameraPos.x, state.hitEntityPos.y - cameraPos.y, state.hitEntityPos.z - cameraPos.z, OUTLINE_COLOR_RED);
    }
  }
}
