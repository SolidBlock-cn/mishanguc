package pers.solid.mishang.uc.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucEntityData;
import pers.solid.mishang.uc.block.ColoredBlock;

import java.util.OptionalInt;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityMixin extends Entity {
  private FallingBlockEntityMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  @Shadow
  public abstract BlockState getBlockState();

  @Shadow
  private BlockState blockState;

  @Shadow
  @Nullable
  public CompoundTag blockData;

  @Inject(method = "defineSynchedData", at = @At("TAIL"))
  private void addMoreSynchedData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
    entityData.define(MishangucEntityData.FALLING_BLOCK_COLOR, OptionalInt.empty());
  }

  @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
  private void syncBlockDataToSyncedData(ValueInput input, CallbackInfo ci) {
    updateSyncedBlockColor();
  }

  @Inject(method = "tick", at = @At("TAIL"))
  private void syncBlockColorInTick(CallbackInfo ci) {
    updateSyncedBlockColor();
  }

  @Unique
  private void updateSyncedBlockColor() {
    if (blockState.getBlock() instanceof ColoredBlock && blockData != null) {
      final Tag colorData = blockData.get("color");
      entityData.set(MishangucEntityData.FALLING_BLOCK_COLOR, colorData == null ? OptionalInt.empty() : OptionalInt.of(MishangUtils.readColorFromNbtElement(colorData)));
    }
  }
}
