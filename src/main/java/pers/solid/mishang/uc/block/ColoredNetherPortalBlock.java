package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

@ApiStatus.AvailableSince("1.0.2")
public class ColoredNetherPortalBlock extends NetherPortalBlock implements ColoredBlock {
  public static final MapCodec<NetherPortalBlock> CODEC = simpleCodec(ColoredNetherPortalBlock::new);

  public ColoredNetherPortalBlock(Properties settings) {
    super(settings);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext ctx) {
    final BlockState state = super.getStateForPlacement(ctx);
    return state == null ? null : state.setValue(AXIS, ctx.getHorizontalDirection().getClockWise().getAxis());
  }

  @Override
  public ItemStack getCloneItemStack(LevelReader world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, (worldView, blockPos, blockState, aBoolean) -> new ItemStack(this));
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

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Identifier ewId = ModelLocationUtils.getModelLocation(this, "_ew");
    final Identifier nsId = ModelLocationUtils.getModelLocation(this, "_ns");
    blockStateModelGenerator.blockStateOutput.accept(MultiVariantGenerator.dispatch(this)
        .with(
            PropertyDispatch.initial(BlockStateProperties.HORIZONTAL_AXIS)
                .select(Direction.Axis.X, BlockModelGenerators.plainVariant(nsId))
                .select(Direction.Axis.Z, BlockModelGenerators.plainVariant(ewId))));
    blockStateModelGenerator.registerSimpleItemModel(this, nsId);
  }

  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    return LootTable.lootTable();
  }

  @Override
  public MapCodec<NetherPortalBlock> codec() {
    return CODEC;
  }
}
