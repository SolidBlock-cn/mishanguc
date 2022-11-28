package pers.solid.mishang.uc;

import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("1.0.4")
public final class MishangucTags {
  public static final TagKey<Block> CENTRAL_HANDRAILS = TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "central_handrails"));

  private MishangucTags() {
  }
}
