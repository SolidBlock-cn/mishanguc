package pers.solid.mishang.uc.blockentity;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
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
          FabricBlockEntityTypeBuilder.create(
              HungSignBlockEntity::new,
              MishangUtils.<Block>blockInstanceStream(HungSignBlocks.class).toArray(Block[]::new)
          ).build());

  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "wall_sign_block_entity"),
          FabricBlockEntityTypeBuilder.create(
              WallSignBlockEntity::new,
              MishangUtils.<Block>blockInstanceStream(WallSignBlocks.class).filter(block -> !(block instanceof FullWallSignBlock)).toArray(Block[]::new)
          ).build());

  public static final BlockEntityType<FullWallSignBlockEntity> FULL_WALL_SIGN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("mishanguc", "full_wall_sign_block_entity"), FabricBlockEntityTypeBuilder.create(FullWallSignBlockEntity::new, MishangUtils.<Block>blockInstanceStream(WallSignBlocks.class).filter(block -> block instanceof FullWallSignBlock).toArray(Block[]::new)).build());
}
