package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.OperatorBlock;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.CraftingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import pers.solid.brrp.v1.generator.ItemResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
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
    if (!player.getAbilities().allowModifyWorld && !stack.canPlaceOn(new CachedBlockPosition(world, blockPos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, blockPos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, LivingEntity.getSlotForHand(hand));
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
    if (!player.getAbilities().allowModifyWorld && !stack.canBreak(new CachedBlockPosition(world, pos, false))) {
      return ActionResult.PASS;
    }
    final ActionResult result = rotateBlock(player, world, pos);
    if (result == ActionResult.SUCCESS) {
      stack.damage(1, player, LivingEntity.getSlotForHand(hand));
    }
    return result;
  }

  @Override
  public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
    super.appendTooltip(stack, context, tooltip, type);
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.1")
            .formatted(Formatting.GRAY));
    tooltip.add(
        TextBridge.translatable("item.mishanguc.rotating_tool.tooltip.2")
            .formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(Models.HANDHELD).addTexture(TextureKey.LAYER0, getTextureId());
  }

  @Override
  public RecipeCategory getRecipeCategory() {
    return RecipeCategory.TOOLS;
  }

  @Override
  public CraftingRecipeJsonBuilder getCraftingRecipe() {
    return ShapedRecipeJsonBuilder.create(getRecipeCategory(), this)
        .patterns("DND", " | ", " | ")
        .input('D', Items.PINK_DYE)
        .input('N', Items.NETHERITE_INGOT)
        .input('|', Items.STICK)
        .criterionFromItem("has_pink_dye", Items.PINK_DYE)
        .criterionFromItem("has_netherite_ingot", Items.NETHERITE_INGOT);
  }
}
