package pers.solid.mishang.uc;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
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

  public static final GameRules.Key<EnumRule<ToolAccess>> FORCE_PLACING_TOOL_ACCESS = register("force_placing_tool_access", GameRuleFactory.createEnumRule(ToolAccess.CREATIVE_ONLY, (server, rule) -> sync(server, rule.get(), 0)));

  public static final GameRules.Key<EnumRule<ToolAccess>> CARRYING_TOOL_ACCESS = register("carrying_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL, (server, rule) -> sync(server, rule.get(), 1)));

  public static final GameRules.Key<EnumRule<ToolAccess>> EXPLOSION_TOOL_ACCESS = register("explosion_tool_access", GameRuleFactory.createEnumRule(ToolAccess.ALL));

  private static void sync(MinecraftServer server, ToolAccess toolAccess, int type) {
    for (ServerPlayerEntity serverPlayerEntity : server.getPlayerManager().getPlayerList()) {
      sync(toolAccess, type, serverPlayerEntity);
    }
  }

  static void sync(ToolAccess toolAccess, int type, ServerPlayerEntity serverPlayerEntity) {
    ServerPlayNetworking.send(serverPlayerEntity, new Identifier("mishanguc", "rule_changed"), PacketByteBufs.create().writeEnumConstant(toolAccess).writeVarInt(type));
  }

  private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Type<T> ruleType) {
    return GameRuleRegistry.register("mishanguc:" + name, GameRules.Category.MISC, ruleType);
  }

  static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
    final ToolAccess value = buf.readEnumConstant(ToolAccess.class);
    final int type = buf.readVarInt();
    client.execute(() -> {
      switch (type) {
        case 0:
          MishangucClient.CLIENT_FORCE_PLACING_TOOL_ACCESS.set(value);
          break;
        case 1:
          MishangucClient.CLIENT_CARRYING_TOOL_ACCESS.set(value);
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
