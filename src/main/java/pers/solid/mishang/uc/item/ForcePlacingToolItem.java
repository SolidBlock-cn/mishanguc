package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

public class ForcePlacingToolItem extends BlockToolItem implements InteractsWithEntity {

  public ForcePlacingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (!player.getAbilities().creativeMode) {
      // 仅限创造模式玩家使用。
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
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().creativeMode) {
      // 仅限创造模式玩家使用。
      return ActionResult.PASS;
    }
    final BlockState blockState = world.getBlockState(pos);
    world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(world.getBlockState(pos)));
    FluidState fluidState = blockState.getFluidState();
    if (!world.isClient) {
      // 在破坏时，直接先将其内容清除。
      if (world.getBlockEntity(pos) instanceof final Inventory inventory) {
        for (int i = 0; i < inventory.size(); i++) {
          inventory.setStack(i, ItemStack.EMPTY);
        }
      }
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
        Text.translatable("item.mishanguc.force_placing_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        Text.translatable("item.mishanguc.force_placing_tool.tooltip.2")
            .formatted(Formatting.GRAY));
    if (Boolean.TRUE.equals(includesFluid(stack))) {
      tooltip.add(
          Text.translatable("item.mishanguc.force_placing_tool.tooltip.fluids")
              .formatted(Formatting.GRAY));
    }
    tooltip.add(
        Text.translatable("item.mishanguc.force_placing_tool.tooltip.3")
            .formatted(Formatting.GRAY));
  }

  @SuppressWarnings("AlibabaMethodTooLong")
  @Environment(EnvType.CLIENT)
  @Override
  public boolean rendersBlockOutline(
      PlayerEntity player,
      ItemStack mainHandStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext) {
    final MinecraftClient client = MinecraftClient.getInstance();
    if (!player.getAbilities().creativeMode) {
      // 只有在创造模式下，才会绘制边框。
      return true;
    }
    final VertexConsumerProvider consumers = worldRenderContext.consumers();
    if (consumers == null) {
      return true;
    }
    final VertexConsumer vertexConsumer = consumers.getBuffer(RenderLayer.LINES);
    final BlockHitResult raycast;
    try {
      raycast = (BlockHitResult) client.crosshairTarget;
      if (raycast == null) {
        return true;
      }
    } catch (ClassCastException e) {
      return true;
    }
    final boolean includesFluid = this.includesFluid(mainHandStack, player.isSneaking());
    final BlockPlacementContext blockPlacementContext =
        new BlockPlacementContext(
            worldRenderContext.world(),
            blockOutlineContext.blockPos(),
            player,
            mainHandStack,
            raycast,
            includesFluid);
    WorldRendererInvoker.drawShapeOutline(
        worldRenderContext.matrixStack(),
        vertexConsumer,
        blockPlacementContext.stateToPlace.getOutlineShape(
            blockPlacementContext.world, blockPlacementContext.posToPlace),
        blockPlacementContext.posToPlace.getX() - blockOutlineContext.cameraX(),
        blockPlacementContext.posToPlace.getY() - blockOutlineContext.cameraY(),
        blockPlacementContext.posToPlace.getZ() - blockOutlineContext.cameraZ(),
        0,
        1,
        1,
        0.8f);
    if (includesFluid) {
      WorldRendererInvoker.drawShapeOutline(
          worldRenderContext.matrixStack(),
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
    WorldRendererInvoker.drawShapeOutline(
        worldRenderContext.matrixStack(),
        vertexConsumer,
        blockPlacementContext.hitState.getOutlineShape(
            blockPlacementContext.world, blockPlacementContext.blockPos),
        blockPlacementContext.blockPos.getX() - blockOutlineContext.cameraX(),
        blockPlacementContext.blockPos.getY() - blockOutlineContext.cameraY(),
        blockPlacementContext.blockPos.getZ() - blockOutlineContext.cameraZ(),
        1,
        0,
        0,
        0.8f);
    if (includesFluid) {
      WorldRendererInvoker.drawShapeOutline(
          worldRenderContext.matrixStack(),
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
    return false;
  }

  @Override
  public @NotNull ActionResult attackEntityCallback(
      PlayerEntity player,
      World world,
      Hand hand,
      Entity entity,
      @Nullable EntityHitResult hitResult) {
    if (!player.getAbilities().creativeMode) return ActionResult.FAIL;
    entity.setInvisible(true);
    entity.setPos(entity.getX(), -114514, entity.getZ());
    entity.kill();
    return ActionResult.SUCCESS;
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
}
