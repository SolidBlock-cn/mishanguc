package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.networking.SignEditFinishPayload;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.HashMap;
import java.util.List;

public abstract class BlockEntityWithText extends BlockEntity {

  public static final Component MESSAGE_GLOW_ON = TextBridge.translatable("message.mishanguc.sign.glow_on");
  public static final Component MESSAGE_GLOW_OFF = TextBridge.translatable("message.mishanguc.sign.glow_off");
  public static final Component MESSAGE_WAX_ON = TextBridge.translatable("message.mishanguc.sign.wax_on");
  public static final Component MESSAGE_WAX_OFF = TextBridge.translatable("message.mishanguc.sign.wax_off");
  public static final PacketHandler PACKET_HANDLER = new PacketHandler();

  public BlockEntityWithText(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
    return saveWithoutMetadata(registryLookup);
  }

  /**
   * 该方块的文字渲染部分的高度。1表示1/16个方块。用于渲染器中的 {@link TextContext#draw(Font, PoseStack, SubmitNodeCollector, int, float, float)} 中的 height 参数。
   *
   * @return 该方块的文字渲染部分的高度。
   */
  public abstract @Range(from = 0, to = 16) float getHeight();

  /**
   * 该方块实体的默认的 {@link TextContext} 对象。
   *
   * @return 该方块实体的默认 <tt>TextContext</tt>。
   */
  @Contract("-> new")
  public abstract TextContext createDefaultTextContext();

  /**
   * @return 正在编辑该告示牌的玩家。如果没有玩家正在编辑，则返回 {@code null}。
   */
  public abstract @Nullable Player getEditor();

  /**
   * 设置正在编辑该告示牌的玩家。
   *
   * @param editor 正在编辑该告示牌的玩家。如果取消玩家编辑权限或者结束编辑，则为 {@code null}。
   */
  public abstract void setEditor(@Nullable Player editor);

  /**
   * 检查编辑告示牌的玩家是否有效。如果玩家正好是旁观模式，或者已经死亡，或者不在该世界内，则取消该玩家编辑权限。
   */
  public void checkEditorValidity() {
    final Player editor = getEditor();
    if (editor != null && editor.isSpectator() && !editor.showVehicleHealth() && editor.level() != level) {
      setEditor(null);
    }
  }

  public void markDirtyAndUpdate() {
    setChanged();
    level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
  }

  public static class PacketHandler implements ServerPlayNetworking.PlayPayloadHandler<SignEditFinishPayload> {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PacketHandler.class);

    @Override
    public void receive(SignEditFinishPayload payload, ServerPlayNetworking.Context context) {
      LOGGER.info("Server side sign_edit_finish packet received!");
      final BlockPos blockPos = payload.blockPos();
      final CompoundTag nbt = payload.nbt();
      final ServerPlayer player = context.player();
      context.server().execute(() -> {
        try {
          final BlockEntityWithText entity = (BlockEntityWithText) player.level().getBlockEntity(blockPos);
          if (entity == null) {
            LOGGER.warn(
                "The entity is null! Cannot write the block entity data at {} {} {}.",
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ());
            return;
          }
          final Player editorAllowed = entity.getEditor();
          entity.setEditor(null);
          final @Unmodifiable ImmutableList<TextContext> textContexts = nbt != null
              ? nbt.getList("texts").stream().flatMap(ListTag::stream)
                .map(e -> TextContext.fromNbt(e, entity.createDefaultTextContext(), context.player().registryAccess()))
              .collect(ImmutableList.toImmutableList())
              : null;
          if (editorAllowed != player) {
            LOGGER.warn(
                "The player editing the block entity {} {} {} is not the player allowed to edit.",
                blockPos.getX(),
                blockPos.getY(),
                blockPos.getZ());
            return;
          }
          if (entity instanceof final HungSignBlockEntity hungSignBlockEntity) {
            final Direction editedSide = hungSignBlockEntity.editedSide;
            hungSignBlockEntity.editedSide = null;
            if (nbt == null) return;
            final HashMap<Direction, List<TextContext>> builder = new HashMap<>(hungSignBlockEntity.texts);
            if (editedSide != null) {
              if (!textContexts.isEmpty()) {
                builder.put(editedSide, textContexts);
              } else {
                builder.remove(editedSide);
              }
            }
            hungSignBlockEntity.texts = ImmutableMap.copyOf(builder);
          } else if (entity instanceof final WallSignBlockEntity wallSignBlockEntity) {
            if (nbt == null) return;
            wallSignBlockEntity.textContexts = textContexts;
          } else if (entity instanceof final StandingSignBlockEntity standingSignBlockEntity) {
            final Boolean editedSite = standingSignBlockEntity.editedSide;
            if (editedSite != null && nbt != null) {
              standingSignBlockEntity.setTextsOnSide(editedSite, textContexts);
            }
          }
          if (entity.getLevel() != null) {
            entity.getLevel().sendBlockUpdated(entity.worldPosition, entity.getBlockState(), entity.getBlockState(), Block.UPDATE_CLIENTS);
          }
          entity.setChanged();
        } catch (ClassCastException e) {
          LOGGER.error("Error when trying to parse NBT received: ", e);
        }
        // 编辑成功。
      });
    }
  }
}
