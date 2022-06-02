package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.block.FullWallSignBlock;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

public class MishangucBlockEntities {
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "hung_block_entity"),
          new BlockEntityType<>(
              HungSignBlockEntity::new,
              MishangUtils.instanceStream(HungSignBlocks.class,Block.class).collect(ImmutableSet.toImmutableSet())
              , null));

  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "wall_sign_block_entity"),
          new BlockEntityType<>(
              WallSignBlockEntity::new,
              MishangUtils.instanceStream(WallSignBlocks.class, Block.class).filter(block -> !(block instanceof FullWallSignBlock)).collect(ImmutableSet.toImmutableSet())
          ,null));

  public static final BlockEntityType<FullWallSignBlockEntity> FULL_WALL_SIGN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("mishanguc", "full_wall_sign_block_entity"), new BlockEntityType<>(FullWallSignBlockEntity::new, MishangUtils.instanceStream(WallSignBlocks.class,Block.class).filter(block -> block instanceof FullWallSignBlock).collect(ImmutableSet.toImmutableSet()), null));

  // 不做事情，但是会初始化类。
  public static void init() {

  }
}
