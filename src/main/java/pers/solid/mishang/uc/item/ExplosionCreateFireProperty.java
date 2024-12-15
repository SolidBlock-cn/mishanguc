package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.render.item.property.bool.BooleanProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;

public enum ExplosionCreateFireProperty implements BooleanProperty {
  INSTANCE;
  public static final MapCodec<ExplosionCreateFireProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public boolean getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ModelTransformationMode modelTransformationMode) {
    final ExplosionToolComponent component = stack.get(MishangucComponents.EXPLOSION_TOOL_DATA);
    return component != null && component.createFire();
  }

  @Override
  public MapCodec<? extends BooleanProperty> getCodec() {
    return CODEC;
  }
}
