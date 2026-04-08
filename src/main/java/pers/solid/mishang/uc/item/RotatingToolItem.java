package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class RotatingToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {

  public RotatingToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final InteractionResult result = rotateBlock(player, world, blockPos);
    if (result == InteractionResult.SUCCESS) {
      stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
    }
    return result;
  }

  private InteractionResult rotateBlock(Player player, Level world, BlockPos blockPos) {
    if (world.getBlockState(blockPos).getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
      return InteractionResult.FAIL;
    }
    final Rotation rotation = player.isShiftKeyDown() ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90;
    return rotateBlock(world, blockPos, rotation);
  }

  private InteractionResult rotateBlock(Level world, BlockPos blockPos, Rotation rotation) {
    world.setBlockAndUpdate(blockPos, world.getBlockState(blockPos).rotate(rotation));
    return InteractionResult.SUCCESS;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().mayBuild && !stack.canBreakBlockInAdventureMode(new BlockInWorld(world, pos, false))) {
      return InteractionResult.PASS;
    }
    final InteractionResult result = rotateBlock(player, world, pos);
    if (result == InteractionResult.SUCCESS) {
      stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
    }
    return result;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(
        Component.translatable("item.mishanguc.rotating_tool.tooltip.1")
            .withStyle(ChatFormatting.GRAY));
    tooltip.add(
        Component.translatable("item.mishanguc.rotating_tool.tooltip.2")
            .withStyle(ChatFormatting.GRAY));
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.shaped(RecipeCategory.TOOLS, this)
        .pattern("DND")
        .pattern(" | ")
        .pattern(" | ")
        .define('D', Items.PINK_DYE)
        .define('N', Items.NETHERITE_INGOT)
        .define('|', Items.STICK)
        .unlockedBy("has_pink_dye", recipeGenerator.has(Items.PINK_DYE))
        .unlockedBy("has_netherite_ingot", recipeGenerator.has(Items.NETHERITE_INGOT));
  }
}
