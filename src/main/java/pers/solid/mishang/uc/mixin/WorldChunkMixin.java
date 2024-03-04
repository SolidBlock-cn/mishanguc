package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import pers.solid.mishang.uc.item.ForcePlacingToolItem;

@Mixin(WorldChunk.class)
public class WorldChunkMixin {
  @WrapWithCondition(method = "setBlockState", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockAdded(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V"))
  public boolean wrappedCallOnBlockAdded(BlockState instance, World world, BlockPos pos, BlockState state, boolean notify) {
    return !ForcePlacingToolItem.suppressOnBlockAdded;
  }
}
