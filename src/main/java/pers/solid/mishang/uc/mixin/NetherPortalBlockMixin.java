package pers.solid.mishang.uc.mixin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
  @Redirect(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"))
  private boolean validColored(BlockState instance, Block block) {
    return instance.is(block) || (block == Blocks.NETHER_PORTAL && instance.is(ColoredBlocks.COLORED_NETHER_PORTAL)) || (block == ColoredBlocks.COLORED_NETHER_PORTAL && instance.is(Blocks.NETHER_PORTAL));
  }
}
