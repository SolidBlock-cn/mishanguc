package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentsAccess;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.components.MishangucComponents;

public class ColoredStandingSignBlockEntity extends StandingSignBlockEntity implements ColoredBlockEntity {
  public int color = 0;

  public ColoredStandingSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  protected void readData(ReadView view) {
    super.readData(view);
    color = view.read("color", MishangUtils.COLOR_CODEC).orElse(0);
    if (world != null && world.isClient) {
      world.updateListeners(pos, this.getCachedState(), this.getCachedState(), 3);
    }
  }

  @Override
  protected void writeData(WriteView view) {
    super.writeData(view);
    view.put("color", MishangUtils.COLOR_CODEC, color);
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
  public void removeFromCopiedStackData(WriteView view) {
    super.removeFromCopiedStackData(view);
    view.remove("color");
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
