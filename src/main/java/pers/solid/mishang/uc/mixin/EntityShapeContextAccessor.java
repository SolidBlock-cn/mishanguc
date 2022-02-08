package pers.solid.mishang.uc.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 访问 {@link EntityShapeContext}，用于获取玩家手持的物品，从而用于 {@link
 * pers.solid.mishang.uc.block.HungSignBarBlock#getOutlineShape(BlockState, BlockView, BlockPos,
 * ShapeContext)} 进行判断。
 */
@Mixin(EntityShapeContext.class)
public interface EntityShapeContextAccessor {
  @Accessor
  Item getHeldItem();
}
