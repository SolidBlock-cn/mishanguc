package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Direction;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.Mishanguc;
import pers.solid.mishang.uc.text.TextContext;
import pers.solid.mishang.uc.util.ColorMixtureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see net.minecraft.component.DataComponentTypes
 */
public final class MishangucComponents {
  public static final ComponentType<CarryingToolData> CARRYING_TOOL_DATA = register("carrying_tool_data", CarryingToolData.CODEC, CarryingToolData.PACKET_CODEC);

  /**
   * 颜色工具需要应用的颜色。
   */
  public static final ComponentType<Integer> COLOR = register("color", MishangUtils.COLOR_CODEC, PacketCodecs.INTEGER);

  /**
   * 颜色工具应用颜色的不透明度，对所有颜色混合类型都有效。
   */
  public static final ComponentType<Float> OPACITY = register("opacity", Codec.FLOAT, PacketCodecs.FLOAT);

  /**
   * 颜色工具的颜色混合类型。
   */
  public static final ComponentType<ColorMixtureType> COLOR_MIXTURE_TYPE = register("color_mixture_type", ColorMixtureType.CODEC, ColorMixtureType.PACKET_CODEC);

  /**
   * 颜色工具对颜色的修改数量，仅限于部分混合类型。
   */
  public static final ComponentType<Float> COLOR_CHANGE_AMOUNT = register("color_change_amount", Codec.FLOAT, PacketCodecs.FLOAT);

  public static final ComponentType<ExplosionToolComponent> EXPLOSION_TOOL_DATA = register("explosion_tool_data", ExplosionToolComponent.CODEC, ExplosionToolComponent.PACKET_CODEC);
  public static final ComponentType<FastBuildingToolData> FAST_BUILDING_TOOL_DATA = register("fast_building_tool_data", FastBuildingToolData.CODEC, FastBuildingToolData.PACKET_CODEC);
  public static final ComponentType<Boolean> INCLUDES_FIELD = register("includes_field", Codec.BOOL, PacketCodecs.BOOL);
  public static final ComponentType<Integer> LENGTH = register("length", Codec.intRange(1, 64), PacketCodecs.INTEGER);
  public static final ComponentType<Integer> STRENGTH = register("strength", Codec.intRange(0, 10), PacketCodecs.INTEGER);
  public static final ComponentType<TextCopyToolComponent> TEXT_COPY_TOOL_PROPERTIES = register("text_copy_tool_properties", TextCopyToolComponent.CODEC, TextCopyToolComponent.PACKET_CODEC);
  public static final ComponentType<List<TextContext>> TEXTS = register("texts", Codec.list(TextContext.CODEC), PacketCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final ComponentType<List<TextContext>> FRONT_TEXTS = register("front_texts", Codec.list(TextContext.CODEC), PacketCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final ComponentType<List<TextContext>> BACK_TEXTS = register("back_texts", Codec.list(TextContext.CODEC), PacketCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC));
  public static final ComponentType<Map<Direction, List<TextContext>>> TEXT_MAP = register("text_map", Codec.unboundedMap(Direction.CODEC, Codec.list(TextContext.CODEC)), PacketCodecs.map(HashMap::new, Direction.PACKET_CODEC, PacketCodecs.collection(ArrayList::new, TextContext.PACKET_CODEC)));

  private MishangucComponents() {
  }

  private static <T> ComponentType<T> register(String id, Codec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec) {
    return Registry.register(Registries.DATA_COMPONENT_TYPE, Mishanguc.id(id), ComponentType.<T>builder().codec(codec).packetCodec(packetCodec).build());
  }
}
