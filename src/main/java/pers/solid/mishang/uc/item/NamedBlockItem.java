package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;

/**
 * <p>类似于 {@link BlockItem}，但是名称会调用 {@link Block#getName()}。</p>
 * <p>必须注意：由于 {@link Block#getName()} 仅限客户端，因此本类的方块必须确保覆盖该方法时，没有注解为 {@code @}{@link Environment}{@code (EnvType.CLIENT)}！！</p>
 */
public class NamedBlockItem extends BlockItem {
  public static int cachedColor = -1;

  public NamedBlockItem(Block block, Settings settings) {
    super(block, settings);
  }

  @Override
  @Environment(EnvType.CLIENT)
  public Text getName() {
    return getBlock().getName();
  }

  @Override
  public Text getName(ItemStack stack) {
    final Block block = getBlock();
    try {
      if (getBlock() instanceof ColoredBlock) {
        final NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
        if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
          final int color = nbt.getInt("color");
          return Text.translatable("block.mishanguc.colored_block.color", block.getName(), MishangUtils.describeColor(color));
        } else {
          return Text.translatable("block.mishanguc.colored_block.auto_color", block.getName());
        }
      }
      return block.getName();
    } catch (NoSuchMethodError error) {
      throw new NoSuchMethodError(String.format("Please ensure the 'getName' method of block '%s' is not annotated with '@Environment(EnvType.CLIENT)'!!!", block));
    }
  }

  /**
   * 每次放置<i>之前</i>就会保存好放置时所需要的颜色，这是因为，放置后，客户端在收到实体更新之前，就已经决定好了颜色，导致方块放置之后的颜色是默认颜色，而不是方块物品储存的颜色。
   */
  @Override
  protected boolean place(ItemPlacementContext context, BlockState state) {
    final ItemStack stack = context.getStack();
    if (getBlock() instanceof ColoredBlock) {
      final NbtCompound nbt = stack.getSubNbt("BlockEntityTag");
      if (nbt != null && nbt.contains("color", NbtType.NUMBER)) {
        cachedColor = nbt.getInt("color");
      } else {
        final BlockPos blockPos = ((ItemUsageContextInvoker) context).invokeGetHitResult().getBlockPos();
        final World world = context.getWorld();
        if (world.getBlockEntity(blockPos) instanceof ColoredBlockEntity coloredBlockEntity) {
          cachedColor = coloredBlockEntity.getColor();
        } else {
          cachedColor = world.getBlockState(blockPos).getMapColor(world, blockPos).color;
        }
      }
      final boolean place = super.place(context, state);
      cachedColor = -1;
      return place;
    } else {
      return super.place(context, state);
    }
  }
}
