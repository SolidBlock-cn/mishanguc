package pers.solid.mishang.uc.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TransparentBlock;
import net.minecraft.util.math.Direction;

public class GlowingLampBlock extends TransparentBlock {
  public GlowingLampBlock(Settings settings) {
    super(settings);
  }

  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return stateFrom.getBlock() instanceof GlowingLampBlock
        || super.isSideInvisible(state, stateFrom, direction);
  }
}
