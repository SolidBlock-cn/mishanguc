package pers.solid.mishang.uc.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
  @Shadow
  public abstract Block getBlock();

  @Inject(method = "getMapColor", at = @At("HEAD"), cancellable = true)
  private void getColoredMapColor(BlockGetter level, BlockPos pos, CallbackInfoReturnable<MapColor> cir) {
    if (getBlock() instanceof ColoredBlock && level.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity) {
      cir.setReturnValue(coloredBlockEntity.getNearestMapColor());
    }
  }
}
