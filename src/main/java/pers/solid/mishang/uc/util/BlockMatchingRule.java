package pers.solid.mishang.uc.util;

import com.google.common.collect.Sets;
import com.mojang.serialization.Lifecycle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 一个方块匹配规则，根据该规则来匹配两个方块是否匹配。
 */
public abstract class BlockMatchingRule implements StringIdentifiable {
  protected static final RegistryKey<Registry<BlockMatchingRule>> REGISTRY_KEY =
      RegistryKey.ofRegistry(new Identifier("mishanguc", "block_matching_rule"));
  public static final SimpleRegistry<BlockMatchingRule> REGISTRY =
      new SimpleRegistry<>(REGISTRY_KEY, Lifecycle.stable());
  public static final BlockMatchingRule SAME_STATE =
      new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
          return state1.equals(state2);
        }
      }.register("same_state");
  public static final BlockMatchingRule SAME_BLOCK =
      new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
          return state1.getBlock() == state2.getBlock();
        }
      }.register("same_block");
  public static final BlockMatchingRule SAME_MATERIAL =
      new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
          return state1.getMaterial() == state2.getMaterial();
        }
      }.register("same_material");
  public static final BlockMatchingRule ANY =
      new BlockMatchingRule() {
        @Override
        public boolean match(@NotNull BlockState state1, @NotNull BlockState state2) {
          return state1.isAir() == state2.isAir();
        }
      }.register("any");

  public static @Nullable BlockMatchingRule fromString(String name) {
    return REGISTRY.get(new Identifier(name));
  }

  public static @NotNull BlockMatchingRule fromString(String name, BlockMatchingRule defaultValue) {
    final BlockMatchingRule value = fromString(name);
    return value == null ? defaultValue : value;
  }

  public abstract boolean match(@NotNull BlockState state1, @NotNull BlockState state2);

  public BlockMatchingRule register(Identifier identifier) {
    return Registry.register(REGISTRY, identifier, this);
  }

  @Override
  public @Nullable String asString() {
    final @Nullable Identifier id = REGISTRY.getId(this);
    return id == null ? null : id.toString();
  }

  /**
   * 类似于{@link #asString()}，但是未注册的会返回空字符串而不是null。
   */
  public @NotNull String asStringOrEmpty() {
    final String s = asString();
    return s == null ? "" : s;
  }

  /**
   * 将其注册到注册表，并使用本模组的命名空间。
   */
  protected BlockMatchingRule register(String string) {
    return register(new Identifier("mishanguc", string));
  }

  /**
   * 获取该方块匹配规则注册表中的 id。
   */
  public Identifier getId() {
    return REGISTRY.getId(this);
  }

  @Environment(EnvType.CLIENT)
  public MutableText getName() {
    return new TranslatableText(
        Util.createTranslationKey("blockMatchingRule", REGISTRY.getId(this)));
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
      @NotNull World world, @NotNull BlockPos centerPos, @NotNull Direction side, int range) {
    final LinkedHashSet<BlockPos> set = Sets.newLinkedHashSet();
    final Direction.Axis axis = side.getAxis();
    for (BlockPos pos :
        BlockPos.iterateOutwards(
            centerPos,
            axis == Direction.Axis.X ? 0 : range,
            axis == Direction.Axis.Y ? 0 : range,
            axis == Direction.Axis.Z ? 0 : range)) {
      final BlockPos offsetPos = pos.offset(side);
      final boolean isValid =
          world
              .getBlockState(offsetPos)
              .getCollisionShape(world, pos)
              .getFace(side.getOpposite())
              .isEmpty();
      if (!isValid || !this.match(world.getBlockState(pos), world.getBlockState(centerPos))) {
        continue;
      }
      if (pos.equals(centerPos)) {
        set.add(pos.toImmutable());
        continue;
      }
      for (BlockPos pos1 :
          BlockPos.iterateOutwards(
              pos,
              axis == Direction.Axis.X ? 0 : 1,
              axis == Direction.Axis.Y ? 0 : 1,
              axis == Direction.Axis.Z ? 0 : 1)) {
        if (set.contains(pos1.toImmutable())) {
          set.add(pos.toImmutable());
        }
      }
    }
    return set;
  }
}
