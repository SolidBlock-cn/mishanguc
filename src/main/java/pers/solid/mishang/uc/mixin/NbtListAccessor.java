package pers.solid.mishang.uc.mixin;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(NbtList.class)
public interface NbtListAccessor {
    /**
     * @see NbtList#value
     */
    @Accessor("value")
    List<NbtElement> getValue();
}
