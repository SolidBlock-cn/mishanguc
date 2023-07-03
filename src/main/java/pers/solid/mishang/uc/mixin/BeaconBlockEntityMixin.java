package pers.solid.mishang.uc.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

import java.util.Arrays;
import java.util.List;

@Mixin(BeaconBlockEntity.class)
public abstract class BeaconBlockEntityMixin {
  @Unique
  private static final TagKey<Block> TINTS_BEACON_BEAMS = TagKey.of(RegistryKeys.BLOCK, new Identifier("mishanguc", "tints_beacon_beams"));

  @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
  private static void acceptColoredBlocksInTick(World world, BlockPos pos, BlockState state, BeaconBlockEntity blockEntity, CallbackInfo ci, int i, int j, int k, BlockPos blockPos, BeaconBlockEntity.BeamSegment beamSegment, int l, int m, BlockState blockState, @Share("is_colored") LocalBooleanRef localBooleanRef, @Local LocalRef<BeaconBlockEntity.BeamSegment> beamSegmentLocalRef) {
    final List<BeaconBlockEntity.BeamSegment> checkingBeamSegments = ((BeaconBlockEntityAccessor) blockEntity).getCheckingBeamSegments();
    if (world.getBlockEntity(blockPos) instanceof ColoredBlockEntity coloredBlockEntity && blockState.isIn(TINTS_BEACON_BEAMS)) {
      localBooleanRef.set(true);
      int color = coloredBlockEntity.getColor();
      float[] fs = {(color >> 16 & 255) / 255f, (color >> 8 & 255) / 255f, (color & 255) / 255f};
      if (checkingBeamSegments.size() <= 1) {
        beamSegment = new BeaconBlockEntity.BeamSegment(fs);
        beamSegmentLocalRef.set(beamSegment);
        checkingBeamSegments.add(beamSegment);
      } else if (beamSegment != null) {
        final float[] beamSegmentColor = beamSegment.getColor();
        if (Arrays.equals(fs, beamSegmentColor)) {
          ((BeaconBlockEntityAccessor.BeamSegmentAccessor) beamSegment).invokeIncreaseHeight();
        } else {
          beamSegment = new BeaconBlockEntity.BeamSegment(new float[]{(beamSegmentColor[0] + fs[0]) / 2.0F, (beamSegmentColor[1] + fs[1]) / 2.0F, (beamSegmentColor[2] + fs[2]) / 2.0F});
          beamSegmentLocalRef.set(beamSegment);
          checkingBeamSegments.add(beamSegment);
        }
      }
    } else {
      localBooleanRef.set(false);
    }
  }

  @WrapWithCondition(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BeaconBlockEntity$BeamSegment;increaseHeight()V"))
  private static boolean wrappedIncreaseHeight(BeaconBlockEntity.BeamSegment instance, @Share("is_colored") LocalBooleanRef localBooleanRef) {
    return !localBooleanRef.get();
  }
}
