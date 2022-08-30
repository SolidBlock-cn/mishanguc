package pers.solid.mishang.uc.blockentity;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.ColoredBlocks;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MishangucBlockEntities {

  public static final BlockEntityType<SimpleColoredBlockEntity>
      SIMPLE_COLORED_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "simple_colored_block_entity"),
      new BlockEntityType<>(
          SimpleColoredBlockEntity::new,
          Stream.concat(MishangUtils.instanceStream(ColoredBlocks.class, Block.class), MishangUtils.instanceStream(HungSignBlocks.class, ColoredHungSignBarBlock.class))
              .collect(Collectors.toSet()), null
      ));
  public static final BlockEntityType<HungSignBlockEntity>
      HUNG_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "hung_sign_block_entity"),
      new BlockEntityType<>(
          HungSignBlockEntity::new,
          MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class)
              .filter(Predicates.instanceOf(ColoredBlocks.class).negate())
              .collect(Collectors.toSet()), null
      ));

  @Deprecated
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY_removed = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "hung_block_entity"),
      new BlockEntityType<>(
          HungSignBlockEntity::new,
          MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class)
              .filter(Predicates.instanceOf(ColoredBlocks.class).negate())
              .collect(Collectors.toSet()), null
      )
  );

  public static final BlockEntityType<ColoredHungSignBlockEntity>
      COLORED_HUNG_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "colored_hung_sign_block_entity"),
      new BlockEntityType<>(
          ColoredHungSignBlockEntity::new,
          MishangUtils.instanceStream(HungSignBlocks.class, ColoredHungSignBlock.class)
              .collect(Collectors.toSet()), null
      ));

  public static final BlockEntityType<WallSignBlockEntity>
      WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "wall_sign_block_entity"),
      new BlockEntityType<>(
          WallSignBlockEntity::new,
          MishangUtils.instanceStream(WallSignBlocks.class, Block.class)
              .filter(block -> !(block instanceof FullWallSignBlock))
              .collect(Collectors.toSet()),
          null));

  public static final BlockEntityType<FullWallSignBlockEntity>
      FULL_WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "full_wall_sign_block_entity"),
      new BlockEntityType<>(FullWallSignBlockEntity::new, MishangUtils.instanceStream(WallSignBlocks.class, Block.class)
          .filter(block -> block instanceof FullWallSignBlock)
          .filter(Predicates.instanceOf(ColoredWallSignBlockEntity.class).negate())
          .collect(Collectors.toSet()),
          null));

  public static final BlockEntityType<ColoredWallSignBlockEntity>
      COLORED_WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "colored_wall_sign_block_entity"),
      new BlockEntityType<>(
          ColoredWallSignBlockEntity::new,
          MishangUtils.instanceStream(WallSignBlocks.class, ColoredWallSignBlock.class)
              .collect(Collectors.toSet()),
          null)
  );

  @Deprecated
  public static final BlockEntityType<SimpleColoredBlockEntity> COLORED_HUNG_SIGN_BAR_BLOCK_ENTITY_removed = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("mishanguc", "colored_hung_sign_bar_block_entity"), new BlockEntityType<>(
      SimpleColoredBlockEntity::new,
      ImmutableSet.of(HungSignBlocks.COLORED_CONCRETE_HUNG_SIGN_BAR,
          HungSignBlocks.COLORED_TERRACOTTA_HUNG_SIGN_BAR), null
  ));

  // 不做事情，但是会初始化类。
  @SuppressWarnings("EmptyMethod")
  public static void init() {

  }
}
