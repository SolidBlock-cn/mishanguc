package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;

@Environment(EnvType.CLIENT)
public class ForcePlacingToolState implements MishangRenderState {
  public VoxelShape cyanShape;
  public BlockPos cyanPos;
  public VoxelShape blueShape;
  public BlockPos bluePos;
  public VoxelShape redShape;
  public BlockPos redPos;
  public VoxelShape yellowShape;
  public BlockPos yellowPos;
  public Box hitEntityBoundingBox;

  @Override
  public void clear() {
    cyanShape = null;
    cyanPos = null;
    blueShape = null;
    bluePos = null;
    redShape = null;
    redPos = null;
    yellowShape = null;
    yellowPos = null;
    hitEntityBoundingBox = null;
  }
}
