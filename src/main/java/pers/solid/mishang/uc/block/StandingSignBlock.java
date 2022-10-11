package pers.solid.mishang.uc.block;

import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.generator.BlockResourceGenerator;
import net.devtech.arrp.generator.ResourceGeneratorHelper;
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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.data.client.model.TextureKey;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.solid.mishang.uc.MishangUtils;
import pers.solid.mishang.uc.blockentity.StandingSignBlockEntity;
import pers.solid.mishang.uc.blocks.WallSignBlocks;
import pers.solid.mishang.uc.mixin.ItemUsageContextInvoker;
import pers.solid.mishang.uc.util.TextBridge;

import java.util.List;

/**
 * 本模组中的直立告示牌方块。
 *
 * @see StandingSignBlockEntity
 * @see pers.solid.mishang.uc.blocks.StandingSignBlocks
 * @see pers.solid.mishang.uc.render.StandingSignBlockEntityRenderer
 */
@ApiStatus.AvailableSince("1.0.2")
public class StandingSignBlock extends Block implements BlockEntityProvider, Waterloggable, BlockResourceGenerator {

  public static final IntProperty ROTATION = Properties.ROTATION;
  public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
  /**
   * 指定告示牌底部是否有杆子。默认取决于底部方块的侧面形状。按住 Shift 并点击告示牌可以切换。
   */
  public static final BooleanProperty DOWN = Properties.DOWN;
  protected static final VoxelShape SHAPE_NS = createCuboidShape(0, 8, 6.5, 16, 16, 9.5);
  protected static final VoxelShape SHAPE_WE = createCuboidShape(6.5, 8, 0, 9.5, 16, 16);
  protected static final VoxelShape SHAPE_NS_WIDE = createCuboidShape(2, 8, 5, 14, 16, 11);
  protected static final VoxelShape SHAPE_WE_WIDE = createCuboidShape(5, 8, 2, 11, 16, 14);
  protected static final VoxelShape SHAPE_CENTER = createCuboidShape(2.5, 8, 2.5, 13.5, 16, 13.5);
  protected static final VoxelShape CULLING_SHAPE = createCuboidShape(7.5, 0, 7.5, 8.5, 8, 8.5);
  protected static final VoxelShape BAR_SHAPE = createCuboidShape(6.5, 0, 6.5, 9.5, 8, 9.5);
  public final @Nullable Block baseBlock;
  public @Nullable String baseTexture, barTexture;

  public StandingSignBlock(@Nullable Block baseBlock, Settings settings) {
    super(settings);
    this.baseBlock = baseBlock;
    setDefaultState(stateManager.getDefaultState().with(WATERLOGGED, false).with(ROTATION, 0).with(DOWN, true));
  }

  public StandingSignBlock(@NotNull Block baseBlock) {
    this(baseBlock, FabricBlockSettings.copyOf(baseBlock));
  }

