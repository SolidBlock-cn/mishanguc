package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.BlockPlacementContext;

public class FastBuildingToolOutlineRenderer implements WorldRenderEvents.BlockOutline {
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final VertexConsumer vertexConsumer = blockOutlineContext.vertexConsumer();
        Vec3d vec3d = worldRenderContext.camera().getPos();
        PlayerEntity player;
        try {
            player = (PlayerEntity) blockOutlineContext.entity();
        } catch (ClassCastException e) {
            return true;
        }
        final ItemStack mainHandStack = player.getMainHandStack();
        final Item item = mainHandStack.getItem();
        if (item instanceof FastBuildingToolItem) {
            final BlockMatchingRule matchingRule = ((FastBuildingToolItem) item).getMatchingRule(mainHandStack);
            final int range = ((FastBuildingToolItem) item).getRange(mainHandStack);
            final BlockHitResult raycast;
            try {
                raycast = (BlockHitResult) MinecraftClient.getInstance().crosshairTarget;
                if (raycast == null) return true;
            } catch (ClassCastException e) {
                return true;
            }
            final ClientWorld world = worldRenderContext.world();
            final BlockPlacementContext blockPlacementContext = new BlockPlacementContext(world, blockOutlineContext.blockPos(), player, mainHandStack, raycast, false);
            for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getSide(), range)) {
                final BlockState state = world.getBlockState(pos);
                final BlockPlacementContext offsetBlockPlacementContext = new BlockPlacementContext(blockPlacementContext, pos);
                if (offsetBlockPlacementContext.canPlace() && offsetBlockPlacementContext.canReplace())
                    WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, offsetBlockPlacementContext.stateToPlace.getOutlineShape(world, pos), offsetBlockPlacementContext.posToPlace.getX() - vec3d.getX(), offsetBlockPlacementContext.posToPlace.getY() - vec3d.getY(), offsetBlockPlacementContext.posToPlace.getZ() - vec3d.getZ(), 0, 1, 1, 0.8f);
                WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer, state.getOutlineShape(world, pos), pos.getX() - vec3d.getX(), pos.getY() - vec3d.getY(), pos.getZ() - vec3d.getZ(), 1, 0, 0, 0.8f);
            }
            return false;
        }
        return true;
    }
}
