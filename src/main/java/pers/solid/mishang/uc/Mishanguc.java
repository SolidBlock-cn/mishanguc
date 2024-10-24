package pers.solid.mishang.uc;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.GlassHandrailBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.ColoredBlockEntity;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.*;
import pers.solid.mishang.uc.networking.*;
import pers.solid.mishang.uc.text.SpecialDrawableTypes;
import pers.solid.mishang.uc.util.BlockMatchingRule;
import pers.solid.mishang.uc.util.ColorfulBlockRegistry;

import java.util.Collection;
import java.util.Map;

public class Mishanguc implements ModInitializer {
  public static final Logger MISHANG_LOGGER = LoggerFactory.getLogger("Mishang Urban Construction");
  /**
   * 比 {@link AttackBlockCallback#EVENT} 更好！
   */
  public static final Event<AttackBlockCallback> BEGIN_ATTACK_BLOCK_EVENT =
      EventFactory.createArrayBacked(
          AttackBlockCallback.class,
          (listeners) ->
              (player, world, hand, pos, direction) -> {
                for (AttackBlockCallback event : listeners) {
                  ActionResult result = event.interact(player, world, hand, pos, direction);

                  if (result != ActionResult.PASS) {
                    return result;
                  }
                }
                return ActionResult.PASS;
              });

  public static final Event<AttackBlockCallback> PROGRESS_ATTACK_BLOCK_EVENT =
      EventFactory.createArrayBacked(
          AttackBlockCallback.class,
          (listeners) ->
              (player, world, hand, pos, direction) -> {
                for (AttackBlockCallback event : listeners) {
                  ActionResult result = event.interact(player, world, hand, pos, direction);
                  if (result != ActionResult.PASS) {
                    return result;
                  }
                }
                return ActionResult.PASS;
              });

  private static final @NotNull Identifier EXAMPLE_ID = Identifier.of("mishanguc", "");

  /**
   * 创建使用本模组命名空间（{@code mishanguc}）的 ID。此方法可以提高不同版本之间的兼容性，同时在部分版本中避免命名空间的冗余校验。
   *
   * @return 使用本模组命名空间（{@code mishanguc}）的 ID。
   */
  public static @NotNull Identifier id(@NotNull String path) {
    return EXAMPLE_ID.withPath(path);
  }

  private static void registerCommands() {
  }

