package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HeldItemContext;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum TransparencyPropertyProperty implements NumericProperty {
  INSTANCE;
  public static final MapCodec<TransparencyPropertyProperty> CODEC = MapCodec.unit(TransparencyPropertyProperty.INSTANCE);

  @Override
  public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable HeldItemContext context, int seed) {
    return 1 - stack.getOrDefault(MishangucComponents.OPACITY, 1f);
  }

  @Override
  public MapCodec<? extends NumericProperty> getCodec() {
    return CODEC;
  }
}
