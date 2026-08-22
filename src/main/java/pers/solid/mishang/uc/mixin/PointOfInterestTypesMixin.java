package pers.solid.mishang.uc.mixin;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import pers.solid.mishang.uc.blocks.ColoredBlocks;

import java.util.Set;

@Mixin(PointOfInterestTypes.class)
public abstract class PointOfInterestTypesMixin {
  @Redirect(method = "registerAndGetDefault", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/poi/PointOfInterestTypes;getStatesOfBlock(Lnet/minecraft/block/Block;)Ljava/util/Set;", ordinal = 0), slice = @Slice(from = @At(value = "FIELD", target = "Lnet/minecraft/block/Blocks;NETHER_PORTAL:Lnet/minecraft/block/Block;")))
  private static Set<BlockState> injected2(Block block) {
    return ImmutableSet.copyOf(Iterables.concat(block.getStateManager().getStates(), ColoredBlocks.COLORED_NETHER_PORTAL.getStateManager().getStates()));
  }
}
