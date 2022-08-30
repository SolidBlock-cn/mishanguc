package pers.solid.mishang.uc.block;

import com.google.common.collect.ImmutableMap;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.json.blockstate.JBlockModel;
import net.devtech.arrp.json.blockstate.JBlockStates;
import net.devtech.arrp.json.blockstate.JVariants;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.devtech.arrp.json.recipe.JRecipe;
import net.devtech.arrp.json.recipe.JShapedRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.WallMountLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.WallSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.render.WallSignBlockEntityRenderer;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.Map;

/**
 * 与 Minecraft 原版的 {@link net.minecraft.block.WallSignBlock} 不同，这里的 {@code WallSignBlock}
 * 更加强大，可以编辑，且可以放在地上或者天花板上。
 *
 * @see WallSignBlockEntity
 * @see WallSignBlockEntityRenderer
 */
public class WallSignBlock extends WallMountedBlock implements Waterloggable, BlockEntityProvider, BlockResourceGenerator {
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_WALL =
      MishangUtils.createHorizontalDirectionToShape(0, 4, 0, 16, 12, 1);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_FLOOR =
      MishangUtils.createHorizontalDirectionToShape(0, 0, 4, 16, 1, 12);
  public static final Map<Direction, VoxelShape> SHAPES_WHEN_CEILING =
      MishangUtils.createHorizontalDirectionToShape(0, 15, 4, 16, 16, 12);
  /**
   * 告示牌自身的纹理。默认为 {@code null}，可在后期修改。若为 {@code null}，则直接根据其基础方块 {@link #baseBlock} 推断纹理。
   */
  @ApiStatus.AvailableSince("0.1.7")
  public @Nullable String texture;

  @Unmodifiable
  public static final Map<WallMountLocation, Map<Direction, VoxelShape>>
      SHAPE_PER_WALL_MOUNT_LOCATION =
      ImmutableMap.of(
          WallMountLocation.CEILING,
          SHAPES_WHEN_CEILING,
          WallMountLocation.FLOOR,
          SHAPES_WHEN_FLOOR,
          WallMountLocation.WALL,
          SHAPES_WHEN_WALL);

  public final Block baseBlock;

  public WallSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    setDefaultState(
        stateManager
            .getDefaultState()
            .with(FACING, Direction.SOUTH)
            .with(FACE, WallMountLocation.WALL)
            .with(WATERLOGGED, false));
  }

  @ApiStatus.AvailableSince("0.1.7")
  public WallSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(FACE, FACING, WATERLOGGED);
  }

  @Nullable
  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final BlockState placementState = super.getPlacementState(ctx);
    return placementState != null
        ? placementState.with(
        WATERLOGGED, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
        : null;
  }

  @Override
  public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(
      BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return SHAPE_PER_WALL_MOUNT_LOCATION.get(state.get(FACE)).get(state.get(FACING));
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @Override
  public BlockState getStateForNeighborUpdate(
      BlockState state,
      Direction direction,
      BlockState neighborState,
      WorldAccess world,
      BlockPos pos,
      BlockPos neighborPos) {
    super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return state;
  }

  @Override
  public MutableText getName() {
    return baseBlock == null
        ? super.getName()
        : TextBridge.translatable("block.mishanguc.wall_sign", baseBlock.getName());
  }

  /**
   * 玩家点击该告示牌时，编辑告示牌。
   */
  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(
      BlockState state,
      World world,
      BlockPos pos,
      PlayerEntity player,
      Hand hand,
      BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hand, hit);
    if (actionResult != ActionResult.PASS) return actionResult;
    // 在服务端触发打开告示牌编辑界面。Open the edit interface, triggered in the server side.
    final BlockEntity blockEntity = world.getBlockEntity(pos);
    if (!(blockEntity instanceof final WallSignBlockEntity entity)) {
      return ActionResult.PASS;
    } else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players has no permission to edit.
      return ActionResult.FAIL;
    } else if (player.getMainHandStack().getItem() == Items.MAGMA_CREAM) {
      MishangUtils.rearrange(entity.textContexts);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BALL) {
      MishangUtils.replaceArrows(entity.textContexts);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BLOCK) {
      final WorldChunk worldChunk = world.getWorldChunk(pos);
      for (BlockEntity value : worldChunk.getBlockEntities().values()) {
        if (value instanceof WallSignBlockEntity wallSignBlockEntity) {
          MishangUtils.replaceArrows(wallSignBlockEntity.textContexts);
          wallSignBlockEntity.markDirty();
        }
      }
      return ActionResult.SUCCESS;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      // In this case, the sign is occupied, and the players has not editing
      // permission.
      player.sendMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    // 此时告示牌已被编辑。
    entity.setEditor(player);
    ServerPlayNetworking.send(
        ((ServerPlayerEntity) player),
        new Identifier("mishanguc", "edit_sign"),
        PacketByteBufs.create().writeBlockPos(pos).writeEnumConstant(hit.getSide()));
    return ActionResult.SUCCESS;
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new WallSignBlockEntity(pos, state);
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @Nullable JBlockStates getBlockStates() {
    final JVariants variants = new JVariants();
    final JBlockStates state = JBlockStates.ofVariants(variants);
    for (WallMountLocation wallMountLocation : WallMountLocation.values()) {
      final int x = switch (wallMountLocation) {
        case WALL -> 0;
        case FLOOR -> 90;
        default -> -90;
      };
      for (Direction direction : Direction.Type.HORIZONTAL) {
        float y = direction.asRotation();
        variants.addVariant(
            String.format("face=%s,facing=%s", wallMountLocation.asString(), direction.asString()),
            new JBlockModel(getBlockModelId()).x(x).y((int) y).uvlock());
      }
    }
    return state;
  }

  @Override
  @Environment(EnvType.CLIENT)
  public @Nullable JModel getBlockModel() {
    return new JModel("mishanguc:block/wall_sign").textures(new JTextures().var("texture", getBaseTexture()));
  }

  @Environment(EnvType.CLIENT)
  public String getBaseTexture() {
    if (texture != null) return texture;
    final Identifier id = Registry.BLOCK.getId(baseBlock);
    return String.format("%s:block/%s", id.getNamespace(), id.getPath());
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("---", "###", "---")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_WALL_SIGN)
        .resultCount(6);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
    return recipe;
  }

  @Override
  public Identifier getAdvancementIdForRecipe(Identifier recipeId) {
    return recipeId.brrp_prepend("recipes/signs/");
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    return direction.getAxis().isHorizontal() && state.getBlock() instanceof WallSignBlock && stateFrom.getBlock() instanceof WallSignBlock && state.get(FACING) == stateFrom.get(FACING) && direction.getAxis() != state.get(FACING).getAxis();
  }
}
