package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.blocks.HungSignBlocks;
import pers.solid.mishang.uc.blocks.WallSignBlocks;

public class MishangucBlockEntities {
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "hung_block_entity"),
          FabricBlockEntityTypeBuilder.create(
                  HungSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .addAll(HungSignBlocks.CONCRETE_HUNG_SIGNS.values())
                      .addAll(HungSignBlocks.TERRACOTTA_HUNG_SIGNS.values())
                      .addAll(HungSignBlocks.GLOWING_CONCRETE_HUNG_SIGNS.values())
                      .addAll(HungSignBlocks.GLOWING_TERRACOTTA_HUNG_SIGNS.values())
                      .add(
                          HungSignBlocks.GLOWING_NETHERRACK_HUNG_SIGN,
                          HungSignBlocks.GLOWING_NETHER_BRICK_HUNG_SIGN,
                          HungSignBlocks.GLOWING_BLACKSTONE_HUNG_SIGN,
                          HungSignBlocks.GLOWING_POLISHED_BLACKSTONE_HUNG_SIGN)
                      .build()
                      .toArray(new Block[]{}))
              .build(null));
  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "wall_sign_block_entity"),
          FabricBlockEntityTypeBuilder.create(
                  WallSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .add(
                          WallSignBlocks.OAK_WALL_SIGN,
                          WallSignBlocks.SPRUCE_WALL_SIGN,
                          WallSignBlocks.BIRCH_WALL_SIGN,
                          WallSignBlocks.JUNGLE_WALL_SIGN,
                          WallSignBlocks.ACACIA_WALL_SIGN,
                          WallSignBlocks.DARK_OAK_WALL_SIGN,
                          WallSignBlocks.CRIMSON_WALL_SIGN,
                          WallSignBlocks.WARPED_WALL_SIGN)
                      .addAll(WallSignBlocks.CONCRETE_WALL_SIGNS.values())
                      .addAll(WallSignBlocks.TERRACOTTA_WALL_SIGNS.values())
                      .addAll(WallSignBlocks.GLOWING_CONCRETE_WALL_SIGNS.values())
                      .addAll(WallSignBlocks.GLOWING_TERRACOTTA_WALL_SIGNS.values())
                      .addAll(WallSignBlocks.FULL_CONCRETE_WALL_SIGNS.values())
                      .addAll(WallSignBlocks.FULL_TERRACOTTA_WALL_SIGNS.values())
                      .add(WallSignBlocks.INVISIBLE_WALL_SIGN, WallSignBlocks.INVISIBLE_GLOWING_WALL_SIGN)
                      .build()
                      .toArray(new Block[]{}))
              .build(null));
}
