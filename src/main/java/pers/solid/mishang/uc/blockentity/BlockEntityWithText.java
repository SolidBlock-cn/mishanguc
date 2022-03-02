package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import pers.solid.mishang.uc.util.TextContext;

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

  /** 检查编辑告示牌的玩家是否有效。如果玩家正好是旁观模式，或者已经死亡，或者不在该世界内，则取消该玩家编辑权限。 */
  public void checkEditorValidity() {
    final PlayerEntity editor = getEditor();
    if (editor != null && editor.isSpectator() && !editor.isLiving() && editor.world != world) {
      setEditor(null);
    }
  }
}
