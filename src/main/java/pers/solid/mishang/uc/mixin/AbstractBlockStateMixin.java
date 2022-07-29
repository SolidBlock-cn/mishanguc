package pers.solid.mishang.uc.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
  @Shadow
  public abstract Block getBlock();

  @Inject(method = "getMapColor", at = @At("HEAD"), cancellable = true)
  private void getColoredMapColor(BlockView world, BlockPos pos, CallbackInfoReturnable<MapColor> cir) {
    if (getBlock() instanceof ColoredBlock && world.getBlockEntity(pos) instanceof ColoredBlockEntity coloredBlockEntity) {
      cir.setReturnValue(coloredBlockEntity.getNearestMapColor());
    }
  }
}
