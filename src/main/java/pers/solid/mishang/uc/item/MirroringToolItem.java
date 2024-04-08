package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
    if (!player.getAbilities().allowModifyWorld && !stack.canPlaceOn(new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    if (world.getBlockState(blockPos).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, blockPos, blockHitResult.getSide(), player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, LivingEntity.getSlotForHand(hand));
    return result;
  }

  @Override
  public ActionResult beginAttackBlock(
      ItemStack stack, PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!player.getAbilities().allowModifyWorld && !stack.canBreak(new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    if (world.getBlockState(pos).getBlock() instanceof OperatorBlock && !player.hasPermissionLevel(2)) {
      return ActionResult.FAIL;
    }
    final ActionResult result = mirror(world, pos, direction, player);
    if (result == ActionResult.SUCCESS) stack.damage(1, player, LivingEntity.getSlotForHand(hand));
    return result;
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
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
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this).patterns("CNL", " | ", " | ")
        .input('C', Items.CYAN_DYE)
        .input('N', Items.NETHERITE_INGOT)
        .input('L', Items.LIME_DYE)
        .input('|', Items.STICK)
        .criterionFromItem("has_cyan_dye", Items.CYAN_DYE)
        .criterionFromItem("has_netherite_ingot", Items.NETHERITE_INGOT)
        .criterionFromItem("has_lime_dye", Items.LIME_DYE)
        .setCustomRecipeCategory("tools");
  }
}
