package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.FastBuildingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum FastBuildingRangeProperty implements RangeSelectItemModelProperty {
  INSTANCE;
  public static final MapCodec<FastBuildingRangeProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public float get(ItemStack stack, @Nullable ClientLevel world, @Nullable ItemOwner context, int seed) {
    final FastBuildingToolData data = stack.get(MishangucComponents.FAST_BUILDING_TOOL_DATA);
    return data == null ? 0 : data.range();
  }

  @Override
  public MapCodec<? extends RangeSelectItemModelProperty> type() {
    return CODEC;
  }
}
