package pers.solid.mishang.uc.text;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.IdentifierException;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.Mishanguc;

/**
 * SpecialDrawableType（特殊可渲染内容类型）表示一个 SpecialDrawable 的类型，可以指定一个 id 以及如何根据字符串参数或者 NBT 进行反序列化。SpecialDrawableType 拥有注册表。
 */
@ApiStatus.AvailableSince("0.2.4")
public interface SpecialDrawableType<S extends SpecialDrawable> {
  ResourceKey<Registry<SpecialDrawableType<? extends SpecialDrawable>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Mishanguc.id("special_drawable_type"));
  /**
   * SpecialDrawableType 的注册表。
   */
  MappedRegistry<SpecialDrawableType<? extends SpecialDrawable>> REGISTRY = FabricRegistryBuilder.create(REGISTRY_KEY).buildAndRegister();

  /**
   * 根据已注册的 id 查询对象，如果不存在则返回 {@code null}。
   */
  static @Nullable SpecialDrawableType<? extends SpecialDrawable> fromId(Identifier id) {
    return REGISTRY.getValue(id);
  }

  /**
   * 根据已注册的 id 查询对象。这个 id 字符串如果没有指定命名空间，则默认为 {@code mishanguc}。
   *
   * @return null 如果 id 有效但并不存在。
   * @throws IdentifierException 如果这个 id 是无效的。
   */
  static @Nullable SpecialDrawableType<? extends SpecialDrawable> fromId(String id) throws IdentifierException {
    int i = id.indexOf(':');
    if (i >= 0) {
      // id 中有冒号的情况，使用指定的命名空间。
      return fromId(Identifier.parse(id));
    } else {
      // id 中没有冒号的情况，使用默认命名空间。
      return fromId(Mishanguc.id(id));
    }
  }

  /**
   * 根据已注册的 id 查询对象。这个 id 字符串如果没有指定命名空间，则默认为 {@code mishanguc}。
   *
   * @return null 如果 id 无效，或者有效但不存在。
   */
  static @Nullable SpecialDrawableType<? extends SpecialDrawable> tryFromId(String id) {
    try {
      return fromId(id);
    } catch (IdentifierException ignore) {
      return null;
    }
  }

  /**
   * 根据注册表查询该对象的 id。如果不存在，则返回 {@code null}。
   */
  default Identifier getId() {
    return REGISTRY.getKey(this);
  }

  /**
   * 根据已有的 TextContext 和一段 nbt，返回一个该类型的 SpecialDrawable 对象。通常来说，已经确保该 nbt 的 id 字段是符合该对象的 id 的。
   *
   * @return 该类型的 SpecialDrawable 对象。
   */
  @Contract(pure = true)
  @Nullable S fromNbt(TextContext textContext, CompoundTag nbt);

  /**
   * 根据已有的参数（字符串形式的）返回对象，通常用于告示牌编辑界面中。如果在文本框中输入 {@code -rect 2 3}，则会调用 {@code fromStringArgs(textContext, "2 3")}。
   */
  @Contract(pure = true)
  S fromStringArgs(TextContext textContext, String args) throws CommandSyntaxException;
}
