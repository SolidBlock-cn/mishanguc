package pers.solid.mishang.uc.block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassHandrailBlock extends GlassHandrailBlock implements ColoredBlock {

  public ColoredGlassHandrailBlock(Block baseBlock, Properties settings, String frameTexture, String decorationTexture, Identifier identifier) {
    super(baseBlock, settings, frameTexture, decorationTexture, ColoredCentral::new, ColoredCorner::new, ColoredStair::new, ColoredOuter::new, identifier);
  }

  @Override
  public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public String customRecipeCategory() {
    return "handrails";
  }

  public static class ColoredCentral extends CentralBlock implements ColoredBlock {

    protected ColoredCentral(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
      return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
    }

    @Override
    public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
      return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredCorner extends CornerBlock implements ColoredBlock {

    protected ColoredCorner(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
      return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
    }

    @Override
    public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
      return blockLootTableGenerator.createSingleItemTable(this, ContextIntProviders.exactly(2)).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredOuter extends OuterBlock implements ColoredBlock {

    protected ColoredOuter(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
      return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
    }

    @Override
    public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
      return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredStair extends StairBlock implements ColoredBlock {

    protected ColoredStair(GlassHandrailBlock baseRail, Properties settings) {
      super(baseRail, settings);
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
      return getColoredPickStack(world, pos, state, includeData, super::getCloneItemStack);
    }

    @Override
    public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag options) {
      ColoredBlock.appendColorTooltip(stack, tooltip);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
      return new SimpleColoredBlockEntity(pos, state);
    }

    @Override
    public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
      return blockLootTableGenerator.createSingleItemTable(this).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }
}
