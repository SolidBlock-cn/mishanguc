package pers.solid.mishang.uc.text;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.function.BiFunction;

public final class SpecialDrawableTypes {
  private SpecialDrawableTypes() {
  }

  public static final SpecialDrawableType<SpecialDrawable> INVALID = register("invalid", (textContext, nbtCompound) -> SpecialDrawable.INVALID, (textContext, s) -> SpecialDrawable.INVALID);

  public static final SpecialDrawableType<RectSpecialDrawable> RECT = register("rect", RectSpecialDrawable::fromNbt, RectSpecialDrawable::fromStringArgs);

  public static final SpecialDrawableType<PatternSpecialDrawable> PATTERN = register("pattern", PatternSpecialDrawable::fromNbt, (textContext, args) -> {
    final PatternSpecialDrawable pattern = PatternSpecialDrawable.fromName(textContext, args);
    if (pattern.isEmpty()) {
      return null;
    } else {
      return pattern;
    }
  });

  public static final SpecialDrawableType<TextureSpecialDrawable> TEXTURE = register("texture", (textContext, nbt) -> {
    final Identifier texture = Identifier.tryParse(nbt.getString("texture"));
    return texture != null ? new TextureSpecialDrawable(texture, textContext) : null;
  }, (textContext, args) -> {
    final Identifier identifier = Identifier.tryParse(args);
    if (identifier == null) {
      return null;
    } else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      // 考虑到输入过程中，还未输入完成时会抛出纹理不存在的错误，故在这里先进行抑制。
      try (final ResourceTexture resourceTexture = new ResourceTexture(identifier)) {
        resourceTexture.load(MinecraftClient.getInstance().getResourceManager());
        return new TextureSpecialDrawable(identifier, textContext);
      } catch (IOException e) {
        return null;
      }
    }
    return null;
  });

  private static <T extends SpecialDrawableType<? extends SpecialDrawable>> T register(String namePath, T specialDrawable) {
    return Registry.register(SpecialDrawableType.REGISTRY, Identifier.of("mishanguc", namePath), specialDrawable);
  }

  private static <S extends SpecialDrawable> SpecialDrawableType<S> register(String namePath, BiFunction<TextContext, NbtCompound, S> fromNbt, BiFunction<TextContext, String, S> fromStringArgs) {
    return register(namePath, new Simple<>(fromNbt, fromStringArgs));
  }

  private record Simple<S extends SpecialDrawable>(BiFunction<TextContext, NbtCompound, S> fromNbt, BiFunction<TextContext, String, S> fromStringArgs) implements SpecialDrawableType<S> {

    @Override
    public S fromNbt(TextContext textContext, @NotNull NbtCompound nbt) {
      return fromNbt.apply(textContext, nbt);
    }

    @Override
    public S fromStringArgs(TextContext textContext, String args) {
      return fromStringArgs.apply(textContext, args);
    }
  }

  @SuppressWarnings("EmptyMethod")
  public static void init() {
  }
}
