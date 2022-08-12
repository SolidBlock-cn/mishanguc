package pers.solid.mishang.uc;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pers.solid.mishang.uc.block.ColoredBlock;
import pers.solid.mishang.uc.block.HandrailBlock;
import pers.solid.mishang.uc.block.Road;
import pers.solid.mishang.uc.blockentity.BlockEntityWithText;
import pers.solid.mishang.uc.blockentity.MishangucBlockEntities;
import pers.solid.mishang.uc.blocks.*;
import pers.solid.mishang.uc.item.BlockToolItem;
import pers.solid.mishang.uc.item.HotbarScrollInteraction;
import pers.solid.mishang.uc.item.InteractsWithEntity;
import pers.solid.mishang.uc.item.MishangucItems;

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
        HungSignBlocks.DARK_OAK_HUNG_SIGN,
        HungSignBlocks.MANGROVE_HUNG_SIGN,
        HungSignBlocks.OAK_HUNG_SIGN_BAR,
        HungSignBlocks.SPRUCE_HUNG_SIGN_BAR,
        HungSignBlocks.BIRCH_HUNG_SIGN_BAR,
        HungSignBlocks.JUNGLE_HUNG_SIGN_BAR,
        HungSignBlocks.ACACIA_HUNG_SIGN_BAR,
        HungSignBlocks.DARK_OAK_HUNG_SIGN_BAR,
        HungSignBlocks.MANGROVE_HUNG_SIGN_BAR,
        WallSignBlocks.OAK_WALL_SIGN,
        WallSignBlocks.SPRUCE_WALL_SIGN,
        WallSignBlocks.BIRCH_WALL_SIGN,
        WallSignBlocks.JUNGLE_WALL_SIGN,
        WallSignBlocks.ACACIA_WALL_SIGN,
        WallSignBlocks.DARK_OAK_WALL_SIGN,
        WallSignBlocks.MANGROVE_WALL_SIGN,
        WallSignBlocks.COLORED_WOODEN_WALL_SIGN
    };
    for (Block block : woodenBlocks) {
      flammableBlockRegistry.add(block, 5, 20);
      fuelRegistry.add(block, 100);
    }
    final HandrailBlock[] woodenHandrails = {
        HandrailBlocks.SIMPLE_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_SPRUCE_HANDRAIL,
        HandrailBlocks.SIMPLE_BIRCH_HANDRAIL,
        HandrailBlocks.SIMPLE_JUNGLE_HANDRAIL,
        HandrailBlocks.SIMPLE_ACACIA_HANDRAIL,
        HandrailBlocks.SIMPLE_DARK_OAK_HANDRAIL,
        HandrailBlocks.SIMPLE_MANGROVE_HANDRAIL
    };
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
    ServerPlayNetworking.registerGlobalReceiver(
        new Identifier("mishanguc", "edit_sign_finish"), BlockEntityWithText.PACKET_HANDLER);
    ServerPlayNetworking.registerGlobalReceiver(new Identifier("mishanguc", "item_scroll"), (server, player, handler, buf, responseSender) -> {
      final int selectedSlot = buf.readInt();
      final double scrollAmount = buf.readDouble();
      server.execute(() -> {
        final ItemStack stack = player.getInventory().getStack(selectedSlot);
        if (stack.getItem() instanceof HotbarScrollInteraction interaction) {
          interaction.onScroll(selectedSlot, scrollAmount, player, stack);
        }
      });
    });
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
            final BlockHitResult hitResult = (BlockHitResult) player.raycast(5, 0, blockToolItem.includesFluid(stack, player.isSneaking()));
            return blockToolItem.beginAttackBlock(player, world, hand, hitResult.getBlockPos(), hitResult.getSide(), blockToolItem.includesFluid(stack, player.isSneaking()));
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
            final BlockHitResult hitResult = (BlockHitResult) player.raycast(5, 0, ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
            return blockToolItem.beginAttackBlock(player, world, hand, hitResult.getBlockPos(), hitResult.getSide(), ((BlockToolItem) item).includesFluid(stack, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    UseBlockCallback.EVENT.register(
        (player, world, hand, hitResult) -> {
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (!player.getAbilities().allowModifyWorld && !stackInHand.canPlaceOn(Registry.BLOCK, new CachedBlockPosition(world, hitResult.getBlockPos(), false))) {
            return ActionResult.PASS;
          }
          if (item instanceof final BlockToolItem blockToolItem) {
            return blockToolItem.useOnBlock(player, world, hitResult, hand, blockToolItem.includesFluid(stackInHand, player.isSneaking()));
          } else {
            return ActionResult.PASS;
          }
        });
    AttackEntityCallback.EVENT.register(
        (player, world, hand, entity, hitResult) -> {
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
          final ItemStack stackInHand = player.getStackInHand(hand);
          final Item item = stackInHand.getItem();
          if (item instanceof final InteractsWithEntity interactsWithEntity) {
            return interactsWithEntity.useEntityCallback(player, world, hand, entity, hitResult);
          } else {
            return ActionResult.PASS;
          }
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
    blockMap.put(Blocks.STONE, ColoredBlocks.COLORED_STONE);
    blockMap.put(Blocks.STONE_STAIRS, ColoredBlocks.COLORED_STONE_STAIRS);
    blockMap.put(Blocks.STONE_SLAB, ColoredBlocks.COLORED_STONE_SLAB);
    blockMap.put(Blocks.QUARTZ_BLOCK, ColoredBlocks.COLORED_QUARTZ_BLOCK);
    blockMap.put(Blocks.CHISELED_QUARTZ_BLOCK, ColoredBlocks.COLORED_CHISELED_QUARTZ_BLOCK);
    blockMap.put(Blocks.QUARTZ_BRICKS, ColoredBlocks.COLORED_QUARTZ_BRICKS);
    blockMap.put(Blocks.SMOOTH_QUARTZ, ColoredBlocks.COLORED_SMOOTH_QUARTZ);
    blockMap.put(Blocks.QUARTZ_PILLAR, ColoredBlocks.COLORED_QUARTZ_PILLAR);
    blockMap.put(Blocks.PURPUR_BLOCK, ColoredBlocks.COLORED_PURPUR_BLOCK);
    blockMap.put(Blocks.PURPUR_PILLAR, ColoredBlocks.COLORED_PURPUR_PILLAR);
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
    blockMap.put(Blocks.ICE, ColoredBlocks.COLORED_ICE);
    blockMap.put(Blocks.SNOW_BLOCK, ColoredBlocks.COLORED_SNOW_BLOCK);
    blockMap.put(Blocks.PACKED_ICE, ColoredBlocks.COLORED_PACKED_ICE);

    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "concrete_hung_signs")), HungSignBlocks.COLORED_CONCRETE_HUNG_SIGN);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "concrete_hung_sign_bars")), HungSignBlocks.COLORED_CONCRETE_HUNG_SIGN_BAR);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "terracotta_hung_signs")), HungSignBlocks.COLORED_TERRACOTTA_HUNG_SIGN);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "terracotta_hung_sign_bars")), HungSignBlocks.COLORED_TERRACOTTA_HUNG_SIGN);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "wooden_wall_signs")), WallSignBlocks.COLORED_WOODEN_WALL_SIGN);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "concrete_wall_signs")), WallSignBlocks.COLORED_CONCRETE_WALL_SIGN);
    tagMap.put(TagKey.of(Registry.BLOCK_KEY, new Identifier("mishanguc", "terracotta_wall_signs")), WallSignBlocks.COLORED_TERRACOTTA_WALL_SIGN);
  }

  @Override
  public void onInitialize() {
    // 初始化静态字段
    MishangucBlocks.init();
    MishangucItems.init();
    MishangucBlockEntities.init();

    registerEvents();
    registerNetworkingReceiver();
    registerFlammableAndFuels();

    // 玩家踩在道路方块上时，予以加速。
    ServerTickEvents.END_WORLD_TICK.register(Road.CHECK_MULTIPLIER::accept);

    registerCommands();
    registerColoredBlocks();
  }
}
