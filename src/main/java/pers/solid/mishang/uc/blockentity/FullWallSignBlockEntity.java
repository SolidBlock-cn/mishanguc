package pers.solid.mishang.uc.blockentity;

import pers.solid.mishang.uc.text.TextContext;

public class FullWallSignBlockEntity extends WallSignBlockEntity {
  public FullWallSignBlockEntity() {
    super(MishangucBlockEntities.FULL_WALL_SIGN_BLOCK_ENTITY);
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
