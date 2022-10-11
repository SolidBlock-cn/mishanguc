package pers.solid.mishang.uc.block;

import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;
import java.util.function.Function;

@ApiStatus.AvailableSince("0.2.4")
public class ColoredLeavesBlock extends LeavesBlock implements ColoredBlock, BlockResourceGenerator {
  private final Function<Block, LootTable.Builder> lootBuilder;
  private final String allTexture;

  public ColoredLeavesBlock(Settings settings, Function<Block, LootTable.Builder> lootBuilder, String allTexture) {
    super(settings);
    this.lootBuilder = lootBuilder;
    this.allTexture = allTexture;
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
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
    return JBlockStates.simple(getBlockModelId());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    return new JModel("block/leaves").addTexture("all", allTexture);
  }

  @Override
  public JLootTable getLootTable() {
    return JLootTable.delegate(lootBuilder.apply(this).apply(COPY_COLOR_LOOT_FUNCTION).build());
  }
}
