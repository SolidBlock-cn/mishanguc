package pers.solid.mishang.uc.block;

import com.mojang.datafixers.util.Function3;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;

/**
 * <p>所有带有颜色的方块应有的接口。其对应的方块实体应该实现 {@link pers.solid.mishang.uc.blockentity.ColoredBlockEntity}。
 * <p>在 {@link pers.solid.mishang.uc.MishangucClient} 中，本模组中所有实现该接口的方块都会为其自身以及方块物品注册颜色提供器。
 */
public interface ColoredBlock extends BlockEntityProvider {

  /**
   * 子类在覆盖 {@link net.minecraft.block.Block#getPickStack(BlockView, BlockPos, BlockState)} 时，可以这么写（下列代码使用yarn映射）：
   * <pre>{@code
   *     return getColoredPickStack(world, pos, state, super::getPickStack);}</pre>
   */
  default ItemStack getColoredPickStack(BlockView world, BlockPos pos, BlockState state, Function3<BlockView, BlockPos, BlockState, ItemStack> superGetPickStack) {
    final ItemStack stack = superGetPickStack.apply(world, pos, state);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      stack.getOrCreateSubNbt("BlockEntityTag").putInt("color", coloredBlockEntity.getColor());
    }
    return stack;
  }
}
