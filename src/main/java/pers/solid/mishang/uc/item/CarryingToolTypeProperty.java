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
import pers.solid.mishang.uc.components.CarryingToolData;
import pers.solid.mishang.uc.components.MishangucComponents;

@Environment(EnvType.CLIENT)
public enum CarryingToolTypeProperty implements SelectItemModelProperty<Short> {
  INSTANCE;
  public static final SelectItemModelProperty.Type<CarryingToolTypeProperty, Short> TYPE = Type.create(MapCodec.unit(INSTANCE), Codec.SHORT);

  @Override
  public @Nullable Short get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity user, int seed, ItemDisplayContext displayContext) {
    final CarryingToolData data = stack.get(MishangucComponents.CARRYING_TOOL_DATA);
    if (data == null) {
      return null;
    } else {
      return data.type();
    }
  }

  @Override
  public Codec<Short> valueCodec() {
    return Codec.SHORT;
  }

  @Override
  public Type<? extends SelectItemModelProperty<Short>, Short> type() {
    return TYPE;
  }
}
