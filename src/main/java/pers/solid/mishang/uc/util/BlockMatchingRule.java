package pers.solid.mishang.uc.util;

import com.google.common.collect.Sets;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Util;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 一个方块匹配规则，根据该规则来匹配两个方块是否匹配。
 */
public abstract class BlockMatchingRule implements StringRepresentable {
  public static final ResourceKey<Registry<BlockMatchingRule>> REGISTRY_KEY =
      ResourceKey.createRegistryKey(Mishanguc.id("block_matching_rule"));
  public static final MappedRegistry<BlockMatchingRule> REGISTRY = FabricRegistryBuilder.create(REGISTRY_KEY).buildAndRegister();
  public static final BlockMatchingRule SAME_STATE =
      new BlockMatchingRule() {
        @Override
        public boolean match(BlockState state1, BlockState state2) {
          return state1.equals(state2);
        }
      }.register("same_state");
  public static final BlockMatchingRule SAME_BLOCK =
      new BlockMatchingRule() {
        @Override
        public boolean match(BlockState state1, BlockState state2) {
          return state1.getBlock() == state2.getBlock();
        }
      }.register("same_block");
  public static final BlockMatchingRule SAME_MATERIAL =
      new BlockMatchingRule() {
        @Override
        public boolean match(BlockState state1, BlockState state2) {
          // 仅限于 1.20 的临时解决方案
          return state1.getSoundType() == state2.getSoundType() && state1.isAir() == state2.isAir();
        }
      }.register("same_material");
  public static final BlockMatchingRule ANY =
      new BlockMatchingRule() {
        @Override
        public boolean match(BlockState state1, BlockState state2) {
          return state1.isAir() == state2.isAir();
        }
      }.register("any");

  public abstract boolean match(BlockState state1, BlockState state2);

  public BlockMatchingRule register(Identifier identifier) {
    return Registry.register(REGISTRY, identifier, this);
  }

  @Override
  public @Nullable String getSerializedName() {
    final @Nullable Identifier id = REGISTRY.getKey(this);
    return id == null ? null : id.toString();
  }

  /**
   * 将其注册到注册表，并使用本模组的命名空间。
   */
  protected BlockMatchingRule register(String string) {
    return register(Mishanguc.id(string));
  }

  /**
   * 获取该方块匹配规则注册表中的 id。
   */
  public Identifier getId() {
    return REGISTRY.getKey(this);
  }

  public MutableComponent getName() {
    return Component.translatable(Util.makeDescriptionId("blockMatchingRule", REGISTRY.getKey(this)));
  }

  /**
   * 获得指定大小平面内，所有有效的方块位置。有效的方块位置是指未被遮挡住的。
   *
   * @param world     世界。可以是客户端的，也可以是服务端的。
   * @param centerPos 中心方块坐标，一般就是工具指向的方块的坐标。
   * @param side      工具所指向的方块所在的面。
   * @param range     范围，一般不建议超过32。将会生成一个在face所在轴为法线的平面内、以centerPos为中心的正方形、边长为2*range+1的平面。
   * @return 包含这些坐标的链式集合。
   */
  public Set<BlockPos> getPlainValidBlockPoss(
      Level world, BlockPos centerPos, Direction side, int range) {
    final LinkedHashSet<BlockPos> set = Sets.newLinkedHashSet();
    final Direction.Axis axis = side.getAxis();
    for (BlockPos pos : BlockPos.withinManhattan(
        centerPos,
        axis == Direction.Axis.X ? 0 : range,
        axis == Direction.Axis.Y ? 0 : range,
        axis == Direction.Axis.Z ? 0 : range)) {
      final BlockPos offsetPos = pos.relative(side);
      final boolean isValid = world
          .getBlockState(offsetPos)
          .getCollisionShape(world, pos)
          .getFaceShape(side.getOpposite())
          .isEmpty();
      if (!isValid || !this.match(world.getBlockState(pos), world.getBlockState(centerPos))) {
        continue;
      }
      if (pos.equals(centerPos)) {
        set.add(pos.immutable());
        continue;
      }
      for (BlockPos pos1 : BlockPos.withinManhattan(
          pos,
          axis == Direction.Axis.X ? 0 : 1,
          axis == Direction.Axis.Y ? 0 : 1,
          axis == Direction.Axis.Z ? 0 : 1)) {
        if (set.contains(pos1.immutable())) {
          set.add(pos.immutable());
        }
      }
    }
    return set;
  }
}
