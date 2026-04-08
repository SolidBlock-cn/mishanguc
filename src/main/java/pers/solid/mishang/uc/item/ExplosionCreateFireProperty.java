package pers.solid.mishang.uc.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.ExplosionToolComponent;
import pers.solid.mishang.uc.components.MishangucComponents;

public enum ExplosionCreateFireProperty implements ConditionalItemModelProperty {
  INSTANCE;
  public static final MapCodec<ExplosionCreateFireProperty> CODEC = MapCodec.unit(INSTANCE);

  @Override
  public boolean get(ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
    final ExplosionToolComponent component = stack.get(MishangucComponents.EXPLOSION_TOOL_DATA);
    return component != null && component.createFire();
  }

  @Override
  public MapCodec<? extends ConditionalItemModelProperty> type() {
    return CODEC;
  }
}
