package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.LineType;

import java.util.List;

public abstract class AbstractRoadBlock extends Block implements Road {
  protected final LineColor lineColor;
  protected final LineType lineType;

  public AbstractRoadBlock(Settings settings, LineColor lineColor, LineType lineType) {
    super(settings);
    this.lineColor = lineColor;
    this.lineType = lineType;
  }

  @Override
  public LineType getLineType(BlockState state, Direction direction) {
    return lineType;
  }

  @Override
  public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    appendRoadProperties(builder);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    return withPlacementState(super.getPlacementState(ctx), ctx);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return mirrorRoad(super.mirror(state, mirror), mirror);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return rotateRoad(super.rotate(state, rotation), rotation);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    ActionResult result = super.onUse(state, world, pos, player, hand, hit);
    if (result == ActionResult.FAIL) {
      return result;
    }
    return onUseRoad(state, world, pos, player, hand, hit);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborUpdate(
      BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
    super.neighborUpdate(state, world, pos, block, fromPos, notify);
    neighborRoadUpdate(state, world, pos, block, fromPos, notify);
  }

  @Override
  public LineColor getLineColor(BlockState state, Direction direction) {
    return lineColor;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    appendRoadTooltip(stack, world, tooltip, options);
  }
}
