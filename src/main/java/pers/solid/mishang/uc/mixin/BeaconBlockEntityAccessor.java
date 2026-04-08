package pers.solid.mishang.uc.mixin;

import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
  @Accessor("checkingBeamSections")
  List<BeaconBeamOwner.Section> getCheckingBeamSegments();

  @Mixin(BeaconBeamOwner.Section.class)
  interface BeamSegmentAccessor {
    @Invoker
    void invokeIncreaseHeight();
  }
}
