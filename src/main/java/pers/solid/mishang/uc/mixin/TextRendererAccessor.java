package pers.solid.mishang.uc.mixin;

import net.minecraft.client.font.FontStorage;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TextRenderer.class)
public interface TextRendererAccessor {
  @Invoker
  FontStorage invokeGetFontStorage(Identifier id);
}
