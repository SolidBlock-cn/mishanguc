package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.item.NamedBlockItem;

public class ColoredWallSignBlockEntity extends WallSignBlockEntity implements ColoredBlockEntity {
  public int color = NamedBlockItem.cachedColor;

  public ColoredWallSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_WALL_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
    if (world != null && world.isClient) {
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  public void writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    nbt.putInt("color", color);
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
