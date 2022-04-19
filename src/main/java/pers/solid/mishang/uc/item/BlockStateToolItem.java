package pers.solid.mishang.uc.item;

import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;

public class BlockStateToolItem extends BlockToolItem {

  public BlockStateToolItem(Settings settings, @Nullable Boolean includesFluid) {
    super(settings, includesFluid);
  }

  public static void broadcastProperties(
      BlockPos blockPos, PlayerEntity player, BlockState blockState) {
    // 吐槽：为什么 Block#getName 要注解为 @Environment(EnvType.CLIENT)，导致这些东西都只能在客户端使用。
    final Collection<Property<?>> properties = blockState.getProperties();
    if (properties.isEmpty()) {
      player.sendSystemMessage(
          new TranslatableText(
              "debug.mishanguc.blockStates.none",
              String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
              blockState.getBlock().getName().formatted(Formatting.BOLD))
              .formatted(Formatting.RED),
          player.getUuid());
    } else {
      player.sendSystemMessage(
          new TranslatableText(
              "debug.mishanguc.blockStates",
              String.format("%s %s %s", blockPos.getX(), blockPos.getY(), blockPos.getZ()),
              blockState.getBlock().getName().formatted(Formatting.BOLD))
              .formatted(Formatting.YELLOW),
          player.getUuid());
    }
    for (Property<?> property : properties) {
      final MutableText value = getFormattedValue(blockState, property);
      player.sendSystemMessage(
          new LiteralText("  ")
              .append(
                  new LiteralText(property.getName())
                      .styled(style -> style.withColor(0xcccccc)))
              .append(" = ")
              .append(value),
          player.getUuid());
    }
  }

  /**
   * 本方法的目的是考虑到泛型。如果内联，则 {@link Property#name(Comparable)} 会因为泛型而存在问题。
   */
  @NotNull
  private static <T extends Comparable<T>> MutableText getFormattedValue(BlockState blockState, Property<T> property) {
    final T propertyValue = blockState.get(property);
    final MutableText value = new LiteralText(property.name(propertyValue));
    if (property instanceof BooleanProperty) {
      value.formatted(propertyValue == Boolean.TRUE ? Formatting.GREEN : Formatting.RED);
    } else if (property instanceof IntProperty) {
      value.styled(style -> style.withColor(0x00eedd));
    }
    return value;
  }

  @Override
  public ActionResult useOnBlock(
      PlayerEntity player,
      World world,
      BlockHitResult blockHitResult,
      Hand hand,
      boolean fluidIncluded) {
    if (world.isClient) {
      return getBlockStateOf(player, world, blockHitResult.getBlockPos(), false);
    } else {
      return ActionResult.SUCCESS;
    }
  }

  @Override
  public ActionResult beginAttackBlock(
      PlayerEntity player, World world, Hand hand, BlockPos pos, Direction direction, boolean fluidIncluded) {
    if (!world.isClient()) return ActionResult.SUCCESS;
    return getBlockStateOf(player, world, pos, fluidIncluded);
  }

  @Override
  public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity miner) {
    return super.canMine(state, world, pos, miner);
  }

  public ActionResult getBlockStateOf(
      PlayerEntity player, World world, BlockPos blockPos, boolean fluidIncluded) {
    BlockState blockState = world.getBlockState(blockPos);
    broadcastProperties(blockPos, player, blockState);
    if (fluidIncluded) {
      final FluidState fluidState = world.getFluidState(blockPos);
      final int fluidLevel = fluidState.getLevel();
      if (fluidLevel != 0) {
        player.sendSystemMessage(
            new LiteralText("  ")
                .append(
                    new TranslatableText("debug.mishanguc.blockStates.fluidLevel")
                        .styled(style -> style.withColor(0xcccccc)))
                .append(" = ")
                .append(String.valueOf(fluidLevel)),
            player.getUuid());
      }
    }
    return ActionResult.SUCCESS;
  }

  @Override
  public void appendTooltip(
      ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
    super.appendTooltip(stack, world, tooltip, context);
    tooltip.add(
        new TranslatableText("item.mishanguc.block_state_tool.tooltip").formatted(Formatting.GRAY));
    final Boolean includesFluid = includesFluid(stack);
    if (includesFluid == null) {
      tooltip.add(
          new TranslatableText("item.mishanguc.block_state_tool.tooltip.includesFluidWhileSneaking")
              .formatted(Formatting.GRAY));
    } else if (includesFluid) {
      tooltip.add(
          new TranslatableText("item.mishanguc.block_state_tool.tooltip.includesFluid")
              .formatted(Formatting.GRAY));
    }
  }
}
