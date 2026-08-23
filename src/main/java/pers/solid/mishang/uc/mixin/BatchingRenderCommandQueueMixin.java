package pers.solid.mishang.uc.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import pers.solid.mishang.uc.render.BatchingRenderCommandQueueExtension;
import pers.solid.mishang.uc.render.RenderCommandQueueExtension;
import pers.solid.mishang.uc.render.SpecialDrawableCommand;
import pers.solid.mishang.uc.text.SpecialDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
@Mixin(SubmitNodeCollection.class)
public abstract class BatchingRenderCommandQueueMixin implements RenderCommandQueueExtension, BatchingRenderCommandQueueExtension {
  @Shadow
  public abstract void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor);

  /**
   * 用于渲染 {@link SpecialDrawable} 的命令列表，会在 {@link net.minecraft.client.renderer.feature.TextFeatureRenderer TextCommandRenderer} 中执行。
   *
   * @see TextFeatureRendererMixin
   */
  @Unique
  private final List<SpecialDrawableCommand> specialDrawableCommands = new ArrayList<>();

  @Override
  public void submitSpecialDrawable$mishang(PoseStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    submitText(matrixStack, x, y, FormattedCharSequence.EMPTY, false, Font.DisplayMode.NORMAL, light, 0, 0, 0);
    specialDrawableCommands.add(new SpecialDrawableCommand(new Matrix4f(matrixStack.last().pose()), specialDrawable, light, x, y));
  }

  @Override
  public List<SpecialDrawableCommand> getSpecialDrawableCommands$mishang() {
    return specialDrawableCommands;
  }
}
