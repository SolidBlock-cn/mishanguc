package pers.solid.mishang.uc.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.BlockStateSupplier;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.brrp.v1.generator.BlockResourceGenerator;
import pers.solid.brrp.v1.impl.BRRPBlockLootTableGenerator;
import pers.solid.brrp.v1.model.ModelJsonBuilder;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;
import java.util.function.BiFunction;

@ApiStatus.AvailableSince("0.2.4")
public class ColoredLeavesBlock extends LeavesBlock implements ColoredBlock, BlockResourceGenerator {
  private final @Nullable BiFunction<Block, BlockLootTableGenerator, LootTable.Builder> lootBuilder;
  private final String allTexture;

  public static final MapCodec<ColoredLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createSettingsCodec(), Codec.STRING.fieldOf("all_texture").forGetter(o -> o.allTexture)).apply(instance, (settings1, s) -> new ColoredLeavesBlock(settings1, null, s)));

  public ColoredLeavesBlock(Settings settings, @Nullable BiFunction<Block, BlockLootTableGenerator, LootTable.Builder> lootBuilder, String allTexture) {
    super(settings);
    this.lootBuilder = lootBuilder;
    this.allTexture = allTexture;
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

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull BlockStateSupplier getBlockStates() {
    return BlockStateModelGenerator.createSingletonBlockState(this, getBlockModelId());
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull ModelJsonBuilder getBlockModel() {
    return ModelJsonBuilder.create(Models.LEAVES).addTexture(TextureKey.ALL, allTexture);
  }

  @Override
  public LootTable.Builder getLootTable() {
    if (lootBuilder == null) return null;
    return lootBuilder.apply(this, BRRPBlockLootTableGenerator.INSTANCE).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  public MapCodec<? extends ColoredLeavesBlock> getCodec() {
    return CODEC;
  }
}
