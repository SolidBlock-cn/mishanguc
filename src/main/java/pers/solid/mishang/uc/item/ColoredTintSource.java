package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.tint.TintSource;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;

import java.awt.*;

@Environment(EnvType.CLIENT)
public enum ColoredTintSource implements TintSource {
  INSTANCE;
  public static final MapCodec<ColoredTintSource> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public int getTint(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity user) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color != null) {
      return 0xff000000 | color;
    }
    return Color.HSBtoRGB(Util.getMeasuringTimeMs() / 4096f + (stack.getItem().hashCode() >> 16) / 64f, 0.5f, 0.95f);
  }

  @Override
  public MapCodec<? extends TintSource> getCodec() {
    return CODEC;
  }
}
