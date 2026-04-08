package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.model.VariantMutator;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(BlockModelGenerators.class)
public interface BlockStateModelGeneratorAccessor {
  @Accessor
  @Contract
  static PropertyDispatch<VariantMutator> getROTATION_HORIZONTAL_FACING() {
    throw new AssertionError();
  }

  @Accessor
  @Contract
  static PropertyDispatch<VariantMutator> getROTATION_HORIZONTAL_FACING_ALT() {
    throw new AssertionError();
  }

  @Accessor
  @Contract
  static PropertyDispatch<VariantMutator> getROTATION_TORCH() {
    throw new AssertionError();
  }
}
