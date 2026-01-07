package pers.solid.mishang.uc.render.state;

import net.minecraft.util.shape.VoxelShape;

public class SlabToolState implements MishangRenderState {
  public VoxelShape slabShape;

  @Override
  public void clear() {
    slabShape = null;
  }
}
