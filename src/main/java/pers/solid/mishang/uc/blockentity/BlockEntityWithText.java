package pers.solid.mishang.uc.blockentity;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public abstract class BlockEntityWithText extends BlockEntity
    implements BlockEntityClientSerializable {

  public BlockEntityWithText(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  /** @see net.minecraft.block.entity.SignBlockEntity#getCommandSource(ServerPlayerEntity) */
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

  @Override
  public void fromClientTag(NbtCompound tag) {
    readNbt(tag);
  }

  @Override
  public NbtCompound toClientTag(NbtCompound tag) {
    return writeNbt(tag);
  }
}
