package pers.solid.mishang.uc.text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix4f;

public record DebugTextSpecialDrawable(String text, TextContext textContext) implements SpecialDrawable {
  @Override
  public void drawInternal(Matrix4f matricesEntry, MultiBufferSource.BufferSource vertexConsumers, int light, float x, float y) {
    final Font textRenderer = Minecraft.getInstance().font;
    textRenderer.drawInBatch(text, x, y, textContext.color, textContext.shadow, matricesEntry, vertexConsumers, Font.DisplayMode.NORMAL, 0, light);
  }

  @Override
  public SpecialDrawableType<DebugTextSpecialDrawable> getType() {
    return SpecialDrawableTypes.DEBUG_TEXT;
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(TextContext textContext) {
    return new DebugTextSpecialDrawable(text, textContext);
  }

  @Override
  public void writeNbt(CompoundTag nbt, HolderLookup.Provider registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("text", text);
  }

  @Override
  public String asStringArgs() {
    return text;
  }

  @Override
  public MutableComponent asStyledText() {
    return Component.literal("debug_text: ").append(text);
  }
}
