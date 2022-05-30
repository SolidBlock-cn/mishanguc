package pers.solid.mishang.uc.mixin;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SnowBlock.class)
public abstract class SnowBlockMixin {
  private static final Tag<Block> ROADS = TagRegistry.block(new Identifier("mishanguc", "roads"));

  /**
   * 雪不能放置在道路方块上面。
   */
  @Inject(method = "canPlaceAt", at = @At("HEAD"), cancellable = true)
  public void canPlaceAtMixin(BlockState state, WorldView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
    if (world.getBlockState(pos.down()).isIn(ROADS)) {
      cir.setReturnValue(false);
      cir.cancel();
    }
  }
}
