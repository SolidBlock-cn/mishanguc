package pers.solid.mishang.uc.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.explosion.Explosion;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.function.Consumer;

public record ExplosionToolComponent(float power, boolean createFire, Explosion.DestructionType destructionType) implements TooltipAppender {
  public static final ExplosionToolComponent DEFAULT = new ExplosionToolComponent(4, false, Explosion.DestructionType.DESTROY);

  public static final Codec<Explosion.DestructionType> DESTRUCTION_TYPE_CODEC = Codec.STRING.xmap(s -> switch (s) {
    case "none", "keep" -> Explosion.DestructionType.KEEP;
    case "trigger_block" -> Explosion.DestructionType.TRIGGER_BLOCK;
    case "destroy_with_decay" -> Explosion.DestructionType.DESTROY_WITH_DECAY;
    default -> Explosion.DestructionType.DESTROY;
  }, s -> switch (s) {
    case KEEP -> "keep";
    case DESTROY -> "destroy";
    case DESTROY_WITH_DECAY -> "destroy_with_decay";
    case TRIGGER_BLOCK -> "trigger_block";
  });
  public static final Codec<ExplosionToolComponent> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.floatRange(-128, 128).optionalFieldOf("power", 4f).forGetter(ExplosionToolComponent::power), Codec.BOOL.optionalFieldOf("create_fire", false).forGetter(ExplosionToolComponent::createFire), DESTRUCTION_TYPE_CODEC.optionalFieldOf("destruction_type", Explosion.DestructionType.DESTROY).forGetter(ExplosionToolComponent::destructionType)).apply(i, ExplosionToolComponent::new));
  public static final PacketCodec<RegistryByteBuf, ExplosionToolComponent> PACKET_CODEC = PacketCodec.tuple(PacketCodecs.FLOAT, ExplosionToolComponent::power, PacketCodecs.BOOL, ExplosionToolComponent::createFire, PacketCodec.of((value, buf) -> buf.writeEnumConstant(value), buf -> buf.readEnumConstant(Explosion.DestructionType.class)), ExplosionToolComponent::destructionType, ExplosionToolComponent::new);

  @Override
  public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
    tooltip.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.power", TextBridge.literal(String.valueOf(power)).formatted(Formatting.YELLOW)).formatted(Formatting.GRAY));
    tooltip.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.createFire", createFire ? ScreenTexts.YES.copy().formatted(Formatting.GREEN) : ScreenTexts.NO.copy().formatted(Formatting.RED)).formatted(Formatting.GRAY));
    tooltip.accept(TextBridge.translatable("item.mishanguc.explosion_tool.tooltip.destructionType", TextBridge.translatable("item.mishanguc.explosion_tool.destructionType." + destructionType.name().toLowerCase()).styled(style -> style.withColor(0x779999))).formatted(Formatting.GRAY));
  }

  public ExplosionToolComponent withPower(float power) {
    return new ExplosionToolComponent(power, this.createFire, this.destructionType);
  }
}
