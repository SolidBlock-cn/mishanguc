package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangucClient;
import pers.solid.mishang.uc.MishangucRules;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.BlockPlacementContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class ForcePlacingToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline {

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
    BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            world,
            blockHitResult.getBlockPos(),
            player,
            player.getStackInHand(hand),
            blockHitResult,
            fluidIncluded);
    blockPlacementContext.playSound();
    if (!world.isClient()) {
      // 放置方块。
      blockPlacementContext.setBlockState(0b11010);
      blockPlacementContext.setBlockEntity();
    }
    return ActionResult.success(world.isClient);
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
    if (!world.isClient) {
      // 在破坏时，直接先将其内容清除。
      world.removeBlockEntity(pos);
      world.setBlockState(
          pos, fluidIncluded ? Blocks.AIR.getDefaultState() : fluidState.getBlockState(), 24);
    }
    return ActionResult.success(world.isClient);
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("item.mishanguc.force_placing_tool.tooltip.2")
            .formatted(Formatting.GRAY));
    if (Boolean.TRUE.equals(includesFluid(stack))) {
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
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final MinecraftClient client = MinecraftClient.getInstance();
    if (!hasAccess(player, worldRenderContext.world(), false)) {
      // 只有在符合条件的情况下，才会绘制边框。
      return true;
    } else {
      final Item item = player.getMainHandStack().getItem();
      if (hand == Hand.OFF_HAND && (item instanceof BlockItem || item instanceof HoldingToolItem)) {
        // 当玩家副手持有物品，主手持有方块时，直接跳过，不绘制。
        return true;
      }
    }
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) {
      return true;
    }
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);

    final BlockHitResult blockHitResult;
    final MatrixStack matrices = worldRenderContext.matrixStack();
    HitResult crosshairTarget = client.crosshairTarget;
    if (crosshairTarget instanceof BlockHitResult) {
      blockHitResult = (BlockHitResult) crosshairTarget;
    } else {
      return true;
    }
    final boolean includesFluid = this.includesFluid(itemStack, player.isSneaking());
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            worldRenderContext.world(),
            blockOutlineContext.blockPos(),
            player,
            itemStack,
            blockHitResult,
            includesFluid);
    WorldRendererInvoker.drawShapeOutline(
        matrices,
        vertexConsumer,
        blockPlacementContext.stateToPlace.getOutlineShape(
            blockPlacementContext.world, blockPlacementContext.posToPlace, ShapeContext.of(player)),
        blockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
        blockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
        blockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
        0,
        1,
        1,
        0.8f);
    if (includesFluid) {
      WorldRendererInvoker.drawShapeOutline(
          matrices,
          vertexConsumer,
          blockPlacementContext
              .stateToPlace
              .getFluidState()
              .getShape(blockPlacementContext.world, blockPlacementContext.posToPlace),
          blockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
          blockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
          blockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
          0,
          0.5f,
          1,
          0.5f);
    }
    if (hand == Hand.MAIN_HAND) {
      // 只有当主手持有此物品时，才绘制红色边框。
      WorldRendererInvoker.drawShapeOutline(
          matrices,
          vertexConsumer,
          blockPlacementContext.hitState.getOutlineShape(
              blockPlacementContext.world, blockPlacementContext.blockPos, ShapeContext.of(player)),
          blockPlacementContext.blockPos.getX() - blockOutlineContext.cameraX(),
          blockPlacementContext.blockPos.getY() - blockOutlineContext.cameraY(),
          blockPlacementContext.blockPos.getZ() - blockOutlineContext.cameraZ(),
          1,
          0,
          0,
          0.8f);
      if (includesFluid) {
        WorldRendererInvoker.drawShapeOutline(
            matrices,
            vertexConsumer,
            blockPlacementContext
                .hitState
                .getFluidState()
                .getShape(blockPlacementContext.world, blockPlacementContext.blockPos),
            blockPlacementContext.blockPos.getX() - blockOutlineContext.cameraX(),
            blockPlacementContext.blockPos.getY() - blockOutlineContext.cameraY(),
            blockPlacementContext.blockPos.getZ() - blockOutlineContext.cameraZ(),
            1,
            0.5f,
            0,
            0.5f);
      }
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
    if (!world.isClient) {
      if (entity instanceof PlayerEntity) {
        entity.kill();
      } else {
        entity.remove(Entity.RemovalReason.KILLED);
      }
      if (entity instanceof EnderDragonPart enderDragonPart) {
        enderDragonPart.owner.kill();
      }
    }
    return ActionResult.SUCCESS;
  }

  /**
   * 玩家是否有权使用此物品。
   */
  @ApiStatus.AvailableSince("1.0.0")
  private static boolean hasAccess(PlayerEntity player, World world, boolean warn) {
    if (world.isClient) {
      return MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.get().hasAccess(player);
    } else {
      final MishangucRules.ToolAccess toolAccess = world.getGameRules().get(MishangucRules.FORCE_PLACING_TOOL_ACCESS).get();
      return toolAccess.hasAccess(player, warn);
    }
  }

  @Override
  public @NotNull ActionResult useEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    return ActionResult.PASS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(WorldRenderContext context, HitResult hitResult, ClientPlayerEntity player, Hand hand) {
    // 只在使用主手持有此物品时进行渲染。
    if (hand != Hand.MAIN_HAND || !hasAccess(player, context.world(), false)) return;
    final MatrixStack matrices = context.matrixStack();
    final VertexConsumerProvider consumers = context.consumers();
    if (consumers == null) return;
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.getLines());
    final Vec3d cameraPos = context.camera().getPos();
    if (hitResult instanceof EntityHitResult entityHitResult) {
      final Entity entity = entityHitResult.getEntity();
      WorldRendererInvoker.drawShapeOutline(matrices, vertexConsumer, VoxelShapes.cuboid(entity.getBoundingBox()), -cameraPos.x, -cameraPos.y, -cameraPos.z, 1.0f, 0f, 0f, 0.8f);
    }
  }
}
