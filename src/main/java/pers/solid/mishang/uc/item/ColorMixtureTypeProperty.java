package pers.solid.mishang.uc.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.select.SelectProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.ColorMixtureType;

@Environment(EnvType.CLIENT)
public enum ColorMixtureTypeProperty implements SelectProperty<ColorMixtureType> {
  INSTANCE;
  public static final Type<ColorMixtureTypeProperty, ColorMixtureType> TYPE = Type.create(MapCodec.unit(INSTANCE), ColorMixtureType.CODEC);

  @Override
  public @Nullable ColorMixtureType getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
    return stack.get(MishangucComponents.COLOR_MIXTURE_TYPE);
  }

  @Override
  public Codec<ColorMixtureType> valueCodec() {
    return ColorMixtureType.CODEC;
  }

  @Override
  public Type<? extends SelectProperty<ColorMixtureType>, ColorMixtureType> getType() {
    return TYPE;
  }
}
