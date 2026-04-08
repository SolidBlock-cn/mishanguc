package pers.solid.mishang.uc.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class MirroringToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {
  public MirroringToolItem(Properties settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public InteractionResult mirror(Level world, BlockPos blockPos, Direction side, @Nullable Entity entity) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Direction.Axis axis = side.getAxis();
    final Mirror mirror = switch (axis) {
      case X -> Mirror.FRONT_BACK;
      case Z -> Mirror.LEFT_RIGHT;
      default -> entity == null ? Mirror.NONE : switch (entity.getDirection().getAxis()) {
        case X -> Mirror.FRONT_BACK;
        case Z -> Mirror.LEFT_RIGHT;
        default -> Mirror.NONE;
      };
    };
    final BlockState mirrored = blockState.mirror(mirror);
    final boolean setBlockState = world.setBlockAndUpdate(blockPos, mirrored);
    return setBlockState && !blockState.equals(mirrored) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
  }

  @Override
  public InteractionResult useOnBlock(
      ItemStack stack, Player player,
      Level world,
      BlockHitResult blockHitResult,
      InteractionHand hand,
      boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    if (world.getBlockState(blockPos).getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
      return InteractionResult.FAIL;
    }
    final InteractionResult result = mirror(world, blockPos, blockHitResult.getDirection(), player);
    if (result == InteractionResult.SUCCESS) stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
    return result;
  }

  @Override
  public InteractionResult beginAttackBlock(
      ItemStack stack, Player player, Level world, InteractionHand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().mayBuild && !stack.canBreakBlockInAdventureMode(new BlockInWorld(world, pos, false))) {
      return InteractionResult.PASS;
    }
    if (world.getBlockState(pos).getBlock() instanceof GameMasterBlock && !player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)) {
      return InteractionResult.FAIL;
    }
    final InteractionResult result = mirror(world, pos, direction, player);
    if (result == InteractionResult.SUCCESS) stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
    return result;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    tooltip.add(
        Component.translatable("item.mishanguc.mirroring_tool.tooltip").withStyle(ChatFormatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
    if (stack.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT).shows(MishangucComponents.INCLUDES_FLUID)) {
      if (includesFluid == null) {
        tooltip.add(
            Component.translatable("item.mishanguc.block_tool.tooltip.includesFluidWhileSneaking")
                .withStyle(ChatFormatting.GRAY));
      } else if (includesFluid) {
        tooltip.add(
            Component.translatable("item.mishanguc.block_tool.tooltip.includesFluid")
                .withStyle(ChatFormatting.GRAY));
      }
    }
  }

  @Override
  public RecipeBuilder getCraftingRecipe(RecipeProvider recipeGenerator) {
    return recipeGenerator.shaped(RecipeCategory.TOOLS, this)
        .pattern("CNL")
        .pattern(" | ")
        .pattern(" | ")
        .define('C', Items.CYAN_DYE)
        .define('N', Items.NETHERITE_INGOT)
        .define('L', Items.LIME_DYE)
        .define('|', Items.STICK)
        .unlockedBy("has_cyan_dye", recipeGenerator.has(Items.CYAN_DYE))
        .unlockedBy("has_netherite_ingot", recipeGenerator.has(Items.NETHERITE_INGOT))
        .unlockedBy("has_lime_dye", recipeGenerator.has(Items.LIME_DYE));
  }
}
