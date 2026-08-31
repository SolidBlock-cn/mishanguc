package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class TpToolItem extends Item implements MishangucItem, WithMishangTooltip {
  public TpToolItem(Properties settings) {
    super(settings);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.tp_tool.tooltip", Component.keybind("key.use").withStyle(style -> style.withColor(0xdddddd))).withStyle(ChatFormatting.GRAY));
  }

  @Override
  public InteractionResult use(Level world, Player user, InteractionHand hand) {
    if (world.isClientSide()) {
      return InteractionResult.SUCCESS;
    }
    final Vec3 oldPos = user.position();
    final HitResult raycast = user.pick(256, 0, user.isShiftKeyDown());
    if (raycast.getType() == HitResult.Type.MISS) {
      return InteractionResult.FAIL;
    }
    final Vec3 pos = raycast.getLocation();
    user.fallDistance = 0;

    // 原先这里是 teleport，并将 particleEffect 设置为 true。
    // 由于自 1.21 开始，这种传送可能会失败，所以调整了传送方式。
    user.teleportTo(pos.x, pos.y, pos.z);
    world.broadcastEntityEvent(user, (byte) 46);

    world.gameEvent(GameEvent.TELEPORT, pos, GameEvent.Context.of(user));
    world.broadcastEntityEvent(user, (byte) 46);
    world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
    user.getItemInHand(hand).hurtAndBreak((int) Mth.sqrt((float) (Mth.square(oldPos.x - pos.x) + Mth.square(oldPos.y - pos.y) + Mth.square(oldPos.z - pos.z))), user, hand.asEquipmentSlot());
    return InteractionResult.SUCCESS;
  }
}
