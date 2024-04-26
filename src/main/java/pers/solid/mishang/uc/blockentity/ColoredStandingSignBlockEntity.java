package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.components.MishangucComponents;

public class ColoredStandingSignBlockEntity extends StandingSignBlockEntity implements ColoredBlockEntity {
  public int color = 0;

  public ColoredStandingSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY, pos, state);
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
  protected void readComponents(ComponentsAccess components) {
    super.readComponents(components);
    color = components.getOrDefault(MishangucComponents.COLOR, color);
  }

  @Override
  protected void addComponents(ComponentMap.Builder componentMapBuilder) {
    super.addComponents(componentMapBuilder);
    componentMapBuilder.add(MishangucComponents.COLOR, color);
  }

  @Override
  public void removeFromCopiedStackNbt(NbtCompound nbt) {
    super.removeFromCopiedStackNbt(nbt);
    nbt.remove("color");
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
