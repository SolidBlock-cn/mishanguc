package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.block.HungSignBlock;
import pers.solid.mishang.uc.util.TextContext;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @see pers.solid.mishang.uc.block.HungSignBlock
 * @see pers.solid.mishang.uc.renderer.HungSignBlockEntityRenderer
 */
public class HungSignBlockEntity extends BlockEntityWithText {
  /**
   * 该方块正在被编辑的方向。同时存在于客户端与服务器。<br>
   * 若未被编辑则为 {@code null}。
   */
  public @Nullable Direction editedSide;

  /** 编辑该方块的玩家。若为 <code>true</code>，则其他玩家不可编辑。 */
  public @Nullable PlayerEntity editor;

  public HungSignBlockEntity(BlockPos pos, BlockState state) {
    super(MishangucBlockEntities.HUNG_SIGN_BLOCK_ENTITY, pos, state);
  }

  public static final TextContext DEFAULT_TEXT_CONTEXT =
      Util.make(new TextContext(), textContext1 -> textContext1.size = 6);
  public Map<@NotNull Direction, @NotNull List<@NotNull TextContext>> texts = ImmutableMap.of();

  @Override
  public void readNbt(NbtCompound nbt) {
    super.readNbt(nbt);
    ImmutableMap.Builder<Direction, List<TextContext>> builder = new ImmutableMap.Builder<>();
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final NbtElement element = nbt.get(direction.asString());
      if (element instanceof NbtString || element instanceof NbtCompound) {
        builder.put(
            direction, ImmutableList.of(TextContext.fromNbt(element, DEFAULT_TEXT_CONTEXT)));
      } else if (element instanceof NbtList) {
        builder.put(
            direction,
            ((NbtList) element).stream().map(TextContext::fromNbt).collect(Collectors.toList()));
      }
    }
    texts = builder.build();
  }

  @Override
  public NbtCompound writeNbt(NbtCompound nbt) {
    super.writeNbt(nbt);
    for (Direction direction : Direction.Type.HORIZONTAL) {
      final List<@NotNull TextContext> textContexts = texts.get(direction);
      if (textContexts == null || direction.getAxis() != getCachedState().get(HungSignBlock.AXIS)) {
        continue;
      }
      final NbtList nbtList = new NbtList();
      for (TextContext textContext : textContexts) {
        nbtList.add(textContext.writeNbt(new NbtCompound()));
      }
      nbt.put(direction.asString(), nbtList);
    }
    return nbt;
  }
}
