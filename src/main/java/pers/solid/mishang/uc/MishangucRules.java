package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
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
  public static final GameRules.Key<GameRules.BooleanRule> WARN_DEPRECATED_VERSION = register("warn_deprecated_version", GameRuleFactory.createBooleanRule(true));

  public static final GameRules.Key<EnumRule<ToolAccess>> FORCE_PLACING_TOOL_ACCESS = register("force_placing_tool_access", GameRuleFactory.createEnumRule(ToolAccess.CREATIVE_ONLY, (server, rule) -> sync(server, rule, 0)));

  public static final GameRules.Key<EnumRule<ToolAccess>> CARRYING_TOOL_ACCESS = register("carrying_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL, (server, rule) -> sync(server, rule, 1)));

  public static final GameRules.Key<EnumRule<ToolAccess>> EXPLOSION_TOOL_ACCESS = register("explosion_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL));

  public static final GameRules.Key<GameRules.BooleanRule> SUSPENDS_BLOCK_LIGHT_UPDATE = FabricLoader.getInstance().isDevelopmentEnvironment() ? register("suspends_block_light_update", GameRuleFactory.createBooleanRule(false, (minecraftServer, booleanRule) -> sync(minecraftServer, booleanRule, 2))) : null;

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
    }
    ServerPlayNetworking.send(serverPlayerEntity, new Identifier("mishanguc", "rule_changed"), buf);
  }

  private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> ruleType) {
    return GameRuleRegistry.register("mishanguc:" + name, GameRules.Category.MISC, ruleType);
  }

  static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    final short type = buf.readShort();
    final ToolAccess value = type != 2 ? buf.readEnumConstant(ToolAccess.class) : null;
    final boolean booleanValue = type == 2 && buf.readBoolean();
    client.execute(() -> {
      switch (type) {
        case 0:
          MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.set(value);
          break;
        case 1:
          MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.set(value);
          break;
        case 2:
          if (MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE != null) {
            MishangucClient.CLIENT_SUSPENDS_LIGHT_UPDATE.set(booleanValue);
          }
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
