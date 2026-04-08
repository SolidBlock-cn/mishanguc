package pers.solid.mishang.uc.text;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.IdentifierException;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;

public final class SpecialDrawableTypes {
  private SpecialDrawableTypes() {
  }

  public static final SpecialDrawableType<SpecialDrawable> INVALID = register("invalid", (textContext, nbtCompound) -> SpecialDrawable.INVALID, (textContext, s) -> SpecialDrawable.INVALID);

  public static final SpecialDrawableType<DebugTextSpecialDrawable> DEBUG_TEXT = register("debug_text", (textContext, nbtCompound) -> new DebugTextSpecialDrawable(nbtCompound.getStringOr("text", "debug_text"), textContext), (textContext, s) -> new DebugTextSpecialDrawable(s, textContext));

  public static final SpecialDrawableType<RectSpecialDrawable> RECT = register("rect", RectSpecialDrawable::fromNbt, RectSpecialDrawable::fromStringArgs);

  public static final SpecialDrawableType<PatternSpecialDrawable> PATTERN = register("pattern", PatternSpecialDrawable::fromNbt, (textContext, args) -> {
    final PatternSpecialDrawable pattern = PatternSpecialDrawable.fromName(textContext, args);
    if (pattern == null) {
      throw new CommandSyntaxException(null, Component.translatable("special_drawable.pattern.invalid_name", args));
    } else {
      return pattern;
    }
  });

  public static final SpecialDrawableType<TextureSpecialDrawable> TEXTURE = register("texture", (textContext, nbt) -> {
    final Identifier texture = Identifier.tryParse(nbt.getStringOr("texture", null));
    return texture != null && TextureSpecialDrawable.isValidIdentifier(texture) ? new TextureSpecialDrawable(texture, textContext) : null;
  }, (textContext, args) -> {
    final Identifier identifier;
    try {
      identifier = Identifier.parse(args);
    } catch (IdentifierException e) {
      throw Identifier.ERROR_INVALID.create();
    }
    if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
      try {
        TextureSpecialDrawable.validateIdentifier(identifier);
      } catch (IllegalArgumentException e) {
        throw new CommandSyntaxException(null, Component.literal(e.getMessage()));
      }
      return new TextureSpecialDrawable(identifier, textContext);
    }
    throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.dispatcherUnknownArgument().create();
  });

  private static <T extends SpecialDrawableType<? extends SpecialDrawable>> T register(String namePath, T specialDrawable) {
    return Registry.register(SpecialDrawableType.REGISTRY, Mishanguc.id(namePath), specialDrawable);
  }

  private static <S extends SpecialDrawable> SpecialDrawableType<S> register(String namePath, FromNbt<S> fromNbt, FromStringArgs<S> fromStringArgs) {
    return register(namePath, new Simple<>(fromNbt, fromStringArgs));
  }

  private record Simple<S extends SpecialDrawable>(FromNbt<S> fromNbt, FromStringArgs<S> fromStringArgs) implements SpecialDrawableType<S> {

    @Override
    public @Nullable S fromNbt(TextContext textContext, CompoundTag nbt) {
      return fromNbt.fromNbt(textContext, nbt);
    }

    @Override
    public S fromStringArgs(TextContext textContext, String args) throws CommandSyntaxException {
      return fromStringArgs.fromStringArgs(textContext, args);
    }
  }

  @SuppressWarnings("EmptyMethod")
  public static void init() {
  }

  @FunctionalInterface
  public interface FromNbt<S extends SpecialDrawable> {
    @Nullable S fromNbt(TextContext textContext, CompoundTag nbt);
  }

  @FunctionalInterface
  public interface FromStringArgs<S> {
    S fromStringArgs(TextContext textContext, String args) throws CommandSyntaxException;
  }
}
