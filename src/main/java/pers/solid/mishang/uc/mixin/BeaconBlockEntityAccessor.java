package pers.solid.mishang.uc.mixin;

import net.minecraft.block.entity.BeaconBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(BeaconBlockEntity.class)
public interface BeaconBlockEntityAccessor {
  @Accessor("field_19178")
  List<BeaconBlockEntity.BeamSegment> getCheckingBeamSegments();

  @Mixin(BeaconBlockEntity.BeamSegment.class)
  interface BeamSegmentAccessor {
    @Invoker
    void invokeIncreaseHeight();
  }
}
