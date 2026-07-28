package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;

@Environment(EnvType.CLIENT)
public class CarryingToolState implements MishangRenderState {
  public VoxelShape cyanShape, blueShape, redShape, orangeShape;
  public BlockPos cyanPos, bluePos, redPos, orangePos;
  public Vec3d cyanEntityPos;
  public float cyanEntityWidth, cyanEntityHeight;
  public VoxelShape redEntityShape;

  @Override
  public void clear() {
    cyanPos = null;
    bluePos = null;
    redPos = null;
    orangePos = null;
    cyanShape = null;
    blueShape = null;
    redShape = null;
    orangeShape = null;
    cyanEntityPos = null;
    cyanEntityWidth = 0;
    cyanEntityHeight = 0;
    redEntityShape = null;
  }
}
