package pers.solid.mishang.uc.blockentity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.*;
import pers.solid.mishang.uc.blocks.ColoredBlocks;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.StandingSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

import java.util.stream.Stream;

public final class MishangucBlockEntities {

  public static final BlockEntityType<SimpleColoredBlockEntity> SIMPLE_COLORED_BLOCK_ENTITY = register(
      "simple_colored_block_entity",
      SimpleColoredBlockEntity::new,
      Stream.concat(
          MishangUtils.instanceStream(ColoredBlocks.class, Block.class),
          MishangUtils.instanceStream(HungSignBlocks.class, ColoredHungSignBarBlock.class)));
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY = register(
      "hung_sign_block_entity",
      HungSignBlockEntity::new,
      MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class)
          .filter(block -> !(block instanceof ColoredBlock)));

  public static final BlockEntityType<ColoredHungSignBlockEntity> COLORED_HUNG_SIGN_BLOCK_ENTITY = register(
      "colored_hung_sign_block_entity",
      ColoredHungSignBlockEntity::new,
      MishangUtils.instanceStream(HungSignBlocks.class, HungSignBlock.class).filter(block -> block instanceof ColoredBlock));

  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY = register(
      "wall_sign_block_entity",
      WallSignBlockEntity::new,
      MishangUtils.instanceStream(WallSignBlocks.class, Block.class)
          .filter(block -> !(block instanceof FullWallSignBlock || block instanceof ColoredBlock)));

  public static final BlockEntityType<FullWallSignBlockEntity> FULL_WALL_SIGN_BLOCK_ENTITY = register(
      "full_wall_sign_block_entity",
      FullWallSignBlockEntity::new,
      MishangUtils.instanceStream(WallSignBlocks.class, FullWallSignBlock.class)
          .filter(block -> !(block instanceof ColoredBlock)));

  public static final BlockEntityType<ColoredWallSignBlockEntity> COLORED_WALL_SIGN_BLOCK_ENTITY = register(
      "colored_wall_sign_block_entity",
      ColoredWallSignBlockEntity::new,
      MishangUtils.instanceStream(WallSignBlocks.class, WallSignBlock.class)
          .filter(block -> block instanceof ColoredBlock));

  public static final BlockEntityType<StandingSignBlockEntity> STANDING_SIGN_BLOCK_ENTITY = register(
      "standing_sign_block_entity",
      StandingSignBlockEntity::new,
      MishangUtils.instanceStream(StandingSignBlocks.class, StandingSignBlock.class)
          .filter(block -> !(block instanceof ColoredBlock)));

  public static final BlockEntityType<ColoredStandingSignBlockEntity> COLORED_STANDING_SIGN_BLOCK_ENTITY = register(
      "colored_standing_sign_block_entity",
      ColoredStandingSignBlockEntity::new,
      MishangUtils.instanceStream(StandingSignBlocks.class, StandingSignBlock.class)
          .filter(block -> block instanceof ColoredBlock));

  // 不做事情，但是会初始化类。
  @SuppressWarnings("EmptyMethod")
  public static void init() {
  }

  private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntityFactory<T> factory, Block... blocks) {
    return Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of("mishanguc", name), BlockEntityType.Builder.create(factory, blocks).build());
  }

  private static <T extends BlockEntity> BlockEntityType<T> register(String name, BlockEntityType.BlockEntityFactory<T> factory, Stream<? extends Block> blockStream) {
    return register(name, factory, blockStream.toArray(Block[]::new));
  }
}
