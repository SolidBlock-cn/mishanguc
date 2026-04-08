package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;

public enum ExplosionPowerProperty implements RangeSelectItemModelProperty {
  INSTANCE;
  public static final MapCodec<ExplosionPowerProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
    final ExplosionToolComponent component = stack.get(MishangucComponents.EXPLOSION_TOOL_DATA);
    return component == null ? 0 : component.power();
  }

  @Override
  public MapCodec<? extends RangeSelectItemModelProperty> type() {
    return CODEC;
  }
}
