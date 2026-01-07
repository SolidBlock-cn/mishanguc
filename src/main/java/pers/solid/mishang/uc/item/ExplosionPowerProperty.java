package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HeldItemContext;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;

public enum ExplosionPowerProperty implements NumericProperty {
  INSTANCE;
  public static final MapCodec<ExplosionPowerProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable HeldItemContext context, int seed) {
    final ExplosionToolComponent component = stack.get(MishangucComponents.EXPLOSION_TOOL_DATA);
    return component == null ? 0 : component.power();
  }

  @Override
  public MapCodec<? extends NumericProperty> getCodec() {
    return CODEC;
  }
}
