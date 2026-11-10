package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import pers.solid.mishang.uc.render.MovingBlockRenderStateExtension;

@Mixin(MovingBlockRenderState.class)
@Environment(EnvType.CLIENT)
public abstract class MovingBlockRenderStateMixin implements MovingBlockRenderStateExtension {
  @Unique
  private boolean hasCustomColor;
  @Unique
  private int customColor;

  @Override
  public boolean hasCustomColor$mishang() {
    return hasCustomColor;
  }

  @Override
  public int customColor$mishang() {
    return customColor;
  }

  @Override
  public void setCustomColor$mishang(int color) {
    hasCustomColor = true;
    customColor = color;
  }
}
