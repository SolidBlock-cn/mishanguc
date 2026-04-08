package pers.solid.mishang.uc.blocks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.apache.commons.lang3.Strings;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.item.NamedBlockItem;
import pers.solid.mishang.uc.mixin.ItemsAccessor;

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
  public static final BiMap<AbstractRoadBlock, AbstractRoadSlabBlock> BLOCK_TO_SLABS = HashBiMap.create();
  public static final List<SmartRoadSlabBlock<AbstractRoadBlock>> SLABS = MishangUtils.instanceStream(RoadBlocks.class, AbstractRoadBlock.class).map(RoadSlabBlocks::of).toList();

  @SuppressWarnings("unchecked")
  private static <T extends AbstractRoadBlock & Road> SmartRoadSlabBlock<T> of(T baseBlock) {
    final Identifier baseId = BuiltInRegistries.BLOCK.getKey(baseBlock);
    final String path = baseId.getPath();
    final String slabPath = Strings.CS.replace(Strings.CS.removeEnd(path, "_block"), "road", "road_slab", 1);

    final SmartRoadSlabBlock<T> slab;
    if (baseBlock instanceof RoadBlockWithAutoLine) {
      slab = (SmartRoadSlabBlock<T>) register(slabPath, settings -> new RoadSlabBlockWithAutoLine((RoadBlockWithAutoLine) baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
    } else {
      slab = register(slabPath, settings -> new SmartRoadSlabBlock<>(baseBlock, settings), BlockBehaviour.Properties.ofFullCopy(baseBlock));
    }
    if (BLOCK_TO_SLABS.containsKey(baseBlock)) {
      throw new IllegalArgumentException(String.format("The slab for this road (%s) already exists!", baseBlock));
    }
    BLOCK_TO_SLABS.put(baseBlock, slab);
    return slab;
  }

  static void registerAll() {
    SLABS.forEach(slab -> ItemsAccessor.callRegisterBlock(slab, NamedBlockItem::new));
  }
}
