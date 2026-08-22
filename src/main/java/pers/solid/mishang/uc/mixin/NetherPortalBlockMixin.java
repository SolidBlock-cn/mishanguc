package pers.solid.mishang.uc.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
  @Redirect(method = "getStateForNeighborUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
  private boolean validColored(BlockState instance, Block block) {
    return instance.isOf(block) || (block == Blocks.NETHER_PORTAL && instance.isOf(ColoredBlocks.COLORED_NETHER_PORTAL)) || (block == ColoredBlocks.COLORED_NETHER_PORTAL && instance.isOf(Blocks.NETHER_PORTAL));
  }
}
