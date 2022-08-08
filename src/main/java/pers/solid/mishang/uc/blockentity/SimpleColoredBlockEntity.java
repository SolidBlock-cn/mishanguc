package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.item.NamedBlockItem;

public class SimpleColoredBlockEntity extends BlockEntity implements ColoredBlockEntity {
  public int color;

  public SimpleColoredBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.SIMPLE_COLORED_BLOCK_ENTITY, pos, state);
    color = NamedBlockItem.cachedColor;
  }

  @Override
  public int getColor() {
    return color;
  }

  @Override
  public void setColor(int color) {
    this.color = color;
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    color = nbt.getInt("color");
    if (world != null && world.isClient) {
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  protected void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putInt("color", color);
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
}
