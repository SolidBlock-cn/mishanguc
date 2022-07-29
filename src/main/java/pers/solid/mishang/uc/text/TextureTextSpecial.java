package pers.solid.mishang.uc.text;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record TextureTextSpecial(Identifier identifier, @NotNull TextContext textContext) implements TextSpecial {

  @Override
  public void drawExtra(TextRenderer textRenderer, MatrixStack matrixStack, VertexConsumerProvider vertexConsumers, int light, float x, float y) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, identifier);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();
    DrawableHelper.drawTexture(matrixStack, (int) x, (int) y, 0, 0, 16, 16, 16, 16);
  }

  @Override
  public String getId() {
    return "texture_beta";
  }

  @Override
  public float getWidth() {
    return 16;
  }

  @Override
  public float getHeight() {
    return 16;
  }

  @Override
  public TextSpecial cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new TextureTextSpecial(identifier, textContext);
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    final NbtCompound nbtCompound = TextSpecial.super.writeNbt(nbt);
    nbtCompound.putString("texture", identifier.toString());
    return nbtCompound;
  }

  @Override
  public String describeArgs() {
    return identifier.toString();
  }
}
