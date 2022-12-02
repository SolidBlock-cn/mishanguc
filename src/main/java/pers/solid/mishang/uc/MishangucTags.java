package pers.solid.mishang.uc;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.AvailableSince("1.0.4")
public final class MishangucTags {
  public static final TagKey<Block> CENTRAL_HANDRAILS = TagKey.of(RegistryKeys.BLOCK, new Identifier("mishanguc", "central_handrails"));

  private MishangucTags() {
  }
}
