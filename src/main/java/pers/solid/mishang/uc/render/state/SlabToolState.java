package pers.solid.mishang.uc.render.state;

import net.minecraft.world.phys.shapes.VoxelShape;

public class SlabToolState implements MishangRenderState {
  public VoxelShape slabShape;

  @Override
  public void clear() {
    slabShape = null;
  }
}
