package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
  @ModifyExpressionValue(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
  private boolean validColored(boolean original, @Local(argsOnly = true, name = "neighborState", ordinal = 1) BlockState neighborState) {
    Block block = (Block) (Object) this;
    return original || (block == Blocks.NETHER_PORTAL && neighborState.isOf(ColoredBlocks.COLORED_NETHER_PORTAL)) || (block == ColoredBlocks.COLORED_NETHER_PORTAL && neighborState.isOf(Blocks.NETHER_PORTAL));
  }
}
