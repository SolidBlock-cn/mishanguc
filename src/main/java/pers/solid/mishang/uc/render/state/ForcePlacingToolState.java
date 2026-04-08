package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

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
  public AABB hitEntityBoundingBox;

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
