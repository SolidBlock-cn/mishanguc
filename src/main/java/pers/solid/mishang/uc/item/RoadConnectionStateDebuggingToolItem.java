package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class RoadConnectionStateDebuggingToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {

  public RoadConnectionStateDebuggingToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  /**
   * 向聊天框广播各个方向的道路连接状态。
   */
  public static InteractionResult sendMessageOfState(
      Player playerEntity, BlockState blockState, BlockPos blockPos) {
    Block block = blockState.getBlock();
    if (!(block instanceof final Road road)) {
      playerEntity.sendSystemMessage(Component.translatable("debug.mishanguc.notRoad").withStyle(ChatFormatting.RED));
      return InteractionResult.FAIL;
    }
    playerEntity.sendSystemMessage(
        Component.translatable("debug.mishanguc.roadConnectionState.allDir", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
            .withStyle(ChatFormatting.YELLOW));
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      final RoadConnectionState connectionState = road.getConnectionStateOf(blockState, direction);
      playerEntity.sendSystemMessage(
          Component.translatable("debug.mishanguc.roadConnectionState.brief", RoadConnectionState.text(direction), (connectionState.offsetLevel() == 0 ? RoadConnectionState.text(connectionState.direction()) : Component.translatable("debug.mishanguc.roadConnectionState.offset", RoadConnectionState.text(direction), RoadConnectionState.text(connectionState.offsetDirection()), connectionState.offsetLevel())).withStyle(ChatFormatting.WHITE), RoadConnectionState.text(connectionState.lineColor()), RoadConnectionState.text(connectionState.lineType()).withStyle(ChatFormatting.WHITE), RoadConnectionState.text(connectionState.whetherConnected())).withStyle(style -> style.withColor(0xcccccc)));
    }
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    if (world.isClientSide())
      return sendMessageOfState(
          player, world.getBlockState(blockHitResult.getBlockPos()), blockHitResult.getBlockPos());
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClientSide()) return sendMessageOfState(player, world.getBlockState(pos), pos);
    return InteractionResult.SUCCESS;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(Component.translatable("item.mishanguc.road_connection_state_debugging_tool.tooltip.1").withStyle(ChatFormatting.GRAY));
    tooltip.add(Component.translatable("item.mishanguc.road_connection_state_debugging_tool.tooltip.2").withStyle(ChatFormatting.GRAY));
  }
}
