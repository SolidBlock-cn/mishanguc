package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.item.NamedBlockItem;

/**
 * 染色的悬挂告示牌方块。
 */
public class ColoredHungSignBlockEntity extends HungSignBlockEntity implements ColoredBlockEntity {
  public int color = NamedBlockItem.cachedColor;

  public ColoredHungSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_HUNG_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    super.readNbt(nbt, registryLookup);
    color = MishangUtils.readColorFromNbtElement(nbt.get("color"));
    if (world != null && world.isClient) {
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    super.writeNbt(nbt, registryLookup);
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
