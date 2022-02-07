package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.block.MishangucBlocks;

public class MishangucBlockEntities {
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "hung_block_entity"),
          FabricBlockEntityTypeBuilder.create(
                  HungSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .addAll(MishangucBlocks.CONCRETE_HUNG_SIGNS.values())
                      .addAll(MishangucBlocks.TERRACOTTA_HUNG_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_CONCRETE_HUNG_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_TERRACOTTA_HUNG_SIGNS.values())
                      .add(
                          MishangucBlocks.GLOWING_NETHERRACK_HUNG_SIGN,
                          MishangucBlocks.GLOWING_NETHER_BRICK_HUNG_SIGN,
                          MishangucBlocks.GLOWING_BLACKSTONE_HUNG_SIGN,
                          MishangucBlocks.GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN)
                      .build()
                      .toArray(new Block[] {}))
              .build(null));
  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "wall_sign_block_entity"),
          FabricBlockEntityTypeBuilder.create(
                  WallSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .addAll(MishangucBlocks.CONCRETE_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.TERRACOTTA_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_CONCRETE_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_TERRACOTTA_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.FULL_CONCRETE_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.FULL_TERRACOTTA_WALL_SIGNS.values())
                      .add(MishangucBlocks.INVISIBLE_WALL_SIGN)
                      .build()
                      .toArray(new Block[] {}))
              .build(null));
}
