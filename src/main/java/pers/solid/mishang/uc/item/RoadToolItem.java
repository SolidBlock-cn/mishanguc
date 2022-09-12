package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
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
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.block.RoadWithAutoLine;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.blocks.RoadSlabBlocks;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

@ApiStatus.AvailableSince("0.2.4")
public class RoadToolItem extends BlockToolItem implements ItemResourceGenerator {
  public RoadToolItem(Settings settings) {
    super(settings, Boolean.FALSE);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(TextBridge.translatable("item.mishanguc.road_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.road_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockState blockState = world.getBlockState(blockPos);
    if (blockState.isOf(RoadBlocks.ROAD_BLOCK)) {
      if (!world.isClient) {
        world.setBlockState(blockPos, RoadBlocks.ROAD_WITH_WHITE_AUTO_BA_LINE.getStateWithProperties(blockState));
        player.getStackInHand(hand).damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.painted"), true);
      }
      return ActionResult.SUCCESS;
    } else if (blockState.isOf(RoadSlabBlocks.ROAD_SLAB)) {
      if (!world.isClient) {
        world.setBlockState(blockPos, RoadSlabBlocks.ROAD_SLAB_WITH_WHITE_AUTO_BA_LINE.getStateWithProperties(blockState));
        player.getStackInHand(hand).damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.painted"), true);
      }
      return ActionResult.SUCCESS;
    } else if (blockState.getBlock() instanceof RoadWithAutoLine roadWithAutoLine) {
      if (!world.isClient) {
        final BlockState newState = roadWithAutoLine.makeState(roadWithAutoLine.getConnectionStateMap(world, blockPos), blockState);
        world.setBlockState(blockPos, newState);
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.converted"), true);
      }
      return ActionResult.SUCCESS;
    } else if (!(blockState.getBlock() instanceof Road)) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.not_road").formatted(Formatting.RED), true);
      return ActionResult.FAIL;
    }
    return ActionResult.PASS;
  }

  @Override
  public ActionResult beginAttackBlock(ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    final BlockState blockState = world.getBlockState(pos);
    final Block block = blockState.getBlock();
    if (!(block instanceof Road)) {
      player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.not_road").formatted(Formatting.RED), true);
      return ActionResult.FAIL;
    }
    if (block instanceof AbstractRoadBlock) {
      if (!world.isClient) {
        world.setBlockState(pos, RoadBlocks.ROAD_BLOCK.getStateWithProperties(blockState));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.cleared"), true);
        player.getStackInHand(hand).damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
      }
      return ActionResult.SUCCESS;
    } else if (block instanceof AbstractRoadSlabBlock) {
      if (!world.isClient) {
        world.setBlockState(pos, RoadSlabBlocks.ROAD_SLAB.getStateWithProperties(blockState));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.cleared"), true);
        player.getStackInHand(hand).damage(1, player, playerEntity -> playerEntity.sendToolBreakStatus(hand));
      }
      return ActionResult.SUCCESS;
    }
    return ActionResult.PASS;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public boolean renderBlockOutline(PlayerEntity player, ItemStack itemStack, WorldRenderContext worldRenderContext, WorldRenderContext.BlockOutlineContext blockOutlineContext, Hand hand) {
    if (worldRenderContext.world().getBlockState(blockOutlineContext.blockPos()).getBlock() instanceof Road) {
      return super.renderBlockOutline(player, itemStack, worldRenderContext, blockOutlineContext, hand);
    }
    return false;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getItemModel() {
    return ItemResourceGenerator.super.getItemModel().parent("item/handheld");
  }
}
