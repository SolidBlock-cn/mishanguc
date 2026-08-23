package pers.solid.mishang.uc.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColorCollection;

import java.util.*;

public final class ColorfulBlockRegistry {
  private static final Map<Block, ColorCollection<? extends Block>> WHITE_TO_COLORFUL_INTERNAL = new HashMap<>();
  public static final Map<Block, ColorCollection<? extends Block>> WHITE_TO_COLORFUL = Collections.unmodifiableMap(WHITE_TO_COLORFUL_INTERNAL);
  private static final Set<Block> COLORFUL_BLOCKS_INTERNAL = new HashSet<>();
  public static final Set<Block> COLORFUL_BLOCKS = Collections.unmodifiableSet(COLORFUL_BLOCKS_INTERNAL);

  public static <B extends Block> void registerColorfulBlocks(ColorCollection<B> colorCollection) {
    final Block whiteBlock = Objects.requireNonNull(colorCollection.white());
    WHITE_TO_COLORFUL_INTERNAL.put(whiteBlock, colorCollection);
    COLORFUL_BLOCKS_INTERNAL.addAll(colorCollection.asList());
  }
}
