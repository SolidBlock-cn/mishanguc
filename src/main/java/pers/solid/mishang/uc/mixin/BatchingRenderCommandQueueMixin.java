package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.command.BatchingRenderCommandQueue;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import org.joml.Matrix4f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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
@Mixin(BatchingRenderCommandQueue.class)
public abstract class BatchingRenderCommandQueueMixin implements RenderCommandQueueExtension, BatchingRenderCommandQueueExtension {
  @Shadow
  private boolean hasCommands;

  @Shadow
  public abstract void submitText(MatrixStack matrices, float x, float y, OrderedText text, boolean dropShadow, TextRenderer.TextLayerType layerType, int light, int color, int backgroundColor, int outlineColor);

  /**
   * 用于渲染 {@link SpecialDrawable} 的命令列表，会在 {@link net.minecraft.client.render.command.TextCommandRenderer TextCommandRenderer} 中执行。
   *
   * @see TextCommandRendererMixin
   */
  @Unique
  private final List<SpecialDrawableCommand> specialDrawableCommands = new ArrayList<>();

  @Override
  public void submitSpecialDrawable$mishang(MatrixStack matrixStack, SpecialDrawable specialDrawable, int light, float x, float y) {
    this.hasCommands = true;
    submitText(matrixStack, x, y, OrderedText.EMPTY, false, TextRenderer.TextLayerType.NORMAL, light, 0, 0, 0);
    specialDrawableCommands.add(new SpecialDrawableCommand(new Matrix4f(matrixStack.peek().getPositionMatrix()), specialDrawable, light, x, y));
  }

  @Inject(method = "clear", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/command/BatchingRenderCommandQueue;leashCommands:Ljava/util/List;", opcode = Opcodes.GETFIELD))
  private void clearSpecialDrawable(CallbackInfo ci) {
    specialDrawableCommands.clear();
  }

  @Override
  public List<SpecialDrawableCommand> getSpecialDrawableCommands$mishang() {
    return specialDrawableCommands;
  }
}
