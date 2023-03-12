package pers.solid.mishang.uc.block;

import net.devtech.arrp.json.loot.JLootTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassHandrailBlock extends GlassHandrailBlock implements ColoredBlock {
  public ColoredGlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture) {
    super(baseBlock, settings, frameTexture, decorationTexture, ColoredCentral::new, ColoredCorner::new, ColoredStair::new, ColoredOuter::new);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
  }

  public static class ColoredCentral extends CentralBlock implements ColoredBlock {

    protected ColoredCentral(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return getColoredPickStack(world, pos, state, super::getPickStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
      super.appendTooltip(stack, world, tooltip, options);
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public JLootTable getLootTable() {
      return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
    }
  }

  public static class ColoredCorner extends CornerBlock implements ColoredBlock {

    protected ColoredCorner(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return getColoredPickStack(world, pos, state, super::getPickStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
      super.appendTooltip(stack, world, tooltip, options);
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public JLootTable getLootTable() {
      return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
    }
  }

  public static class ColoredOuter extends OuterBlock implements ColoredBlock {

    protected ColoredOuter(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return getColoredPickStack(world, pos, state, super::getPickStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
      super.appendTooltip(stack, world, tooltip, options);
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public JLootTable getLootTable() {
      return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
    }
  }

  public static class ColoredStair extends StairBlock implements ColoredBlock {

    protected ColoredStair(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
      return getColoredPickStack(world, pos, state, super::getPickStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
      super.appendTooltip(stack, world, tooltip, options);
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public JLootTable getLootTable() {
      return JLootTable.delegate(new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION));
    }
  }
}
