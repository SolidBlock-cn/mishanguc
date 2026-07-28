package pers.solid.mishang.uc.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(BlockStateModelGenerator.class)
public interface BlockStateModelGeneratorAccessor {
  @Accessor
  @Contract
  static BlockStateVariantMap<ModelVariantOperator> getNORTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS() {
    throw new AssertionError();
  }

  @Accessor
  @Contract
  static BlockStateVariantMap<ModelVariantOperator> getSOUTH_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS() {
    throw new AssertionError();
  }

  @Accessor
  @Contract
  static BlockStateVariantMap<ModelVariantOperator> getEAST_DEFAULT_HORIZONTAL_ROTATION_OPERATIONS() {
    throw new AssertionError();
  }
}
