package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.util.TextContext;

import java.util.HashMap;
import java.util.List;

public abstract class BlockEntityWithText extends BlockEntity {

  public BlockEntityWithText(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  /**
   * @see net.minecraft.block.entity.SignBlockEntity#getCommandSource(ServerPlayerEntity)
   */
  public ServerCommandSource getCommandSource(@Nullable ServerPlayerEntity player) {
    String string = player == null ? "TextPad" : player.getName().getString();
    Text text = player == null ? new LiteralText("TextPad") : player.getDisplayName();
    return new ServerCommandSource(
        CommandOutput.DUMMY,
        Vec3d.ofCenter(this.pos),
        Vec2f.ZERO,
        (ServerWorld) this.world,
        2,
        string,
        text,
        world == null ? null : world.getServer(),
        player);
  }

  @Nullable
  @Override
  public Packet<ClientPlayPacketListener> toUpdatePacket() {
    return BlockEntityUpdateS2CPacket.create(this);
  }

  @Override
  public NbtCompound toInitialChunkDataNbt() {
    return createNbt();
  }

  /**
   * 该方块的文字渲染部分的高度。1表示1/16个方块。用于渲染器中的 {@link
   * pers.solid.mishang.uc.util.TextContext#draw(TextRenderer, MatrixStack, VertexConsumerProvider,
   * int, float, float)} 中的 height 参数。
   *
   * @return 该方块的文字渲染部分的高度。
   */
  public abstract @Range(from = 0, to = 16) float getHeight();

  /**
   * 该方块实体的默认的 {@link TextContext} 对象。可以是 {@code new TextContext()}。
   *
   * @return 该方块实体的默认 <tt>TextContext</tt>。
   */
  @Contract("->new")
  public abstract TextContext getDefaultTextContext();

  /**
   * @return 正在编辑该告示牌的玩家。如果没有玩家正在编辑，则返回 {@code null}。
   */
  public abstract @Nullable PlayerEntity getEditor();

  /**
   * 设置正在编辑该告示牌的玩家。
   *
   * @param editor 正在编辑该告示牌的玩家。如果取消玩家编辑权限或者结束编辑，则为 {@code null}。
   */
  public abstract void setEditor(@Nullable PlayerEntity editor);

  /**
   * 检查编辑告示牌的玩家是否有效。如果玩家正好是旁观模式，或者已经死亡，或者不在该世界内，则取消该玩家编辑权限。
   */
  public void checkEditorValidity() {
    final PlayerEntity editor = getEditor();
    if (editor != null && editor.isSpectator() && !editor.isLiving() && editor.world != world) {
      setEditor(null);
    }
  }

  public static final PacketHandler PACKET_HANDLER = new PacketHandler();

  private static class PacketHandler implements ServerPlayNetworking.PlayChannelHandler {
    protected static final Logger LOGGER = LoggerFactory.getLogger(PacketHandler.class);

    @Override
    public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
      LOGGER.info("Server side sign_edit_finish packet received!");
      final BlockPos blockPos = buf.readBlockPos();
      final NbtCompound nbt = buf.readNbt();
      server.execute(
          () -> {
            final BlockEntityWithText entity =
                (BlockEntityWithText) player.world.getBlockEntity(blockPos);
            // 该参数仅限对应实体为 HungSignBlockEntity 时存在，也仅在此情况下，buf 中会存在此值。。
            try {
              if (entity == null) {
                LOGGER.warn(
                    "The entity is null! Cannot write the block entity data at {} {} {}.",
                    blockPos.getX(),
                    blockPos.getY(),
                    blockPos.getZ());
                return;
              }
              final PlayerEntity editorAllowed = entity.getEditor();
              entity.setEditor(null);
              final @Unmodifiable List<TextContext> textContexts =
                  nbt != null
                      ? new ImmutableList.Builder<TextContext>()
                      .addAll(
                          nbt.getList("texts", 10).stream()
                              .map(e -> TextContext.fromNbt(e, entity.getDefaultTextContext()))
                              .iterator())
                      .build()
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
                final HashMap<@NotNull Direction, @NotNull List<@NotNull TextContext>> builder =
                    new HashMap<>(hungSignBlockEntity.texts);
                if (editedSide != null) builder.put(editedSide, textContexts);
                hungSignBlockEntity.texts = ImmutableMap.copyOf(builder);
              } else if (entity instanceof final WallSignBlockEntity wallSignBlockEntity) {
                if (nbt == null) return;
                wallSignBlockEntity.textContexts = textContexts;
              }
              entity.markDirty();
            } catch (ClassCastException e) {
              LOGGER.error("Error when trying to parse NBT received: ", e);
            }
            // 编辑成功。
          });
    }
  }
}
