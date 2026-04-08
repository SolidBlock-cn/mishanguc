package pers.solid.mishang.uc.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.ColorMixtureType;

@Environment(EnvType.CLIENT)
public enum ColorMixtureTypeProperty implements SelectItemModelProperty<ColorMixtureType> {
  INSTANCE;
  public static final Type<ColorMixtureTypeProperty, ColorMixtureType> TYPE = Type.create(MapCodec.unit(INSTANCE), ColorMixtureType.CODEC);

  @Override
  public @Nullable ColorMixtureType get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
    return stack.get(MishangucComponents.COLOR_MIXTURE_TYPE);
  }

  @Override
  public Codec<ColorMixtureType> valueCodec() {
    return ColorMixtureType.CODEC;
  }

  @Override
  public Type<? extends SelectItemModelProperty<ColorMixtureType>, ColorMixtureType> type() {
    return TYPE;
  }
}
