package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.command.DefaultPermissions;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.data.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.util.TextBridge;
import pers.solid.mishang.uc.util.WithMishangTooltip;

import java.util.List;

public class MirroringToolItem extends BlockToolItem implements MishangucItem, WithMishangTooltip {
  public MirroringToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult mirror(World world, BlockPos blockPos, Direction side, @Nullable Entity entity) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Direction.Axis axis = side.getAxis();
    final BlockMirror mirror = switch (axis) {
      case X -> BlockMirror.FRONT_BACK;
      case Z -> BlockMirror.LEFT_RIGHT;
      default -> entity == null ? BlockMirror.NONE : switch (entity.getHorizontalFacing().getAxis()) {
        case X -> BlockMirror.FRONT_BACK;
        case Z -> BlockMirror.LEFT_RIGHT;
        default -> BlockMirror.NONE;
      };
    };
    final BlockState mirrored = blockState.mirror(mirror);
    final boolean setBlockState = world.setBlockState(blockPos, mirrored);
    return setBlockState && !blockState.equals(mirrored) ? ActionResult.SUCCESS : ActionResult.FAIL;
  }

  @Override
  public ActionResult useOnBlock(
      ItemStack stack, PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    final BlockPos blockPos = blockHitResult.getBlockPos();
    if (world.getBlockState(blockPos).getBlock() instanceof OperatorBlock && !player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, blockPos, blockHitResult.getSide(), player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, hand.getEquipmentSlot());
    return result;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().allowModifyWorld && !stack.canBreak(new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    if (world.getBlockState(pos).getBlock() instanceof OperatorBlock && !player.getPermissions().hasPermission(DefaultPermissions.GAMEMASTERS)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, pos, direction, player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, hand.getEquipmentSlot());
    return result;
  }

  @Override
  public void getMishangTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType options) {
    tooltip.add(
        TextBridge.translatable("item.mishanguc.mirroring_tool.tooltip").formatted(Formatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
    if (stack.getOrDefault(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplayComponent.DEFAULT).shouldDisplay(MishangucComponents.INCLUDES_FLUID)) {
      if (includesFluid == null) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.block_tool.tooltip.includesFluidWhileSneaking")
                .formatted(Formatting.GRAY));
      } else if (includesFluid) {
        tooltip.add(
            TextBridge.translatable("item.mishanguc.block_tool.tooltip.includesFluid")
                .formatted(Formatting.GRAY));
      }
    }
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe(RecipeGenerator recipeGenerator) {
    return recipeGenerator.createShaped(RecipeCategory.TOOLS, this)
        .pattern("CNL")
        .pattern(" | ")
        .pattern(" | ")
        .input('C', Items.CYAN_DYE)
        .input('N', Items.NETHERITE_INGOT)
        .input('L', Items.LIME_DYE)
        .input('|', Items.STICK)
        .criterion("has_cyan_dye", recipeGenerator.conditionsFromItem(Items.CYAN_DYE))
        .criterion("has_netherite_ingot", recipeGenerator.conditionsFromItem(Items.NETHERITE_INGOT))
        .criterion("has_lime_dye", recipeGenerator.conditionsFromItem(Items.LIME_DYE));
  }
}
