package pers.solid.mishang.uc.item;

import net.devtech.arrp.generator.ItemResourceGenerator;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
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

public class MirroringToolItem extends BlockToolItem implements ItemResourceGenerator {
  public MirroringToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult mirror(World world, BlockPos blockPos, Direction side, @Nullable Entity entity) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Direction.Axis axis = side.getAxis();
    final BlockMirror mirror = switch (axis) {
      case X -> BlockMirror.FRONT_BACK;
      default -> entity == null ? BlockMirror.NONE : switch (entity.getHorizontalFacing().getAxis()) {
        case X -> BlockMirror.FRONT_BACK;
        case Z -> BlockMirror.LEFT_RIGHT;
        default -> BlockMirror.NONE;
      };
      case Z -> BlockMirror.LEFT_RIGHT;
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
    if (!player.getAbilities().allowModifyWorld && !stack.canPlaceOn(Registries.BLOCK, new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    if (world.getBlockState(blockPos).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, blockPos, blockHitResult.getSide(), player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().allowModifyWorld && !stack.canDestroy(Registries.BLOCK, new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    if (world.getBlockState(pos).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, pos, direction, player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        TextBridge.translatable("item.mishanguc.mirroring_tool.tooltip").formatted(Formatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
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

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return null;
  }

  @Override
  public @NotNull JRecipe getCraftingRecipe() {
    return new JShapedRecipe(this).pattern("CNL", " | ", " | ")
        .addKey("C", Items.CYAN_DYE)
        .addKey("N", Items.NETHERITE_INGOT)
        .addKey("L", Items.LIME_DYE)
        .addKey("|", Items.STICK)
        .addInventoryChangedCriterion("has_cyan_dye", Items.CYAN_DYE)
        .addInventoryChangedCriterion("has_netherite_ingot", Items.NETHERITE_INGOT)
        .addInventoryChangedCriterion("has_lime_dye", Items.LIME_DYE)
        .recipeCategory(getRecipeCategory());
  }
}
