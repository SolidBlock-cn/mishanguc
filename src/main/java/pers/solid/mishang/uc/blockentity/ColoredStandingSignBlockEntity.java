package pers.solid.mishang.uc.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.components.MishangucComponents;

public class ColoredStandingSignBlockEntity extends StandingSignBlockEntity implements ColoredBlockEntity {
  public int color = 0;

  public ColoredStandingSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.COLORED_STANDING_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  protected void loadAdditional(ValueInput view) {
    super.loadAdditional(view);
    color = view.read("color", MishangUtils.COLOR_CODEC).orElse(0);
    if (level != null && level.isClientSide()) {
      level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), 3);
    }
  }

  @Override
  protected void saveAdditional(ValueOutput view) {
    super.saveAdditional(view);
    view.store("color", MishangUtils.COLOR_CODEC, color);
  }

  @Override
  protected void applyImplicitComponents(DataComponentGetter components) {
    super.applyImplicitComponents(components);
    color = components.getOrDefault(MishangucComponents.COLOR, color);
  }

  @Override
  protected void collectImplicitComponents(DataComponentMap.Builder componentMapBuilder) {
    super.collectImplicitComponents(componentMapBuilder);
    componentMapBuilder.set(MishangucComponents.COLOR, color);
  }

  @Override
  public void removeComponentsFromTag(ValueOutput view) {
    super.removeComponentsFromTag(view);
    view.discard("color");
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
