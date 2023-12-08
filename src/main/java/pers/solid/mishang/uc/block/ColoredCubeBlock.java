package pers.solid.mishang.uc.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.loottable.vanilla.VanillaBlockLootTableGenerator;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import pers.solid.brrp.v1.generator.BRRPCubeBlock;
import pers.solid.mishang.uc.blockentity.SimpleColoredBlockEntity;

import java.util.List;

public class ColoredCubeBlock extends BRRPCubeBlock implements ColoredBlock {
  public static final MapCodec<ColoredCubeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(createSettingsCodec(), Identifier.CODEC.fieldOf("parent").forGetter(b -> b.parent)).apply(instance, (settings1, identifier) -> new ColoredCubeBlock(settings1, identifier, new TextureMap())));

  @ApiStatus.Internal
  public ColoredCubeBlock(Settings settings, Identifier parent, TextureMap textures) {
    super(settings, parent, textures);
  }

  public static ColoredCubeBlock cubeAll(Settings settings, String allTexture) {
    return new ColoredCubeBlock(settings, new Identifier("mishanguc", "block/colored_cube_all"), TextureMap.all(new Identifier(allTexture)));
  }

  public static ColoredCubeBlock cubeBottomTop(Settings settings, String topTexture, String sideTexture, String bottomTexture) {
    return new ColoredCubeBlock(settings, new Identifier("mishanguc:block/colored_cube_bottom_top"), TextureMap.of(TextureKey.TOP, new Identifier(topTexture)).put(TextureKey.SIDE, new Identifier(sideTexture)).put(TextureKey.BOTTOM, new Identifier(bottomTexture)));
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

  @Override
  public LootTable.@UnknownNullability Builder getLootTable() {
    return new VanillaBlockLootTableGenerator().drops(this).apply(COPY_COLOR_LOOT_FUNCTION);
  }

  @Override
  protected MapCodec<? extends ColoredCubeBlock> getCodec() {
    return CODEC;
  }
}
