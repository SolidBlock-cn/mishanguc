package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * <p>类似于 {@link BlockItem}，但是名称会调用 {@link Block#getName()}。</p>
 * <p>必须注意：由于 {@link Block#getName()} 仅限客户端，因此本类的方块必须确保覆盖该方法时，没有注解为 {@code @}{@link Environment}{@code (EnvType.CLIENT)}！！</p>
 */
public class NamedBlockItem extends BlockItem {
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
      return block.getName();
    } catch (NoSuchMethodError error) {
      throw new NoSuchMethodError(String.format("Please ensure the 'getName' method of block '%s' is not annotated with '@Environment(EnvType.CLIENT)'!!!", block));
    }
  }
}
