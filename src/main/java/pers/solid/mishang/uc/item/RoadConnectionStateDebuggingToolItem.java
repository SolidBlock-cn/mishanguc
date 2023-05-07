package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.model.Models;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.util.RoadConnectionState;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class RoadConnectionStateDebuggingToolItem extends BlockToolItem implements ItemResourceGenerator {

  public RoadConnectionStateDebuggingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  /**
   * 向聊天框广播各个方向的道路连接状态。
   */
  public static ActionResult sendMessageOfState(
      PlayerEntity playerEntity, BlockState blockState, BlockPos blockPos) {
    Block block = blockState.getBlock();
    if (!(block instanceof final Road road)) {
      playerEntity.sendMessage(TextBridge.translatable("debug.mishanguc.notRoad").formatted(Formatting.RED), false);
      return ActionResult.FAIL;
    }
    playerEntity.sendMessage(
        TextBridge.translatable("debug.mishanguc.roadConnectionState.allDir", String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()))
            .formatted(Formatting.YELLOW),
        false);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final RoadConnectionState connectionState = road.getConnectionStateOf(blockState, direction);
      playerEntity.sendMessage(
          TextBridge.translatable("debug.mishanguc.roadConnectionState.brief",
              RoadConnectionState.text(direction),
              (connectionState.offsetLevel() == 0 ? RoadConnectionState.text(connectionState.direction()) : TextBridge.translatable("debug.mishanguc.roadConnectionState.offset", RoadConnectionState.text(direction), RoadConnectionState.text(connectionState.offsetDirection()), connectionState.offsetLevel())).formatted(Formatting.WHITE),
              RoadConnectionState.text(connectionState.lineColor()),
              RoadConnectionState.text(connectionState.lineType()).formatted(Formatting.WHITE),
              RoadConnectionState.text(connectionState.whetherConnected())).styled(style -> style.withColor(0xcccccc)),
          false);
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (world.isClient)
      return sendMessageOfState(
          player, world.getBlockState(blockHitResult.getBlockPos()), blockHitResult.getBlockPos());
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (world.isClient) return sendMessageOfState(player, world.getBlockState(pos), pos);
    return ActionResult.SUCCESS;
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.road_connection_state_debugging_tool.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.road_connection_state_debugging_tool.tooltip.2").formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    return ItemResourceGenerator.super.getItemModel().parent(Models.HANDHELD);
  }
}
