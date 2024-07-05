import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;

public class Main {
  private static int t() {
    System.out.println("t called!");
    return 32;
  }

  private static int getT() {
    return Suppliers.memoize(new Supplier<Integer>() {
      @Override
      public Integer get() {
        return t();
      }
    }).get();
  }

  public static void main(String[] args) {
    // serializing a BlockPos
    final BlockPos blockPos = new BlockPos(1, 2, 3);
    final DataResult<NbtElement> encodeResult = BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, blockPos);

    // deserializing a BlockPos

    // get the result when succeed
    final NbtList nbtList = new NbtList();
    nbtList.add(NbtInt.of(1));
    nbtList.add(NbtInt.of(2));
    nbtList.add(NbtInt.of(3));
    final DataResult<Pair<BlockPos, NbtElement>> result1 = BlockPos.CODEC.decode(NbtOps.INSTANCE, nbtList);
    System.out.println(result1.getOrThrow().getFirst());

    // get the result when error
    final NbtString nbtString = NbtString.of("can't decode me");
    final DataResult<Pair<BlockPos, NbtElement>> result2 = BlockPos.CODEC.decode(NbtOps.INSTANCE, nbtString);
    System.out.println(result2.error().get().message());

    Codec<Identifier> identifierCodec = Codec.STRING.comapFlatMap(s -> {
      try {
        return DataResult.success(Identifier.of(s));
      } catch (InvalidIdentifierException e) {
        return DataResult.error(() -> "The identifier is invalid:" + e.getMessage());
      }
    }, identifier -> identifier.toString());
  }
}
