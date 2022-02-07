package pers.solid.mishang.uc;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.block.MishangucBlocks;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.HungSignBlockEntity;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.InteractsWithEntity;
import pers.solid.mishang.uc.item.MishangucItems;
import pers.solid.mishang.uc.util.TextContext;

import java.util.HashMap;
import java.util.List;

public class MishangUc implements ModInitializer {
  public static final Logger MISHANG_LOGGER = LogManager.getLogger("Mishang Urban Construction");

  /** 用于接受玩家在客户端完成告示牌方块编辑时发送过来的 packet。 */
  private void handleEditSignFinish(
      MinecraftServer server,
      ServerPlayerEntity player,
      ServerPlayNetworkHandler handler,
      PacketByteBuf buf,
      PacketSender responseSender) {
    MISHANG_LOGGER.info("Server side sign_edit_finish packet received!");
    final BlockPos blockPos = buf.readBlockPos();
    final NbtCompound nbtCompound = buf.readNbt();
    if (nbtCompound == null) return;
    final NbtList nbt = nbtCompound.getList("texts", 10);
    server.execute(
        () -> {
          final BlockEntityWithText entity =
              (BlockEntityWithText) player.world.getBlockEntity(blockPos);
          try {
            if (entity == null) {
              MISHANG_LOGGER.warn(
                  "The entity is null! Cannot write the block entity data at {} {} {}.",
                  blockPos.getX(),
                  blockPos.getY(),
                  blockPos.getZ());
              return;
            }
            final PlayerEntity editorAllowed = entity.getEditor();
            entity.setEditor(null);
            final @Unmodifiable List<TextContext> textContexts =
                new ImmutableList.Builder<TextContext>()
                    .addAll(
                        nbt.stream()
                            .map(e -> TextContext.fromNbt(e, entity.getDefaultTextContext()))
                            .iterator())
                    .build();
            if (editorAllowed != player) {
              MISHANG_LOGGER.warn(
                  "The player editing the block entity {} {} {} is not the player allowed to edit.",
                  blockPos.getX(),
                  blockPos.getY(),
                  blockPos.getZ());
              return;
            }
            if (entity instanceof HungSignBlockEntity) {
              final HashMap<@NotNull Direction, @NotNull List<@NotNull TextContext>> builder =
                  Maps.newHashMap(((HungSignBlockEntity) entity).texts);
              final Direction editedSide = ((HungSignBlockEntity) entity).editedSide;
              if (editedSide != null) builder.put(editedSide, textContexts);
              ((HungSignBlockEntity) entity).editedSide = null;
              ((HungSignBlockEntity) entity).texts = ImmutableMap.copyOf(builder);
            } else if (entity instanceof WallSignBlockEntity) {
              ((WallSignBlockEntity) entity).textContexts = textContexts;
            }
          } catch (ClassCastException e) {
            MISHANG_LOGGER.error("Error when trying to parse NBT received: ", e);
            MISHANG_LOGGER.error(
                "The NBT string received is as follows:\n" + NbtHelper.toPrettyPrintedText(nbt));
          }
          // 编辑成功。
        });
  }

  @Override
  public void onInitialize() {
    // 初始化静态字段
    MishangucBlocks.init();
    MishangucItems.init();

    // 注册事件
    AttackBlockCallback.EVENT.register(
        (player, world, hand, pos, direction) -> {
          if (player.isSpectator()) {
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
                .attackBlock(
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
        new Identifier("mishanguc", "edit_sign_finish"), this::handleEditSignFinish);

    // 注册可燃方块
  }
}
