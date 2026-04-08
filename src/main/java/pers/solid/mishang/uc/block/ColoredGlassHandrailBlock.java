package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
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
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredGlassHandrailBlock extends GlassHandrailBlock implements ColoredBlock {
  public static final MapCodec<ColoredGlassHandrailBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("base_block").forGetter(GlassHandrailBlock::baseBlock), propertiesCodec()).apply(instance, (block, settings1) -> new ColoredGlassHandrailBlock(block, settings1, null, null, null)));

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
  protected MapCodec<? extends ColoredGlassHandrailBlock> codec() {
    return CODEC;
  }

  @Override
  public String customRecipeCategory() {
    return "handrails";
  }

  public static class ColoredCentral extends CentralBlock implements ColoredBlock {
    public static final MapCodec<ColoredCentral> CODEC = createSubCodec(b -> b.baseHandrail, ColoredCentral::new);

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
    protected MapCodec<? extends ColoredCentral> codec() {
      return CODEC;
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredCorner extends CornerBlock implements ColoredBlock {
    public static final MapCodec<ColoredCorner> CODEC = createSubCodec(b -> b.baseHandrail, ColoredCorner::new);

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
      return blockLootTableGenerator.createSingleItemTable(this, ConstantValue.exactly(2)).apply(COPY_COLOR_LOOT_FUNCTION);
    }

    @Override
    protected MapCodec<? extends ColoredCorner> codec() {
      return CODEC;
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredOuter extends OuterBlock implements ColoredBlock {
    public static final MapCodec<ColoredOuter> CODEC = createSubCodec(b -> b.baseHandrail, ColoredOuter::new);

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
    protected MapCodec<? extends ColoredOuter> codec() {
      return CODEC;
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }

  public static class ColoredStair extends StairBlock implements ColoredBlock {
    public static final MapCodec<ColoredStair> CODEC = createSubCodec(b -> b.baseHandrail, ColoredStair::new);

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
    protected MapCodec<? extends ColoredStair> codec() {
      return CODEC;
    }

    @Override
    public String customRecipeCategory() {
      return "handrails";
    }
  }
}
