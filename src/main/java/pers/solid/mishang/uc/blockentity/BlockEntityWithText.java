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
import pers.solid.mishang.uc.text.TextContext;

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
   * ??????????????????????????????????????????1??????1/16????????????????????????????????? {@link
   * TextContext#draw(TextRenderer, MatrixStack, VertexConsumerProvider,
   * int, float, float)} ?????? height ?????????
   *
   * @return ??????????????????????????????????????????
   */
  public abstract @Range(from = 0, to = 16) float getHeight();

  /**
   * ??????????????????????????? {@link TextContext} ?????????????????? {@code new TextContext()}???
   *
   * @return ???????????????????????? <tt>TextContext</tt>???
   */
  @Contract("->new")
  public abstract TextContext getDefaultTextContext();

  /**
   * @return ?????????????????????????????????????????????????????????????????????????????? {@code null}???
   */
  public abstract @Nullable PlayerEntity getEditor();

  /**
   * ??????????????????????????????????????????
   *
   * @param editor ????????????????????????????????????????????????????????????????????????????????????????????? {@code null}???
   */
  public abstract void setEditor(@Nullable PlayerEntity editor);

  /**
   * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
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
            // ?????????????????????????????? HungSignBlockEntity ????????????????????????????????????buf ????????????????????????
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
              }
              responseSender.sendPacket(BlockEntityUpdateS2CPacket.create(entity));
              entity.markDirty();
            } catch (ClassCastException e) {
              LOGGER.error("Error when trying to parse NBT received: ", e);
            }
            // ???????????????
          });
    }
  }
}
