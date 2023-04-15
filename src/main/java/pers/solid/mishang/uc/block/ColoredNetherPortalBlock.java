package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

@ApiStatus.AvailableSince("1.0.2")
public class ColoredNetherPortalBlock extends NetherPortalBlock implements ColoredBlock, BlockResourceGenerator {
  public ColoredNetherPortalBlock(Settings settings) {
    super(settings);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    return placementState != null ? placementState.with(AXIS, ctx.getPlayerFacing().rotateYClockwise().getAxis()) : null;
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, (blockView, pos1, state1) -> new ItemStack(this));
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

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull BlockStateSupplier getBlockStates() {
    return VariantsBlockStateSupplier.create(Blocks.NETHER_PORTAL)
        .coordinate(
            BlockStateVariantMap.create(Properties.HORIZONTAL_AXIS)
                .register(Direction.Axis.X, BlockStateVariant.create().put(VariantSettings.MODEL, getBlockModelId().brrp_suffixed("_ns")))
                .register(Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, getBlockModelId().brrp_suffixed("_ew"))));
  }

  @Override
  public ModelJsonBuilder getItemModel() {
    return ModelJsonBuilder.create(getBlockModelId().brrp_suffixed("_ns"));
  }

  @Override
  public LootTable.Builder getLootTable() {
    return LootTable.builder();
  }
}
