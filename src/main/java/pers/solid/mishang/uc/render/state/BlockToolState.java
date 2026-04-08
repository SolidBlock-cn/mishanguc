package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;

@Environment(EnvType.CLIENT)
public class BlockToolState implements MishangRenderState {
  // 没有 greenShape 和 greenPos，因为已经存储于 outlineRenderShape 中。
  public VoxelShape lightGreenShape;
  public BlockPos lightGreenPos;

  @Override
  public void clear() {
    lightGreenShape = null;
    lightGreenPos = null;
  }
}
