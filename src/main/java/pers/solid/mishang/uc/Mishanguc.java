package pers.solid.mishang.uc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blocks.MishangucBlocks;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.FastBuildingToolItem;
import pers.solid.mishang.uc.item.InteractsWithEntity;
import pers.solid.mishang.uc.item.MishangucItems;

public class Mishanguc implements ModInitializer {
  public static final Logger MISHANG_LOGGER = LogManager.getLogger("Mishang Urban Construction");
  /**
   * 比 {@link AttackBlockCallback#EVENT} 更好！
   */
  public static final Event<AttackBlockCallback> BEGIN_ATTACK_BLOCK_EVENT =
      EventFactory.createArrayBacked(
          AttackBlockCallback.class,
          (listeners) ->
              (player, world, hand, pos, direction) -> {
                for (AttackBlockCallback event : listeners) {
                  ActionResult result = event.interact(player, world, hand, pos, direction);

                  if (result != ActionResult.PASS) {
                    return result;
                  }
                }
                return ActionResult.PASS;
              });

  public static final Event<AttackBlockCallback> PROGRESS_ATTACK_BLOCK_EVENT =
      EventFactory.createArrayBacked(
          AttackBlockCallback.class,
          (listeners) ->
              (player, world, hand, pos, direction) -> {
                for (AttackBlockCallback event : listeners) {
                  ActionResult result = event.interact(player, world, hand, pos, direction);
                  if (result != ActionResult.PASS) {
                    return result;
                  }
                }
                return ActionResult.PASS;
              });

  @Override
  public void onInitialize() {
    // 初始化静态字段
    MishangucBlocks.init();
    MishangucItems.init();

    // 注册事件
    BEGIN_ATTACK_BLOCK_EVENT.register(
        // 仅限客户端执行
        (player, world, hand, pos, direction) -> {
          if (!world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof BlockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return ((BlockToolItem) item)
                .beginAttackBlock(
                    player,
                    world,
                    hitResult.getBlockPos(),
                    hitResult.getSide(),
                    ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });

    PROGRESS_ATTACK_BLOCK_EVENT.register(
        // 仅限客户端执行
        (player, world, hand, pos, direction) -> {
          if (!world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof BlockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return ((BlockToolItem) item)
                .progressAttackBlock(
                    player,
                    world,
                    hitResult.getBlockPos(),
                    hitResult.getSide(),
                    ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    AttackBlockCallback.EVENT.register(
        // 仅限服务器执行
        (player, world, hand, pos, direction) -> {
          if (world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof BlockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return ((BlockToolItem) item)
                .beginAttackBlock(
                    player,
                    world,
                    hitResult.getBlockPos(),
                    hitResult.getSide(),
                    ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (!player.getAbilities().allowModifyWorld && !stackInHand.canPlaceOn(world.getTagManager(), new CachedBlockPosition(world, hitResult.getBlockPos(), false))) {
            return ActionResult.PASS;
          }
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
    // 注册服务器接收
    ServerPlayNetworking.registerGlobalReceiver(
        new Identifier("mishanguc", "edit_sign_finish"), BlockEntityWithText.PACKET_HANDLER);
    ServerPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "update_matching_rule"), FastBuildingToolItem.TOOL_CYCLE_HANDLER);

    // 注册服务器运行事件
    ServerLifecycleEvents.SERVER_STARTED.register(
        new Identifier("mishanguc", "notice"),
        server ->
            server.sendSystemMessage(new TranslatableText("notice.mishanguc.load"), Util.NIL_UUID));

    // 注册可燃方块
    FlammableBlockRegistry.getDefaultInstance().add(TagRegistry.block(new Identifier("mishanguc", "burnable")), 5, 20);
  }
}
