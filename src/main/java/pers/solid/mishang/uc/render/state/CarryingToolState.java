package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

@Environment(EnvType.CLIENT)
public class CarryingToolState implements MishangRenderState {
  public VoxelShape cyanShape, blueShape, redShape, orangeShape;
  public BlockPos cyanPos, bluePos, redPos, orangePos;
  public Vec3 cyanEntityPos;
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
