package pers.solid.mishang.uc.item;

import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class RotatingToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {

  public RotatingToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    final ActionResult result = rotateBlock(player, world, blockPos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, hand.getEquipmentSlot());
    }
    return result;
  }

  @NotNull
  private ActionResult rotateBlock(PlayerEntity player, World world, BlockPos blockPos) {
    if (world.getBlockState(blockPos).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final BlockRotation rotation = player.isSneaking() ? BlockRotation.COUNTERCLOCKWISE_90 : BlockRotation.CLOCKWISE_90;
    return rotateBlock(world, blockPos, rotation);
  }

  @NotNull
  private ActionResult rotateBlock(World world, BlockPos blockPos, BlockRotation rotation) {
    world.setBlockState(blockPos, world.getBlockState(blockPos).rotate(rotation));
    return ActionResult.SUCCESS;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().allowModifyWorld && !stack.canBreak(new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, pos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, hand.getEquipmentSlot());
    }
    return result;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.2")
            .formatted(Formatting.GRAY));
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return recipeGenerator.createShaped(RecipeCategory.TOOLS, this)
        .pattern("DND")
        .pattern(" | ")
        .pattern(" | ")
        .input('D', Items.PINK_DYE)
        .input('N', Items.NETHERITE_INGOT)
        .input('|', Items.STICK)
        .criterion("has_pink_dye", recipeGenerator.conditionsFromItem(Items.PINK_DYE))
        .criterion("has_netherite_ingot", recipeGenerator.conditionsFromItem(Items.NETHERITE_INGOT));
  }
}
