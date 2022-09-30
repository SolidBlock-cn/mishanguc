package pers.solid.mishang.uc.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class TpToolItem extends Item {
  public TpToolItem(Settings settings) {
    super(settings);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.tp_tool.tooltip", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    final TypedActionResult<ItemStack> data = super.use(world, user, hand);
    if (world.isClient) return data;
    final Vec3d oldPos = user.getPos();
    final HitResult raycast = user.raycast(64, 0, user.isSneaking());
    if (raycast.getType() == HitResult.Type.MISS) {
      return TypedActionResult.fail(data.getValue());
    }
    final Vec3d pos = raycast.getPos();
    user.fallDistance = 0;
    user.teleport(pos.x, pos.y, pos.z);
//    world.emitGameEvent(GameEvent.TELEPORT, pos, GameEvent.Emitter.of(user));
    world.sendEntityStatus(user, (byte) 46);
    world.playSound(null, pos.x, pos.y, pos.z, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
    data.getValue().damage((int) MathHelper.sqrt((float) (MathHelper.square(oldPos.x - pos.x) + MathHelper.square(oldPos.y - pos.y) + MathHelper.square(oldPos.z - pos.z))), user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
    return TypedActionResult.success(data.getValue());
  }
}
