package pers.solid.mishang.uc.mixin;

import net.minecraft.server.world.ChunkHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkHolder.class)
public interface ChunkHolderAccessor {
  @Accessor
  void setField_26744(boolean noLightingUpdates);
}
