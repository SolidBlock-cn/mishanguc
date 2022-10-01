package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
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
}
