package pers.solid.mishang.uc.block;

import net.minecraft.block.Block;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class GlowingWallSignBlock extends WallSignBlock {
  public GlowingWallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @Override
  public MutableText getName() {
    return new TranslatableText("block.mishanguc.glowing_wall_sign", baseBlock.getName());
  }
}
