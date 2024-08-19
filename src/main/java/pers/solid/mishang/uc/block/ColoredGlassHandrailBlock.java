package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.impl.BRRPBlockLootTableGenerator;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassHandrailBlock extends GlassHandrailBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlassHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Registries.BLOCK.getCodec().fieldOf("base_block").forGetter(GlassHandrailBlock::baseBlock), createSettingsCodec()).apply(instance, (block, settings1) -> new ColoredGlassHandrailBlock(block, settings1, null, null)));

  public ColoredGlassHandrailBlock(Block baseBlock, Settings settings, String frameTexture, String decorationTexture) {
    super(baseBlock, settings, frameTexture, decorationTexture, ColoredCentral::new, ColoredCorner::new, ColoredStair::new, ColoredOuter::new);
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
  public LootTable.Builder getLootTable() {
    return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredGlassHandrailBlock> getCodec() {
    return CODEC;
  }

  public static class ColoredCentral extends CentralBlock implements ColoredBlock {
    public static final MapCodec<ColoredCentral> CODEC = createSubCodec(b -> b.baseHandrail, ColoredCentral::new);

    protected ColoredCentral(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
    public LootTable.Builder getLootTable() {
      return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    protected MapCodec<? extends ColoredCentral> getCodec() {
      return CODEC;
    }
  }

  public static class ColoredCorner extends CornerBlock implements ColoredBlock {
    public static final MapCodec<ColoredCorner> CODEC = createSubCodec(b -> b.baseHandrail, ColoredCorner::new);

    protected ColoredCorner(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
    public LootTable.Builder getLootTable() {
      return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    protected MapCodec<? extends ColoredCorner> getCodec() {
      return CODEC;
    }
  }

  public static class ColoredOuter extends OuterBlock implements ColoredBlock {
    public static final MapCodec<ColoredOuter> CODEC = createSubCodec(b -> b.baseHandrail, ColoredOuter::new);

    protected ColoredOuter(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
    public LootTable.Builder getLootTable() {
      return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    protected MapCodec<? extends ColoredOuter> getCodec() {
      return CODEC;
    }
  }

  public static class ColoredStair extends StairBlock implements ColoredBlock {
    public static final MapCodec<ColoredStair> CODEC = createSubCodec(b -> b.baseHandrail, ColoredStair::new);

    protected ColoredStair(@NotNull GlassHandrailBlock baseRail) {
      super(baseRail);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
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
    public LootTable.Builder getLootTable() {
      return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    protected MapCodec<? extends ColoredStair> getCodec() {
      return CODEC;
    }
  }
}
