package pers.solid.mishang.uc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import pers.solid.mishang.uc.block.MUBlocks;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.MUItems;

public class MishangUc implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        new MUBlocks();
        new MUItems();

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
//            player.sendMessage(new LiteralText("[ATTACK BLOCK]"+world.toString() + "   " + pos), false);
            if (player.isSpectator()) return true;
            final ItemStack stack = player.getMainHandStack();
            final Item item = stack.getItem();
            if (item instanceof BlockToolItem) {
                final BlockHitResult hitResult = (BlockHitResult) player.raycast(5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
                return ((BlockToolItem) item).attackBlock(player,world, hitResult.getBlockPos(),hitResult.getSide(), ((BlockToolItem) item).includesFluid(stack,player.isSneaking()))==ActionResult.PASS;
            } else {
                return true;
            }
        });
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
//            player.sendMessage(new LiteralText("[USE BLOCK]"+world.toString() + "   " + hand.toString() + "   " + hitResult.getBlockPos()),false);
            final ItemStack stackInHand = player.getStackInHand(hand);
            final Item item = stackInHand.getItem();
            if (item instanceof BlockToolItem) {
                return ((BlockToolItem) item).useOnBlock(player,world, hitResult, hand, ((BlockToolItem) item).includesFluid(stackInHand,player.isSneaking()));
            } else {
                return ActionResult.PASS;
            }
        });
    }
}
