package pers.solid.mishang.uc.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum CarryingToolTypeProperty implements SelectProperty<Short> {
  INSTANCE;
  public static final SelectProperty.Type<CarryingToolTypeProperty, Short> TYPE = Type.create(MapCodec.unit(INSTANCE), Codec.SHORT);

  @Nullable
  @Override
  public Short getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ModelTransformationMode modelTransformationMode) {
    final CarryingToolData data = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (data == null) {
      return null;
    } else {
      return data.type();
    }
  }

  @Override
  public Type<? extends SelectProperty<Short>, Short> getType() {
    return TYPE;
  }
}
