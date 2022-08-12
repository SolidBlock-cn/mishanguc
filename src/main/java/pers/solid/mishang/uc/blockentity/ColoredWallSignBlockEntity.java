package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import pers.solid.mishang.uc.item.NamedBlockItem;

public class ColoredWallSignBlockEntity extends WallSignBlockEntity implements ColoredBlockEntity {
  public int color = NamedBlockItem.cachedColor;

  public ColoredWallSignBlockEntity() {
    super(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY);
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
  public int getColor() {
    return color;
  }

  @Override
  public void setColor(int color) {
    this.color = color;
  }
}
