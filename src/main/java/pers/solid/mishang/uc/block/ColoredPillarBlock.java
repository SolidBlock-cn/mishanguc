package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipType;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.TextureMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pers.solid.brrp.v1.api.RuntimeResourcePack;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.impl.BRRPBlockLootTableGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredPillarBlock extends PillarBlock implements ColoredBlock, BlockResourceGenerator {
  public static final MapCodec<ColoredPillarBlock> CODEC = createCodec(settings1 -> new ColoredPillarBlock(settings1, null));
  private final @Nullable TextureMap textures;

  public ColoredPillarBlock(Settings settings, @Nullable TextureMap textures) {
    super(settings);
    this.textures = textures;
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
    return getColoredPickStack(world, pos, state, super::getPickStack);
  }

  @Override
  public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    super.appendTooltip(stack, context, tooltip, options);
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @UnknownNullability ModelJsonBuilder getBlockModel() {
    if (textures == null) return null;
    return ModelJsonBuilder.create(new Identifier("mishanguc:block/colored_cube_column")).setTextures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    ColoredBlock.super.writeBlockModel(pack);
    pack.addModel(getBlockModelId().brrp_suffixed("_horizontal"), getBlockModel().withParent(new Identifier("mishanguc:block/colored_cube_column_horizontal")));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @UnknownNullability BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createAxisRotatedBlockState(this, getBlockModelId(), getBlockModelId().brrp_suffixed("_horizontal"));
  }

  @Override
  public LootTable.Builder getLootTable() {
    return BRRPBlockLootTableGenerator.INSTANCE.drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredPillarBlock> getCodec() {
    return CODEC;
  }
}
