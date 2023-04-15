package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.model.Models;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.brrp.v1.util.RecipeJsonFactory;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

public class MirroringToolItem extends BlockToolItem implements ItemResourceGenerator {
  public MirroringToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public ActionResult mirror(World world, BlockPos blockPos, Direction side, @Nullable Entity entity) {
    final BlockState blockState = world.getBlockState(blockPos);
    final Direction.Axis axis = side.getAxis();
    final BlockMirror mirror;
    switch (axis) {
      case X:
        mirror = BlockMirror.FRONT_BACK;
        break;
      default:
        if (entity == null) mirror = BlockMirror.NONE;
        else {
          switch (entity.getHorizontalFacing().getAxis()) {
            case X:
              mirror = BlockMirror.FRONT_BACK;
              break;
            case Z:
              mirror = BlockMirror.LEFT_RIGHT;
              break;
            default:
              mirror = BlockMirror.NONE;
              break;
          }
        }
        break;
      case Z:
        mirror = BlockMirror.LEFT_RIGHT;
        break;
    }
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
    if (!player.abilities.allowModifyWorld && !stack.canPlaceOn(world.getTagManager(), new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    if (MishangUtils.isOperatorBlock(world.getBlockState(blockPos).getBlock()) && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, blockPos, blockHitResult.getSide(), player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.abilities.allowModifyWorld && !stack.canDestroy(world.getTagManager(), new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    if (MishangUtils.isOperatorBlock(world.getBlockState(pos).getBlock()) && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, pos, direction, player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, player1 -> player1.sendToolBreakStatus(hand));
    return result;
  }

  @Environment(EnvType.CLIENT)
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
  public ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.HANDHELD).addTexture(TextureKey.LAYER0, getTextureId());
  }

  @Override
  public RecipeJsonFactory getCraftingRecipe() {
    return ShapedRecipeJsonFactory.create(this).patterns("CNL", " | ", " | ")
        .input('C', Items.CYAN_DYE)
        .input('N', Items.NETHERITE_INGOT)
        .input('L', Items.LIME_DYE)
        .input('|', Items.STICK)
        .criterionFromItem("has_cyan_dye", Items.CYAN_DYE)
        .criterionFromItem("has_netherite_ingot", Items.NETHERITE_INGOT)
        .criterionFromItem("has_lime_dye", Items.LIME_DYE)::offerTo;
  }
}
