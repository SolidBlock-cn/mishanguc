package pers.solid.mishang.uc.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Function;

@Mixin(AbstractBlock.Settings.class)
public interface AbstractBlockSettingsAccessor {
  @Accessor
  Function<BlockState, MapColor> getMapColorProvider();
}
