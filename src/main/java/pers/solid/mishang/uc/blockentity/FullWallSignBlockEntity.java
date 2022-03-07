package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.util.TextContext;

public class FullWallSignBlockEntity extends WallSignBlockEntity {
  public FullWallSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  public float getHeight() {
    return 8;
  }

  @Override
  public TextContext getDefaultTextContext() {
    final TextContext textContext = new TextContext();
    textContext.size = 8;
    return textContext;
  }
}
