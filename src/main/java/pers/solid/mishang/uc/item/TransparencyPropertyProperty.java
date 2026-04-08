package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum TransparencyPropertyProperty implements RangeSelectItemModelProperty {
  INSTANCE;
  public static final MapCodec<TransparencyPropertyProperty> CODEC = MapCodec.unit(TransparencyPropertyProperty.INSTANCE);

  @Override
  public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
    return 1 - stack.getOrDefault(MishangucComponents.OPACITY, 1f);
  }

  @Override
  public MapCodec<? extends RangeSelectItemModelProperty> type() {
    return CODEC;
  }
}
