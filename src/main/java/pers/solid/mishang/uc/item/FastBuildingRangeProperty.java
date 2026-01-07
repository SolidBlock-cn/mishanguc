package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.property.numeric.NumericProperty;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HeldItemContext;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.FastBuildingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum FastBuildingRangeProperty implements NumericProperty {
  INSTANCE;
  public static final MapCodec<FastBuildingRangeProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public float getValue(ItemStack stack, @Nullable ClientWorld world, @Nullable HeldItemContext context, int seed) {
    final FastBuildingToolData data = stack.get(MishangucComponents.FAST_BUILDING_TOOL_DATA);
    return data == null ? 0 : data.range();
  }

  @Override
  public MapCodec<? extends NumericProperty> getCodec() {
    return CODEC;
  }
}