  /**
   * 根据 BlockHitResult 来判断玩家点击的告示牌是点击的哪一面（front 或 back）。如果点击的是顶部而无法判断哪一面，则返回 {@code null}。
   */
  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, BlockHitResult blockHitResult) {
    final Direction side = blockHitResult.getSide();
    if (side.getAxis().isVertical()) {
      final Vec3d pos = blockHitResult.getPos();
      double minAngle = MathHelper.RADIANS_PER_DEGREE * (360 / 16f * blockState.get(ROTATION));
      double clickAngle = MathHelper.atan2(MathHelper.floorMod(pos.z, 1) - 0.5, MathHelper.floorMod(pos.x, 1) - 0.5);
      return (minAngle < clickAngle && clickAngle < minAngle + MathHelper.PI)
          || (minAngle - 2 * MathHelper.PI < clickAngle && clickAngle < minAngle - MathHelper.PI);
    }
    return getHitSide(blockState, side);
  }

  @Contract(pure = true)
  public static @Nullable Boolean getHitSide(BlockState blockState, Direction side) {
    final int rotation = blockState.get(ROTATION);
    return switch (rotation) {
      case 0 -> switch (side) {
        case NORTH -> Boolean.FALSE;
        case SOUTH -> Boolean.TRUE;
        default -> null;
      };
      case 8 -> switch (side) {
        case SOUTH -> Boolean.FALSE;
        case NORTH -> Boolean.TRUE;
        default -> null;
      };
      case 4 -> switch (side) {
        case EAST -> Boolean.FALSE;
        case WEST -> Boolean.TRUE;
        default -> null;
      };
      case 12 -> switch (side) {
        case WEST -> Boolean.FALSE;
        case EAST -> Boolean.TRUE;
        default -> null;
      };
      case 1, 2, 3 -> switch (side) {
        case WEST, SOUTH -> Boolean.TRUE;
        case EAST, NORTH -> Boolean.FALSE;
        default -> null;
      };
      case 5, 6, 7 -> switch (side) {
        case WEST, NORTH -> Boolean.TRUE;
        case EAST, SOUTH -> Boolean.FALSE;
        default -> null;
      };
      case 9, 10, 11 -> switch (side) {
        case EAST, NORTH -> Boolean.TRUE;
        case WEST, SOUTH -> Boolean.FALSE;
        default -> null;
      };
      case 13, 14, 15 -> switch (side) {
        case EAST, SOUTH -> Boolean.TRUE;
        case WEST, NORTH -> Boolean.FALSE;
        default -> null;
      };
      default -> null;
    };
  }

  @Environment(EnvType.CLIENT)
  public String getBaseTexture() {
    if (baseTexture != null) return baseTexture;
    return ResourceGeneratorHelper.getTextureId(baseBlock == null ? this : baseBlock, TextureKey.ALL);
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
    super.appendProperties(builder);
    builder.add(WATERLOGGED, ROTATION, DOWN);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    final World world = ctx.getWorld();
    final BlockPos blockPos = ctx.getBlockPos();
    final BlockState blockState = world.getBlockState(((ItemUsageContextInvoker) ctx).invokeGetHitResult().getBlockPos());
    FluidState fluidState = world.getFluidState(blockPos);
    return this.getDefaultState()
        // 毗邻直立的告示牌放置时，使用相同的方向。
        .with(ROTATION, blockState.getBlock() instanceof StandingSignBlock ? blockState.get(ROTATION) : MathHelper.floor((double) ((180.0F + ctx.getPlayerYaw()) * 16.0F / 360.0F) + 0.5) & 15)
        .with(DOWN, world.getBlockState(blockPos.down()).isSideSolid(world, blockPos.down(), Direction.UP, SideShapeType.CENTER))
        .with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
    if (state.get(WATERLOGGED)) {
      world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    final BlockState state1 = super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    return direction == Direction.DOWN ? state1.with(DOWN, neighborState.isSideSolid(world, neighborPos, Direction.UP, SideShapeType.CENTER)) : state1;
  }

  @SuppressWarnings("deprecation")
  @Override
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return state.with(ROTATION, rotation.rotate(state.get(ROTATION), 16));
  }

  @SuppressWarnings("deprecation")
  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return state.with(ROTATION, mirror.mirror(state.get(ROTATION), 16));
  }

  @Override
  public MutableText getName() {
    if (baseBlock != null) return TextBridge.translatable("block.mishanguc.standing_sign", baseBlock.getName());
    return super.getName();
  }

  @Override
  public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
    super.appendTooltip(stack, world, tooltip, options);
    tooltip.add(TextBridge.translatable("block.mishanguc.standing_sign.tooltip.1").formatted(Formatting.GRAY));
    tooltip.add(TextBridge.translatable("block.mishanguc.standing_sign.tooltip.2").formatted(Formatting.GRAY));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @NotNull JModel getBlockModel() {
    final String texture = getBaseTexture();
    final JTextures textures = new JTextures().var("texture", texture).var("bar", barTexture);
    return new JModel("mishanguc:block/standing_sign").textures(textures);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void writeBlockModel(RuntimeResourcePack pack) {
    final JModel model = getBlockModel();
    final Identifier modelId = getBlockModelId();
    pack.addModel(model, modelId);
    pack.addModel(model.clone().parent(model.parent + "_1"), modelId.brrp_append("_1"));
    pack.addModel(model.clone().parent(model.parent + "_2"), modelId.brrp_append("_2"));
    pack.addModel(model.clone().parent(model.parent + "_3"), modelId.brrp_append("_3"));
    pack.addModel(model.clone().parent(model.parent + "_barred"), modelId.brrp_append("_barred"));
    pack.addModel(model.clone().parent(model.parent + "_barred_1"), modelId.brrp_append("_barred_1"));
    pack.addModel(model.clone().parent(model.parent + "_barred_2"), modelId.brrp_append("_barred_2"));
    pack.addModel(model.clone().parent(model.parent + "_barred_3"), modelId.brrp_append("_barred_3"));
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JBlockStates getBlockStates() {
    final Identifier modelId = getBlockModelId();
    final JVariants variants = new JVariants();
    for (int i = 0; i < 16; i += 4) {
      final int y = i * 90 / 4;
      variants.addVariant("down=false,rotation=" + i, new JBlockModel(modelId).y(y));
      variants.addVariant("down=false,rotation=" + (i + 1), new JBlockModel(modelId.brrp_append("_1")).y(y));
      variants.addVariant("down=false,rotation=" + (i + 2), new JBlockModel(modelId.brrp_append("_2")).y(y));
      variants.addVariant("down=false,rotation=" + (i + 3), new JBlockModel(modelId.brrp_append("_3")).y(y + 90));
      variants.addVariant("down=true,rotation=" + i, new JBlockModel(modelId.brrp_append("_barred")).y(y));
      variants.addVariant("down=true,rotation=" + (i + 1), new JBlockModel(modelId.brrp_append("_barred_1")).y(y));
      variants.addVariant("down=true,rotation=" + (i + 2), new JBlockModel(modelId.brrp_append("_barred_2")).y(y));
      variants.addVariant("down=true,rotation=" + (i + 3), new JBlockModel(modelId.brrp_append("_barred_3")).y(y + 90));
    }
    return JBlockStates.ofVariants(variants);
  }

  @Environment(EnvType.CLIENT)
  @Override
  public @Nullable JModel getItemModel() {
    return new JModel(getBlockModelId().brrp_append("_barred"));
  }

  @Override
  public @Nullable JRecipe getCraftingRecipe() {
    if (baseBlock == null) return null;
    final JShapedRecipe recipe = new JShapedRecipe(this)
        .pattern("---", "###", " | ")
        .addKey("#", baseBlock).addKey("-", WallSignBlocks.INVISIBLE_WALL_SIGN).addKey("|", Items.STICK)
        .resultCount(4);
    recipe.addInventoryChangedCriterion("has_base_block", baseBlock).addInventoryChangedCriterion("has_sign", WallSignBlocks.INVISIBLE_WALL_SIGN);
    return recipe;
  }

  @Override
  public Identifier getAdvancementIdForRecipe(Identifier recipeId) {
    return recipeId.brrp_prepend("recipes/signs/");
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    final VoxelShape bodyShape = switch (state.get(ROTATION)) {
      case 0, 8 -> SHAPE_NS;
      case 1, 7, 9, 15 -> SHAPE_NS_WIDE;
      default -> SHAPE_CENTER;
      case 3, 5, 11, 13 -> SHAPE_WE_WIDE;
      case 4, 12 -> SHAPE_WE;
    };
    return state.get(DOWN) ? VoxelShapes.union(bodyShape, BAR_SHAPE) : bodyShape;
  }

  @Nullable
  @Override
  public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
    return new StandingSignBlockEntity(pos, state);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
    return state.get(ROTATION) % 4 == 0 && state.get(DOWN) ? CULLING_SHAPE : VoxelShapes.empty();
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
    return VoxelShapes.empty();
  }

  /**
   * 鉴于其实际外观与碰撞形状不一致，告示牌使用手动的侧面隐形判断。
   */
  @SuppressWarnings("deprecation")
  @Override
  public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
    if (direction.getAxis().isHorizontal() && stateFrom.getBlock() instanceof StandingSignBlock standingSignBlockFrom) {
      final int r1 = state.get(ROTATION);
      final int r2 = stateFrom.get(ROTATION);
      if ((r1 - r2) % 8 == 0) {
        if (direction.getAxis() == Direction.Axis.X && (r1 == 0 || r1 == 8) || direction.getAxis() == Direction.Axis.Z && (r1 == 4 || r1 == 12)) {
          if (standingSignBlockFrom.baseBlock instanceof TransparentBlock) {
            if (baseBlock instanceof TransparentBlock) {
              // 自身和相邻方块都为透明方块，则双方均为同一方块时隐藏。
              return baseBlock == standingSignBlockFrom.baseBlock;
            } else {
              return false;
            }
          }
          return true;
        }
      }
    }
    return super.isSideInvisible(state, stateFrom, direction);
  }

  @SuppressWarnings("deprecation")
  @Override
  public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
    final ActionResult actionResult = super.onUse(state, world, pos, player, hand, hit);
    if (actionResult.isAccepted()) {
      return actionResult;
    }

    final BlockEntity blockEntity = world.getBlockEntity(pos);
    final Boolean isFront = getHitSide(state, hit);
    if (!(blockEntity instanceof StandingSignBlockEntity entity)) return ActionResult.PASS;
    else if (player.isSneaking()) {
      // 潜行时点击告示牌，可以切换底部杆子的显示。
      world.setBlockState(pos, state.with(DOWN, !state.get(DOWN)));
      return ActionResult.SUCCESS;
    } else if (isFront == null) return ActionResult.PASS;
    else if (!player.getAbilities().allowModifyWorld) {
      // 冒险模式玩家无权编辑。Adventure players has no permission to edit.
      return ActionResult.FAIL;
    } else if (player.getMainHandStack().getItem() == Items.MAGMA_CREAM) {
      // 玩家手持岩浆膏时，可快速进行重整。
      MishangUtils.rearrange(entity.getTextsOnSide(isFront));
      world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BALL) {
      // 玩家手持粘液球时，可快速进行替换箭头。
      MishangUtils.replaceArrows(entity.getTextsOnSide(isFront));
      world.updateListeners(pos, state, state, Block.NOTIFY_ALL);
      entity.markDirty();
      return ActionResult.SUCCESS;
    } else if (player.getMainHandStack().getItem() == Items.SLIME_BLOCK) {
      final WorldChunk worldChunk = world.getWorldChunk(pos);
      for (BlockEntity value : worldChunk.getBlockEntities().values()) {
        if (value instanceof StandingSignBlockEntity standingSignBlockEntity) {
          MishangUtils.rearrange(standingSignBlockEntity.frontTexts);
          MishangUtils.rearrange(standingSignBlockEntity.backTexts);
          world.updateListeners(value.getPos(), value.getCachedState(), value.getCachedState(), Block.NOTIFY_ALL);
          standingSignBlockEntity.markDirty();
        }
      }
      return ActionResult.SUCCESS;
    } else if (world.isClient) {
      return ActionResult.SUCCESS;
    }

    entity.checkEditorValidity();
    final PlayerEntity editor = entity.getEditor();
    if (editor != null && editor != player) {
      // 这种情况下，告示牌被占用，玩家无权编辑。
      player.sendMessage(TextBridge.translatable("message.mishanguc.no_editing_permission.occupied", editor.getName()), false);
      return ActionResult.FAIL;
    }
    entity.editedSide = isFront;
    entity.setEditor(player);
    ServerPlayNetworking.send(
        ((ServerPlayerEntity) player),
        new Identifier("mishanguc", "edit_sign"),
        Util.make(PacketByteBufs.create(), packet -> packet.writeBlockPos(pos).writeBlockHitResult(hit)));
    return ActionResult.SUCCESS;
  }
}
