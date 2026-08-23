package pers.solid.mishang.uc.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollection;

import java.util.List;

/**
 * @see SubmitNodeCollection
 * @since Minecraft 1.21.10
 */
@Environment(EnvType.CLIENT)
public interface BatchingRenderCommandQueueExtension {
  default List<SpecialDrawableCommand> getSpecialDrawableCommands$mishang() {
    throw new AssertionError();
  }
}
