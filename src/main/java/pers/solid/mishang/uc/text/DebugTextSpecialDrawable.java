package pers.solid.mishang.uc.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public record DebugTextSpecialDrawable(@NotNull String text, @NotNull TextContext textContext) implements SpecialDrawable {
  @Override
  public void drawInternal(Matrix4f matricesEntry, VertexConsumerProvider.Immediate vertexConsumers, int light, float x, float y) {
    final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
    textRenderer.draw(text, x, y, textContext.color, textContext.shadow, matricesEntry, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, light);
  }

  @Override
  public @NotNull SpecialDrawableType<DebugTextSpecialDrawable> getType() {
    return SpecialDrawableTypes.DEBUG_TEXT;
  }

  @Override
  public SpecialDrawable cloneWithNewTextContext(@NotNull TextContext textContext) {
    return new DebugTextSpecialDrawable(text, textContext);
  }

  @Override
  public void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
    SpecialDrawable.super.writeNbt(nbt, registryLookup);
    nbt.putString("text", text);
  }

  @Override
  public String asStringArgs() {
    return text;
  }

  @Override
  public @NotNull MutableText asStyledText() {
    return Text.literal("debug_text: ").append(text);
  }
}
