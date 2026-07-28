package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.block.AbstractBlock;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.item.NamedBlockItem;

import java.util.List;

/**
 * <h1>道路台阶部分</h1>
 * <p>
 * 道路方块对应的台阶。
 */
public final class RoadSlabBlocks extends MishangucBlocks {

  /**
   * 方块到台阶方块的双向映射表。
   */
  public static final BiMap<@NotNull AbstractRoadBlock, @NotNull AbstractRoadSlabBlock> BLOCK_TO_SLABS = HashBiMap.create();
  public static final List<@NotNull SmartRoadSlabBlock<AbstractRoadBlock>> SLABS = MishangUtils.instanceStream(RoadBlocks.class, AbstractRoadBlock.class).map(RoadSlabBlocks::of).toList();

  @SuppressWarnings("unchecked")
  private static <T extends AbstractRoadBlock & Road> SmartRoadSlabBlock<T> of(T baseBlock) {
    final Identifier baseId = Registries.BLOCK.getId(baseBlock);
    final String path = baseId.getPath();
    final String slabPath = StringUtils.replace(StringUtils.removeEnd(path, "_block"), "road", "road_slab", 1);

    final SmartRoadSlabBlock<T> slab;
    if (baseBlock instanceof RoadBlockWithAutoLine) {
      slab = (SmartRoadSlabBlock<T>) register(slabPath, settings -> new RoadSlabBlockWithAutoLine((RoadBlockWithAutoLine) baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
    } else {
      slab = register(slabPath, settings -> new SmartRoadSlabBlock<>(baseBlock, settings), AbstractBlock.Settings.copy(baseBlock));
    }
    if (BLOCK_TO_SLABS.containsKey(baseBlock)) {
      throw new IllegalArgumentException(String.format("The slab for this road (%s) already exists!", baseBlock));
    }
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }

  static void registerAll() {
    SLABS.forEach(slab -> Items.register(slab, NamedBlockItem::new));
  }
}
