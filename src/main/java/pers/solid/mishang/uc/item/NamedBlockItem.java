package pers.solid.mishang.uc.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

/**
 * 类似于 {@link BlockItem}，但是名称会调用 {@link Block#getName()}，而不是直接调用 {@link #getTranslationKey}。
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
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      // 由于 Block#getName 仅限客户端，因此这里也仅限客户端执行。
      return getBlock().getName();
    } else {
      return super.getName(stack);
    }
  }
}
