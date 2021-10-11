package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;

public class BlockToolOutlineRenderer implements WorldRenderEvents.BlockOutline, WorldRenderEvents.DebugRender {
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final PlayerEntity player;
        if (blockOutlineContext.entity() instanceof PlayerEntity) {
            player = (PlayerEntity) blockOutlineContext.entity();
        } else {
            return true;
        }
        final ItemStack mainHandStack = player.getMainHandStack();
        if (!(mainHandStack.getItem() instanceof BlockToolItem)) return true;
        if (((BlockToolItem) mainHandStack.getItem()).includesFluid(mainHandStack, player.isSneaking())) return false;
        final VertexConsumer vertexConsumer = blockOutlineContext.vertexConsumer();
        final ClientWorld world = worldRenderContext.world();
        final BlockPos blockPos = blockOutlineContext.blockPos();
        final BlockState state = blockOutlineContext.blockState();
        WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, state.getOutlineShape(world, blockPos), blockPos.getX() - blockOutlineContext.cameraX(), blockPos.getY() - blockOutlineContext.cameraY(), blockPos.getZ() - blockOutlineContext.cameraZ(), 0, 1, 0, 0.8f);
        return false;
    }

    @Override
    public void beforeDebugRender(WorldRenderContext context) {
        final ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        final ItemStack mainHandStack = player.getMainHandStack();
        if (!(mainHandStack.getItem() instanceof BlockToolItem)) return;
        final ClientWorld world = context.world();
        if (((BlockToolItem) mainHandStack.getItem()).includesFluid(mainHandStack, player.isSneaking())) {
            final BlockHitResult fluidRaycast = BlockToolItem.raycast(world, player, RaycastContext.FluidHandling.ANY);
            BlockPos blockPos = fluidRaycast.getBlockPos();
            final BlockState blockState = world.getBlockState(blockPos);
            final FluidState fluidState = world.getFluidState(blockPos);
            final Vec3d cameraPos = context.camera().getPos();
            WorldRendererInvoker.drawShapeOutline(context.matrixStack(), context.consumers().getBuffer(RenderLayer.LINES), blockState.getOutlineShape(world, blockPos), blockPos.getX() - cameraPos.getX(), blockPos.getY() - cameraPos.getY(), blockPos.getZ() - cameraPos.getZ(), 0, 1, 0, 0.8f);
            WorldRendererInvoker.drawShapeOutline(context.matrixStack(), context.consumers().getBuffer(RenderLayer.LINES), fluidState.getShape(world, blockPos), blockPos.getX() - cameraPos.getX(), blockPos.getY() - cameraPos.getY(), blockPos.getZ() - cameraPos.getZ(), 0, 1, 0.75f, 0.8f);
        }
    }
}
