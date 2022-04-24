package pers.solid.mishang.uc.block;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;

import java.util.List;

public class ColoredHungSignBarBlock extends HungSignBarBlock implements BlockEntityProvider {
  public ColoredHungSignBarBlock(@Nullable Block baseBlock, Settings settings) {
    super(baseBlock, settings);
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new Entity(pos, state);
  }

  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    final ItemStack stack = super.getPickStack(world, pos, state);
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
      stack.getOrCreateSubNbt("BlockEntityTag").putInt("color", coloredBlockEntity.getColor());
    }
    return stack;
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    ColoredBlockEntity.appendColorTooltip(stack, tooltip);
  }

  public static class Entity extends BlockEntity implements ColoredBlockEntity {

    public Entity(BlockPos pos, BlockState state) {
      super(MishangucBlockEntities.COLORED_HUNG_SIGN_BAR_BLOCK_ENTITY, pos, state);
    }

    public int color = ColoredBlockEntity.getDefaultColorFromPos(pos);

    @Override
    public int getColor() {
      return color;
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
      super.writeNbt(nbt);
      nbt.putInt("color", color);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
      super.readNbt(nbt);
      color = nbt.contains("color", NbtType.NUMBER) ? nbt.getInt("color") : ColoredBlockEntity.getDefaultColorFromPos(pos);
      if (world != null) {
        world.updateListeners(pos, getCachedState(), getCachedState(), 3);
      }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
      return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
      return createNbt();
    }
  }
}
