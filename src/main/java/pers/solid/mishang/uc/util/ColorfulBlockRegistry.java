package pers.solid.mishang.uc.util;

import net.minecraft.block.Block;
import net.minecraft.util.DyeColor;
import org.apache.commons.lang3.Validate;

import java.util.*;

public final class ColorfulBlockRegistry {
  private static final Map<Block, Map<DyeColor, ? extends Block>> WHITE_TO_COLORFUL_INTERNAL = new HashMap<>();
  public static final Map<Block, Map<DyeColor, ? extends Block>> WHITE_TO_COLORFUL = Collections.unmodifiableMap(WHITE_TO_COLORFUL_INTERNAL);
  private static final Set<Block> COLORFUL_BLOCKS_INTERNAL = new HashSet<>();
  public static final Set<Block> COLORFUL_BLOCKS = Collections.unmodifiableSet(COLORFUL_BLOCKS_INTERNAL);

  public static void registerColorfulBlocks(Map<DyeColor, ? extends Block> blockMap) {
    Validate.notEmpty(blockMap);
    final Block whiteBlock = Objects.requireNonNull(blockMap.get(DyeColor.WHITE));
    WHITE_TO_COLORFUL_INTERNAL.put(whiteBlock, blockMap);
    COLORFUL_BLOCKS_INTERNAL.addAll(blockMap.values());
  }
}
