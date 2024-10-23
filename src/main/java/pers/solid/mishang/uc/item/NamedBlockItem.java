package pers.solid.mishang.uc.item;

import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.ColoredGlassHandrailBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.components.MishangucComponents;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * <p>类似于 {@link BlockItem}，但是名称会调用 {@link Block#getName()}。</p>
 * <p>必须注意：由于 {@link Block#getName()} 仅限客户端，因此本类的方块必须确保覆盖该方法时，没有注解为 {@code @}{@link Environment}{@code (EnvType.CLIENT)}！！</p>
 */
public class NamedBlockItem extends BlockItem {

  public NamedBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  public Text getName() {
    return getBlock().getName();
  }

  @Override
  public Text getName(ItemStack stack) {
    final Block block = getBlock();
    if (getBlock() instanceof ColoredBlock) {
      final Integer color = stack.get(MishangucComponents.COLOR);
      if (color != null) {
        return TextBridge.translatable("block.mishanguc.colored_block.color", block.getName(), MishangUtils.describeColor(color));
      } else if (getBlock() instanceof ColoredGlassHandrailBlock) {
        return TextBridge.translatable("block.mishanguc.colored_block.auto_color_decoration", block.getName());
      } else {
        return TextBridge.translatable("block.mishanguc.colored_block.auto_color", block.getName());
      }
    }
    return block.getName();
  }

  @Override
  protected boolean place(ItemPlacementContext context, BlockState state) {
    final ItemStack stack = context.getStack();
    if (getBlock() instanceof ColoredBlock) {
      final Integer color = stack.get(MishangucComponents.COLOR);
      final World world = context.getWorld();
      int dependentColor = -1;
      if (color == null) {
        final BlockPos dependingPos = ((ItemUsageContextInvoker) context).invokeGetHitResult().getBlockPos();
        if (world.getBlockEntity(dependingPos) instanceof ColoredBlockEntity dependingColoredBlockEntity) {
          dependentColor = dependingColoredBlockEntity.getColor();
        } else {
          dependentColor = world.getBlockState(dependingPos).getMapColor(world, dependingPos).color;
        }
      }
      final boolean place = super.place(context, state);
      final BlockEntity placedEntity = world.getBlockEntity(context.getBlockPos());
      if (color == null && placedEntity instanceof final ColoredBlockEntity placedColoredBlockEntity) {
        placedColoredBlockEntity.setColor(dependentColor);
      }
      return place;
    } else {
      return super.place(context, state);
    }
  }
}
