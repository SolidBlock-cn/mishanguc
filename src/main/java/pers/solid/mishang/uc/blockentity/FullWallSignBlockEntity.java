package pers.solid.mishang.uc.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import pers.solid.mishang.uc.text.TextContext;

public class FullWallSignBlockEntity extends WallSignBlockEntity {
  public FullWallSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY, pos, state);
  }

  @Override
  public float getHeight() {
    return 16;
  }

  @Override
  public TextContext createDefaultTextContext() {
    final TextContext textContext = new TextContext();
    textContext.size = 8;
    return textContext;
  }
}
