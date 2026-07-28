package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(TextRenderer.class)
public interface TextRendererAccessor {
  @Accessor
  TextRenderer.GlyphsProvider getFonts();
}
