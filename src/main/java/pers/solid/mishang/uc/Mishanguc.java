package pers.solid.mishang.uc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.Block;
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
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blocks.HandrailBlocks;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.MishangucBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
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
    MishangucBlockEntities.init();

    // 注册事件
    BEGIN_ATTACK_BLOCK_EVENT.register(
        // 仅限客户端执行
        (player, world, hand, pos, direction) -> {
          if (!world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return blockToolItem
                .beginAttackBlock(
                    player,
                    world,
                    hand, hitResult.getBlockPos(),
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
          final ItemStack stack = player.getStackInHand(hand);
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return blockToolItem
                .progressAttackBlock(
                    player,
                    world,
                    hand, hitResult.getBlockPos(),
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
          final ItemStack stack = player.getStackInHand(hand);
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            final BlockHitResult hitResult =
                (BlockHitResult)
                    player.raycast(
                        5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return blockToolItem
                .beginAttackBlock(
                    player,
                    world,
                    hand, hitResult.getBlockPos(),
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
          if (item instanceof final BlockToolItem blockToolItem) {
            return blockToolItem
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
          if (item instanceof final InteractsWithEntity interactsWithEntity) {
            return interactsWithEntity
                .attackEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
        });
    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof final InteractsWithEntity interactsWithEntity) {
            return interactsWithEntity
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
    ServerWorldEvents.LOAD.register(
        new Identifier("mishanguc", "notice"),
        (entity, world) ->
            entity.sendSystemMessage(
                new TranslatableText("notice.mishanguc.load")
                    .styled(style -> style.withColor(0xd0e8a5)),
                Util.NIL_UUID));

    // 注册可燃方块
    final FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();

    final Block[] woodenBlocks = {
        HungSignBlocks.OAK_HUNG_SIGN,
        HungSignBlocks.SPRUCE_HUNG_SIGN,
        HungSignBlocks.BIRCH_HUNG_SIGN,
        HungSignBlocks.JUNGLE_HUNG_SIGN,
        HungSignBlocks.ACACIA_HUNG_SIGN,
        HungSignBlocks.DARK_OAK_HUNG_SIGN,
        HungSignBlocks.OAK_HUNG_SIGN_BAR,
        HungSignBlocks.SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.DARK_OAK_HUNG_SIGN_BAR,
        WallSignBlocks.OAK_WALL_SIGN,
        WallSignBlocks.SPRUCE_WALL_SIGN,
        WallSignBlocks.BIRCH_WALL_SIGN,
        WallSignBlocks.JUNGLE_WALL_SIGN,
        WallSignBlocks.ACACIA_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WALL_SIGN
    };
    for (Block block : woodenBlocks) {
      flammableBlockRegistry.add(block, 5, 20);
    }
    final HandrailBlock[] woodenHandrails = {
        HandrailBlocks.SIMPLE_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_HANDRAIL
    };
    for (HandrailBlock handrail : woodenHandrails) {
      flammableBlockRegistry.add(handrail, 5, 20);
      flammableBlockRegistry.add(handrail.central(), 5, 20);
      flammableBlockRegistry.add(handrail.corner(), 5, 20);
      flammableBlockRegistry.add(handrail.outer(), 5, 20);
      flammableBlockRegistry.add(handrail.stair(), 5, 20);
    }
  }
}
