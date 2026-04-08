package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;

import java.awt.*;

@Environment(EnvType.CLIENT)
public enum ColoredTintSource implements ItemTintSource {
  INSTANCE;
  public static final MapCodec<ColoredTintSource> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public int calculate(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user) {
    final Integer color = stack.get(MishangucComponents.COLOR);
    if (color != null) {
      return 0xff000000 | color;
    }
    return Color.HSBtoRGB(Util.getMillis() / 4096f + (stack.getItem().hashCode() >> 16) / 64f, 0.5f, 0.95f);
  }

  @Override
  public MapCodec<? extends ItemTintSource> type() {
    return CODEC;
  }
}
