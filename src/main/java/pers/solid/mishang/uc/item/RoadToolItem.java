package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.block.AbstractRoadBlock;
import pers.solid.mishang.uc.block.AbstractRoadSlabBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.block.RoadWithAutoLine;
import pers.solid.mishang.uc.blocks.RoadBlocks;
import pers.solid.mishang.uc.util.LineColor;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

@ApiStatus.AvailableSince("0.2.4")
public class RoadToolItem extends BlockToolItem implements MishangucItem {
  public RoadToolItem(Settings settings) {
    super(settings, Boolean.FALSE);
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    tooltip.add(TextBridge.translatable("item.mishanguc.road_tool.tooltip.1", TextBridge.keybind("key.attack").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.road_tool.tooltip.2", TextBridge.keybind("key.use").styled(style -> style.withColor(0xdddddd))).formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("item.mishanguc.road_tool.tooltip.3").formatted(Formatting.GRAY));
  }

  @Override
  public ActionResult useOnBlock(ItemStack stack, PlayerEntity player, World world, BlockHitResult blockHitResult, Hand hand, boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final BlockState blockState = world.getBlockState(blockPos);
    if (blockState.isOf(RoadBlocks.ROAD_BLOCK)) {
      if (!world.isClient) {
        world.setBlockState(blockPos, (player.isSneaking() ? RoadBlocks.ROAD_WITH_WHITE_AUTO_RA_LINE : RoadBlocks.ROAD_WITH_WHITE_AUTO_BA_LINE).getStateWithProperties(blockState));
        player.getStackInHand(hand).damage(1, player, LivingEntity.getSlotForHand(hand));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.painted"), true);
      }
      return ActionResult.SUCCESS;
    } else if (blockState.isOf(RoadBlocks.ROAD_BLOCK.getRoadSlab())) {
      if (!world.isClient) {
        world.setBlockState(blockPos, (player.isSneaking() ? RoadBlocks.ROAD_WITH_WHITE_AUTO_RA_LINE : RoadBlocks.ROAD_WITH_WHITE_AUTO_BA_LINE).getRoadSlab().getStateWithProperties(blockState));
        player.getStackInHand(hand).damage(1, player, LivingEntity.getSlotForHand(hand));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.painted"), true);
      }
      return ActionResult.SUCCESS;
    } else if (blockState.getBlock() instanceof RoadWithAutoLine roadWithAutoLine) {
      if (!world.isClient) {
        try {
          final BlockState newState = roadWithAutoLine.makeState(roadWithAutoLine.getConnectionStateMap(world, blockPos), blockState);
          world.setBlockState(blockPos, newState);
          player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.converted"), true);
        } catch (Throwable throwable) {
          Mishanguc.MISHANG_LOGGER.error("An error was found when converting block state at {}:", blockPos, throwable);
          player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.error").formatted(Formatting.RED), true);
        }
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
        player.getStackInHand(hand).damage(1, player, LivingEntity.getSlotForHand(hand));
      }
      return ActionResult.SUCCESS;
    } else if (block instanceof AbstractRoadSlabBlock) {
      if (!world.isClient) {
        world.setBlockState(pos, RoadBlocks.ROAD_BLOCK.getRoadSlab().getStateWithProperties(blockState));
        player.sendMessage(TextBridge.translatable("item.mishanguc.road_tool.message.cleared"), true);
        player.getStackInHand(hand).damage(1, player, LivingEntity.getSlotForHand(hand));
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

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, this)
        .pattern("aba")
        .pattern("bXb")
        .pattern("aba")
        .input('a', LineColor.WHITE.getIngredient())
        .input('b', LineColor.YELLOW.getIngredient())
        .input('X', Items.STICK)
        .criterion("has_white_dye", RecipeProvider.conditionsFromTag(LineColor.WHITE.getIngredient()))
        .criterion("has_yellow_dye", RecipeProvider.conditionsFromTag(LineColor.YELLOW.getIngredient()));
  }
}
