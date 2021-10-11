package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.ForcePlacingToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockPlacementContext;

public class ForcePlacingToolOutlineRenderer implements WorldRenderEvents.BlockOutline, WorldRenderEvents.DebugRender {
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final VertexConsumer vertexConsumer = blockOutlineContext.vertexConsumer();
        Vec3d vec3d = worldRenderContext.camera().getPos();
        PlayerEntity player;
        if (blockOutlineContext.entity() instanceof PlayerEntity) {
            player = (PlayerEntity) blockOutlineContext.entity();
        } else {
            return true;
        }
        final ItemStack mainHandStack = player.getMainHandStack();
        final Item item = mainHandStack.getItem();
        if (!(item instanceof ForcePlacingToolItem)) return true;
        if (((ForcePlacingToolItem) item).includesFluid(mainHandStack, player.isSneaking())) return false;
        final BlockHitResult raycast;
        try {
            raycast = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;
            if (raycast == null) return true;
        } catch (ClassCastException e) {
            return true;
        }
        final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(worldRenderContext.world(), blockOutlineContext.blockPos(), player, mainHandStack, raycast, false);
        WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, blockPlacementContext.stateToPlace.getOutlineShape(blockPlacementContext.world, blockPlacementContext.posToPlace), blockPlacementContext.posToPlace.getX() - vec3d.getX(), blockPlacementContext.posToPlace.getY() - vec3d.getY(), blockPlacementContext.posToPlace.getZ() - vec3d.getZ(), 0, 1, 1, 0.8f);
        WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, blockPlacementContext.hitState.getOutlineShape(blockPlacementContext.world, blockPlacementContext.blockPos), blockPlacementContext.blockPos.getX() - vec3d.getX(), blockPlacementContext.blockPos.getY() - vec3d.getY(), blockPlacementContext.blockPos.getZ() - vec3d.getZ(), 1, 0, 0, 0.8f);
        return false;
    }

    @Override
    public void beforeDebugRender(WorldRenderContext context) {
        // 仅在包含流体时进行此渲染。
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        final ItemStack mainHandStack = player.getMainHandStack();
        final Item item = mainHandStack.getItem();
        if (!(item instanceof ForcePlacingToolItem)) return;
        final ClientWorld world = context.world();
        if (((BlockToolItem) item).includesFluid(mainHandStack, player.isSneaking())) {
            final BlockHitResult fluidRaycast = BlockToolItem.raycast(world, player, RaycastContext.FluidHandling.ANY);
            BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, fluidRaycast.getBlockPos(), player, mainHandStack, fluidRaycast, true);
            final MatrixStack matrixStack = context.matrixStack();
            final VertexConsumer buffer = context.consumers().getBuffer(RenderLayer.LINES);
            final BlockPos posToPlace = blockPlacementContext.posToPlace;
            final Vec3d cameraPos = context.camera().getPos();

            // blockStateToPlace
            final BlockState stateToPlace = blockPlacementContext.stateToPlace;
            WorldRendererInvoker.drawShapeOutline(matrixStack, buffer, stateToPlace.getOutlineShape(blockPlacementContext.world, posToPlace), posToPlace.getX() - cameraPos.getX(), posToPlace.getY() - cameraPos.getY(), posToPlace.getZ() - cameraPos.getZ(), 1, 0, 0, 0.8f);
            WorldRendererInvoker.drawShapeOutline(matrixStack, buffer, stateToPlace.getFluidState().getShape(blockPlacementContext.world, posToPlace), posToPlace.getX() - cameraPos.getX(), posToPlace.getY() - cameraPos.getY(), posToPlace.getZ() - cameraPos.getZ(), 1, 0.75f, 0, 0.8f);

            // blockStateToBreak
            final BlockPos blockPos = blockPlacementContext.blockPos;
            final BlockState hitState = blockPlacementContext.hitState;
            WorldRendererInvoker.drawShapeOutline(matrixStack, buffer, hitState.getOutlineShape(blockPlacementContext.world, posToPlace), posToPlace.getX() - cameraPos.getX(), posToPlace.getY() - cameraPos.getY(), posToPlace.getZ() - cameraPos.getZ(), 0, 1, 0, 0.8f);
            WorldRendererInvoker.drawShapeOutline(matrixStack, buffer, hitState.getFluidState().getShape(blockPlacementContext.world, blockPos), blockPos.getX() - cameraPos.getX(), blockPos.getY() - cameraPos.getY(), blockPos.getZ() - cameraPos.getZ(), 0, 1, 0.75f, 0.8f);
        }
    }
}
