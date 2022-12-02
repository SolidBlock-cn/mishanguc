package pers.solid.mishang.uc.block;

import net.devtech.arrp.generator.BRRPCubeBlock;
import net.devtech.arrp.json.loot.JLootTable;
import net.devtech.arrp.json.models.JTextures;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.server.loottable.VanillaBlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredCubeBlock extends BRRPCubeBlock implements ColoredBlock {

  @ApiStatus.Internal
  public ColoredCubeBlock(Settings settings, String parent, JTextures textures) {
    super(settings, parent, textures);
  }

  public static ColoredCubeBlock cubeAll(Settings settings, String allTexture) {
    return new ColoredCubeBlock(settings, "mishanguc:block/colored_cube_all", JTextures.ofAll(allTexture));
  }

  public static ColoredCubeBlock cubeBottomTop(Settings settings, String topTexture, String sideTexture, String bottomTexture) {
    return new ColoredCubeBlock(settings, "mishanguc:block/colored_cube_bottom_top", JTextures.ofSides(topTexture, sideTexture, bottomTexture));
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