  private static void registerFlammableAndFuels() {
    // 注册可燃方块
    final FlammableBlockRegistry flammableBlockRegistry = FlammableBlockRegistry.getDefaultInstance();
    final FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;

    final Block[] woodenBlocks = {
        HungSignBlocks.OAK_HUNG_SIGN,
        HungSignBlocks.SPRUCE_HUNG_SIGN,
        HungSignBlocks.BIRCH_HUNG_SIGN,
        HungSignBlocks.JUNGLE_HUNG_SIGN,
        HungSignBlocks.ACACIA_HUNG_SIGN,
        HungSignBlocks.CHERRY_HUNG_SIGN,
        HungSignBlocks.DARK_OAK_HUNG_SIGN,
        HungSignBlocks.MANGROVE_HUNG_SIGN,
        HungSignBlocks.OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.SPRUCE_WOOD_HUNG_SIGN,
        HungSignBlocks.BIRCH_WOOD_HUNG_SIGN,
        HungSignBlocks.JUNGLE_WOOD_HUNG_SIGN,
        HungSignBlocks.ACACIA_WOOD_HUNG_SIGN,
        HungSignBlocks.CHERRY_WOOD_HUNG_SIGN,
        HungSignBlocks.DARK_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.MANGROVE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_SPRUCE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_BIRCH_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_JUNGLE_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_ACACIA_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_CHERRY_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_DARK_OAK_WOOD_HUNG_SIGN,
        HungSignBlocks.STRIPPED_MANGROVE_WOOD_HUNG_SIGN,
        HungSignBlocks.BAMBOO_HUNG_SIGN,
        HungSignBlocks.BAMBOO_PLANK_HUNG_SIGN,
        HungSignBlocks.BAMBOO_MOSAIC_HUNG_SIGN,
        HungSignBlocks.OAK_HUNG_SIGN_BAR,
        HungSignBlocks.SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.CHERRY_HUNG_SIGN_BAR,
        HungSignBlocks.DARK_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.MANGROVE_HUNG_SIGN_BAR,
        HungSignBlocks.BAMBOO_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_CHERRY_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_DARK_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.STRIPPED_MANGROVE_HUNG_SIGN_BAR,
        WallSignBlocks.OAK_WOOD_WALL_SIGN,
        WallSignBlocks.SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.OAK_WALL_SIGN,
        WallSignBlocks.SPRUCE_WALL_SIGN,
        WallSignBlocks.BIRCH_WALL_SIGN,
        WallSignBlocks.JUNGLE_WALL_SIGN,
        WallSignBlocks.ACACIA_WALL_SIGN,
        WallSignBlocks.CHERRY_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WALL_SIGN,
        WallSignBlocks.MANGROVE_WALL_SIGN,
        WallSignBlocks.OAK_WOOD_WALL_SIGN,
        WallSignBlocks.SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_SPRUCE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_BIRCH_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_JUNGLE_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_ACACIA_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_CHERRY_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_DARK_OAK_WOOD_WALL_SIGN,
        WallSignBlocks.STRIPPED_MANGROVE_WOOD_WALL_SIGN,
        WallSignBlocks.BAMBOO_WALL_SIGN,
        WallSignBlocks.BAMBOO_PLANK_WALL_SIGN,
        WallSignBlocks.BAMBOO_MOSAIC_WALL_SIGN,
        WallSignBlocks.COLORED_WOODEN_WALL_SIGN,
        StandingSignBlocks.OAK_STANDING_SIGN,
        StandingSignBlocks.SPRUCE_STANDING_SIGN,
        StandingSignBlocks.BIRCH_STANDING_SIGN,
        StandingSignBlocks.ACACIA_STANDING_SIGN,
        StandingSignBlocks.CHERRY_STANDING_SIGN,
        StandingSignBlocks.JUNGLE_STANDING_SIGN,
        StandingSignBlocks.DARK_OAK_STANDING_SIGN,
        StandingSignBlocks.MANGROVE_STANDING_SIGN,
        StandingSignBlocks.OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.SPRUCE_WOOD_STANDING_SIGN,
        StandingSignBlocks.BIRCH_WOOD_STANDING_SIGN,
        StandingSignBlocks.ACACIA_WOOD_STANDING_SIGN,
        StandingSignBlocks.CHERRY_WOOD_STANDING_SIGN,
        StandingSignBlocks.JUNGLE_WOOD_STANDING_SIGN,
        StandingSignBlocks.DARK_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.MANGROVE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_SPRUCE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_BIRCH_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_ACACIA_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_CHERRY_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_JUNGLE_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_DARK_OAK_WOOD_STANDING_SIGN,
        StandingSignBlocks.STRIPPED_MANGROVE_WOOD_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_PLANK_STANDING_SIGN,
        StandingSignBlocks.BAMBOO_MOSAIC_STANDING_SIGN
    };
    for (Block block : woodenBlocks) {
      flammableBlockRegistry.add(block, 5, 20);
      fuelRegistry.add(block, 100);
    }
    final Collection<HandrailBlock> woodenHandrails = ImmutableSet.of(
        HandrailBlocks.SIMPLE_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_HANDRAIL,
        HandrailBlocks.SIMPLE_CHERRY_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_MANGROVE_HANDRAIL,
        HandrailBlocks.SIMPLE_OAK_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_CHERRY_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_MANGROVE_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_PLANK_HANDRAIL,
        HandrailBlocks.SIMPLE_BAMBOO_MOSAIC_HANDRAIL,
        HandrailBlocks.GLASS_OAK_HANDRAIL,
        HandrailBlocks.GLASS_SPRUCE_HANDRAIL,
        HandrailBlocks.GLASS_BIRCH_HANDRAIL,
        HandrailBlocks.GLASS_JUNGLE_HANDRAIL,
        HandrailBlocks.GLASS_ACACIA_HANDRAIL,
        HandrailBlocks.GLASS_CHERRY_HANDRAIL,
        HandrailBlocks.GLASS_DARK_OAK_HANDRAIL,
        HandrailBlocks.GLASS_MANGROVE_HANDRAIL,
        HandrailBlocks.GLASS_BAMBOO_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_BAMBOO_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_SPRUCE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BIRCH_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_JUNGLE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_ACACIA_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_CHERRY_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_DARK_OAK_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_MANGROVE_HANDRAIL,
        HandrailBlocks.COLORED_DECORATED_STRIPPED_BAMBOO_HANDRAIL
    );
    for (HandrailBlock handrail : woodenHandrails) {
      flammableBlockRegistry.add(handrail, 5, 20);
      flammableBlockRegistry.add(handrail.central(), 5, 20);
      flammableBlockRegistry.add(handrail.corner(), 5, 20);
      flammableBlockRegistry.add(handrail.outer(), 5, 20);
      flammableBlockRegistry.add(handrail.stair(), 5, 20);
      fuelRegistry.add(handrail, 100);
      fuelRegistry.add(handrail.central(), 100);
      fuelRegistry.add(handrail.corner(), 100);
      fuelRegistry.add(handrail.outer(), 100);
      fuelRegistry.add(handrail.stair(), 100);
    }

    flammableBlockRegistry.add(ColoredBlocks.COLORED_PLANKS, 5, 20);
    fuelRegistry.add(ColoredBlocks.COLORED_PLANKS, 300);
    flammableBlockRegistry.add(ColoredBlocks.COLORED_PLANK_STAIRS, 5, 20);
    fuelRegistry.add(ColoredBlocks.COLORED_PLANK_STAIRS, 300);
    flammableBlockRegistry.add(ColoredBlocks.COLORED_PLANK_SLAB, 5, 20);
    fuelRegistry.add(ColoredBlocks.COLORED_PLANK_SLAB, 150);
    flammableBlockRegistry.add(ColoredBlocks.COLORED_WOOL, 30, 60);
    fuelRegistry.add(ColoredBlocks.COLORED_WOOL, 100);
  }

