package pers.solid.mishang.uc.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredIceBlock extends IceBlock implements ColoredBlock, BlockResourceGenerator {
  private final TextureMap textures;

  public ColoredIceBlock(Settings settings, TextureMap textures) {
    super(settings);
    this.textures = textures;
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

  @NotNull
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/colored_cube_all")).setTextures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @UnknownNullability BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createSingletonBlockState(this, getBlockModelId());
  }

  @Override
  public LootTable.@UnknownNullability Builder getLootTable() {
    return BlockLootTableGenerator.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }
}
