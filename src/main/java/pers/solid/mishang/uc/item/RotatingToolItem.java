package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
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

import java.util.List;

public class RotatingToolItem extends BlockToolItem implements ItemResourceGenerator {

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
    if (!player.getAbilities().allowModifyWorld && !stack.canPlaceOn(Registries.BLOCK, new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, blockPos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
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
    final boolean b = world.setBlockState(blockPos, world.getBlockState(blockPos).rotate(rotation));
    return ActionResult.success(b);
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().allowModifyWorld && !stack.canDestroy(Registries.BLOCK, new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, pos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, p -> p.sendToolBreakStatus(hand));
    }
    return result;
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.2")
            .formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  @Override
  public @NotNull JRecipe getCraftingRecipe() {
    return new JShapedRecipe(this).pattern("DND", " | ", " | ")
        .addKey("D", Items.PINK_DYE)
        .addKey("N", Items.NETHERITE_INGOT)
        .addKey("|", Items.STICK)
        .addInventoryChangedCriterion("has_pink_dye", Items.PINK_DYE)
        .addInventoryChangedCriterion("has_netherite_ingot", Items.NETHERITE_INGOT)
        .recipeCategory(getRecipeCategory());
  }
}
