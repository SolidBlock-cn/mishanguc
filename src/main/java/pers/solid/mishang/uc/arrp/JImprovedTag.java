package pers.solid.mishang.uc.arrp;

import net.devtech.arrp.json.tags.JTag;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class JImprovedTag extends JTag {

  @SuppressWarnings("UnusedReturnValue")
  public JImprovedTag addBlock(Block block) {
    add(Registry.BLOCK.getId(block));
    return this;
  }

  @SuppressWarnings("UnusedReturnValue")
  public JImprovedTag addItem(Item item) {
    add(Registry.ITEM.getId(item));
    return this;
  }
}
