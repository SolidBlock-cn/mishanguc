package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
  @ModifyExpressionValue(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Ljava/lang/Object;)Z"))
  private boolean validColored(boolean original, @Local(argsOnly = true, ordinal = 1) BlockState neighbourState) {
    Block block = (Block) (Object) this;
    return original || (block == Blocks.NETHER_PORTAL && neighbourState.is(ColoredBlocks.COLORED_NETHER_PORTAL)) || (block == ColoredBlocks.COLORED_NETHER_PORTAL && neighbourState.is(Blocks.NETHER_PORTAL));
  }
}
