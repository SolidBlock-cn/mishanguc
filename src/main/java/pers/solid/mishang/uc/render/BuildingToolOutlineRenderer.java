package pers.solid.mishang.uc.render;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.mixin.WorldRendererInvoker;
import pers.solid.mishang.uc.util.BlockMatchingRule;

public class BuildingToolOutlineRenderer implements WorldRenderEvents.BlockOutline{
    @Override
    public boolean onBlockOutline(WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext) {
        final BlockPos blockPos = blockOutlineContext.blockPos();
        DebugRenderer.drawBox(new Box(blockPos),0.2f,1,1,1);
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
            } catch (ClassCastException e) {
                return true;
            }
            final ClientWorld world = worldRenderContext.world();
            @Nullable Block handBlock = null;
            @Nullable Hand hand = null;
            for (Hand hand1 : Hand.values()) {
                final ItemStack stackInHand = player.getStackInHand(hand1);
                if (stackInHand.getItem() instanceof BlockItem) {
                    handBlock = ((BlockItem) stackInHand.getItem()).getBlock();
                    hand = hand1;
                }
            }
            for (BlockPos pos : matchingRule.getPlainValidBlockPoss(world, raycast.getBlockPos(), raycast.getSide(), range)) {
                final BlockState state = world.getBlockState(pos);
                final @NotNull ItemPlacementContext newContext = hand==null ? new ItemPlacementContext(player,hand,new ItemStack(state.getBlock().asItem()),raycast.withBlockPos(pos)) : new ItemPlacementContext(player, hand, player.getStackInHand(hand), raycast.withBlockPos(pos));
                final BlockPos offsetPos = newContext.getBlockPos();
                final BlockState offsetPosState = world.getBlockState(offsetPos);
                BlockState newState = handBlock == null ? null : handBlock.getPlacementState(newContext);
                if (newState==null) newState = newContext.canReplaceExisting() ? state.getBlock().getPlacementState(newContext) : null;
                if (newState==null) newState = state;
                if (newState.getProperties().contains(Properties.WATERLOGGED)) {
                    newState = newState.with(Properties.WATERLOGGED, offsetPosState.getFluidState().isStill());
                }
                if (offsetPosState.canReplace(newContext) && newState.canPlaceAt(world,offsetPos)) WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer,newState.getOutlineShape(world, pos), offsetPos.getX()- vec3d.getX(), offsetPos.getY()- vec3d.getY(), offsetPos.getZ()- vec3d.getZ(), 0,1,1,0.8f);
                WorldRendererInvoker.drawShapeOutline(worldRenderContext.matrixStack(), vertexConsumer,state.getOutlineShape(world,pos), pos.getX()- vec3d.getX(), pos.getY()- vec3d.getY(), pos.getZ()- vec3d.getZ(), 1,0,0,0.8f);
            }
            return false;
        }
        return true;
    }
}
