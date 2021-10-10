package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import pers.solid.mishang.uc.item.ForcePlacingToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockPlacementContext;

public class ForcePlacingToolOutlineRenderer implements WorldRenderEvents.BlockOutline {
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
        if (item instanceof ForcePlacingToolItem) {
            final BlockHitResult raycast;
            try {
                raycast = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;
                if (raycast == null) return true;
            } catch (ClassCastException e) {
                return true;
            }
            final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(worldRenderContext.world(), blockOutlineContext.blockPos(), player, mainHandStack, raycast);
            WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, blockPlacementContext.stateToPlace.getOutlineShape(blockPlacementContext.world, blockPlacementContext.posToPlace), blockPlacementContext.posToPlace.getX() - vec3d.getX(), blockPlacementContext.posToPlace.getY() - vec3d.getY(), blockPlacementContext.posToPlace.getZ() - vec3d.getZ(), 0, 1, 1, 0.8f);
            WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, blockPlacementContext.hitState.getOutlineShape(blockPlacementContext.world, blockPlacementContext.blockPos), blockPlacementContext.blockPos.getX() - vec3d.getX(), blockPlacementContext.blockPos.getY() - vec3d.getY(), blockPlacementContext.blockPos.getZ() - vec3d.getZ(), 1, 0, 0, 0.8f);
            return false;
        }
        return true;
    }
}
