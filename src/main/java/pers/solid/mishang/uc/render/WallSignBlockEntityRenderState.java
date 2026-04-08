package pers.solid.mishang.uc.render;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Unmodifiable;
import pers.solid.mishang.uc.text.TextContext;

import java.util.List;

@Environment(EnvType.CLIENT)
public class WallSignBlockEntityRenderState extends BlockEntityRenderState {
  public @Unmodifiable List<TextContext> textContexts = ImmutableList.of();
  public boolean glowing;
  public float height;
  public VoxelShape voxelShape;
  public Direction facing;
  public AttachFace face;
  public boolean invisible;
  public boolean isGlowingBlock;
}
