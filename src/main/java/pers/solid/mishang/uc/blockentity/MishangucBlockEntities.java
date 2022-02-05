package pers.solid.mishang.uc.blockentity;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import pers.solid.mishang.uc.block.MishangucBlocks;

public class MishangucBlockEntities {
  public static final BlockEntityType<TextPadBlockEntity> TEXT_PAD_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "text_pad_block"),
          BlockEntityType.Builder.create(
                  TextPadBlockEntity::new,
                  MishangucBlocks.TEXT_PAD_BLOCKS.values().toArray(new Block[] {}))
              .build(null));
  public static final BlockEntityType<HungSignBlockEntity> HUNG_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "hung_block_entity"),
          BlockEntityType.Builder.create(
                  HungSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .addAll(MishangucBlocks.HUNG_CONCRETE_GLOWING_SIGNS.values())
                      .addAll(MishangucBlocks.HUNG_TERRACOTTA_GLOWING_SIGNS.values())
                      .add(
                          MishangucBlocks.HUNG_NETHERRACK_GLOWING_SIGN,
                          MishangucBlocks.HUNG_NETHER_BRICK_GLOWING_SIGN,
                          MishangucBlocks.HUNG_BLACKSTONE_GLOWING_SIGN,
                          MishangucBlocks.HUNG_POLISHED_BLACKSTONE_GLOWING_SIGN)
                      .build()
                      .toArray(new Block[] {}))
              .build(null));
  public static final BlockEntityType<WallSignBlockEntity> WALL_SIGN_BLOCK_ENTITY =
      Registry.register(
          Registry.BLOCK_ENTITY_TYPE,
          new Identifier("mishanguc", "wall_sign_block_entity"),
          BlockEntityType.Builder.create(
                  WallSignBlockEntity::new,
                  new ImmutableList.Builder<Block>()
                      .addAll(MishangucBlocks.CONCRETE_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.TERRACOTTA_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_CONCRETE_WALL_SIGNS.values())
                      .addAll(MishangucBlocks.GLOWING_TERRACOTTA_WALL_SIGNS.values())
                      .build()
                      .toArray(new Block[] {}))
              .build(null));
}
