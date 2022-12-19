package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.item.NamedBlockItem;

public class ColoredStandingSignBlockEntity extends StandingSignBlockEntity implements ColoredBlockEntity {
  public int color = NamedBlockItem.cachedColor;

  public ColoredStandingSignBlockEntity() {
    super(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY);
  }

  @Override
  public void fromTag(BlockState blockState, NbtCompound nbt) {
    super.fromTag(blockState, nbt);
    color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
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
