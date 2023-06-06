package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.util.TextBridge;

/**
 * 迷上城建模组新增加的游戏规则。
 *
 * @see GameRules
 */
@ApiStatus.AvailableSince("1.0.0")
public final class MishangucRules {

  public static final GameRules.Key<EnumRule<ToolAccess>> FORCE_PLACING_TOOL_ACCESS = register("force_placing_tool_access", GameRuleFactory.createEnumRule(ToolAccess.CREATIVE_ONLY, (server, rule) -> sync(server, rule, 0)));

  public static final GameRules.Key<EnumRule<ToolAccess>> CARRYING_TOOL_ACCESS = register("carrying_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL, (server, rule) -> sync(server, rule, 1)));

  public static final GameRules.Key<EnumRule<ToolAccess>> EXPLOSION_TOOL_ACCESS = register("explosion_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL));

  public static final GameRules.Key<DoubleRule> ROAD_BOOST_SPEED = register("road_boost_speed", GameRuleFactory.createDoubleRule(1.75, FabricLoader.getInstance().isDevelopmentEnvironment() ? -2 : 0, FabricLoader.getInstance().isDevelopmentEnvironment() ? 50 : 10, (server, rule) -> {
    currentRoadBoostSpeed = rule.get();
    sync(server, rule, 3);
  }));
  /**
   * 这个是需要在客户端与服务器之间进行同步的值。单人游戏中，客户端与服务器共用此字段。在加入专用服务器时，客户端和服务器需要对此字段进行同步。
   */
  @ApiStatus.Internal
  public static double currentRoadBoostSpeed = 1.75;

  private static void sync(MinecraftServer server, GameRules.Rule<?> rule, int type) {
    for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
      sync(rule, type, serverPlayerEntity);
    }
  }

  static void sync(GameRules.Rule<?> rule, int type, ServerPlayerEntity serverPlayerEntity) {
    final PacketByteBuf buf = PacketByteBufs.create();
    buf.writeShort(type);
    if (rule instanceof EnumRule<?>) {
      buf.writeEnumConstant(((EnumRule<?>) rule).get());
    } else if (rule instanceof GameRules.BooleanRule) {
      buf.writeBoolean(((GameRules.BooleanRule) rule).get());
    } else if (rule instanceof DoubleRule) {
      buf.writeDouble(((DoubleRule) rule).get());
    }
    ServerPlayNetworking.send(serverPlayerEntity, new Identifier("mishanguc", "rule_changed"), buf);
  }

  private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> ruleType) {
    return GameRuleRegistry.register("mishanguc:" + name, GameRules.Category.MISC, ruleType);
  }

  static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    final short type = buf.readShort();
    final ToolAccess value = type == 0 || type == 1 ? buf.readEnumConstant(ToolAccess.class) : null;
    final boolean booleanValue = type == 2 && buf.readBoolean();
    final double doubleValue = type == 3 ? buf.readDouble() : Double.NaN;
    client.execute(() -> {
      switch (type) {
        case 0:
          MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.set(value);
          break;
        case 1:
          MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.set(value);
          break;
        case 3:
          if (!Double.isNaN(doubleValue)) MishangucRules.currentRoadBoostSpeed = doubleValue;
          break;
      }
    });
  }

  public enum ToolAccess implements StringIdentifiable {
    ALL {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return true;
      }
    }, CREATIVE_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.isCreative();
      }
    }, OP_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.hasPermissionLevel(2);
      }
    }, CREATIVE_OP_ONLY {
      @Override
      public boolean hasAccess(@Nullable PlayerEntity player) {
        return player != null && player.isCreative() && player.hasPermissionLevel(2);
      }
    };
    private final String name;

    ToolAccess() {
      this.name = name().toLowerCase();
    }

    @Override
    public String asString() {
      return name;
    }

    @Contract(pure = true)
    public abstract boolean hasAccess(@Nullable PlayerEntity player);

    public boolean hasAccess(PlayerEntity player, boolean warn) {
      final boolean hasAccess = hasAccess(player);
      if (warn && !hasAccess && !player.world.isClient) {
        player.sendMessage(createWarnText(), true);
      }
      return hasAccess;
    }

    public MutableText createWarnText() {
      return TextBridge.translatable("message.tool_access", TextBridge.translatable("message.tool_access." + asString())).formatted(Formatting.RED);
    }
  }
}
