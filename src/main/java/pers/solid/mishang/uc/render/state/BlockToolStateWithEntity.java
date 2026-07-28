package pers.solid.mishang.uc.render.state;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.shape.VoxelShape;

@Environment(EnvType.CLIENT)
public class BlockToolStateWithEntity extends BlockToolState {
  public VoxelShape greenEntityShape;

  @Override
  public void clear() {
    super.clear();
    greenEntityShape = null;
  }
}
