package pers.solid.mishang.uc.mixin;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Set;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.entity.ai.village.poi.PoiTypes.class)
public interface PoiTypesAccessor {
  @Invoker
  static PoiType callRegister(Registry<PoiType> key, ResourceKey<PoiType> value, Set<BlockState> matchingStates, int maxTickets, int validRange) {
    throw new UnsupportedOperationException();
  }
}
