package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.RenderCommandQueue;
import org.spongepowered.asm.mixin.Mixin;
import pers.solid.mishang.uc.render.RenderCommandQueueExtension;

@Mixin(RenderCommandQueue.class)
@Environment(EnvType.CLIENT)
public interface RenderCommandQueueMixin extends RenderCommandQueueExtension {
}
