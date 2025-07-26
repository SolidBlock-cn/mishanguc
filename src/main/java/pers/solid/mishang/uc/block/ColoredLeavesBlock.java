package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.data.*;
import net.minecraft.data.loottable.BlockLootTableGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.particle.TintedParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;
import java.util.function.BiFunction;

@ApiStatus.AvailableSince("0.2.4")
public class ColoredLeavesBlock extends LeavesBlock implements ColoredBlock {
  private final @Nullable BiFunction<Block, BlockLootTableGenerator, LootTable.Builder> lootBuilder;
  private final Identifier texture;

  public static final MapCodec<ColoredLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codecs.rangedInclusiveFloat(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter((block) -> block.leafParticleChance), createSettingsCodec(), Identifier.CODEC.fieldOf("texture").forGetter(o -> o.texture)).apply(instance, (chance, settings1, s) -> new ColoredLeavesBlock(chance, settings1, null, s)));

  public ColoredLeavesBlock(float leaveParticleChance, Settings settings, @Nullable BiFunction<Block, BlockLootTableGenerator, LootTable.Builder> lootBuilder, Identifier texture) {
    super(leaveParticleChance, settings);
    this.lootBuilder = lootBuilder;
    this.texture = texture;
  }

  public ColoredLeavesBlock(float leafParticleChance, Settings settings, @Nullable BiFunction<Block, BlockLootTableGenerator, LootTable.Builder> lootBuilder, String texture) {
    this(leafParticleChance, settings, lootBuilder, Identifier.of(texture));
  }

  @Override
  public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
    return getColoredPickStack(world, pos, state, includeData, super::getPickStack);
  }

  @Override
  public void getMishangTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
    ColoredBlock.appendColorTooltip(stack, tooltip);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new SimpleColoredBlockEntity(pos, state);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockStateModelGenerator blockStateModelGenerator) {
    final Identifier modelId = Models.LEAVES.upload(this, TextureMap.all(texture), blockStateModelGenerator.modelCollector);
    blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(this, BlockStateModelGenerator.createWeightedVariant(modelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModels.tinted(modelId, ColoredTintSource.INSTANCE));
  }


  @Override
  public LootTable.Builder getLootTable(BlockLootTableGenerator blockLootTableGenerator) {
    if (lootBuilder == null) return null;
    return (lootBuilder.apply(this, blockLootTableGenerator).apply(COPY_COLOR_LOOT_FUNCTION));
  }

  @Override
  public MapCodec<? extends ColoredLeavesBlock> getCodec() {
    return CODEC;
  }

  @Override
  protected void spawnLeafParticle(World world, BlockPos pos, Random random) {
    TintedParticleEffect entityEffectParticleEffect = TintedParticleEffect.create(ParticleTypes.TINTED_LEAVES, world.getBlockColor(pos));
    ParticleUtil.spawnParticle(world, pos, random, entityEffectParticleEffect);
    // 检查彩色树叶的颗粒颜色
  }
}
