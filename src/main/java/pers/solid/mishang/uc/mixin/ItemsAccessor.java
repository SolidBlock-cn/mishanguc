package pers.solid.mishang.uc.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.BiFunction;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.item.Items.class)
public interface ItemsAccessor {
  // Why are the methods in the Minecraft source PRIVATE???

  @Invoker
  static Item callRegisterBlock(final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory) {
    throw new UnsupportedOperationException();
  }

  @Invoker
  static Item callRegisterBlock(final Block block, final BiFunction<Block, Item.Properties, Item> itemFactory, final Item.Properties properties) {
    throw new UnsupportedOperationException();
  }
}