  private static void registerNetworkingReceiver() {
    // 注册服务器接收
    PayloadTypeRegistry.playC2S().register(SignEditFinishPayload.ID, SignEditFinishPayload.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(SignEditFinishPayload.ID, BlockEntityWithText.PACKET_HANDLER);
    PayloadTypeRegistry.playC2S().register(ItemScrollPayload.ID, ItemScrollPayload.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(ItemScrollPayload.ID, (payload, context) -> {
      final int selectedSlot = payload.selectedSlot();
      final double scrollAmount = payload.scrollAmount();
      final ServerPlayerEntity player = context.player();
      player.server.execute(() -> {
        final ItemStack stack = player.getInventory().getStack(selectedSlot);
        if (stack.getItem() instanceof HotbarScrollInteraction interaction) {
          interaction.onScroll(selectedSlot, scrollAmount, player, stack);
        }
      });
    });
    PayloadTypeRegistry.playC2S().register(SlabToolPayload.ID, SlabToolPayload.CODEC);
    ServerPlayNetworking.registerGlobalReceiver(SlabToolPayload.ID, SlabToolItem.Handler.INSTANCE);
    PayloadTypeRegistry.playS2C().register(EditSignPayload.ID, EditSignPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(GetBlockDataPayload.ID, GetBlockDataPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(GetEntityDataPayload.ID, GetEntityDataPayload.CODEC);
    PayloadTypeRegistry.playS2C().register(RuleChangedPayload.ID, RuleChangedPayload.CODEC);
  }

  private static void registerEvents() {
    // 注册事件
    BEGIN_ATTACK_BLOCK_EVENT.register(
        // 仅限客户端执行
        (player, world, hand, pos, direction) -> {
          if (!world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getMainHandStack();
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            return blockToolItem.beginAttackBlock(stack, player, world, hand, pos, direction, blockToolItem.includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });

    PROGRESS_ATTACK_BLOCK_EVENT.register(
        // 仅限客户端执行
        (player, world, hand, pos, direction) -> {
          if (!world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getStackInHand(hand);
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            final BlockHitResult hitResult = (BlockHitResult) player.raycast(5, 0, blockToolItem.includesFluid(stack, player.isSneaking()));
            return blockToolItem.progressAttackBlock(player, world, hand, hitResult.getBlockPos(), hitResult.getSide(), blockToolItem.includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    AttackBlockCallback.EVENT.register(
        // 仅限服务器执行
        (player, world, hand, pos, direction) -> {
          if (world.isClient || player.isSpectator()) {
            return ActionResult.PASS;
          }
          final ItemStack stack = player.getStackInHand(hand);
          final Item item = stack.getItem();
          if (item instanceof final BlockToolItem blockToolItem) {
            return blockToolItem.beginAttackBlock(stack, player, world, hand, pos, direction, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          if (player.isSpectator()) return ActionResult.PASS;
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (!player.getAbilities().allowModifyWorld && !stackInHand.canPlaceOn(new CachedBlockPosition(world, hitResult.getBlockPos(), false))) {
            return ActionResult.PASS;
          }
          if (item instanceof final BlockToolItem blockToolItem) {
            return blockToolItem.useOnBlock(stackInHand, player, world, hitResult, hand, blockToolItem.includesFluid(stackInHand, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    AttackEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          if (player.isSpectator()) return ActionResult.PASS;
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof final InteractsWithEntity interactsWithEntity) {
            return interactsWithEntity.attackEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
        });
    UseEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
          if (player.isSpectator()) return ActionResult.PASS;
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof final InteractsWithEntity interactsWithEntity) {
            return interactsWithEntity.useEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
        });

    UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
      if (player.isSpectator()) return ActionResult.PASS;
      final ItemStack stack = player.getStackInHand(hand);
      final BlockPos blockPos = hitResult.getBlockPos();
      final BlockState blockState = world.getBlockState(blockPos);
      if (!blockState.isOf(Blocks.WATER_CAULDRON)) {
        return ActionResult.PASS;
      }
      final ItemActionResult result = Road.CLEAN_ROAD_BLOCK.interact(blockState, world, blockPos, player, hand, stack);
      return result.toActionResult();
    });

    UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
      if (player.isSpectator()) {
        return ActionResult.PASS;
      }
      if (hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
        final BlockPos blockPos = blockHitResult.getBlockPos();
        final BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {
          for (Map.Entry<DyeColor, TagKey<Item>> entry : MishangUtils.DYE_ITEM_TAGS.get().entrySet()) {
            final ItemStack stack = player.getStackInHand(hand);
            if (stack.isIn(entry.getValue())) {
              coloredBlockEntity.setColor(entry.getKey().getEntityColor());
              blockEntity.markDirty();
              world.updateListeners(blockPos, blockEntity.getCachedState(), blockEntity.getCachedState(), Block.NOTIFY_LISTENERS);
              stack.decrementUnlessCreative(1, player);
              world.playSound(null, blockPos, SoundEvents.ITEM_DYE_USE, SoundCategory.BLOCKS, 1.0F, 1.0F);
              return ActionResult.SUCCESS;
            }
          }
        }
      }
      return ActionResult.PASS;
    });
  }

  private static void registerColoredBlocks() {
    final Object2ObjectMap<Block, Block> blockMap = ColoredBlock.BASE_TO_COLORED;
    final Object2ObjectMap<TagKey<Block>, Block> tagMap = ColoredBlock.BASE_TAG_TO_COLORED;
    tagMap.put(BlockTags.WOOL, ColoredBlocks.COLORED_WOOL);
    tagMap.put(BlockTags.TERRACOTTA, ColoredBlocks.COLORED_TERRACOTTA);
    blockMap.put(Blocks.WHITE_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.ORANGE_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.MAGENTA_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.LIGHT_BLUE_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.YELLOW_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.LIME_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.PINK_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.GRAY_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.LIGHT_GRAY_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.CYAN_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.PURPLE_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.BLUE_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.BROWN_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.GREEN_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.RED_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    blockMap.put(Blocks.BLACK_CONCRETE, ColoredBlocks.COLORED_CONCRETE);
    tagMap.put(BlockTags.PLANKS, ColoredBlocks.COLORED_PLANKS);
    tagMap.put(BlockTags.WOODEN_STAIRS, ColoredBlocks.COLORED_PLANK_STAIRS);
    tagMap.put(BlockTags.WOODEN_SLABS, ColoredBlocks.COLORED_PLANK_SLAB);
    blockMap.put(Blocks.DIRT, ColoredBlocks.COLORED_DIRT);
    blockMap.put(Blocks.COBBLESTONE, ColoredBlocks.COLORED_COBBLESTONE);
    blockMap.put(Blocks.COBBLESTONE_STAIRS, ColoredBlocks.COLORED_COBBLESTONE_STAIRS);
    blockMap.put(Blocks.COBBLESTONE_SLAB, ColoredBlocks.COLORED_COBBLESTONE_SLAB);
    blockMap.put(Blocks.ANDESITE, ColoredBlocks.COLORED_ANDESITE);
    blockMap.put(Blocks.ANDESITE_STAIRS, ColoredBlocks.COLORED_ANDESITE_STAIRS);
    blockMap.put(Blocks.ANDESITE_SLAB, ColoredBlocks.COLORED_ANDESITE_SLAB);
    blockMap.put(Blocks.DIORITE, ColoredBlocks.COLORED_DIORITE);
    blockMap.put(Blocks.DIORITE_STAIRS, ColoredBlocks.COLORED_DIORITE_STAIRS);
    blockMap.put(Blocks.DIORITE_SLAB, ColoredBlocks.COLORED_DIORITE_SLAB);
    blockMap.put(Blocks.CALCITE, ColoredBlocks.COLORED_CALCITE);
    blockMap.put(Blocks.TUFF, ColoredBlocks.COLORED_TUFF);
    blockMap.put(Blocks.IRON_BLOCK, ColoredBlocks.COLORED_IRON_BLOCK);
    blockMap.put(Blocks.STONE, ColoredBlocks.COLORED_STONE);
    blockMap.put(Blocks.STONE_STAIRS, ColoredBlocks.COLORED_STONE_STAIRS);
    blockMap.put(Blocks.STONE_SLAB, ColoredBlocks.COLORED_STONE_SLAB);
    blockMap.put(Blocks.STONE_BRICKS, ColoredBlocks.COLORED_STONE_BRICKS);
    blockMap.put(Blocks.STONE_BRICK_STAIRS, ColoredBlocks.COLORED_STONE_BRICK_STAIRS);
    blockMap.put(Blocks.STONE_BRICK_SLAB, ColoredBlocks.COLORED_STONE_BRICK_SLAB);
    blockMap.put(Blocks.QUARTZ_BLOCK, ColoredBlocks.COLORED_QUARTZ_BLOCK);
    blockMap.put(Blocks.QUARTZ_STAIRS, ColoredBlocks.COLORED_QUARTZ_STAIRS);
    blockMap.put(Blocks.QUARTZ_SLAB, ColoredBlocks.COLORED_QUARTZ_SLAB);
    blockMap.put(Blocks.CHISELED_QUARTZ_BLOCK, ColoredBlocks.COLORED_CHISELED_QUARTZ_BLOCK);
    blockMap.put(Blocks.QUARTZ_BRICKS, ColoredBlocks.COLORED_QUARTZ_BRICKS);
    blockMap.put(Blocks.SMOOTH_QUARTZ, ColoredBlocks.COLORED_SMOOTH_QUARTZ);
    blockMap.put(Blocks.SMOOTH_QUARTZ_STAIRS, ColoredBlocks.COLORED_SMOOTH_QUARTZ_STAIRS);
    blockMap.put(Blocks.SMOOTH_QUARTZ_SLAB, ColoredBlocks.COLORED_SMOOTH_QUARTZ_SLAB);
    blockMap.put(Blocks.QUARTZ_PILLAR, ColoredBlocks.COLORED_QUARTZ_PILLAR);
    blockMap.put(Blocks.PURPUR_BLOCK, ColoredBlocks.COLORED_PURPUR_BLOCK);
    blockMap.put(Blocks.PURPUR_PILLAR, ColoredBlocks.COLORED_PURPUR_PILLAR);
    blockMap.put(Blocks.END_STONE, ColoredBlocks.COLORED_END_STONE);
    blockMap.put(Blocks.END_STONE_BRICKS, ColoredBlocks.COLORED_END_STONE_BRICKS);
    blockMap.put(Blocks.END_STONE_BRICK_STAIRS, ColoredBlocks.COLORED_END_STONE_BRICK_STAIRS);
    blockMap.put(Blocks.END_STONE_BRICK_SLAB, ColoredBlocks.COLORED_END_STONE_BRICK_SLAB);
    blockMap.put(Blocks.NETHER_PORTAL, ColoredBlocks.COLORED_NETHER_PORTAL);
    blockMap.put(LightBlocks.WHITE_LIGHT, ColoredBlocks.COLORED_LIGHT);
    blockMap.put(LightBlocks.YELLOW_LIGHT, ColoredBlocks.COLORED_LIGHT);
    blockMap.put(LightBlocks.CYAN_LIGHT, ColoredBlocks.COLORED_LIGHT);
    blockMap.put(Blocks.GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.WHITE_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.ORANGE_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.MAGENTA_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.LIGHT_BLUE_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.YELLOW_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.LIME_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.PINK_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.GRAY_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.LIGHT_GRAY_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.CYAN_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.PURPLE_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.BLUE_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.BROWN_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.GREEN_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.RED_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.BLACK_STAINED_GLASS, ColoredBlocks.COLORED_GLASS);
    blockMap.put(Blocks.GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.WHITE_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.ORANGE_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.MAGENTA_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.YELLOW_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.LIME_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.PINK_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.GRAY_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.CYAN_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.PURPLE_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.BLUE_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.BROWN_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.GREEN_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.RED_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.BLACK_STAINED_GLASS_PANE, ColoredBlocks.COLORED_GLASS_PANE);
    blockMap.put(Blocks.ICE, ColoredBlocks.COLORED_ICE);
    blockMap.put(Blocks.SNOW_BLOCK, ColoredBlocks.COLORED_SNOW_BLOCK);
    blockMap.put(Blocks.PACKED_ICE, ColoredBlocks.COLORED_PACKED_ICE);
    blockMap.put(Blocks.OAK_LEAVES, ColoredBlocks.COLORED_OAK_LEAVES);
    blockMap.put(Blocks.DARK_OAK_LEAVES, ColoredBlocks.COLORED_DARK_OAK_LEAVES);
    blockMap.put(Blocks.SPRUCE_LEAVES, ColoredBlocks.COLORED_SPRUCE_LEAVES);
    blockMap.put(Blocks.JUNGLE_LEAVES, ColoredBlocks.COLORED_JUNGLE_LEAVES);
    blockMap.put(Blocks.BIRCH_LEAVES, ColoredBlocks.COLORED_BIRCH_LEAVES);
    blockMap.put(Blocks.ACACIA_LEAVES, ColoredBlocks.COLORED_ACACIA_LEAVES);
    blockMap.put(Blocks.CHERRY_LEAVES, ColoredBlocks.COLORED_CHERRY_LEAVES);
    blockMap.put(Blocks.MANGROVE_LEAVES, ColoredBlocks.COLORED_MANGROVE_LEAVES);
    blockMap.put(Blocks.BRICKS, ColoredBlocks.COLORED_BRICKS);
    blockMap.put(Blocks.BRICK_STAIRS, ColoredBlocks.COLORED_BRICK_STAIRS);
    blockMap.put(Blocks.BRICK_SLAB, ColoredBlocks.COLORED_BRICK_SLAB);
    for (GlassHandrailBlock block : HandrailBlocks.DECORATED_IRON_HANDRAILS.values()) {
      blockMap.put(block, HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL);
      blockMap.put(block.corner(), HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL.corner());
      blockMap.put(block.central(), HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL.central());
      blockMap.put(block.outer(), HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL.outer());
      blockMap.put(block.stair(), HandrailBlocks.COLORED_DECORATED_IRON_HANDRAIL.stair());
    }

    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("concrete_hung_signs")), HungSignBlocks.COLORED_CONCRETE_HUNG_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_concrete_hung_signs")), HungSignBlocks.COLORED_GLOWING_CONCRETE_HUNG_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("concrete_hung_sign_bars")), HungSignBlocks.COLORED_CONCRETE_HUNG_SIGN_BAR);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("terracotta_hung_signs")), HungSignBlocks.COLORED_TERRACOTTA_HUNG_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_terracotta_hung_signs")), HungSignBlocks.COLORED_GLOWING_TERRACOTTA_HUNG_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("terracotta_hung_sign_bars")), HungSignBlocks.COLORED_TERRACOTTA_HUNG_SIGN_BAR);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("concrete_standing_signs")), StandingSignBlocks.COLORED_CONCRETE_STANDING_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("terracotta_standing_signs")), StandingSignBlocks.COLORED_TERRACOTTA_STANDING_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_concrete_standing_signs")), StandingSignBlocks.COLORED_GLOWING_CONCRETE_STANDING_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_terracotta_standing_signs")), StandingSignBlocks.COLORED_GLOWING_TERRACOTTA_STANDING_SIGN);

    blockMap.put(HungSignBlocks.STONE_HUNG_SIGN, HungSignBlocks.COLORED_STONE_HUNG_SIGN);
    blockMap.put(HungSignBlocks.GLOWING_STONE_HUNG_SIGN, HungSignBlocks.COLORED_GLOWING_STONE_HUNG_SIGN);
    blockMap.put(HungSignBlocks.STONE_HUNG_SIGN_BAR, HungSignBlocks.COLORED_STONE_HUNG_SIGN_BAR);
    blockMap.put(HungSignBlocks.COBBLESTONE_HUNG_SIGN, HungSignBlocks.COLORED_COBBLESTONE_HUNG_SIGN);
    blockMap.put(HungSignBlocks.GLOWING_COBBLESTONE_HUNG_SIGN, HungSignBlocks.COLORED_GLOWING_COBBLESTONE_HUNG_SIGN);
    blockMap.put(HungSignBlocks.COBBLESTONE_HUNG_SIGN_BAR, HungSignBlocks.COLORED_COBBLESTONE_HUNG_SIGN_BAR);
    blockMap.put(HungSignBlocks.STONE_BRICK_HUNG_SIGN, HungSignBlocks.COLORED_STONE_BRICK_HUNG_SIGN);
    blockMap.put(HungSignBlocks.GLOWING_STONE_BRICK_HUNG_SIGN, HungSignBlocks.COLORED_GLOWING_STONE_BRICK_HUNG_SIGN);
    blockMap.put(HungSignBlocks.STONE_BRICK_HUNG_SIGN_BAR, HungSignBlocks.COLORED_STONE_BRICK_HUNG_SIGN_BAR);
    blockMap.put(HungSignBlocks.IRON_HUNG_SIGN, HungSignBlocks.COLORED_IRON_HUNG_SIGN);
    blockMap.put(HungSignBlocks.GLOWING_IRON_HUNG_SIGN, HungSignBlocks.COLORED_GLOWING_IRON_HUNG_SIGN);
    blockMap.put(HungSignBlocks.IRON_HUNG_SIGN_BAR, HungSignBlocks.COLORED_IRON_HUNG_SIGN_BAR);

    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("wooden_wall_signs")), WallSignBlocks.COLORED_WOODEN_WALL_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("concrete_wall_signs")), WallSignBlocks.COLORED_CONCRETE_WALL_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("terracotta_wall_signs")), WallSignBlocks.COLORED_TERRACOTTA_WALL_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_concrete_wall_signs")), WallSignBlocks.COLORED_GLOWING_CONCRETE_WALL_SIGN);
    tagMap.put(TagKey.of(RegistryKeys.BLOCK, id("glowing_terracotta_wall_signs")), WallSignBlocks.COLORED_GLOWING_TERRACOTTA_WALL_SIGN);

    blockMap.put(WallSignBlocks.STONE_WALL_SIGN, WallSignBlocks.COLORED_STONE_WALL_SIGN);
    blockMap.put(WallSignBlocks.GLOWING_STONE_WALL_SIGN, WallSignBlocks.COLORED_GLOWING_STONE_WALL_SIGN);
    blockMap.put(WallSignBlocks.COBBLESTONE_WALL_SIGN, WallSignBlocks.COLORED_COBBLESTONE_WALL_SIGN);
    blockMap.put(WallSignBlocks.GLOWING_COBBLESTONE_WALL_SIGN, WallSignBlocks.COLORED_GLOWING_COBBLESTONE_WALL_SIGN);
    blockMap.put(WallSignBlocks.STONE_BRICK_WALL_SIGN, WallSignBlocks.COLORED_STONE_BRICK_WALL_SIGN);
    blockMap.put(WallSignBlocks.GLOWING_STONE_BRICK_WALL_SIGN, WallSignBlocks.COLORED_GLOWING_STONE_BRICK_WALL_SIGN);
    blockMap.put(WallSignBlocks.IRON_WALL_SIGN, WallSignBlocks.COLORED_IRON_WALL_SIGN);
    blockMap.put(WallSignBlocks.GLOWING_IRON_WALL_SIGN, WallSignBlocks.COLORED_GLOWING_IRON_WALL_SIGN);
    blockMap.put(StandingSignBlocks.STONE_STANDING_SIGN, StandingSignBlocks.COLORED_STONE_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.GLOWING_STONE_STANDING_SIGN, StandingSignBlocks.COLORED_GLOWING_STONE_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.COBBLESTONE_STANDING_SIGN, StandingSignBlocks.COLORED_COBBLESTONE_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.GLOWING_COBBLESTONE_STANDING_SIGN, StandingSignBlocks.COLORED_GLOWING_COBBLESTONE_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.STONE_BRICK_STANDING_SIGN, StandingSignBlocks.COLORED_STONE_BRICK_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.GLOWING_STONE_BRICK_STANDING_SIGN, StandingSignBlocks.COLORED_GLOWING_STONE_BRICK_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.IRON_STANDING_SIGN, StandingSignBlocks.COLORED_IRON_STANDING_SIGN);
    blockMap.put(StandingSignBlocks.GLOWING_IRON_STANDING_SIGN, StandingSignBlocks.COLORED_GLOWING_IRON_STANDING_SIGN);
  }

  @Override
  public void onInitialize() {
    // 初始化静态字段
    MishangucBlocks.init();
    MishangucItems.init();
    MishangucBlockEntities.init();
    final GameRules.Key<EnumRule<MishangucRules.ToolAccess>> ignore = MishangucRules.CARRYING_TOOL_ACCESS;
    SpecialDrawableTypes.init();
    MishangucItemGroups.init();
    Validate.validState(!BlockMatchingRule.REGISTRY.isEmpty());

    registerEvents();
    registerNetworkingReceiver();
    registerFlammableAndFuels();

    // 玩家踩在道路方块上时，予以加速。
    ColumnBuildingTool.registerTempMemoryEvents();

    registerCommands();
    registerColoredBlocks();
    registerColorfulBlocks();
  }

  private static void registerColorfulBlocks() {
    ColorfulBlockRegistry.registerColorfulBlocks(HandrailBlocks.DECORATED_IRON_HANDRAILS);
    ColorfulBlockRegistry.registerColorfulBlocks(HandrailBlocks.SIMPLE_CONCRETE_HANDRAILS);
    ColorfulBlockRegistry.registerColorfulBlocks(HandrailBlocks.SIMPLE_TERRACOTTA_HANDRAILS);
    ColorfulBlockRegistry.registerColorfulBlocks(HandrailBlocks.SIMPLE_STAINED_GLASS_HANDRAILS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.CONCRETE_HUNG_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.CONCRETE_HUNG_SIGN_BARS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.TERRACOTTA_HUNG_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.TERRACOTTA_HUNG_SIGN_BARS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.GLOWING_CONCRETE_HUNG_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(HungSignBlocks.GLOWING_TERRACOTTA_HUNG_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(StandingSignBlocks.CONCRETE_STANDING_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(StandingSignBlocks.GLOWING_CONCRETE_STANDING_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(StandingSignBlocks.TERRACOTTA_STANDING_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(StandingSignBlocks.GLOWING_TERRACOTTA_STANDING_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.CONCRETE_WALL_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.GLOWING_CONCRETE_WALL_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.TERRACOTTA_WALL_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.GLOWING_TERRACOTTA_WALL_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.FULL_CONCRETE_WALL_SIGNS);
    ColorfulBlockRegistry.registerColorfulBlocks(WallSignBlocks.FULL_TERRACOTTA_WALL_SIGNS);
  }
}
