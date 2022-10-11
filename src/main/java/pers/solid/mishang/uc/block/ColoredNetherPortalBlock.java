package pers.solid.mishang.uc.block;

import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.model.BlockStateVariant;
import net.minecraft.data.client.model.BlockStateVariantMap;
import net.minecraft.data.client.model.VariantSettings;
import net.minecraft.data.client.model.VariantsBlockStateSupplier;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    return super.getPlacementState(ctx).with(AXIS, ctx.getPlayerFacing().rotateYClockwise().getAxis());
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, (blockView, pos1, state1) -> new ItemStack(this));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockView world) {
    return new SimpleColoredBlockEntity();
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JBlockStates getBlockStates() {
    return JBlockStates.delegate(VariantsBlockStateSupplier.create(Blocks.NETHER_PORTAL)
        .coordinate(
            BlockStateVariantMap.create(Properties.HORIZONTAL_AXIS)
                .register(Direction.Axis.X, BlockStateVariant.create().put(VariantSettings.MODEL, getBlockModelId().brrp_append("_ns")))
                .register(Direction.Axis.Z, BlockStateVariant.create().put(VariantSettings.MODEL, getBlockModelId().brrp_append("_ew")))));
  }

  @Override
  public @Nullable JModel getItemModel() {
    return new JModel(getBlockModelId().brrp_append("_ns"));
  }

  @Override
  public JLootTable getLootTable() {
    return new JLootTable("block");
  }
}
