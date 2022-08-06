package pers.solid.mishang.uc.blockentity;

import com.google.common.base.Predicates;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.ColoredBlocks;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

import java.util.stream.Stream;

public final class MishangucBlockEntities {

  public static final BlockEntityType<SimpleColoredBlockEntity>
      SIMPLE_COLORED_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "simple_colored_block_entity"),
      FabricBlockEntityTypeBuilder.create(
              SimpleColoredBlockEntity::new,
              Stream.concat(MishangUtils.instanceStream(ColoredBlocks.class, Block.class), MishangUtils.instanceStream(HungSignBlocks.class, ColoredHungSignBarBlock.class))
                  .toArray(Block[]::new))
          .build());
  public static final BlockEntityType<HungSignBlockEntity>
      HUNG_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "hung_sign_block_entity"),
      FabricBlockEntityTypeBuilder.create(
          HungSignBlockEntity::new,
          MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class)
              .filter(Predicates.instanceOf(ColoredBlocks.class).negate())
              .toArray(Block[]::new)
      ).build());

  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY_removed = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "hung_block_entity"),
      FabricBlockEntityTypeBuilder.create(
          HungSignBlockEntity::new,
          MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class)
              .filter(Predicates.instanceOf(ColoredBlocks.class).negate())
              .toArray(Block[]::new)
      ).build()
  );

  public static final BlockEntityType<ColoredHungSignBlockEntity>
      COLORED_HUNG_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "colored_hung_sign_block_entity"),
      FabricBlockEntityTypeBuilder.create(
              ColoredHungSignBlockEntity::new,
              MishangUtils.instanceStream(HungSignBlocks.class, ColoredHungSignBlock.class)
                  .toArray(Block[]::new))
          .build());

  public static final BlockEntityType<WallSignBlockEntity>
      WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "wall_sign_block_entity"),
      FabricBlockEntityTypeBuilder.create(
              WallSignBlockEntity::new,
              MishangUtils.instanceStream(WallSignBlocks.class, Block.class)
                  .filter(block -> !(block instanceof FullWallSignBlock))
                  .toArray(Block[]::new))
          .build());

  public static final BlockEntityType<FullWallSignBlockEntity>
      FULL_WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "full_wall_sign_block_entity"),
      FabricBlockEntityTypeBuilder.create(FullWallSignBlockEntity::new, MishangUtils.instanceStream(WallSignBlocks.class, Block.class)
              .filter(block -> block instanceof FullWallSignBlock)
              .filter(Predicates.instanceOf(ColoredWallSignBlockEntity.class).negate())
              .toArray(Block[]::new))
          .build());

  public static final BlockEntityType<ColoredWallSignBlockEntity>
      COLORED_WALL_SIGN_BLOCK_ENTITY = Registry.register(
      Registry.BLOCK_ENTITY_TYPE,
      new Identifier("mishanguc", "colored_wall_sign_block_entity"),
      FabricBlockEntityTypeBuilder.create(
              ColoredWallSignBlockEntity::new,
              MishangUtils.instanceStream(WallSignBlocks.class, ColoredWallSignBlock.class)
                  .toArray(Block[]::new))
          .build()
  );

  // 不做事情，但是会初始化类。
  public static void init() {

  }
}
