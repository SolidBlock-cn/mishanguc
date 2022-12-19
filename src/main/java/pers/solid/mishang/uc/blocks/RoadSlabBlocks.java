package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.MishangucItemGroups;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.item.NamedBlockItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>道路台阶部分</h1>
 * <p>
 * 道路方块对应的台阶。
 */
public final class RoadSlabBlocks extends MishangucBlocks {

  /**
   * 方块到台阶方块的双向映射表。
   */
  public static final BiMap<AbstractRoadBlock, AbstractRoadSlabBlock> BLOCK_TO_SLABS = HashBiMap.create();
  public static final List<SmartRoadSlabBlock<AbstractRoadBlock>> SLABS = MishangUtils.instanceStream(RoadBlocks.class, AbstractRoadBlock.class).map(RoadSlabBlocks::of).collect(Collectors.toList());

  @SuppressWarnings("unchecked")
  private static <T extends AbstractRoadBlock & Road> SmartRoadSlabBlock<T> of(
      T baseBlock) {
    final SmartRoadSlabBlock<T> slab;
    if (baseBlock instanceof RoadBlockWithAutoLine) {
      slab = (SmartRoadSlabBlock<T>) new RoadSlabBlockWithAutoLine((RoadBlockWithAutoLine) baseBlock);
    } else {
      slab = new SmartRoadSlabBlock<>(baseBlock);
    }
    if (BLOCK_TO_SLABS.containsKey(baseBlock)) {
      throw new IllegalArgumentException(String.format("The slab for this road (%s) already exists!", baseBlock));
    }
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }

  static void registerAll() {
    SLABS.forEach(slab -> {
      final Identifier baseId = slab.baseBlock.getBlockId();
      final String namespace = baseId.getNamespace();
      final String path = baseId.getPath();
      final Identifier slabId = new Identifier(namespace, StringUtils.replace(StringUtils.removeEnd(path, "_block"), "road", "road_slab", 1));
      Registry.register(Registry.BLOCK, slabId, slab);
      Registry.register(Registry.ITEM, slabId, new NamedBlockItem(slab, new FabricItemSettings().group(MishangucItemGroups.ROADS)));
    });
  }
}
