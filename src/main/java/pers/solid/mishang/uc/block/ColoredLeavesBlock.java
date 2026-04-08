package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;
import pers.solid.mishang.uc.item.ColoredTintSource;

import java.util.List;
import java.util.function.BiFunction;

@ApiStatus.AvailableSince("0.2.4")
public class ColoredLeavesBlock extends LeavesBlock implements ColoredBlock {
  private final @Nullable BiFunction<Block, BlockLootSubProvider, LootTable.Builder> lootBuilder;
  private final Identifier texture;

  public static final MapCodec<ColoredLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter((block) -> block.leafParticleChance), propertiesCodec(), Identifier.CODEC.fieldOf("texture").forGetter(o -> o.texture)).apply(instance, (chance, settings1, s) -> new ColoredLeavesBlock(chance, settings1, null, s)));

  public ColoredLeavesBlock(float leaveParticleChance, Properties settings, @Nullable BiFunction<Block, BlockLootSubProvider, LootTable.Builder> lootBuilder, Identifier texture) {
    super(leaveParticleChance, settings);
    this.lootBuilder = lootBuilder;
    this.texture = texture;
  }

  public ColoredLeavesBlock(float leafParticleChance, Properties settings, @Nullable BiFunction<Block, BlockLootSubProvider, LootTable.Builder> lootBuilder, String texture) {
    this(leafParticleChance, settings, lootBuilder, Identifier.parse(texture));
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

  @Environment(EnvType.CLIENT)
  @Override
  public void registerModels(ModelProvider modelProvider, BlockModelGenerators blockStateModelGenerator) {
    final Identifier modelId = ModelTemplates.LEAVES.create(this, TextureMapping.cube(texture), blockStateModelGenerator.modelOutput);
    blockStateModelGenerator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(this, BlockModelGenerators.plainVariant(modelId)));
    blockStateModelGenerator.itemModelOutput.accept(asItem(), ItemModelUtils.tintedModel(modelId, ColoredTintSource.INSTANCE));
  }


  @Override
  public LootTable.Builder getLootTable(BlockLootSubProvider blockLootTableGenerator) {
    if (lootBuilder == null) return null;
    return (lootBuilder.apply(this, blockLootTableGenerator).apply(COPY_COLOR_LOOT_FUNCTION));
  }

  @Override
  public MapCodec<? extends ColoredLeavesBlock> codec() {
    return CODEC;
  }

  @Override
  protected void spawnFallingLeavesParticle(Level world, BlockPos pos, RandomSource random) {
    ColorParticleOption entityEffectParticleEffect = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, world.getClientLeafTintColor(pos));
    ParticleUtils.spawnParticleBelow(world, pos, random, entityEffectParticleEffect);
    // 检查彩色树叶的颗粒颜色
  }
}
