package pers.solid.mishang.uc.screen;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.BlockPos;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;

@Environment(EnvType.CLIENT)
public class WallSignBlockEditScreen extends AbstractSignBlockEditScreen {
  public WallSignBlockEditScreen(WallSignBlockEntity entity, BlockPos blockPos) {
    super(entity, blockPos, Lists.newArrayList(entity.textContexts));
  }
}
