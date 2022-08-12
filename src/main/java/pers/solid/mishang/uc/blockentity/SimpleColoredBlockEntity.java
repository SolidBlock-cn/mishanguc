package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import pers.solid.mishang.uc.item.NamedBlockItem;

public class SimpleColoredBlockEntity extends BlockEntity implements ColoredBlockEntity {
  public int color;

  public SimpleColoredBlockEntity() {
    super(MishangucBlockEntities.SIMPLE_COLORED_BLOCK_ENTITY);
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
  public void fromTag(BlockState state, NbtCompound nbt) {
    super.fromTag(state, nbt);
    color = nbt.getInt("color");
    if (world != null && world.isClient) {
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putInt("color", color);
    return nbt;
  }


  @Override
  public BlockEntityUpdateS2CPacket toUpdatePacket() {
    return new BlockEntityUpdateS2CPacket(this.pos, 11, this.toInitialChunkDataNbt());
  }

  @Override
  public NbtCompound toInitialChunkDataNbt() {
    return this.writeNbt(new NbtCompound());
  }
}
