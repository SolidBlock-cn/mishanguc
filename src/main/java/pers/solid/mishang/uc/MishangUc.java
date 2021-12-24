package pers.solid.mishang.uc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.solid.mishang.uc.blocks.MishangucBlocks;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.InteractsWithEntity;
import pers.solid.mishang.uc.items.MishangucItems;

public class MishangUc implements ModInitializer {
  public static final Logger MISHANG_LOGGER = LogManager.getLogger("Mishang Urban Construction");

  @Override
  public void onInitialize() {
    MishangucBlocks.init();
    MishangucItems.init();

    PlayerBlockBreakEvents.BEFORE.register(
        (world, player, pos, state, blockEntity) -> {
          if (player.isSpectator()) {
            return true;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof BlockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return ((BlockToolItem) item)
                    .attackBlock(
                        player,
                        world,
                        hitResult.getBlockPos(),
                        hitResult.getSide(),
                        ((BlockToolItem) item).includesFluid(stack, player.isSneaking()))
                == ActionResult.PASS;
          } else {
            return true;
          }
        });
    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof BlockToolItem) {
            return ((BlockToolItem) item)
                .useOnBlock(
                    player,
                    world,
                    hitResult,
                    hand,
                    ((BlockToolItem) item).includesFluid(stackInHand, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    AttackEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof InteractsWithEntity) {
            return ((InteractsWithEntity) item)
                .attackEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
        });
    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof InteractsWithEntity) {
            return ((InteractsWithEntity) item)
                .useEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
        });
  }
}
