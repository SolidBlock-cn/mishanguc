package pers.solid.mishang.uc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@Mixin(CompoundTag.class)
public interface NbtCompoundAccessor {
  @Accessor("tags")
  Map<String, Tag> getEntries();
}
