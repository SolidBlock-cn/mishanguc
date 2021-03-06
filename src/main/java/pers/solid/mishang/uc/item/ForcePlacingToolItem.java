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
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.render.RendersBeforeOutline;
import pers.solid.mishang.uc.util.BlockPlacementContext;

import java.util.List;

@EnvironmentInterface(value = EnvType.CLIENT, itf = RendersBeforeOutline.class)
public class ForcePlacingToolItem extends BlockToolItem implements InteractsWithEntity, RendersBeforeOutline {

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
      // ?????????????????????????????????
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
      // ???????????????
      blockPlacementContext.setBlockState(0b11010);
      blockPlacementContext.setBlockEntity();
    }
    return ActionResult.success(world.isClient);
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().creativeMode) {
      // ?????????????????????????????????
      return ActionResult.PASS;
    }
    final BlockState blockState = world.getBlockState(pos);
    world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(world.getBlockState(pos)));
    FluidState fluidState = blockState.getFluidState();
    if (!world.isClient) {
      // ?????????????????????????????????????????????
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
        new TranslatableText("item.mishanguc.force_placing_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        new TranslatableText("item.mishanguc.force_placing_tool.tooltip.2")
            .formatted(Formatting.GRAY));
    if (Boolean.TRUE.equals(includesFluid(stack))) {
      tooltip.add(
          new TranslatableText("item.mishanguc.force_placing_tool.tooltip.fluids")
              .formatted(Formatting.GRAY));
    }
    tooltip.add(
        new TranslatableText("item.mishanguc.force_placing_tool.tooltip.3")
            .formatted(Formatting.GRAY));
  }

  @SuppressWarnings("AlibabaMethodTooLong")
  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(
      PlayerEntity player,
      ItemStack itemStack,
      WorldRenderContext worldRenderContext,
      WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    final MinecraftClient client = MinecraftClient.getInstance();
    if (!player.getAbilities().creativeMode) {
      // ????????????????????????????????????????????????
      return true;
    } else if (hand == Hand.OFF_HAND && player.getMainHandStack().getItem() instanceof BlockItem) {
      // ?????????????????????????????????????????????????????????????????????????????????
      return true;
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
      // ????????????????????????????????????????????????????????????
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
    if (!player.getAbilities().creativeMode) return ActionResult.PASS;
    entity.remove(Entity.RemovalReason.KILLED);
    if (entity instanceof EnderDragonPart enderDragonPart) {
      enderDragonPart.owner.kill();
    }
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

  @Environment(EnvType.CLIENT)
  @Override
  public void renderBeforeOutline(WorldRenderContext context, HitResult hitResult, ClientPlayerEntity player, Hand hand) {
    // ???????????????????????????????????????????????????
    if (hand != Hand.MAIN_HAND || !player.getAbilities().creativeMode) return;
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
